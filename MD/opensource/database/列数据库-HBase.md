HBase 是一个分布式的、面向列的开源数据库，基于 HDFS，是 Hadoop 生态的核心组件之一。 它在一个表里存储数据行，一个数据行拥有一个可选择的键和任意数量的列。表是疏松存储的，因此用户可以给行定义各种不同的列。 主要用于需要随机访问、实时读/写大数据的场景。 

这里需要注意的是，HBase 是面向 OLAP 的一种数据库，受限于底层的数据结构，如果不做二次优化， 一般不推荐用于面向用户的业务。

概括来看， HBase 适用于以下存储场景。
1. 半结构化或非结构化数据。

对于数据结构字段不够确定或杂乱无章很难按一个概念去进行抽取的数据适合用 HBase。 当业务发展需要增加存储如一个用户的 E-Mail、 address信息时，RDBMS 需要停机维护， 而 HBase 支持动态增加。

2. 记录非常稀疏。 RDBMS 的行有多少列是固定的，为 NULL 的列会浪费存储空间。 HBase 为 NULL 的 Column 不会被存储，这样既节省了空间又提高了读性能。

3. 多版本数据。 根据 RowKey 和 Column key 定位到的 Value 可以有任意数量的版本值，因此对于需要 存储变动历史记录的数据，用 HBase 就非常方便。

4. 超大数据量。 当数据量越来越大，RDBMS数据库无法支撑，就出现读/写分离和各种分库分表策略，会带来业务复杂度的增加、无法 Join 等问题。 采用 HBase 只需要加机器即可， HBase 会自动水平切分扩展，跟 Hadoop的无缝集成保障了其数据可靠性（ HDFS ）和海量数据分析的高性能（ MapReduce ）


#### 关键概念
- RowKey

行主键，HBase 不支持条件查询和 Order by 查询，读取记录只能按 RowKey（及其 range ）或全表扫描，因此 RowKey 需要根据业务来设计以利用其存储排序特性（Table 按 RowKey 字典序排序如 1 10 100 11 2）提高性能。

- Column Family

列族，在表创建时声明，每个 Column Family 为一个存储单元。

- Column

列， HBase 的每一列都属于一个列族，以列族名为前缀，如列 `user:name 和 user:city` 属于 user 列族，`note:title 和 note:type` 属于 note 列族。 

Column 可以动态新增，同一 Column Family 的 Column 会聚簇在一个存储单元上，并依 Column key 排序，因此设计时应将具有相同 I/O特性的 Column 设计在一个 Column Family 上以提高性能。

- Timestamp

HBase 通过 row 和 column 确定一份数据，这份数据的值可能有多个版本，不同版本的值按照时间倒序排列，即最新的数据排在最前面，查询时默认返回最新版本。 

- Value

每个值通过 4 个键唯一索引： `tableName + RowKey + ColumnKey + Timestamp => value`

- 存储类型
```
- TableName 是字符串
- RowKey 和 ColumnName 是二进制值（byte[]）
- Timestamp 是一个 64 位整数（long）
- value 是一个字节数组（byte[]）
```

HBase 的 HTable 存储结构如下：
```
SortedMap{
	RowKey, List()
		SortedMap(
			Column, List(
				Value, Timestamp 
			)
		)
	)
}
```

即 HTable 按 RowKey 自动排序， 每个 Row 包含任意数量的 Column, Column 之间按 Column key 自动排序，每个 Column 包含任意数量的 Value。


#### 关键实现
HBase 有几个本身架构设计的组件需要了解。

- Zookeeper群：HBase 集群中不可缺少的重要部分，主要用于存储 Master地址、 协调 Master 和 RegionServer 等上下线、存储临时数据等。

- Master群：Master 主要是做一些管理操作，如 Region 的分配、手动管理操作下发等，一般数据的读/写操作不经过 Master集群，所以 Master 不需要很高的配置。

- RegionServer群：RegionServer 群是数据真正存储的地方，每个 RegionServer 由若干个 Region 组成，而一个 Region 维护了一定区间 RowKey 值的数据 

- Hlog： 是 HBase 实现 WAL 方式产生的日志信息，其内部是一个简单的顺序日志，每个 RS 上的 Region 都共享一个 HLog，所有对于该 RS 上的 Region 数据写入都被记录到该 HLog 中，以备在 RS 出现意外崩攒的时候，可以尽量多地恢复数据。

- MemStore：可以看作 HBase 的内部缓存，每次数据写入完成 HLog 后，都会写人对应 Region 的 MemStore

- HFile: HBase的数据底层存储。 每次数据从 MemStore 中 flush，最终都会形成一个 HFile 

基于以上几个集群，先看一下如何定位 RegionServer，这里涉及两个特殊的表 Meta 和 Root，用于存储数据库的元信息

- Meta表：记录了 RowKey 是在哪个 Region 的范围以及各个 Region 是在哪个 RegionServer 上等待等信息，是查询 HBase 时首先要访问的表。

其 RowKey 设计为： `Region 所在的表名 ＋ Region 的 StartKey ＋时间戳`， 三者的 MDS 值是 HBase 在 HDFS 上存储的 Region 的名字。 当 Region 被拆分、合并或者重新分配的时候，都需要修改这张表的内容。

- Root表： 记录了 Meta 表的 Region 信息，Root 表只有一个 Region，其位置信息记录在 ZooKeeper

一个 Region 的定位过程如下：
1.读取 ZooKeeper 中 Root 表的位置信息 --> 2.获取 Meta表的位置 --> 3.读取 Meta 表中用户表的位置 -- 4.读取数据

如果已经读取过一次， 则 Root表和 Meta表都会缓存到本地，直接去用户表的位置读取数据即可。

定位到 RS 之后，就可以进行数据写入和读取。

数据的写入首先要写 HLog 以实现 WAL，之后，按照 RowKey 的值排序写入 MemStore，即成功返回。存储于 MemStore 中的数据是 LSM 的数据结构，需要不定期地进行 compact 以减少文件碎片数、提高性能。

需要注意的是， Hlog 在数据 flush 到磁盘后，会被移动到 .oldlogs目录下，会有一个 HLog 监控线程监控该目录下的 HLog， 根据设置删除过期的 HLog，节约存储空间。

此外，HBase 基于的文件系统 HDFS 是 append only 的，因此数据的删除和过期一开始只是被标记为删除，在 compact 时才真正删除。

另外需要注意的一点是，在数据 flush 的时候，对应 Region 上的访问都是被拒绝的， 因此控制 flush 的时机是非常重要的。 主要有以下几种方式会触发 flush：
- 通过全局内存控制，触发 MemStore 刷盘操作。 通过参数 `hbase.regionserver.global.memstore.upperLimit` 进行设置，内存下降到 `hbase.regionserver.global.memstore.lowerLimit` 的值后， 即停止 MemStore 的刷盘操作。

- HBase 提供 API 接口 ，运行通过外部调用进行 MemStore 的刷盘

- 前面提到 MemStore 的 大小 通过 `hbase.hregion.memstore.flush.size` 进行设置，当Region 中 MemStore 的数据量达到该值时，会自动触发 MemStore 的刷盘操作。

- WAL 达到阀值，会引起 MemStore 的 flush。 WAL 的最大值由 `hbase.regionserver.maxlogs * hbase.regionserver.hlog.blocksize` ( 2GB by default ）决定。

还需要注意的是，一个列族达到阔值触发 flush 的时候，也会导致其他的列族 flush， 因 此列族的数量越少越好。

相比数据的写人，数据的读取相对来说比较简单： HBase 首先检查请求的数据是存在 MemStore，不在的话就到 HFile 中查找，最终返回 merged 的结果给用户 。


#### 使用提示
- RowKey 的设计越短越好，尽量不要超过 16 字节。

- 避免使用时序或者单调（递增/递减）行键，会导致连续到来的数据被分配到同一 Region，可以采取在 RowKey 前面添加 MDS 散列值的方式。

- 列族的数量越少越好，否则会造成在数据查询的时候读取更多的文件，消耗更多的 I/O

- 同一个表中不同列族所存储的记录数量的差别（列族的势）会造成记录数量少的列族的数据分散在多个 Region 上，影响查询效率。

- 尽量最小化行键和列族的大小，避免 HBase 的索引过大，加重系统存储的负担

- HColumnDescriptor 设置版本的数量，避免设置过大，版本保留过多

- 列族可以通过设置 TTL 来实现过期失效

- 为避免热点数据产生和后续文件 split 影响业务使用，一般采用 hash + partition 的方式预分配 Region，首先使用 MDS hash，然后按照首字母 partition 为 32 份，就可以预分配 32 个 Region

- Region 的数量选择可以参考此公式： `一个时的内存消耗＝ MemStore大小 * Region数量 * 列簇数量`

- HBase 支持多种形式的数据压缩，如 GZip、LZO、Snappy等。 其中 Snappy 的压缩率最低，但是编解码速率最高，对 CPU 的消耗也最小

- 对于有随机读的业务，建议开启 Row 类型的过滤器，使用空间换时间，提高随机读性能

- 避免全表扫描 HBase 数据

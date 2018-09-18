常见问题解答

<h3 style="margin-top: 1rem; color: rgb(61, 167, 66); font-size:16px;">Heap表是什么</h3>

Heap表存在于内存中，用于临时调整存储
- 不允许 blog 或 text
- 只能使用比较运算符：`= < > >= <=`
- 不支持auto_increment
- 索引不可为null

大小：`max_heap_table_size`

<h3 style="margin-top: 1rem; color: rgb(61, 167, 66); font-size:16px;">Enum</h3>

```
create table size {
  name ENUM('smail', 'medium', 'large')}
```

<h3 style="margin-top: 1rem; color: rgb(61, 167, 66); font-size:16px;">当前Mysql版本</h3>

`select version()`

<h3 style="margin-top: 1rem; color: rgb(61, 167, 66); font-size:16px;">数据类型</h3>

timestamp update current_timestamp

货币：decimal

最大插入ID: last_insert_id

存储机制
锁级别
索引

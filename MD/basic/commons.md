#### BeanUtils
BeanUtilsBean2
```
BeanUtilsBean beanUtils = new BeanUtilsBean2()
beanUtils.setProperty(user, "name", "testname")
beanUtils.getProperty(user, "name")
```
Map和Bean转换
```
// bean->map
map = beanUtils.describe(user)
// map->bean
beanUtils.populate(user, map)
```

PropertyUtils

#### Codec
常用的解码、编码方法封装，包括 Base64、MD5、SHA-1、URL

#### CollectionUtils
```java
isNotEmpty()
union() // 并集
subtract() // 差集
retainAll() // 交集
```

#### I/O
IOUtils, FileUtils, FileSystemUtils

#### lang3
- StringUtils 和 StringEscapeUtils
- ArrayUtils
- RandomUtils 和 RandomStringUtils
- SecureRandom
- NumberUtils
- DateUtils 和 DateFormatUtils
- MethodUtils
- StopWatch
- ImmutablePair 和 ImmutableTriple

#### HttpClient

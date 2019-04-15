### Optional
NPE
```
get()
isPresent()
```

### Stream

#### 1、创建

```java
// 通过已有的集合来创建流
List<String> strings = Arrays.asList("");
Stream<String> stream = strings.stream();

Stream.of("")
```

#### 2、操作
filter、map、limit/skip、sorted、distinct

forEach、count、collect

```
Stream.of(wordsInMessage)
  .distinct()
  .map(String::toLowerCase)
  .map(WORD_TO_MOOD::get)
  .filter(mood -> mood != null)
  .distinct()
  .map(Mood::name)
  .collect(Collectors.joining(","))
```

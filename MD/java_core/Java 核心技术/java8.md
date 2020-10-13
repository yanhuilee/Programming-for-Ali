### Lambda
```java
Runnable task = () -> { for() {do();} }  // 没有参数
Comparator<String> comp = (first, second) ->
    first.length() - second.length();
```

-  函数式接口：只有一个抽象方法 @FunctionalInterface

```java
Arrays.sort(words, (first, second) ->
  first.length() - second.length());
```

- 方法引用：

```java
Arrays.sort(strings, (x, y) -> x.compareToIgnoreCase(y))
Arrays.sort(strings, String::compareToIgnoreCase)

list.removeIf(Objects::isNull)
list.forEach(x -> System.out.println(x))
list.forEach(System.out::println())
```
this::equals 等同于 x -> this.equals(x)

- 构造函数引用

```java
Stream<Employee> stream = lists.stream().map(Employee::new)
Employee[] buttons = stream.toArray(Employee[]::new)
```


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

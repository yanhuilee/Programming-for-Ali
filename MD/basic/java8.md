#### Optional
NPE
```
get()
isPresent()
```

#### Stream
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

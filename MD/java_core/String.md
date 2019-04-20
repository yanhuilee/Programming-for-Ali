> 字符串常量，每次修改都 new 一个，为避免在一个系统中产生大量String对象，引用了字符串常量池。

```
private final char value[] //节约heap空间
多线程安全：hashcode 缓存
```

### 两种创建方式区别
常量池

StringBuffer 变量 16 缓存 线程安全

equals()
```java
public boolean equals(Object anObject) {
	if (this == anObject) {
		return true;
	}
	if (anObject instanceof String) {
		...
	}
	return false;
}
```

substring()
```java
public String substring(int beginIndex) {
  	// 边界判断
	if (beginIndex < 0) {
		throw new StringIndexOutOfBoundsException(beginIndex);
	}
	int subLen = value.length - beginIndex;
	if (subLen < 0) {
		throw new StringIndexOutOfBoundsException(subLen);
	}
	// beginIndex为0，返回当前对象，否则new一个新对象
	return (beginIndex == 0) ? this : new String(value, beginIndex, subLen);
}

public String substring(int beginIndex, int endIndex) {
    return ((beginIndex == 0) && (endIndex == value.length)) ? this : new String(value, beginIndex, subLen);
}
```

<h3 style="margin-top: 1rem; color: rgb(61, 167, 66); font-size:16px;">String 不变性的理解</h3>

- String 类是final 的，不能被继承
- +号连接字符串的时候会创建新的字符串，底层会转成 StringBuilder 的 `append()`
- `String s = new String("Hello")` 如果静态区中有 hello 字符串常量对象，则仅仅在堆中创建一个对象

<h3 style="margin-top: 1rem; color: rgb(61, 167, 66); font-size:16px;">String.valueOf() 和 Integer.toString() 区别</h3>

<h3 style="margin-top: 1rem; color: rgb(61, 167, 66); font-size:16px;">问：实现字符串反转？</h3>

`return new StringBuffer(str).reverse().toString()`

<h3 style="margin-top: 1rem; color: rgb(61, 167, 66); font-size:16px;">问：检查输入的字符串是否回文</h3>

```java
for (int i = 0; i < length / 2; i++) {
    if (str.charAt(i) != str.charAt(length – i – 1)) {
        return false;
    }
}
```

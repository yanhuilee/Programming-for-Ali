位于sun.misc包下，使Java语言拥有了类似C语言指针一样操作内存空间的能力

```
ublic final class Unsafe {
	// 单例对象
	private static final Unsafe theUnsafe;

	private Unsafe() {}

	@CallerSensitive
	public static Unsafe getUnsafe() {
		Class var0 = Reflection.getCallerClass();
		// 仅在引导类加载器`BootstrapClassLoader`加载时才合法
		if(!VM.isSystemDomainLoader(var0.getClassLoader())) {    
			throw new SecurityException("Unsafe");
		} else {
		    return theUnsafe;
		}
	}
}
```

#### 功能介绍
Unsafe提供的API大致可分为内存操作、CAS、Class相关、对象操作、线程调度、系统信息获取、内存屏障、数组操作等

[查看原文](https://mp.weixin.qq.com/s/h3MB8p0sEA7VnrMXFq9NBA)
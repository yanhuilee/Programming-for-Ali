### Web 安全

Web 安全问题，从大的方面可以分为：
1. 客户端安全： 通过浏览器进行攻击
	- 跨站点脚本攻击
	- 跨站点请求伪造

2. 服务器端安全：通过发送请求到服务器端进行攻击
	- SQL 注入
	- 基于约束条件的 SQL 攻击
	- 分布式拒绝服务攻击－DDOS
	- 会话固定攻击一Session fixation

#### 跨站点脚本攻击
跨站点脚本攻击，全称 Cross Site Script (XSS），跨越两个站点的攻击方式。一般指的是攻击方通过 HTML 注入的方式篡改了网页，插入了恶意的脚本，从而在用户浏览网页或者移动客户端使用 WebView加载时，默默地做了一些控制操作。

一个 xss 攻击的例子如下:
- Java 应用提供了一个接口可以上传个人动态，动态内容是富文本的
- 攻击者上传的内容如下： `<img src＝"1" onerror="alert('attack')" />`
- 在服务器端和客户端程序未做任何过滤的情况下，当其他用户访问这个动态页面时，就会执行这个脚本。

如果脚本不是一个 alert，而是换成跳转到一个具有删除操作的 URL，或者脚本获取用户的 Cookie，然后发送到远程服务器上，危害就会非常大。

防范这种攻击的常用方式有以下几种:
- 对任何允许用户输入的地方做检查，防止其提交脚本相关特殊字符串，如 script、onload、onerror 等。客户端和服务器端都要做检查

- 做输入过滤、，即将特殊字符都过滤掉或者换成 HTML 转义后的字符。 Java 中可以使用 Apache Commons-Lang 中 StringEscapeUtils 的带 escape 前缀的方法来做转义

- 给 Cookie 属性设置 HttpOnly，可以防止脚本获取 Cookie


#### 跨站点请求伪造
Cross Site Request Forgery，简称 CSRF。 也是一种常见的攻击方式。

主要通过诱导用户单击某些链接，从而隐含地发起对其他站点的请求，进而进行数据操作。

攻击示例如下：
- 用户登录一个站点，访问 http://xx/delete_notes?id＝xx 即可删除一个笔记
- 攻击者在它的站点中构造一个页面， HTML 页面含有以下内容：`<img src="http://xx/delete_notes?id=xx" />`
- 当用户被诱导访问攻击者的站点时就发起了一个删除笔记的请求

对于 CSRF 攻击的常用解决方案有以下几种:
- 验证码
- 使用类似防盗链的机制，对 header 的 refer 进行检验以确认请求来有合法的源
- Token，提交时对此 Token 进行验证


#### SQL 注入攻击

SQL 注入攻击是一个很常见的攻击方式，原理是通过发送特殊的参数，拼接服务器端 的 SQL 字符串，从而达到改变 SQL 功能的目的。
```
where user_name = 'admmin'--' and pwd = 'xx'
```

如果服务器的请求错误信息没有做进一步封装，直接把原始的数据库错误返回，有经验的攻击者通过返回结果多次尝试就会有机会找出 SQL 注入的机会。

防范这种攻击的方案有以下几种。
- 在 Java 中构造 SQL 查询语句时，杜绝拼接用户参数，尤其是拼接 SQL 查询的 where条件。 全部使用 PreparedStatement 预编译语句，通过？来传递参数
- 在业务层面，过滤、转义 SQL 特殊字符

#### 基于约束条件的 SQL 攻击

基于约束条件的 SQL 攻击的原理如下:
- 在处理 SQL 中的字符串时，字符串末尾的空格都会被删除，包括 WHERE 子句和 INSERT 语句，但 LIKE 子句除外
- 在任意 INSERT 查询中， SQL 会根据 varchar(n）来限制字符串的最大长度，即超过n 个字符的字符串只保留前 n 个字符

- [ ] 什么意思


#### 分布式拒绝服务攻击一DDOS
攻击者利用很多台机器同时向某个服务发送大量请求，从而使得服务被冲垮，无法为正常用户提供服务。 常见的 DDOS 攻击包括：

其中 SYN flood 是最经典的 DDOS 攻击。 其利用了 TCP 连接三次握手时需要先发送 SYN 的机制，通过发送大量 SYN包使得服务器端建立大量半连接，这就消耗了非常多的 CPU资源和内存。 针对这种攻击，很多解决方案是在 TCP 层就使用相关算法识别异常流量，直接拒绝建立连接。 但是，如果攻击者控制很多机器对一个资源消耗比较大的服务接口发起正常访问请求，那么这个方式就无效了。

由于难以区分是否是正常用户的请求，因此 DDOS 是非常难以防范的，但仍有一些措施能够尽量地减少 DDOS 带来的影响，介绍如下：
- 合理使用缓存、异步等措施提高应用性能。 应用抗并发的能力越强，就越不容易被 DDOS 冲垮服务
- 合理使用云计算相关组件，自动识别高峰流量并做自动扩容
- 在应用中限制来自某一IP 或者某一设备ID 的请求频率。 超过此频率就将其放入黑名单，直接拒绝服务。 Java 中可以通过 Redis 的 incr 和 expire。 如下：

```
String ip = NetworkUtil.getClientIP(request, false);
// 获取客户端 IP 地址
String key = "ddos." + ip;
long count = suishenRedisTemplate.incr(key);
// iner 不会影响 expire
if (count > 10000) { 
	throw new AccessException ("access too frequently with ip:" + 
		StringUtils.defaultString(ip));
} else { 
	if (count == 1) { suishenRedisTemplate.expire(key, 10); }
	return true;
}
```

上述代码即可将同一IP 的请求限制在 10 秒 10000 次。

此逻辑越靠近访问链路的前面效果越好，比如直接在 Nginx 中拦截，效果就要比在业务应用中做得好。


#### 会话固定攻击－Session fixation
在我们平时的 Web 开发中都是基于 Session 做用户会话管理的。 在浏览器中，Session ID 一般是存储在 Cookie 中，或直接附带在 query 参数中。 如果 Session 在未登录变为登录的情况下不发生改变的话， Session fixation 攻击就形成了。

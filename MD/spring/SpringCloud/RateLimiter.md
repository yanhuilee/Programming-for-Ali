> 令牌桶算法的原理是系统会以一个恒定的速度往桶里放入令牌，而如果请求需要被处理，则需要先从桶里获取一个令牌，当桶里没有令牌可取时，则拒绝服务。

RateLimiter 目前支持分钟级和秒级两种速率限制
```java
public class RateLimiter {

    // 速率单位转换成毫秒
    private final long rateToMsConversion;
    
    // averageRateUnit 速率单位
    public RateLimiter(TimeUnit averageRateUnit) {
        switch (averageRateUnit) {
            case SECONDS: // 秒级
                rateToMsConversion = 1000;
                break;
            case MINUTES: // 分钟级
                rateToMsConversion = 60 * 1000;
                break;
            default:
                throw new IllegalArgumentException("TimeUnit of " + averageRateUnit + " is not supported");
        }
    }

    /**
	 * 获取令牌
	 * burstSize：令牌桶上限
     */
    public boolean acquire(int burstSize, long averageRate, long currentTimeMillis) {
    	// Instead of throwing exception, we just let all the traffic go
		if (burstSize <= 0 || averageRate <= 0) {
			return true;
		}

		// 填充 令牌
		refillToken(burstSize, averageRate, currentTimeMillis);
		// 消费 令牌
		return consumeToken(burstSize);
	}
}
```
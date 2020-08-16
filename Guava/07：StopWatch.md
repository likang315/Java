### StopWatch

------

- ##### 秒表

  1. 支持转换TimeUnit，可以将计算后的时间转换为各种单位；
  2. 同一个Stopwatch，可以重置，重复记录使用；
  3. 计时器可以替换，重写Ticker，Spring自带的StopWatch不支持；

```java
package com.google.common.base;
/**
 * 计时器
 */
public final class Stopwatch {
  	// 计时器，用于获取当前时间
    private final Ticker ticker;
  	// 计时器是否运行中的状态标记
    private boolean isRunning;
  	// 用于标记从计时器开启到调用统计的方法时流逝的时间
    private long elapsedNanos;
  	// 计时器开启的时刻时间
    private long startTick;

  	// 创建一个创建时不启动的
    public static Stopwatch createUnstarted() {
        return new Stopwatch();
    }

  	// 创建一个创建时启动的
    public static Stopwatch createStarted() {
        return (new Stopwatch()).start();
    }
  
  	// 可以替换计时器，Spring的不支持
    public static Stopwatch createUnstarted(Ticker ticker) {
        return new Stopwatch(ticker);
    }
		

    public static Stopwatch createStarted(Ticker ticker) {
        return (new Stopwatch(ticker)).start();
    }
		
  	// 判断是否运行中
    public boolean isRunning() {
        return this.isRunning;
    }

  	// 开始计时
    public Stopwatch start() {
      	// 先判断是否处于执行状态
        Preconditions.checkState(!this.isRunning, "This stopwatch is already running.");
        this.isRunning = true;
      	// 获取当前的纳秒时间
        this.startTick = this.ticker.read();
        return this;
    }

  	// 停止计时
    public Stopwatch stop() {
        long tick = this.ticker.read();
        Preconditions.checkState(this.isRunning, "This stopwatch is already stopped.");
        this.isRunning = false;
        this.elapsedNanos += tick - this.startTick;
        return this;
    }
	
  	// 重置 初始化状态
    public Stopwatch reset() {
        this.elapsedNanos = 0L;
        this.isRunning = false;
        return this;
    }
		
  	// 计算task的耗时
    private long elapsedNanos() {
        return this.isRunning ? 
          this.ticker.read() - this.startTick + this.elapsedNanos : this.elapsedNanos;
    }

  	// 获取转换成其他时间单位的耗时值
    public long elapsed(TimeUnit desiredUnit) {
      	// 上一个任务的耗时纳秒，转换成传入的时间单位
        return desiredUnit.convert(this.elapsedNanos(), TimeUnit.NANOSECONDS);
    }

  	// 格式化输出，自动根据耗时转换成相应的时间单位
    @GwtIncompatible("String.format()")
    public String toString() {
        long nanos = this.elapsedNanos();
        TimeUnit unit = chooseUnit(nanos);
        double value = (double)nanos / (double)TimeUnit.NANOSECONDS.convert(1L, unit);
        return String.format("%.4g %s", value, abbreviate(unit));
    }

    private static TimeUnit chooseUnit(long nanos) {
        if (TimeUnit.DAYS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
            return TimeUnit.DAYS;
        } else if (TimeUnit.HOURS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
            return TimeUnit.HOURS;
        } else if (TimeUnit.MINUTES.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
            return TimeUnit.MINUTES;
        } else if (TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
            return TimeUnit.SECONDS;
        } else if (TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
            return TimeUnit.MILLISECONDS;
        } else {
            return TimeUnit.MICROSECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L ? TimeUnit.MICROSECONDS : TimeUnit.NANOSECONDS;
        }
    }

  	// 转换成相应的时间单位
    private static String abbreviate(TimeUnit unit) {
        switch(unit) {
        case NANOSECONDS:
            return "ns";
        case MICROSECONDS:
            return "μs";
        case MILLISECONDS:
            return "ms";
        case SECONDS:
            return "s";
        case MINUTES:
            return "min";
        case HOURS:
            return "h";
        case DAYS:
            return "d";
        default:
            throw new AssertionError();
        }
    }
}
```


- 查看网络状况
```sh
netstat -n I awk '/^tcp/ {++S[$NF]} END {for(a i n S) print a , S[a]}'

```
- 使用 top 令获取进程 CPU 使用率 ，使用/proc 文件查看进程所占内存
```sh
#!/bin/bash
for i in `ps -ef | egrep -v "awk|$0" | awk '/'$1 '/{print $2}''
do
    mymem = `cat /proc/$i/status 2> /dev/null | grep VmRSS I awk '{print $2" "$3}'
    cpu = `top -n 1 -b | awk '/'$i'/{print $9}'
done

```

- Core 转储快照

Core dump 是对内存的快照，可以从 Core dump 中转出 Heap dump 和 Thread dump
```sh
ulimit -c unlimited （使得 JVM 崩溃可以生成 Core dump)

gcore [pid] （主动生成 Core dump)
```

生成的 Core dump 文件在 CentOS 中位于用户当前工作目录下，形如 core.[pid] （可
以通过 `echo '/home/logs/core.%p' > /proc/sys/kernel/core_pattern` 修改位置） ，此文件
可以通过 gdb、jmap 和 jstack 等进行分析，如下：


```sh
gdb - c [core 文件］ $JAVA_HOME/bin/java 
＃进入 gdb 命令行后执行 bt ，显示程序的堆栈信息

jmap -heap $JAVA_HOME/bin/java [Core 文件］

jstack $JAVA_HOME/bin/java [Core 文件]
```

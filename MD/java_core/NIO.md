

<h3 style="margin-top: 1rem; color: rgb(61, 167, 66); font-size:16px;">大文件读写</h3>

```java
FileInputStream in = new FileInputStream("big.txt");
bufferfer buffer = bufferfer.allocate(65535);
FileChannel fileChannel = in.getChannel();

int bytes = -1;
do {
    bytes = fileChannel.read(buffer);
    if (bytes != -1) {
        byte[] array = new byte[bytes];
        buffer.flip();
        buffer.get(array);
        buffer.clear();
        // 拿array做事情
        System.out.println(new String(array));
    }
} while (bytes > 0);

buffer.clear();
```

RandomAccessFile 的 seek() 方法进行分块读写
package com.example.design_pattern;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: Lee
 * @Date: 2019/04/19 19:50
 */
public class LowerCaseInputStream extends FilterInputStream {
    /**
     * Creates a <code>FilterInputStream</code>
     * by assigning the  argument <code>in</code>
     * to the field <code>this.in</code> so as
     * to remember it for later use.
     *
     * @param in the underlying input stream, or <code>null</code> if
     *           this instance is to be created without an underlying stream.
     */
    protected LowerCaseInputStream(InputStream in) {
        super(in);
    }

    @Override
    public int read() throws IOException {
        int c = super.read();
        return (c == -1) ? c : Character.toLowerCase(c);
    }

    /**
     * 字节数组
     * @param b
     * @param offset
     * @param len
     * @return
     * @throws IOException
     */
    @Override
    public int read(byte[] b, int offset, int len) throws IOException {
        int result = super.read(b, offset, len);
        for (int i = offset; i < offset + result; i++) {
            b[i] = (byte) (Character.toLowerCase(((char) b[i])));
        }
        return result;
    }

    public static void main(String[] args) {
        int c;
        String file = "G:\\aa.txt";
        try (InputStream in = new LowerCaseInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            while ((c = in.read()) >= 0) {
                System.out.println("c = " + (char) c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] b = "asdFG".getBytes();

    }
}

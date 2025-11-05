package it.bologna.ausl.proctonutils;

import java.io.*;
import sun.misc.IOUtils;

/**
 *
 * @author Giuseppe De Marco (gdm)
 */
public class MyInputStream extends InputStream {
private BufferedInputStream bis;
private int mark;

    public MyInputStream(InputStream is) throws IOException {
        if (is instanceof BufferedInputStream)
            bis = (BufferedInputStream) is;
        else {
            byte[] bytes = IOUtils.readFully(is, -1, true);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            bis = new BufferedInputStream(bais);
        }
        this.mark = bis.available() + 1;
//        this.mark = 99999;
        bis.mark(this.mark);
    }

    @Override
    public int read() throws IOException {
        return bis.read();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return bis.read(b, off, len);
    }

    @Override
    public void close() throws IOException {
        bis.reset();
        bis.mark(this.mark);
    }

    public void realClose() throws IOException {
        bis.close();
    }

    @Override
    public int available() throws IOException {
        return bis.available();
    }

    @Override
    public boolean markSupported() {
        return bis.markSupported();
    }

    @Override
    public boolean equals(Object obj) {
        return bis.equals(obj);
    }

    @Override
    public int hashCode() {
        return bis.hashCode();
    }

    @Override
    public synchronized void mark(int readlimit) {
        bis.mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        bis.reset();
    }

    @Override
    public long skip(long n) throws IOException {
        return bis.skip(n);
    }

    @Override
    public String toString() {
        return bis.toString();
    }
}

package org.jfantasy.framework.util.common.file.gzip;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class GZipStream extends ServletOutputStream {

    private GZIPOutputStream zipStream;

    public GZipStream(OutputStream out) throws IOException {
        this.zipStream = new GZIPOutputStream(out);
    }

    public void flush() throws IOException {
        this.zipStream.flush();
    }

    public void write(byte[] b, int off, int len) throws IOException {
        this.zipStream.write(b, off, len);
    }

    public void write(byte[] b) throws IOException {
        this.zipStream.write(b);
    }

    public void write(int arg0) throws IOException {
        this.zipStream.write(arg0);
    }

    public void finish() throws IOException {
        this.zipStream.finish();
    }

    public void close() throws IOException {
        this.zipStream.close();
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
    }
}
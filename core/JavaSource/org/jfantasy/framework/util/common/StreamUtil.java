package org.jfantasy.framework.util.common;

import org.apache.log4j.Logger;

import java.io.*;

public abstract class StreamUtil {

    private StreamUtil(){}
    private static final Logger LOGGER = Logger.getLogger(StreamUtil.class);

    public static final int DEFAULT_BUFFER_SIZE = 2048;

    public static void copy(InputStream input, OutputStream output) throws IOException {
        copy(input, output, DEFAULT_BUFFER_SIZE);
    }

    public static void copy(InputStream input, OutputStream output,int bufferSize, int start, int end) throws IOException {
        byte[] buf = new byte[bufferSize];
        int loadLength = end - start + 1;

        if (start > 0) {
            long s = input.skip(start);
        }

        int bytesRead = input.read(buf, 0, loadLength > bufferSize ? bufferSize : loadLength);
        while (bytesRead != -1 && loadLength > 0) {
            loadLength -= bytesRead;
            output.write(buf, 0, bytesRead);
            bytesRead = input.read(buf, 0, loadLength > bufferSize ? bufferSize : loadLength);
        }
        output.flush();
    }

    public static void copy(InputStream input, OutputStream output, int bufferSize) throws IOException {
        byte[] buf = new byte[bufferSize];
        int bytesRead = input.read(buf);
        while (bytesRead != -1) {
            output.write(buf, 0, bytesRead);
            bytesRead = input.read(buf);
        }
        output.flush();
    }

    public static void copyThenClose(InputStream input, OutputStream output) throws IOException {
        copy(input, output);
        closeQuietly(input);
        closeQuietly(output);
    }

    public static byte[] getBytes(InputStream input) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        copy(input, result);
        result.close();
        return result.toByteArray();
    }

    public static void closeQuietly(InputStream input) {
        try {
            input.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }
    }

    public static void closeQuietly(OutputStream output) {
        try {
            output.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }
    }

    public static void closeQuietly(Writer writer) {
        try {
            writer.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }
    }

    public static void closeQuietly(Reader in) {
        try {
            in.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        }
    }
}

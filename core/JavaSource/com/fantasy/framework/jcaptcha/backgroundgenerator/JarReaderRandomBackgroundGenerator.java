package com.fantasy.framework.jcaptcha.backgroundgenerator;

import com.octo.captcha.CaptchaException;
import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.security.SecureRandom;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 与FileReaderRandomBackgroundGenerator类似,但是从jar中加载背景图片
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-4-9 上午12:44:51
 */
public class JarReaderRandomBackgroundGenerator implements BackgroundGenerator {

    private static final Log logger = LogFactory.getLog(JarReaderRandomBackgroundGenerator.class);

    private List<Object> images = new ArrayList<Object>();

    private int height = 100;
    private int width = 200;

    Random myRandom = new SecureRandom();

    protected static final Map<String, Map<String, InputStream>> cachedJars = new HashMap<String, Map<String, InputStream>>();

    public JarReaderRandomBackgroundGenerator(Integer width, Integer height, String rootPath, String jarPath) {
        this.width = (width != null ? width : this.width);
        this.height = (height != null ? height : this.height);

        String findPath = rootPath.replaceAll("^(./)", "");

        Map<String, InputStream> files = findJar(jarPath);
        for (Map.Entry<String, InputStream> entry : files.entrySet()) {
            if (!entry.getKey().startsWith(findPath)) {
                continue;
            }
            BufferedImage out = getImage(entry.getValue());
            if (out != null) {
                this.images.add(this.images.size(), out);
            }
        }
        if (this.images.size() != 0){
            for (int i = 0; i < this.images.size(); i++) {
                BufferedImage bufferedImage = (BufferedImage) this.images.get(i);
                this.images.set(i, tile(bufferedImage));
            }
        } else{
            throw new CaptchaException("Root path directory is valid but does not contains any image (jpg) files");
        }
    }

    @SuppressWarnings("unchecked")
    protected Map<String, InputStream> findJar(String jarPath) {
        if (cachedJars.containsKey(jarPath)) {
            return cachedJars.get(jarPath);
        }
        Hashtable<String, Long> htSizes = new Hashtable<String, Long>();
        Map<String, InputStream> files = new HashMap<String, InputStream>();
        cachedJars.put(jarPath, files);
        try {
            ZipFile zf = new ZipFile(jarPath);
            Enumeration<ZipEntry> e = (Enumeration<ZipEntry>) zf.entries();
            while (e.hasMoreElements()) {
                ZipEntry ze = e.nextElement();
                htSizes.put(ze.getName(), ze.getSize());
            }
            zf.close();
            FileInputStream fis = new FileInputStream(jarPath);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ZipInputStream zis = new ZipInputStream(bis);
            ZipEntry ze;
            while ((ze = zis.getNextEntry()) != null) {
                if (ze.isDirectory()) {
                    continue;
                }
                long size = ze.getSize();
                if (size == -1) {
                    size = htSizes.get(ze.getName());
                }
                byte[] b = new byte[(int) size];
                int rb = 0;
                int chunk;
                while (((int) size - rb) > 0) {
                    chunk = zis.read(b, rb, (int) size - rb);
                    if (chunk == -1) {
                        break;
                    }
                    rb += chunk;
                }
                files.put(ze.getName(), new ByteArrayInputStream(b));
            }
        } catch (NullPointerException e) {
            logger.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return files;
    }

    private BufferedImage tile(BufferedImage tileImage) {
        BufferedImage image = new BufferedImage(getImageWidth(), getImageHeight(), tileImage.getType());
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        int NumberX = getImageWidth() / tileImage.getWidth();
        int NumberY = getImageHeight() / tileImage.getHeight();
        for (int k = 0; k <= NumberY; k++) {
            for (int l = 0; l <= NumberX; l++) {
                g2.drawImage(tileImage, l * tileImage.getWidth(), k * tileImage.getHeight(), Math.min(tileImage.getWidth(), getImageWidth()), Math.min(tileImage.getHeight(), getImageHeight()), null);
            }

        }
        g2.dispose();
        return image;
    }

    private static BufferedImage getImage(InputStream is) {
        try {
            JPEGImageDecoder decoder = JPEGCodec.createJPEGDecoder(is);
            BufferedImage out = decoder.decodeAsBufferedImage();
            is.close();
            return out;
        } catch (IOException e) {
            throw new CaptchaException("Unknown error during file reading ", e);
        }
    }

    public BufferedImage getBackground() {
        return (BufferedImage) this.images.get(this.myRandom.nextInt(this.images.size()));
    }

    public int getImageHeight() {
        return this.height;
    }

    public int getImageWidth() {
        return this.width;
    }

}

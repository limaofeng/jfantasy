package com.fantasy.framework.util.common;

import com.fantasy.framework.dao.mybatis.keygen.GUIDKeyGenerator;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.sun.imageio.plugins.bmp.BMPImageReader;
import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageReader;
import com.sun.imageio.plugins.png.PNGImageReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.awt.image.ToolkitImage;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.util.Iterator;

/**
 * 图片处理工具类
 * 待完善，支持jmagick
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-3-15 上午10:17:58
 */
public final class ImageUtil {

    private static final Log logger = LogFactory.getLog(ImageUtil.class);

    private static final Color BACKGROUND_COLOR = Color.white;// 填充背景色
    private static final String DEST_FORMAT_NAME = "jpg";// 缩放、水印后保存文件格式名称
    private static final String JPEG_FORMAT_NAME = "jpg";// JPEG文件格式名称
    private static final String GIF_FORMAT_NAME = "gif";// GIF文件格式名称
    private static final String BMP_FORMAT_NAME = "bmp";// BMP文件格式名称
    private static final String PNG_FORMAT_NAME = "png";// PNG文件格式名称

    private static ImageObserver imageObserver = new ImageObserver() {

        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            logger.debug("ImageObserver : infoflags:" + infoflags + ",x:" + x + ",y:" + y + ",width:" + width + ",height:" + height);
            return true;
        }

    };


    /**
     * 获取图片文件格式
     *
     * @param imageFile
     *            图片文件
     *
     * @return 图片文件格式
     */
    /**
     * 获取图片文件格式
     *
     * @param imageFile 图片文件
     * @return 图片文件格式
     */
    public static String getFormatName(File imageFile) {
        if (imageFile == null || imageFile.length() == 0) {
            return null;
        }
        try {
            String formatName = null;
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(imageFile);
            Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);
            if (!iterator.hasNext()) {
                return null;
            }
            ImageReader imageReader = iterator.next();
            if (imageReader instanceof JPEGImageReader) {
                formatName = JPEG_FORMAT_NAME;
            } else if (imageReader instanceof GIFImageReader) {
                formatName = GIF_FORMAT_NAME;
            } else if (imageReader instanceof BMPImageReader) {
                formatName = BMP_FORMAT_NAME;
            } else if (imageReader instanceof PNGImageReader) {
                formatName = PNG_FORMAT_NAME;
            }
            imageInputStream.close();
            return formatName;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static String getFormatName(BufferedImage image) {
        if (image.getType() >= BufferedImage.TYPE_INT_ARGB && image.getType() <= BufferedImage.TYPE_4BYTE_ABGR) {
            return PNG_FORMAT_NAME;
        } else {
            return "JPEG";
        }
    }

    /**
     * 转换图片文件为JPEG格式
     *
     * @param srcImageFile  源图片文件
     * @param destImageFile 目标图片文件
     */
    public static void toJpegImageFile(File srcImageFile, File destImageFile) {
        if (srcImageFile == null) {
            return;
        }
        try {
            BufferedImage srcBufferedImage = ImageIO.read(srcImageFile);
            int width = srcBufferedImage.getWidth();
            int height = srcBufferedImage.getHeight();
            BufferedImage destBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = destBufferedImage.createGraphics();
            graphics2D.setBackground(BACKGROUND_COLOR);
            graphics2D.clearRect(0, 0, width, height);
            graphics2D.drawImage(srcBufferedImage, 0, 0, imageObserver);
            graphics2D.dispose();
            ImageIO.write(destBufferedImage, DEST_FORMAT_NAME, destImageFile);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void toJpegImageFile(InputStream srcImageInput, OutputStream destImageOutput) {
        if (srcImageInput == null) {
            return;
        }
        try {
            BufferedImage srcBufferedImage = ImageIO.read(srcImageInput);
            int width = srcBufferedImage.getWidth();
            int height = srcBufferedImage.getHeight();
            BufferedImage destBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics2D = destBufferedImage.createGraphics();
            graphics2D.setBackground(BACKGROUND_COLOR);
            graphics2D.clearRect(0, 0, width, height);
            graphics2D.drawImage(srcBufferedImage, 0, 0, imageObserver);
            graphics2D.dispose();
            ImageIO.write(destBufferedImage, DEST_FORMAT_NAME, destImageOutput);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 图片加水印
     *
     * @param press  水印图片
     * @param target 原始图片
     * @param x      坐标 x
     * @param y      坐标 y
     * @param alpha  水印图片透明度
     * @return {BufferedImage}
     * @throws IOException
     */
    public static BufferedImage watermark(String press, String target, int x, int y, int alpha) throws IOException {
        return watermark(new File(press), new File(target), x, y, alpha);
    }

    /**
     * 图片加水印
     *
     * @param press  水印图片
     * @param target 原始图片
     * @param x      坐标 x
     * @param y      坐标 y
     * @param alpha  水印图片透明度
     * @return {BufferedImage}
     * @throws IOException
     */
    public static BufferedImage watermark(File press, File target, int x, int y, int alpha) throws IOException {
        return watermark(ImageIO.read(press), ImageIO.read(target), x, y, alpha);
    }

    /**
     * 图片加水印
     *
     * @param press  水印图片
     * @param target 原始图片
     * @param x      坐标 x
     * @param y      坐标 y
     * @param alpha  水印图片透明度
     * @return {BufferedImage}
     * @throws IOException
     */
    public static BufferedImage watermark(String press, BufferedImage target, int x, int y, int alpha) throws IOException {
        return watermark(ImageIO.read(new File(press)), target, x, y, alpha);
    }

    /**
     * 图片加水印
     *
     * @param press  水印图片
     * @param target 原始图片
     * @param x      坐标 x
     * @param y      坐标 y
     * @param alpha  水印图片透明度
     * @return {BufferedImage}
     * @throws IOException
     */
    public static BufferedImage watermark(BufferedImage press, File target, int x, int y, int alpha) throws IOException {
        return watermark(press, ImageIO.read(target), x, y, alpha);
    }

    /**
     * 添加图片水印(AWT)
     *
     * @param srcBufferedImage       需要处理的BufferedImage
     * @param watermarkBufferedImage 水印图片文件
     * @param watermarkPosition      水印位置
     * @param alpha                  水印图片透明度
     * @return BufferedImage
     */
    public static BufferedImage watermark(BufferedImage srcBufferedImage, BufferedImage watermarkBufferedImage, WatermarkPosition watermarkPosition, int alpha) {
        int srcWidth = srcBufferedImage.getWidth();
        int srcHeight = srcBufferedImage.getHeight();

        int watermarkImageWidth = watermarkBufferedImage.getWidth();
        int watermarkImageHeight = watermarkBufferedImage.getHeight();

        Position position = watermarkPosition.position(srcWidth, srcHeight, watermarkImageWidth, watermarkImageHeight);

        return watermark(srcBufferedImage, watermarkBufferedImage, position.x, position.y, alpha);
    }

    /**
     * 图片加水印
     *
     * @param press  水印图片
     * @param target 原始图片
     * @param x      坐标 x
     * @param y      坐标 y
     * @param alpha  水印图片透明度
     * @return {BufferedImage}
     */
    public static BufferedImage watermark(BufferedImage press, BufferedImage target, int x, int y, int alpha) {
        int wideth = target.getWidth();
        int height = target.getHeight();
        BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.drawImage(target, 0, 0, wideth, height, imageObserver);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha / 100.0F));

        int wideth_biao = press.getWidth();
        int height_biao = press.getHeight();
        g.drawImage(press, wideth - wideth_biao - x, height - height_biao - y, wideth_biao, height_biao, imageObserver);
        g.dispose();
        return image;
    }

    public static void write(BufferedImage image, File file) throws FileNotFoundException {
        write(image, new FileOutputStream(file));
    }

    public static void write(BufferedImage image, String filePath) throws FileNotFoundException {
        write(image, new FileOutputStream(filePath));
    }

    public static void write(BufferedImage image, String formatName, String filePath) throws FileNotFoundException {
        write(image, formatName, new FileOutputStream(filePath));
    }

    public static void write(BufferedImage image, String formatName, OutputStream out) {
        try {
            ImageIO.write(image, formatName, out);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static void write(BufferedImage image, OutputStream out) {
        write(image, ImageUtil.getFormatName(image), out);
    }

    public static BufferedImage pressText(String pressText, String targetImg, String fontName, int fontStyle, int color, int fontSize, int x, int y) {
        try {
            File _file = new File(targetImg);
            Image src = ImageIO.read(_file);
            int wideth = src.getWidth(null);
            int height = src.getHeight(null);
            BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.createGraphics();
            g.drawImage(src, 0, 0, wideth, height, imageObserver);
            g.setColor(Color.RED);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.drawString(pressText, wideth - fontSize - x, height - fontSize / 2 - y);
            g.dispose();
            return image;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static BufferedImage reduce(InputStream target, float ratio) throws IOException {
        BufferedImage imageOriginal;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        StreamUtil.copy(target, os);
        String picextendname = getFileExtendName(os.toByteArray());
        target = new ByteArrayInputStream(os.toByteArray());
        if (BMP_FORMAT_NAME.equalsIgnoreCase(picextendname)) {
            imageOriginal = toBufferedImage((ToolkitImage) bmpReader(target));
        /*} else if (PNG_FORMAT_NAME.equalsIgnoreCase(picextendname)) {
            imageOriginal = toBufferedImage((ToolkitImage) pngReader(target, os));
        */
        } else if (GIF_FORMAT_NAME.equalsIgnoreCase(picextendname)) {
            throw new RuntimeException("暂不支持GIF图片缩放");
        } else {
            imageOriginal = ImageIO.read(target);
        }
        if ((ratio == 1.0F) || (ratio == 0.0F)) {
            return imageOriginal;
        }
        int realWidth = imageOriginal.getWidth();
        int realHeight = imageOriginal.getHeight();

        int standardWidth = (int) (realWidth * ratio);
        int standardHeight = (int) (realHeight * ratio);

        return reduce(imageOriginal, realWidth, realHeight, standardWidth, standardHeight);
    }

    /**
     * 缩放图片
     *
     * @param target 图片路径
     * @param width  宽
     * @param heigth 高
     * @return {BufferedImage}
     * @throws IOException
     */
    public static BufferedImage reduce(String target, int width, int heigth) throws IOException {
        return reduce(new FileInputStream(new File(target)), width, heigth);
    }

    private static BufferedImage toBufferedImage(ToolkitImage image) {
        BufferedImage imageOriginal = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = imageOriginal.getGraphics();
        g.drawImage(image, 0, 0, imageObserver);
        g.dispose();
        return imageOriginal;
    }

    public static BufferedImage reduce(InputStream target, int width, int heigth) throws IOException {
        BufferedImage imageOriginal;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        StreamUtil.copy(target, os);
        String picextendname = getFileExtendName(os.toByteArray());
        target = new ByteArrayInputStream(os.toByteArray());
        if (BMP_FORMAT_NAME.equalsIgnoreCase(picextendname)) {
            imageOriginal = toBufferedImage((ToolkitImage) bmpReader(target));
        }/* else if (PNG_FORMAT_NAME.equalsIgnoreCase(picextendname)) {
            imageOriginal = toBufferedImage((ToolkitImage) pngReader(target, os));
        }*/ else if (GIF_FORMAT_NAME.equalsIgnoreCase(picextendname)) {
            throw new RuntimeException("暂不支持GIF图片缩放");
        } else {
            imageOriginal = ImageIO.read(target);// toBufferedImage((ToolkitImage)jpgReader(target));
        }
        int realWidth = imageOriginal.getWidth();
        int realHeight = imageOriginal.getHeight();
        if ((realWidth == width) && (realHeight == heigth)){
            return imageOriginal;
        }
        return reduce(imageOriginal, realWidth, realHeight, width, heigth);
    }

    /**
     * 缩放图片
     *
     * @param imageOriginal 图片
     * @param realWidth     真实宽度
     * @param realHeight    真实高度
     * @param width         压缩到的宽度
     * @param heigth        压缩到的高度
     *                      java.awt.image.BufferedImage <br/>
     *                      TYPE_CUSTOM 没有识别出图像类型，因此它必定是一个自定义图像。<br/>
     *                      TYPE_INT_RGB 表示一个图像，它具有合成整数像素的8位RGB颜色分量。<br/>
     *                      TYPE_INT_ARGB 表示一个图像，它具有合成整数像素的8位RGBA颜色分量。<br/>
     *                      TYPE_INT_ARGB_PRE 表示一个图像，它具有合成整数像素的8位RGBA颜色分量。<br/>
     *                      TYPE_INT_BGR 表示一个具有8位RGB颜色分量的图像，对应于Windows或Solaris风格的BGR颜色模型，具有打包为整数像素的Blue、Green和Red三种颜色。<br/>
     *                      TYPE_3BYTE_BGR 表示一个具有8位RGB颜色分量的图像，对应于Windows风格的BGR颜色模型，具有用3字节存储的Blue、Green和Red三种颜色。<br/>
     *                      TYPE_4BYTE_ABGR 表示一个具有8位RGBA颜色分量的图像，具有用3字节存储的Blue、Green和Red颜色以及1字节的alpha。<br/>
     *                      TYPE_4BYTE_ABGR_PRE 表示一个具有8位RGBA颜色分量的图像，具有用3字节存储的Blue、Green和Red颜色以及1字节的alpha。<br/>
     *                      TYPE_USHORT_565_RGB 表示一个具有5-6-5RGB颜色分量（5位red、6位green、5位blue）的图像，不带alpha。<br/>
     *                      TYPE_USHORT_555_RGB 表示一个具有5-5-5RGB颜色分量（5位red、5位green、5位blue）的图像，不带alpha。<br/>
     *                      TYPE_BYTE_GRAY 表示无符号byte灰度级图像（无索引）。<br/>
     *                      TYPE_USHORT_GRAY 表示一个无符号short灰度级图像（无索引）。<br/>
     *                      TYPE_BYTE_BINARY 表示一个不透明的以字节打包的1、2或4位图像。<br/>
     *                      TYPE_BYTE_INDEXED 表示带索引的字节图像。<br/>
     *                      <p/>
     *                      java.awt.Image <br/>
     *                      SCALE_DEFAULT 使用默认的图像缩放算法。<br/>
     *                      SCALE_FAST 选择一种图像缩放算法，在这种缩放算法中，缩放速度比缩放平滑度具有更高的优先级。<br/>
     *                      SCALE_SMOOTH 选择图像平滑度比缩放速度具有更高优先级的图像缩放算法。<br/>
     *                      SCALE_REPLICATE 使用 ReplicateScaleFilter 类中包含的图像缩放算法。<br/>
     *                      SCALE_AREA_AVERAGING 使用 Area Averaging 图像缩放算法。<br/>
     * @return {BufferedImage}
     */
    public static BufferedImage reduce(BufferedImage imageOriginal, int realWidth, int realHeight, int width, int heigth) {
        try {
            int newWidth = 0;
            int newHeight = 0;
            if ((realWidth >= width) && (width > 0)) {
                newWidth = width;
                newHeight = realHeight * newWidth / realWidth;
            }
            if ((newHeight >= heigth) && (heigth > 0)) {
                newHeight = heigth;
                newWidth = realWidth * newHeight / realHeight;
            }
            Image image = imageOriginal.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(newWidth, newHeight, imageOriginal.getType());
            Graphics2D g = tag.createGraphics();
            g.drawImage(image, 0, 0, imageObserver);
            g.dispose();
            return tag;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return imageOriginal;
    }

    public static String measure(String confine, String orig) {
        String[] confineRatio = RegexpUtil.split(confine, "x");
        String[] origRatio = RegexpUtil.split(orig, "x");
        int newWidth = 0, realWidth = Integer.valueOf(origRatio[0]), width = Integer.valueOf(confineRatio[0]);
        int newHeight = 0, realHeight = Integer.valueOf(origRatio[1]), heigth = Integer.valueOf(confineRatio[1]);
        if ((realWidth >= width) && (width > 0)) {
            newWidth = width;
            newHeight = realHeight * newWidth / realWidth;
        }
        if ((newHeight >= heigth) && (heigth > 0)) {
            newHeight = heigth;
            newWidth = realWidth * newHeight / realHeight;
        }
        return newWidth + "x" + newHeight;
    }

    private static String getFileExtendName(byte[] byte1) {
        String strFileExtendName;
        strFileExtendName = null;
        // header bytes contains GIF87a or GIF89a?
        if ((byte1[0] == 71) && (byte1[1] == 73) && (byte1[2] == 70) && (byte1[3] == 56) && ((byte1[4] == 55) || (byte1[4] == 57)) && (byte1[5] == 97)) {
            strFileExtendName = "GIF";
        }
        // header bytes contains JFIF?
        if ((byte1[6] == 74) && (byte1[7] == 70) && (byte1[8] == 73) && (byte1[9] == 70)) {
            strFileExtendName = "JPG";
        }
        // header bytes contains BM?
        if ((byte1[0] == 66) && (byte1[1] == 77)) {
            strFileExtendName = "BMP";
        }
        // header bytes contains PNG?
        if ((byte1[1] == 80) && (byte1[2] == 78) && (byte1[3] == 71)) {
            strFileExtendName = "PNG";
        }
        return strFileExtendName;
    }

    public static PicsModel getFileAttributes(byte[] content) {
        PicsModel model;
        String picextendname;
        int k = content.length;
        // 不想处理的话，请直接获取其字节数
        Integer kk = null;
        String picsize;
        picsize = null;
        if (k >= 1024) {
            // bigger than fact pic file sizes
            k = k / 1024 + 1;
            kk = k;
            picsize = kk.toString() + "K";
        } else if (k > 0) {
            kk = k;
            picsize = kk.toString();
        }
        model = new PicsModel();
        model.setpicsSize(picsize);
        picextendname = getFileExtendName(content);
        int picwidth, picheight, color;
        String piccolor;
        picwidth = 0;
        picheight = 0;
        color = 0;
        piccolor = null;
        if (picextendname.equals("GIF")) {
            // picwidth position
            picwidth = getFileAttribute(content, 7, 2, picextendname);
            // picheight position
            picheight = getFileAttribute(content, 9, 2, picextendname);
            // piccolor position
            color = getFileAttribute(content, 10, 1, picextendname);
            color = color % 8 + 1;
            piccolor = getPicColor(color);
        }
        if (picextendname.equals("JPG")) {
            // 考虑了两种情况
            picwidth = getFileAttribute(content, 166, 2, picextendname);
            picheight = getFileAttribute(content, 164, 2, picextendname);
            color = getFileAttribute(content, 167, 1, picextendname);
            color = color * 8;
            if ((picwidth == 0) || (picheight == 0) || (color > 3)) {
                picwidth = getFileAttribute(content, 197, 2, picextendname);
                picheight = getFileAttribute(content, 195, 2, picextendname);
                color = getFileAttribute(content, 198, 1, picextendname);
                color = color * 8;
            }
            piccolor = getPicColor(color);
        }
        if (picextendname.equals("BMP")) {
            picwidth = getFileAttribute(content, 19, 2, picextendname);
            picheight = getFileAttribute(content, 23, 2, picextendname);
            color = getFileAttribute(content, 28, 1, picextendname);
            piccolor = getPicColor(color);
        }
        if (picextendname.equals("PNG")) {
            picwidth = getFileAttribute(content, 19, 2, picextendname);
            picheight = getFileAttribute(content, 23, 2, picextendname);
            // usually is "16M"??
            piccolor = "16M";
        }
        model.setpicsExtendName(picextendname);
        model.setpicsWidth(picwidth);
        model.setpicsHeight(picheight);
        model.setpicsColor(piccolor);

        return model;
    }

    static class PicsModel {

        private String picsExtendName = null;
        private int picsWidth = 0;
        private int picsHeight = 0;
        private String picsColor = null;
        private String picsSize = null;

        public PicsModel() {
        }

        public String getpicsExtendName() {
            return picsExtendName;
        }

        public void setpicsExtendName(String picsExtendName) {
            this.picsExtendName = picsExtendName;
        }

        public int getpicsWidth() {
            return picsWidth;
        }

        public void setpicsWidth(int picsWidth) {
            this.picsWidth = picsWidth;
        }

        public int getpicsHeight() {
            return picsHeight;
        }

        public void setpicsHeight(int picsHeight) {
            this.picsHeight = picsHeight;
        }

        public String getpicsColor() {
            return picsColor;
        }

        public void setpicsColor(String picsColor) {
            this.picsColor = picsColor;
        }

        public String getpicsSize() {
            return picsSize;
        }

        public void setpicsSize(String picsSize) {
            this.picsSize = picsSize;
        }
    }

    private static int getFileAttribute(byte[] byte2, int n, int m, String fileextendname) {
        int j, FileAttributeValue;
        j = 0;
        FileAttributeValue = 0;
        String str, str1;
        str = "";
        str1 = "";
        // 如果其大于127，则反之出现少于0，需要进行＋256运算
        for (int k = 0; k < m; k++) {
            if (byte2[n - k] < 0) {
                j = byte2[n - k];
                j = j + 256;
            } else {
                j = byte2[n - k];
            }
            str1 = Integer.toHexString(j);
            // 转化为16进制，不足位补0
            if (str1.length() < 2) {
                str1 = "0" + str1;
            }
            // 格式的不同，表达属性的字节也有变化
            if (fileextendname.equalsIgnoreCase("JPG") || fileextendname.equalsIgnoreCase("PNG")) {
                str = str1 + str;
            } else {
                str = str + str1;
            }
        }
        FileAttributeValue = HexToDec(str);
        return FileAttributeValue;
    }

    private static int HexToDec(String cadhex) {
        int n, i, j, k, decimal;
        String CADHEX1;
        n = 0;
        i = 0;
        j = 0;
        k = 0;
        decimal = 0;
        CADHEX1 = null;
        n = cadhex.length();
        CADHEX1 = cadhex.trim().toUpperCase();
        while (i < n) {
            j = CADHEX1.charAt(i);
            if ((j >= 48) && (j < 65)) {
                j = j - 48;
            }
            if (j >= 65) {
                j = j - 55;
            }
            i = i + 1;

            // 16幂运算
            k = 1;
            for (int m = 0; m < (n - i); m++) {
                k = 16 * k;
            }
            decimal = j * k + decimal;
        }
        return decimal;
    }

    private static String getPicColor(int color) {
        int k;
        k = 1;
        String piccolor;
        piccolor = null;
        // 2幂运算
        for (int m = 0; m < color; m++) {
            k = 2 * k;
        }
        Integer kk;
        if (k >= 1048576) {
            k = k / 1048576;
            kk = k;
            piccolor = kk.toString() + "M";
        } else if (k >= 1024) {
            k = k / 1024;
            kk = k;
            piccolor = kk.toString() + "K";
        } else if (k > 0) {
            kk = k;
            piccolor = kk.toString();
        }
        return piccolor;
    }

    public static Image bmpReader(File file) throws IOException {
        return bmpReader(new FileInputStream(file));
    }

    public static Image pngReader(InputStream in, ByteArrayOutputStream os) {
        try {
            if (in instanceof ByteArrayInputStream) {
                in.reset();
            }
            ImageIcon imageIcon = new ImageIcon(os.toByteArray());
            return imageIcon.getImage();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public static Image bmpReader(InputStream fs) throws IOException {
        Image image = null;
        try {
            int bflen = 14;
            byte[] bf = new byte[bflen];
            fs.read(bf, 0, bflen);
            int bilen = 40;
            byte[] bi = new byte[bilen];
            fs.read(bi, 0, bilen);

            int nwidth = (bi[7] & 0xFF) << 24 | (bi[6] & 0xFF) << 16 | (bi[5] & 0xFF) << 8 | bi[4] & 0xFF;
            logger.debug("width: " + nwidth);
            int nheight = (bi[11] & 0xFF) << 24 | (bi[10] & 0xFF) << 16 | (bi[9] & 0xFF) << 8 | bi[8] & 0xFF;
            logger.debug("height: " + nheight);

            int nbitcount = (bi[15] & 0xFF) << 8 | bi[14] & 0xFF;
            logger.debug("bits: " + nbitcount);

            int nsizeimage = (bi[23] & 0xFF) << 24 | (bi[22] & 0xFF) << 16 | (bi[21] & 0xFF) << 8 | bi[20] & 0xFF;
            logger.debug("source size: " + nsizeimage);

            if (nbitcount == 24) {
                int npad = nsizeimage / nheight - nwidth * 3;
                int[] ndata = new int[nheight * nwidth];
                byte[] brgb = new byte[(nwidth + npad) * 3 * nheight];
                fs.read(brgb, 0, (nwidth + npad) * 3 * nheight);
                int nindex = 0;
                for (int j = 0; j < nheight; j++) {
                    for (int i = 0; i < nwidth; i++) {
                        ndata[(nwidth * (nheight - j - 1) + i)] = (0xFF000000 | (brgb[(nindex + 2)] & 0xFF) << 16 | (brgb[(nindex + 1)] & 0xFF) << 8 | brgb[nindex] & 0xFF);
                        nindex += 3;
                    }
                    nindex += npad;
                }
                Toolkit kit = Toolkit.getDefaultToolkit();
                image = kit.createImage(new MemoryImageSource(nwidth, nheight, ndata, 0, nwidth));
                logger.debug("read bmp image success");
            } else {
                throw new RuntimeException("it's not 24bits bmp, fail.");
            }
        } finally {
            StreamUtil.closeQuietly(fs);
        }
        return image;
    }

    public static Image jpgReader(InputStream in) throws IOException {
        Toolkit kit = Toolkit.getDefaultToolkit();
        File file = new File(System.getProperty("java.io.tmpdir") + GUIDKeyGenerator.getInstance().getGUID());
        FileOutputStream out = new FileOutputStream(file);
        StreamUtil.copy(in, out);
        URL url = new URL("file:///" + file.getPath());
        return kit.getImage(url);
    }

    /**
     * @param img
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
    public static BufferedImage screenshots(BufferedImage img, int x, int y, int w, int h) {
        if (logger.isDebugEnabled()){
            logger.debug("Method:screenshots,param:{x:" + x + ",y:" + y + ",w:" + w + ",h:" + h + "}");
        }
        ImageFilter cropFilter = new CropImageFilter(x, y, w, h);
        Image newImg = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(img.getSource(), cropFilter));
        BufferedImage tag = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        g.drawImage(newImg, 0, 0, imageObserver);
        g.dispose();
        return tag;
    }

    /**
     * 获取图片的编码字符串<br/><img src="data:image/gif;base64,64位编码字符串"/>
     *
     * @param in 输入流
     * @return {String}
     */
    public static String getImage(InputStream in) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        try {
            byte[] data = new byte[in.available()];// 读取图片字节数组
            in.read(data);
            BASE64Encoder encoder = new BASE64Encoder();// 对字节数组Base64编码
            return encoder.encode(data);// 返回Base64编码过的字节数组字符串
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        } finally {
            StreamUtil.closeQuietly(in);
        }
    }

    /**
     * 获取base64位编码的字符串 转换为图片对象
     *
     * @param base64
     * @return BufferedImage
     */
    public static BufferedImage getImage(String base64) {
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            ByteArrayInputStream in = new ByteArrayInputStream(decoder.decodeBuffer(base64));
            return ImageIO.read(in);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    private static class Position {
        int y;
        int x;

        public Position(int x, int y) {
            super();
            this.x = x;
            this.y = y;
        }

    }

    // 水印位置(无、左上、右上、居中、左下、右下)
    public enum WatermarkPosition {
        no, topLeft, topRight, center, bottomLeft, bottomRight;

        public Position position(int srcWidth, int srcHeight, int watermarkImageWidth, int watermarkImageHeight) {
            switch (this) {
                case topLeft:
                    return new Position(0, 0);
                case topRight:
                    return new Position(srcWidth - watermarkImageWidth, 0);
                case center:
                    return new Position((srcWidth - watermarkImageWidth) / 2, (srcHeight - watermarkImageHeight) / 2);
                case bottomLeft:
                    return new Position(0, srcHeight - watermarkImageHeight);
                case bottomRight:
                    return new Position(srcWidth - watermarkImageWidth, srcHeight - watermarkImageHeight);
            }
            return null;
        }
    }

}

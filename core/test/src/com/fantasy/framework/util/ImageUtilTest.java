package com.fantasy.framework.util;


import magick.*;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtilTest {

    private static boolean isImageMagickEnable = false;

    static {
        try {
            System.setProperty("jmagick.systemclassloader", "no");
            new Magick();
            isImageMagickEnable = true;
        } catch (Throwable e) {
            isImageMagickEnable = false;
        }
    }

    public static void main(String[] args) throws IOException {

//		BufferedImage image = ImageUtil.reduce(new FileInputStream(new File("c:/001.jpg")), 160, 160);
//
//		BufferedImage imageOriginal = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
//
//		BoxBlurFilter filter = new BoxBlurFilter(200,200,200);
//
//		imageOriginal = filter.filter(ImageIO.read(new File("c:/001.jpg")), imageOriginal);
//
//		ImageIO.write(imageOriginal, "jpg", new File("c:/test.jpg"));
//
//		ImageUtil.write(image, new File("c:/test.jpg"));

		int width = 652;
		int height = 340;

		String str = "测试文字xx";

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();

		g2.setBackground(Color.white);
		g2.clearRect(0, 0, width, height);

		//g2.drawImage(ImageIO.read(new File("c:/圆通.jpg")), 0, 0, width, height, imageObserver);

		//g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 100 / 100.0F));

		Font font = new Font("黑体", Font.PLAIN, 12);
		g2.setFont(font);
		g2.setPaint(Color.black);

		Rectangle2D bounds = font.getStringBounds(str, g2.getFontRenderContext());

		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g2.drawString(str, 100, (int)bounds.getHeight());

		font = new Font("黑体", Font.PLAIN, 12);
		g2.setFont(font);
		g2.setPaint(Color.black);

		g2.drawString(str, 100, (int) (bounds.getHeight()*2));
		g2.dispose();

	}

    /**
     * 添加图片水印(ImageMagick)
     *
     * @param srcMagickImage 需要处理的MagickImage
     * @param watermarkFile  水印图片文件
     * @param x              坐标 x
     * @param y              坐标 y
     * @param alpha          水印图片透明度
     * @return {MagickImage}
     */
    public static MagickImage watermark(MagickImage srcMagickImage, File watermarkFile, int x, int y, int alpha) {
        MagickImage watermarkMagickImage = null;
        try {
            if (watermarkFile != null && watermarkFile.exists()) {
                // Dimension dimension = srcMagickImage.getDimension();
                // int srcWidth = (int) dimension.getWidth();
                // int srcHeight = (int) dimension.getHeight();
                watermarkMagickImage = new MagickImage(new ImageInfo(watermarkFile.getAbsolutePath()));
                // Dimension watermarkDimension = watermarkMagickImage.getDimension();
                // int watermarkImageWidth = (int) watermarkDimension.getWidth();
                // int watermarkImageHeight = (int) watermarkDimension.getHeight();
                watermarkMagickImage.transparentImage(PixelPacket.queryColorDatabase("white"), 655 * alpha);
                srcMagickImage.compositeImage(CompositeOperator.AtopCompositeOp, watermarkMagickImage, x, y);
                watermarkMagickImage.destroyImages();
            }
        } catch (MagickException e) {
            if (srcMagickImage != null) {
                srcMagickImage.destroyImages();
            }
        } finally {
            if (watermarkMagickImage != null) {
                watermarkMagickImage.destroyImages();
            }
        }
        return srcMagickImage;
    }

}

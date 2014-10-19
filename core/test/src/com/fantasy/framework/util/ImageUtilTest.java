package com.fantasy.framework.util;


import magick.*;

import java.io.File;

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

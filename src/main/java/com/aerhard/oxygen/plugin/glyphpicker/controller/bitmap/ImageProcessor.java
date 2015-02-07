package com.aerhard.oxygen.plugin.glyphpicker.controller.bitmap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageProcessor {

    /**
     * Scales an image to maximally fill the bound dimension, keeping the aspect ratio.
     *
     * @param image     the image
     * @param maxWidth  the maximum width of the result image
     * @param maxHeight the maximum height of the result image
     * @return the scaled image
     */
    public Image scaleToBound(BufferedImage image, int maxWidth, int maxHeight) {

        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        int resultWidth = originalWidth;
        int resultHeight = originalHeight;

        if (originalWidth > maxWidth) {
            resultWidth = maxWidth;
            resultHeight = (resultWidth * originalHeight) / originalWidth;
        }

        if (resultHeight > maxHeight) {
            resultHeight = maxHeight;
            resultWidth = (resultHeight * originalWidth) / originalHeight;
        }

        // set minimum dimension of 1 px
        resultWidth = Math.max(resultWidth, 1);
        resultHeight = Math.max(resultHeight, 1);

        return image.getScaledInstance(resultWidth, resultHeight,
                Image.SCALE_AREA_AVERAGING);
    }


}

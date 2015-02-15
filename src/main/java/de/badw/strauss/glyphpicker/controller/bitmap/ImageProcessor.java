/**
 * Copyright 2015 Alexander Erhard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.badw.strauss.glyphpicker.controller.bitmap;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The Class ImageProcessor, providing the method scaleToBound
 */
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

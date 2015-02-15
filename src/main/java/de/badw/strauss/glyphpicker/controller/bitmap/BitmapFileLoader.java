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

import de.badw.strauss.glyphpicker.model.GlyphDefinition;
import de.badw.strauss.glyphpicker.view.renderer.GlyphBitmapIcon;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The Class BitmapLoader.
 */
public class BitmapFileLoader implements BitmapLoader {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(BitmapFileLoader.class.getName());

    /**
     * The image processor used to scale the image.
     */
    private final ImageProcessor imageProcessor;

    /**
     * The glyph definition
     */
    private final GlyphDefinition d;

    /**
     * The bitmap container's size
     */
    private final int containerSize;

    /**
     * The image file
     */
    private final File imageFile;

    /**
     * Instantiates a new bitmap file loader.
     *
     * @param d              The glyph definition
     * @param basePath       The base path to the graphic
     * @param relativePath   The path to the graphic relative to the base path
     * @param imageProcessor The image processor used to scale the image
     * @param containerSize  The bitmap container's size
     */
    public BitmapFileLoader(GlyphDefinition d, String basePath, String relativePath, ImageProcessor imageProcessor, int containerSize) {
        this.d = d;
        this.imageProcessor = imageProcessor;
        this.containerSize = containerSize;

        File parentFolder = new File(new File(basePath).getParent());
        imageFile = new File(parentFolder, relativePath);
    }

    /**
     * Instantiates a new bitmap file loader.
     *
     * @param d              The glyph definition
     * @param imageFile      The image file
     * @param imageProcessor The image processor used to scale the image
     * @param containerSize  The bitmap container's size
     */
    public BitmapFileLoader(GlyphDefinition d, File imageFile, ImageProcessor imageProcessor, int containerSize) {
        this.d = d;
        this.imageProcessor = imageProcessor;
        this.containerSize = containerSize;

        this.imageFile = imageFile;
    }

    /**
     * Loads the image.
     *
     * @return the image
     */
    public GlyphBitmapIcon getImage() {

        BufferedImage bi = getImageFromFile(imageFile);
        if (bi != null) {
            int scaledSize = Math
                    .round(containerSize * d.getDataSource().getSizeFactor());
            return new GlyphBitmapIcon(imageProcessor.scaleToBound(bi, scaledSize, scaledSize),
                    scaledSize);
        }

        return null;
    }

    /**
     * Gets an image from a file.
     *
     * @param file the file
     * @return the image from the file
     */
    public BufferedImage getImageFromFile(File file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            LOGGER.warn("\"" + file.toPath() + "\" could not be loaded. "
                    + e.getMessage());
        }
        return image;
    }

}

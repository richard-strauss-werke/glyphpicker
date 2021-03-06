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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.SystemDefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * A class loading a bitmap image from a URL implementing the ImageCache.
 */
public class BitmapUrlLoader implements BitmapLoader {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(BitmapFileLoader.class.getName());

    /**
     * The get parameters to be added to the http request
     */
    private static final String REQUEST_PARAMETER_STRING = "?res=low";

    /**
     * The base path to the graphic.
     */
    private final String basePath;

    /**
     * The path to the graphic relative to the base path.
     */
    private final String relativePath;

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
     * The image cache
     */
    private final ImageCache imageCache;

    /**
     * Instantiates a new bitmap file loader.
     *
     * @param d              The glyph definition
     * @param basePath       The base path to the graphic
     * @param relativePath   The path to the graphic relative to the base path
     * @param imageProcessor The image processor used to scale the image
     * @param containerSize  The bitmap container's size
     * @param imageCache     The image cache
     */
    public BitmapUrlLoader(GlyphDefinition d, String basePath, String relativePath,
                           ImageProcessor imageProcessor, int containerSize, ImageCache imageCache) {
        this.d = d;
        this.basePath = basePath;
        this.relativePath = relativePath;
        this.imageProcessor = imageProcessor;
        this.containerSize = containerSize;
        this.imageCache = imageCache;
    }

    /**
     * Loads the image.
     *
     * @return the image
     */
    public GlyphBitmapIcon getImage() {
        try {
            String imagePath = (new URL(new URL(basePath), relativePath))
                    .toString();

            String imageNameInCache;
            if (imageCache != null) {
                imageNameInCache = ImageCache.createCacheFileName(imagePath);
                File cachedImageFile = imageCache.getFile(imageNameInCache);
                if (cachedImageFile != null) {
                    GlyphBitmapIcon iconFromFile = new BitmapFileLoader(d, cachedImageFile, imageProcessor, containerSize).getImage();
                    if (iconFromFile != null) {
                        return iconFromFile;
                    }
                }
            } else {
                imageNameInCache = null;
            }

            BufferedImage bi = getImageFromUrl(imagePath + REQUEST_PARAMETER_STRING);
            if (bi != null) {
                int scaledSize = Math
                        .round(containerSize * d.getDataSource().getSizeFactor());
                Image scaledImage = imageProcessor.scaleToBound(bi, scaledSize, scaledSize);

                if (imageCache != null) {

                    // NB: saves the original image instead of the scaled image so the image cache
                    // doesn't have to be cleared when a new bitmap size ratio is provided by
                    // the user:
                    imageCache.writeImage(bi, imageNameInCache);
                }

                return new GlyphBitmapIcon(scaledImage, scaledSize);
            }
        } catch (Exception e) {
            LOGGER.info(e);
        }
        return null;
    }

    /**
     * Gets an image from a URL.
     *
     * @param url the url
     * @return the image from the url
     */
    public BufferedImage getImageFromUrl(String url) {
        SystemDefaultHttpClient httpClient = new SystemDefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);

            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                byte[] bytes = EntityUtils.toByteArray(entity);
                return ImageIO.read(new ByteArrayInputStream(bytes));
            } else {
                throw new IOException("Download failed, HTTP response code "
                        + statusCode + " - " + statusLine.getReasonPhrase());
            }
        } catch (IOException e) {
            LOGGER.warn("Error loading image from \"" + url + "\". "
                    + e.getMessage());
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return null;
    }

}

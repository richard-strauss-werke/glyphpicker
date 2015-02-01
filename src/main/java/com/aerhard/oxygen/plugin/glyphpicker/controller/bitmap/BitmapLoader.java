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
package com.aerhard.oxygen.plugin.glyphpicker.controller.bitmap;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.renderer.GlyphBitmapIcon;

/**
 * The Class BitmapLoader.
 */
public class BitmapLoader implements Runnable {

    /** The logger. */
    private static final Logger LOGGER = Logger
            .getLogger(BitmapLoader.class.getName());

    /** The worker from which the bitmap loader has been called. */
    private final BitmapLoadWorker worker;
    
    /** The glyph definition containing the path to the bmp. */
    private final GlyphDefinition glyphDefinition;
    
    /** The size to which the bitmap should be scaled. */
    private final int size;

    /**
     * Instantiates a new bitmap loader.
     *
     * @param worker The worker from which the bitmap loader has been called
     * @param glyphDefinition The glyph definition containing the path to the bmp
     * @param size The size to which the bitmap should be scaled
     */
    public BitmapLoader(BitmapLoadWorker worker,
            GlyphDefinition glyphDefinition, int size) {
        this.worker = worker;
        this.glyphDefinition = glyphDefinition;
        this.size = size;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        if (!worker.isCancelled()) {
            try {
                GlyphBitmapIcon icon = null;

                BufferedImage bi = loadImage(glyphDefinition.getDataSource()
                        .getBasePath(), glyphDefinition.getUrl());
                if (bi != null) {
                    icon = new GlyphBitmapIcon(scaleToBound(bi, size, size),
                            size);
                }

                if (icon != null) {
                    glyphDefinition.setIcon(icon);
                    worker.firePropertyChange("iconLoaded", null,
                            glyphDefinition);
                }
            } catch (Exception e) {
                LOGGER.warn(e);
            }
        }
    }

    /**
     * Scales an image to maximally fill the bound dimension, keeping the aspect ratio.
     *
     * @param image the image
     * @param maxWidth the maximum width of the result image
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

    /**
     * Loads an image from the specified path.
     *
     * @param basePath the base path
     * @param relativePath the relative path
     * @return the image
     */
    public BufferedImage loadImage(String basePath, String relativePath) {
        BufferedImage image = null;
        if (relativePath != null) {
            if (isLocalFile(basePath)) {
                File a = new File(basePath);
                File parentFolder = new File(a.getParent());
                File b = new File(parentFolder, relativePath);
                image = getImageFromFile(b);
            } else {
                try {
                    String imagePath = (new URL(new URL(basePath), relativePath))
                            .toString();
                    image = getImageFromUrl("guest", "guest", imagePath);
                } catch (MalformedURLException e) {
                    LOGGER.info(e);
                }

            }
        }
        return image;
    }

    /**
     * Checks if a string refers to a local file.
     *
     * @param path the path
     * @return the result
     */
    public static Boolean isLocalFile(String path) {
        return !path.matches("^\\w+://.*");
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

    /**
     * Gets an image from a URL.
     *
     * @param user the user name
     * @param password the password
     * @param url the url
     * @return the image from the url
     */
    public BufferedImage getImageFromUrl(String user, String password,
            String url) {
        HttpResponse response;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader(BasicScheme.authenticate(
                    new UsernamePasswordCredentials(user, password), "UTF-8",
                    false));
            response = httpclient.execute(httpGet);

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
            httpclient.getConnectionManager().shutdown();
        }
        return null;
    }

}

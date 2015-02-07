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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * A class providing access methods to the image cache
 */
public class ImageCacheAccess {

    /**
     * The image cache folder
     */
    private final File cacheFolder;

    /**
     * Instantiates a new ImageCacheAccess
     *
     * @param cacheFolder the cache folder
     */
    public ImageCacheAccess(File cacheFolder) {
        this.cacheFolder = cacheFolder;
    }

    /**
     * Creates a cache file name for the specified url
     * @param url the url
     * @return the file name or null if no file name could be created
     */
    public static String createCacheFileName(String url) {
        try {
            return URLEncoder.encode(url, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Gets a file from the cache folder
     * @param imageNameInCache the name of the image in the cache
     * @return the file or null if the file doesn't exist
     */
    public File getFile(String imageNameInCache) {
        File f = new File(cacheFolder, imageNameInCache);
        return f.exists() ? f : null;
    }

    /**
     * Write a buffered image to the image cache on dist
     * @param image the image
     * @param imageNameInCache the name of the image in the cache
     */
    public void writeImage(BufferedImage image, String imageNameInCache) {
        try {
            File f = new File(cacheFolder, imageNameInCache);
            ImageIO.write(image, "png", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Clears the cache
     */
    public void clear() {
        File[] files = cacheFolder.listFiles();
        if (files == null)
            return;
        for (File f : files)
            f.delete();
    }

}

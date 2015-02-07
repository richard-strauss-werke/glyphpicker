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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingWorker;

import com.aerhard.oxygen.plugin.glyphpicker.model.DataSource;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import org.apache.log4j.Logger;

/**
 * A worker class for bulk loading bitmap images from a list of glyph definitions.
 */
public class BitmapLoadWorker extends
        SwingWorker<List<GlyphDefinition>, Void> {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(BitmapLoadWorker.class.getName());

    /**
     * The executor service.
     */
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /**
     * The glyph definitions.
     */
    private final List<GlyphDefinition> glyphDefinitions;

    /**
     * The maximum scaled height / width of the images.
     */
    private final int size;

    /**
     * The plugin's config directory
     */
    private final ImageCacheAccess imageCacheAccess;

    /**
     * Instantiates a new BitmapLoadWorker.
     * @param glyphDefinitions the glyph definitions
     * @param size             The maximum scaled height / width of the images
     * @param imageCacheAccess         The plugin's config directory
     */
    public BitmapLoadWorker(List<GlyphDefinition> glyphDefinitions,
                            int size, ImageCacheAccess imageCacheAccess) {
        this.glyphDefinitions = glyphDefinitions;
        this.size = size;
        this.imageCacheAccess = imageCacheAccess;
    }

    /**
     * Shuts down the executor service.
     */
    public void shutdownExecutor() {
        executorService.shutdownNow();
    }

    /* (non-Javadoc)
     * @see javax.swing.SwingWorker#doInBackground()
     */
    @Override
    protected List<GlyphDefinition> doInBackground() {

        ImageProcessor imageProcessor = new ImageProcessor();

//        File cacheFolder = new File(configDir, "cache");
//        if (!((cacheFolder.exists() && cacheFolder.isDirectory()) || cacheFolder.mkdirs())) {
//            LOGGER.error(String.format("Could not create cache folder at %s", cacheFolder.toString()));
//        }
//
//        ImageCache imageCache = new ImageCache(configDir);


        for (GlyphDefinition d : glyphDefinitions) {
            if (isCancelled()) {
                return null;
            }

            DataSource dataSource = d.getDataSource();
            String relativePath = d.getUrl();

            // only load bitmaps for glyphDefinitions whose DataSource specifies a
            // DataSource.GLYPH_BITMAP_RENDERER and has a graphic path
            if (DataSource.GLYPH_BITMAP_RENDERER.equals(dataSource
                    .getGlyphRenderer()) && relativePath != null) {

                String basePath = dataSource.getBasePath();

                BitmapLoader loader = (isLocalFile(basePath))
                        ? new BitmapFileLoader(d, basePath, relativePath, imageProcessor, size)
                        : new BitmapUrlLoader(d, basePath, relativePath, imageProcessor, size, imageCacheAccess);

                executorService.submit(new BitmapLoadRunnable(this, d, loader));

            }
        }

        return null;
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

}

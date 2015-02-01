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

/**
 * A worker class for bulk loading bitmap images from a list of glyph definitions.
 */
public class BitmapLoadWorker extends
        SwingWorker<List<GlyphDefinition>, Void> {

    /** The executor service. */
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /** The glyph definitions. */
    private final List<GlyphDefinition> glyphDefinitions;
    
    /** The maximum scaled height / width of the images. */
    private final int size;

    /**
     * Instantiates a new BitmapLoadWorker.
     *
     * @param glyphDefinitions the glyph definitions
     * @param size The maximum scaled height / width of the images
     */
    public BitmapLoadWorker(List<GlyphDefinition> glyphDefinitions,
            int size) {
        this.glyphDefinitions = glyphDefinitions;
        this.size = size;
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

        for (GlyphDefinition gd : glyphDefinitions) {
            if (isCancelled()) {
                return null;
            }
            if (DataSource.DISPLAY_MODE_BITMAP.equals(gd.getDataSource()
                    .getDisplayMode())) {
                float factor = gd.getDataSource().getSizeFactor();
                executorService.submit(new BitmapLoader(this, gd, Math
                        .round(size * factor)));
            }
        }

        return null;
    }

}

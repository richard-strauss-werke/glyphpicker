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

/**
 * The Class BitmapLoadRunnable.
 */
public class BitmapLoadRunnable implements Runnable {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(BitmapLoadRunnable.class.getName());

    /**
     * The worker from which the bitmap loader has been called.
     */
    private final BitmapLoadWorker worker;

    /**
     * The glyph definition containing the path to the bmp.
     */
    private final GlyphDefinition glyphDefinition;

    /**
     * The BitmapLoader loading the image.
     */
    private final BitmapLoader loader;

    /**
     * Instantiates a new bitmap loader.
     *
     * @param worker          The worker from which the bitmap loader has been called
     * @param glyphDefinition The glyph definition containing the path to the bmp
     * @param loader          The BitmapLoader loading the image
     */
    public BitmapLoadRunnable(BitmapLoadWorker worker,
                              GlyphDefinition glyphDefinition, BitmapLoader loader) {
        this.worker = worker;
        this.glyphDefinition = glyphDefinition;
        this.loader = loader;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        if (!worker.isCancelled()) {
            try {
                GlyphBitmapIcon icon = loader.getImage();
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

}

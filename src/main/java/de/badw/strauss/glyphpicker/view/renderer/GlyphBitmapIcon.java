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
package de.badw.strauss.glyphpicker.view.renderer;

import javax.swing.*;
import java.awt.*;

/**
 * An ImageIcon with fixed dimensions.
 */
public class GlyphBitmapIcon extends ImageIcon {

    private static final long serialVersionUID = 1L;

    /**
     * The height and width.
     */
    private final int size;

    /**
     * Instantiates a new GlyphBitmapIcon.
     *
     * @param image the image
     * @param size  the size
     */
    public GlyphBitmapIcon(Image image, int size) {
        super(image);
        this.size = size;
    }

    /* (non-Javadoc)
     * @see javax.swing.ImageIcon#getIconWidth()
     */
    @Override
    public int getIconWidth() {
        return size;
    }

    /* (non-Javadoc)
     * @see javax.swing.ImageIcon#getIconHeight()
     */
    @Override
    public int getIconHeight() {
        return size;
    }

}

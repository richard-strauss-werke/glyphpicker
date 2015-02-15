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
package de.badw.strauss.glyphpicker.view;

import java.util.ResourceBundle;

import ca.odell.glazedlists.gui.TableFormat;

import de.badw.strauss.glyphpicker.model.GlyphDefinition;

/**
 * A class providing information about the format of the glyph table.
 */
public class GlyphTableFormat implements TableFormat<GlyphDefinition> {

    /**
     * The glyph column label.
     */
    private final String glyphLabel;

    /**
     * The description column label.
     */
    private final String descriptionLabel;

    /**
     * Instantiates a new GlyphTableFormat object.
     */
    public GlyphTableFormat() {
        ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
        String className = this.getClass().getSimpleName();
        glyphLabel = i18n.getString(className + ".glyphLabel");
        descriptionLabel = i18n.getString(className + ".descriptionLabel");
    }

    /* (non-Javadoc)
     * @see ca.odell.glazedlists.gui.TableFormat#getColumnCount()
     */
    public int getColumnCount() {
        return 2;
    }

    /* (non-Javadoc)
     * @see ca.odell.glazedlists.gui.TableFormat#getColumnName(int)
     */
    public String getColumnName(int column) {
        if (column == 0) {
            return glyphLabel;
        } else if (column == 1) {
            return descriptionLabel;
        }

        throw new IllegalStateException();
    }

    /* (non-Javadoc)
     * @see ca.odell.glazedlists.gui.TableFormat#getColumnValue(java.lang.Object, int)
     */
    public Object getColumnValue(GlyphDefinition baseObject, int column) {

        if (column == 0 || column == 1) {
            return baseObject;
        }

        throw new IllegalStateException();
    }
}

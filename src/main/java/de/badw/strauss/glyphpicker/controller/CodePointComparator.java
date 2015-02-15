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

package de.badw.strauss.glyphpicker.controller;

import java.io.Serializable;
import java.util.Comparator;

import de.badw.strauss.glyphpicker.model.GlyphDefinition;

/**
 * A Comparator used to sort glyph definitions based on the value of ther codePoint property.
 */
public class CodePointComparator implements Comparator<GlyphDefinition>,
        Serializable {

    private static final long serialVersionUID = 1L;

    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(GlyphDefinition glyphA, GlyphDefinition glyphB) {

        String aString = glyphA.getCodePoint();
        String bString = glyphB.getCodePoint();

        return (aString != null && bString != null) ? aString
                .compareToIgnoreCase(bString) : -1;
    }
}
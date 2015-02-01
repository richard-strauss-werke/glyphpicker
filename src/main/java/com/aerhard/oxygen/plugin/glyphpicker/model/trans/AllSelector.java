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
package com.aerhard.oxygen.plugin.glyphpicker.model.trans;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

/**
 * A PropertySelector returning the most important properties of a glyph definition.
 */
public class AllSelector implements PropertySelector {

    /* (non-Javadoc)
     * @see com.aerhard.oxygen.plugin.glyphpicker.model.trans.PropertySelector#get(com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition)
     */
    @Override
    public String get(GlyphDefinition d) {
        return d.getCodePointString() + " " + d.getId() + " " + d.getRange()
                + " " + d.getCharName();
    }

}

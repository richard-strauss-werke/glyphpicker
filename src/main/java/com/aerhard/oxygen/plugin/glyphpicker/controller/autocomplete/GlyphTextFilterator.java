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
package com.aerhard.oxygen.plugin.glyphpicker.controller.autocomplete;

import java.util.List;

import ca.odell.glazedlists.TextFilterator;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.model.selector.PropertySelector;

/**
 * A TextFilterator implementing filtering on a certain property of a
 * GlyphDefinition selected by the provided PropertySelector.
 */
public class GlyphTextFilterator implements TextFilterator<GlyphDefinition> {

    /** The property selector. */
    private final PropertySelector propertySelector;

    /**
     * Instantiates a new GlyphTextFilterator.
     *
     * @param propertySelector
     *            the property selector
     */
    public GlyphTextFilterator(PropertySelector propertySelector) {
        this.propertySelector = propertySelector;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ca.odell.glazedlists.TextFilterator#getFilterStrings(java.util.List,
     * java.lang.Object)
     */
    @Override
    public void getFilterStrings(List<String> baseList, GlyphDefinition element) {
        baseList.add(propertySelector.get(element));
    }
}
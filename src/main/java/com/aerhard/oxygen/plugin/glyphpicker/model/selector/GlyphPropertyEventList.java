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
package com.aerhard.oxygen.plugin.glyphpicker.model.selector;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.event.ListEvent;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

/**
 * A TransformedList deriving its values by applying a PropertySelector to a GlyphDefinition event list.
 */
public class GlyphPropertyEventList extends
        TransformedList<GlyphDefinition, String> {

    /**
     * The property selector.
     */
    private final PropertySelector propertySelector;

    /**
     * Instantiates a new GlyphPropertyEventList.
     *
     * @param source           the source glyph list
     * @param propertySelector the property selector
     */
    public GlyphPropertyEventList(EventList<GlyphDefinition> source,
                                  PropertySelector propertySelector) {
        super(source);
        this.propertySelector = propertySelector;
        source.addListEventListener(this);
    }

    /* (non-Javadoc)
     * @see ca.odell.glazedlists.TransformedList#get(int)
     */
    public String get(int index) {
        return propertySelector.get(source.get(index));
    }

    /**
     * When the source list changes, propagate the exact same changes for the
     * transformed list.
     *
     * @param listChanges the list changes
     */
    public void listChanged(ListEvent<GlyphDefinition> listChanges) {
        updates.forwardEvent(listChanges);
    }

    /**
     * {@inheritDoc}
     */
    protected boolean isWritable() {
        return false;
    }
}
package com.aerhard.oxygen.plugin.glyphpicker.model.trans;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.event.ListEvent;

public class TransformedGlyphList extends
        TransformedList<GlyphDefinition, String> {

    private PropertySelector propertySelector;

    public TransformedGlyphList(EventList<GlyphDefinition> source,
            PropertySelector propertySelector) {
        super(source);
        this.propertySelector = propertySelector;
        source.addListEventListener(this);
    }

    public String get(int index) {
        return propertySelector.get(source.get(index));
    }

    /**
     * When the source list changes, propogate the exact same changes for the
     * transformed list.
     */
    public void listChanged(ListEvent<GlyphDefinition> listChanges) {
        updates.forwardEvent(listChanges);
    }

    /** {@inheritDoc} */
    protected boolean isWritable() {
        return false;
    }
}
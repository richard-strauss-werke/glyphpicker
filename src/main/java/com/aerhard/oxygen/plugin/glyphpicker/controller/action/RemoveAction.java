package com.aerhard.oxygen.plugin.glyphpicker.controller.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;

public class RemoveAction extends AbstractPickerAction {
    private static final long serialVersionUID = 1L;
    private final EventList<GlyphDefinition> glyphList;
    private final FilterList<GlyphDefinition> filterList;
    private final GlyphGrid list;

    private static final String className = RemoveAction.class.getSimpleName();

    public RemoveAction(PropertyChangeListener listener,
            EventList<GlyphDefinition> glyphList,
            FilterList<GlyphDefinition> filterList, GlyphGrid list) {
        super(className, "/images/minus.png");
        this.addPropertyChangeListener(listener);
        this.glyphList = glyphList;
        this.filterList = filterList;
        this.list = list;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int index = list.getSelectedIndex();
        if (index != -1) {
            firePropertyChange("listInSync", null, false);
            GlyphDefinition item = filterList.get(index);

            boolean itemRemoved = glyphList.remove(item);

            if (itemRemoved) {
                index = Math.min(index, glyphList.size() - 1);
                if (index >= 0) {
                    list.setSelectedIndex(index);
                }
            }
        }
    }
}
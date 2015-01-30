package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;

public class RemoveFromUserCollectionAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private EventList<GlyphDefinition> glyphList;
    private FilterList<GlyphDefinition> filterList;
    private GlyphGrid list;

    public RemoveFromUserCollectionAction(PropertyChangeListener listener,
            EventList<GlyphDefinition> glyphList,
            FilterList<GlyphDefinition> filterList, GlyphGrid list) {
        super(null, new ImageIcon(
                ChangeViewAction.class.getResource("/images/minus.png")));

        this.addPropertyChangeListener(listener);
        this.glyphList = glyphList;
        this.filterList = filterList;
        this.list = list;
        
        String mnemonic = "R";

        putValue(SHORT_DESCRIPTION,
                "Remove the selected glyph (Alt+"+mnemonic+")");
        putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
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
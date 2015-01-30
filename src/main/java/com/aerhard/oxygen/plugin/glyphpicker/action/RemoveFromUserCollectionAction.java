package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

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

    private static ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
    private static String className = RemoveFromUserCollectionAction.class.getSimpleName();
    
    public RemoveFromUserCollectionAction(PropertyChangeListener listener,
            EventList<GlyphDefinition> glyphList,
            FilterList<GlyphDefinition> filterList, GlyphGrid list) {
        super(i18n.getString(className + ".label"), new ImageIcon(
                ChangeViewAction.class.getResource("/images/minus.png")));

        this.addPropertyChangeListener(listener);
        this.glyphList = glyphList;
        this.filterList = filterList;
        this.list = list;
        
        String description = i18n.getString(className + ".description");
        String mnemonic = i18n.getString(className + ".mnemonic");
        
        putValue(SHORT_DESCRIPTION,
                description + " (Alt+"+mnemonic+")");
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
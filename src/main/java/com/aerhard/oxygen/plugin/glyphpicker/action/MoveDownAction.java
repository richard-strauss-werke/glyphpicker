package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;

import ca.odell.glazedlists.EventList;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;
import com.aerhard.oxygen.plugin.glyphpicker.view.GlyphGrid;

public class MoveDownAction extends AbstractPickerAction {
    private static final long serialVersionUID = 1L;
    private EventList<GlyphDefinition> glyphList;
    private GlyphGrid list;

    private static String className = MoveDownAction.class.getSimpleName();
    
    public MoveDownAction(PropertyChangeListener listener,
            EventList<GlyphDefinition> glyphList, GlyphGrid list) {
        super(i18n.getString(className + ".label"), new ImageIcon(
                MoveDownAction.class.getResource("/images/arrow-270.png")));

        this.addPropertyChangeListener(listener);
        this.glyphList = glyphList;
        this.list = list;
        
        String description = i18n.getString(className + ".description");
        
        putValue(SHORT_DESCRIPTION, description + " ("+modifierName+"+↓)");
        putValue(MNEMONIC_KEY, KeyEvent.VK_DOWN);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int index = list.getSelectedIndex();
        if (index != -1 && index < glyphList.size() - 1) {

            firePropertyChange("listInSync", null, false);
            GlyphDefinition item = glyphList.get(index);

            boolean itemRemoved = glyphList.remove(item);

            if (itemRemoved) {
                glyphList.add(index + 1, item);
                list.setSelectedIndex(index + 1);
            }
        }
    }
}

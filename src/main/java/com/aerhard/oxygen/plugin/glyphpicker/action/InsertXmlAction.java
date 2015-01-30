package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import ca.odell.glazedlists.swing.DefaultEventSelectionModel;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class InsertXmlAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private DefaultEventSelectionModel<GlyphDefinition> selectionModel;

    private static ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
    private static String className = InsertXmlAction.class.getSimpleName();
    
    public InsertXmlAction(PropertyChangeListener listener,
            DefaultEventSelectionModel<GlyphDefinition> selectionModel) {
        super(i18n.getString(className + ".label"), new ImageIcon(
                InsertXmlAction.class
                        .getResource("/images/tick.png")));
        
        addPropertyChangeListener(listener);
        this.selectionModel = selectionModel;
        
        String description = i18n.getString(className + ".description");
        String mnemonic = i18n.getString(className + ".mnemonic");
        
        putValue(SHORT_DESCRIPTION,
                description + " (Alt+"+mnemonic+")");
        putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!selectionModel.isSelectionEmpty()) {
            GlyphDefinition d = selectionModel.getSelected().get(0);
            if (d != null) {
                firePropertyChange("insert", null, d);
            }
        }
    }
    
}
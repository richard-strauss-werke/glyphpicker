package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import ca.odell.glazedlists.swing.DefaultEventSelectionModel;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class InsertXmlAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private DefaultEventSelectionModel<GlyphDefinition> selectionModel;

    public InsertXmlAction(PropertyChangeListener listener,
            DefaultEventSelectionModel<GlyphDefinition> selectionModel) {
        super(null, new ImageIcon(
                InsertXmlAction.class
                        .getResource("/images/blue-document-import.png")));
        
        addPropertyChangeListener(listener);
        this.selectionModel = selectionModel;
        
        String mnemonic = "I";
        
        putValue(SHORT_DESCRIPTION,
                "Insert the selected glyph to the document (Alt+"+mnemonic+")");
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
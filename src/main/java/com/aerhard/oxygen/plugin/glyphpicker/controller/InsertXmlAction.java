package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import ca.odell.glazedlists.swing.DefaultEventSelectionModel;

import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

public class InsertXmlAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private Controller controller;
    private DefaultEventSelectionModel<GlyphDefinition> selectionModel;

    public InsertXmlAction(Controller controller,
            DefaultEventSelectionModel<GlyphDefinition> selectionModel) {
        super(null, new ImageIcon(
                BrowserController.class
                        .getResource("/images/blue-document-import.png")));
        
        this.controller = controller;
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
                controller.fireEvent("insert", d);
            }
        }
    }
    
}
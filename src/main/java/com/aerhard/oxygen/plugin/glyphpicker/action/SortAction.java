package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

public class SortAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    public SortAction() {
        super(null, new ImageIcon(
                SortAction.class.getResource("/images/sort-number.png")));
        
        String mnemonic = "O";
        
        putValue(SHORT_DESCRIPTION, "Sort glyphs by code point (Alt+"+mnemonic+")");
        putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}

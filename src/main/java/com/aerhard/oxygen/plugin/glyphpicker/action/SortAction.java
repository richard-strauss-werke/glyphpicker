package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

public class SortAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    private static ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
    private static String className = SortAction.class.getSimpleName();
    
    public SortAction() {
        super(i18n.getString(className + ".label"), new ImageIcon(
                SortAction.class.getResource("/images/sort-number.png")));
        
        String description = i18n.getString(className + ".description");
        String mnemonic = i18n.getString(className + ".mnemonic");
        
        putValue(SHORT_DESCRIPTION,
                description + " (Alt+"+mnemonic+")");
        putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}

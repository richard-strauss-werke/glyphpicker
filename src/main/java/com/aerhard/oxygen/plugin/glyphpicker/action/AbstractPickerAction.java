package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

public abstract class AbstractPickerAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    protected static final ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
    protected static final String modifierName = (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0) ? "Option" : "Alt";

    public AbstractPickerAction(String className) {
        super(i18n.getString(className + ".label"));

        String description = i18n.getString(className + ".description");
        String mnemonic = i18n.getString(className + ".mnemonic");
        
        putValue(SHORT_DESCRIPTION,
                description + " ("+modifierName+"+"+mnemonic+")");
        putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    }
    
    public AbstractPickerAction(String className, String icon) {
        super(i18n.getString(className + ".label"), new ImageIcon(
                AbstractPickerAction.class.getResource(icon)));

        String description = i18n.getString(className + ".description");
        String mnemonic = i18n.getString(className + ".mnemonic");
        
        putValue(SHORT_DESCRIPTION,
                description + " ("+modifierName+"+"+mnemonic+")");
        putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    }

    public AbstractPickerAction(String label, ImageIcon icon) {
        super(label, icon);
    }

}
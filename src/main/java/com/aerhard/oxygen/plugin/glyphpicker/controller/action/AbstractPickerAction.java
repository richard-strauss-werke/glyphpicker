package com.aerhard.oxygen.plugin.glyphpicker.controller.action;

import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

public abstract class AbstractPickerAction extends AbstractAction {
    private static final long serialVersionUID = 1L;

    protected static final ResourceBundle I18N = ResourceBundle
            .getBundle("GlyphPicker");
    static final String MODIFIER_NAME = (System
            .getProperty("os.name").toLowerCase().contains("mac")) ? "Option"
            : "Alt";

    public AbstractPickerAction(String className) {
        super(I18N.getString(className + ".label"));

        String description = I18N.getString(className + ".description");
        String mnemonic = I18N.getString(className + ".mnemonic");

        putValue(SHORT_DESCRIPTION, description + " (" + MODIFIER_NAME + "+"
                + mnemonic + ")");
        putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    }

    public AbstractPickerAction(String className, String icon) {
        super(I18N.getString(className + ".label"), new ImageIcon(
                AbstractPickerAction.class.getResource(icon)));

        String description = I18N.getString(className + ".description");
        String mnemonic = I18N.getString(className + ".mnemonic");

        putValue(SHORT_DESCRIPTION, description + " (" + MODIFIER_NAME + "+"
                + mnemonic + ")");
        putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    }

    public AbstractPickerAction(String label, ImageIcon icon) {
        super(label, icon);
    }

}
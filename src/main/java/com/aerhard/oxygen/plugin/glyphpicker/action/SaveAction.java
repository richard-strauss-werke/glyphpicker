package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

public class SaveAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private Set<Action> actions;

    private static ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
    private static String className = SaveAction.class.getSimpleName();
    
    public SaveAction(PropertyChangeListener listener, Set<Action> actions) {
        super(i18n.getString(className + ".label"), new ImageIcon(
                SaveAction.class
                        .getResource("/images/disk.png")));
        
        this.addPropertyChangeListener(listener);
        this.actions = actions;
        
        String description = i18n.getString(className + ".description");
        String mnemonic = i18n.getString(className + ".mnemonic");
        
        putValue(SHORT_DESCRIPTION,
                description + " (Alt+"+mnemonic+")");
        putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        firePropertyChange("saveData", null, null);
        for (Action action : actions) {
            action.setEnabled(false);
        }
    }
}
package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

public class SaveAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private Set<Action> actions;

    public SaveAction(PropertyChangeListener listener, Set<Action> actions) {
        super(null, new ImageIcon(
                SaveAction.class
                        .getResource("/images/disk.png")));
        
        this.addPropertyChangeListener(listener);
        this.actions = actions;
        
        String mnemonic = "S";
        
        putValue(SHORT_DESCRIPTION, "Save the User Collection (Alt+"+mnemonic+")");
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
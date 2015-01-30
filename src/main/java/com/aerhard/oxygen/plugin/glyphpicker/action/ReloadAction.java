package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

public class ReloadAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private Set<Action> actions;

    public ReloadAction(PropertyChangeListener listener, Set<Action> actions) {
        super(null, new ImageIcon(
                ChangeViewAction.class
                        .getResource("/images/arrow-circle-225-left.png")));
        
        addPropertyChangeListener(listener);
        this.actions = actions;
        
        String mnemonic = "L";
        
        putValue(SHORT_DESCRIPTION, "Reload the User Collection (Alt+"+mnemonic+")");
        putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        firePropertyChange("reload", null, null);
        for (Action action : actions) {
            action.setEnabled(false);
        }
    }
}
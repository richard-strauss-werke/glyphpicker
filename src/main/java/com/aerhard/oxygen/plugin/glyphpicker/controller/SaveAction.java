package com.aerhard.oxygen.plugin.glyphpicker.controller;

import java.awt.event.ActionEvent;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

public class SaveAction extends AbstractAction {
    private static final long serialVersionUID = 1L;
    private Set<Action> actions;
    private Controller controller;

    public SaveAction(Controller controller, Set<Action> actions) {
        super(null, new ImageIcon(
                UserCollectionController.class
                        .getResource("/images/disk.png")));
        
        this.controller = controller;
        this.actions = actions;
        
        String mnemonic = "S";
        
        putValue(SHORT_DESCRIPTION, "Save the User Collection (Alt+"+mnemonic+")");
        putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        controller.saveData();
        for (Action action : actions) {
            action.setEnabled(false);
        }
    }
}
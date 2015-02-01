package com.aerhard.oxygen.plugin.glyphpicker.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

import javax.swing.Action;

public class SaveAction extends AbstractPickerAction {
    private static final long serialVersionUID = 1L;
    private Set<Action> actions;

    private static String className = SaveAction.class.getSimpleName();

    public SaveAction(PropertyChangeListener listener, Set<Action> actions) {
        super(className, "/images/disk.png");
        this.addPropertyChangeListener(listener);
        this.actions = actions;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        firePropertyChange("saveData", null, null);
        for (Action action : actions) {
            action.setEnabled(false);
        }
    }
}
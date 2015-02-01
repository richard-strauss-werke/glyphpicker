package com.aerhard.oxygen.plugin.glyphpicker.controller.action;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

import javax.swing.Action;

public class ReloadAction extends AbstractPickerAction {
    private static final long serialVersionUID = 1L;
    private final Set<Action> actions;

    private static final String className = ReloadAction.class.getSimpleName();

    public ReloadAction(PropertyChangeListener listener, Set<Action> actions) {
        super(className, "/images/arrow-circle-225-left.png");
        addPropertyChangeListener(listener);
        this.actions = actions;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        firePropertyChange("reload", null, null);
        for (Action action : actions) {
            action.setEnabled(false);
        }
    }
}
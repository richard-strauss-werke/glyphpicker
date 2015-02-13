/**
 * Copyright 2015 Alexander Erhard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aerhard.oxygen.plugin.glyphpicker.controller.action;

import ro.sync.ui.Icons;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

import javax.swing.Action;

/**
 * An action to reload the list model.
 */
public class ReloadAction extends AbstractPickerAction {
    
    private static final long serialVersionUID = 1L;

    /** The key of the action. */
    public static final String KEY = "reload";

    /** The actions affected by this action. */
    private final Set<Action> actions;

    /** The name of the class. */
    private static final String CLASS_NAME = ReloadAction.class.getSimpleName();

    /**
     * Instantiates a new ReloadAction.
     *
     * @param listener the property change listener to be added to this action
     * @param actions The actions affected by this action
     */
    public ReloadAction(PropertyChangeListener listener, Set<Action> actions) {
        super(CLASS_NAME, Icons.REFRESH);
        addPropertyChangeListener(listener);
        this.actions = actions;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        firePropertyChange(KEY, null, null);
        for (Action action : actions) {
            action.setEnabled(false);
        }
    }
}
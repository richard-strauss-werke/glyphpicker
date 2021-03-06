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
package de.badw.strauss.glyphpicker.controller.action;

import de.badw.strauss.glyphpicker.view.TabPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

/**
 * An action to reload the list model.
 */
public class ReloadAction extends AbstractPickerAction {

    /**
     * The key of the action.
     */
    public static final String KEY = "reload";
    private static final long serialVersionUID = 1L;
    /**
     * The name of the class.
     */
    private static final String CLASS_NAME = ReloadAction.class.getSimpleName();
    /**
     * The actions affected by this action.
     */
    private final Set<Action> actions;

    /**
     * Instantiates a new ReloadAction.
     *
     * @param panel    The container tab panel
     * @param listener the property change listener to be added to this action
     * @param actions  The actions affected by this action
     */
    public ReloadAction(TabPanel panel, PropertyChangeListener listener, Set<Action> actions) {
        super(CLASS_NAME, "/images/oxygen/Refresh16.png", "ctrl L");
        addPropertyChangeListener(listener);
        this.actions = actions;
        bindAcceleratorToComponent(this, panel);
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
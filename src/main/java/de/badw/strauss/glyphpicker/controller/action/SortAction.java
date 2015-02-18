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

import com.jidesoft.swing.JideToggleButton;
import de.badw.strauss.glyphpicker.view.TabPanel;
import ro.sync.ui.Icons;

import java.awt.event.ActionEvent;

/**
 * An action to trigger sorting of a glyph list model.
 */
public class SortAction extends AbstractPickerAction {

    private static final long serialVersionUID = 1L;

    /**
     * The name of the class.
     */
    private static final String CLASS_NAME = SortAction.class.getSimpleName();

    /**
     * the toggle button from which the toggle state is read
     */
    private final JideToggleButton button;

    /**
     * Instantiates a new SortAction.
     *
     * @param panel  The container tab panel
     * @param button the toggle button from which the toggle state is read
     */
    public SortAction(TabPanel panel, JideToggleButton button) {
        super(CLASS_NAME, Icons.SORT_ASCENDING_MENU, "ctrl O");
        this.button = button;
        bindAcceleratorToComponent(this, panel);
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // select the button *only* if the action has been triggered by pressing the accelerator
        // combination
        if (e.getActionCommand() != null) {
            button.setSelected(!button.isSelected());
        }
    }
}

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

package com.aerhard.oxygen.plugin.glyphpicker;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import com.aerhard.oxygen.plugin.glyphpicker.controller.action.AbstractPickerAction;
import com.aerhard.oxygen.plugin.glyphpicker.view.MainPanel;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

/**
 * An action to show or hide the GlyphPicker panel in oXygen.
 */
public class ToggleWindowAction extends AbstractPickerAction {
    
    private static final long serialVersionUID = 1L;

    /** oXygen's plugin workspace */
    private final StandalonePluginWorkspace workspace;

    /** The plugin's view ID in oXygen. */
    private final String viewId;

    private MainPanel mainPanel;

    /** The name of the current class. */
    private static final String className = ToggleWindowAction.class
            .getSimpleName();

    /**
     * Instantiates a new ToggleWindowAction.
     *
     * @param workspace oXygen's plugin workspace
     * @param icon The plugin's icon
     * @param viewId The plugin's view ID in oXygen
     */
    public ToggleWindowAction(StandalonePluginWorkspace workspace,
                              String icon, String viewId, MainPanel mainPanel) {
        super("GyphPicker", new ImageIcon(
                ToggleWindowAction.class.getResource(icon)));

        this.workspace = workspace;
        this.viewId = viewId;
        this.mainPanel = mainPanel;

        String description = I18N.getString(className + ".description");

        putValue(SHORT_DESCRIPTION, description);

        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (workspace.isViewShowing(viewId)) {
            mainPanel.requestFocusInWindow();
        } else {
            workspace.showView(viewId, true);
        }
    }
}
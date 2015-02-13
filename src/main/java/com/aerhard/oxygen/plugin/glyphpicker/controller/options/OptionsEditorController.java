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
package com.aerhard.oxygen.plugin.glyphpicker.controller.options;

import com.aerhard.oxygen.plugin.glyphpicker.controller.action.AbstractPickerAction;
import com.aerhard.oxygen.plugin.glyphpicker.controller.bitmap.ImageCacheAccess;
import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import com.aerhard.oxygen.plugin.glyphpicker.view.options.OptionsEditor;
import ro.sync.ui.Icons;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

public class OptionsEditorController {

    /**
     * The window's content pane.
     */
    private final OptionsEditor contentPane;

    /**
     * The panel from which the window has been opened.
     */
    private final JPanel parentPanel;

    /**
     * the ImageCacheAccess object
     */
    private final ImageCacheAccess imageCacheAccess;

    /**
     * The i18n resource bundle.
     */
    private final ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");

    /**
     * The plugin's config
     */
    private final Config config;

    /**
     * The action to clear the cache;
     */
    private ClearCacheAction clearCacheAction;

    /**
     * Initializes a new OptionsEditorController
     * @param contentPane the content pane of the the editor popup
     * @param parentPanel the panel
     * @param config the plugin's config
     * @param imageCacheAccess the panel from which the window has been opened
     */
    public OptionsEditorController(final OptionsEditor contentPane, JPanel parentPanel, Config config, final ImageCacheAccess imageCacheAccess) {
        this.contentPane = contentPane;
        this.parentPanel = parentPanel;
        this.config = config;
        this.imageCacheAccess = imageCacheAccess;

    }

    public String load() {

        contentPane.getShortcutTextField().setText(config.getShortcut());
        contentPane.updateCacheItemCount(imageCacheAccess.getSize());

        contentPane.getApplyShortcutButton().requestFocusInWindow();

        Action applyShortcutAction = new ApplyShortcutAction();
        clearCacheAction = new ClearCacheAction();
        updateCacheActionState(imageCacheAccess.getSize());

        contentPane.getApplyShortcutButton().setAction(applyShortcutAction);

        contentPane.getClearCacheButton().setAction(clearCacheAction);

        contentPane.getShortcutTextField().getDocument().addDocumentListener(
                new ShortcutFieldInputHandler(config, applyShortcutAction));

        PropertyChangeListener imageCacheListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (ImageCacheAccess.IMAGE_STORED.equals(e.getPropertyName())) {
                    int imageCacheSize = imageCacheAccess.getSize();
                    contentPane.updateCacheItemCount(imageCacheSize);
                    updateCacheActionState(imageCacheSize);
                }

                else if (ImageCacheAccess.CACHE_CLEARED.equals(e.getPropertyName())) {
                    contentPane.updateCacheItemCount(0);
                    updateCacheActionState(0);
                }

            }
        };

        imageCacheAccess.addPropertyChangeListener(imageCacheListener);

        JOptionPane.showOptionDialog(parentPanel, contentPane,
                i18n.getString("OptionsEditorController.frameTitle"),
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, new String[]{i18n.getString("OptionsEditorController.close")}, null);

        imageCacheAccess.removePropertyChangeListener(imageCacheListener);

        return null;
    }

    private void updateCacheActionState(int imageCacheSize) {
        if (imageCacheSize == 0 && clearCacheAction.isEnabled()) {
            clearCacheAction.setEnabled(false);
        } else if (imageCacheSize != 0 && !clearCacheAction.isEnabled()) {
            clearCacheAction.setEnabled(true);
        }
    }

    /**
     * An action to apply a new shortcut string.
     */
    private final class ApplyShortcutAction extends AbstractPickerAction {

        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new ApplyShortcutAction.
         */
        private ApplyShortcutAction() {
            super(ApplyShortcutAction.class.getSimpleName(), Icons.ACCEPT_CHANGE_MENU);
            setEnabled(false);
        }

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            config.setShortcut(contentPane.getShortcutTextField().getText());
            setEnabled(false);
        }
    }

    /**
     * An action to trigger clearing the image cache.
     */
    private final class ClearCacheAction extends AbstractPickerAction {

        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new ClearCacheAction.
         */
        private ClearCacheAction() {
            super(ClearCacheAction.class.getSimpleName(), Icons.REMOVE_FROM_DISK_MENU);
        }

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            imageCacheAccess.clear();
        }
    }
}

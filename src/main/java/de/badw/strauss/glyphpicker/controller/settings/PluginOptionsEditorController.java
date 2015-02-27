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
package de.badw.strauss.glyphpicker.controller.settings;

import de.badw.strauss.glyphpicker.controller.action.AbstractPickerAction;
import de.badw.strauss.glyphpicker.controller.bitmap.ImageCache;
import de.badw.strauss.glyphpicker.model.Config;
import de.badw.strauss.glyphpicker.view.settings.OptionsEditor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PluginOptionsEditorController {

    /**
     * the ImageCache object
     */
    private final ImageCache imageCache;

    private final PropertyChangeListener imageCacheListener;

    /**
     * The action to clear the cache;
     */
    private ClearCacheAction clearCacheAction;

    /**
     * Initializes a new PluginOptionsEditorController
     *  @param contentPane the content pane of the the editor popup
     * @param config      the plugin's config
     * @param imageCache  the panel from which the window has been opened
     * @param applyAction the action associated with the dialog's apply button
     */
    public PluginOptionsEditorController(final OptionsEditor contentPane, final Config config, final ImageCache imageCache, final Action applyAction) {

        this.imageCache = imageCache;

        contentPane.getShortcutTextField().setText(config.getShortcut());
        contentPane.updateCacheItemCount(imageCache.getSize());

        contentPane.getApplyShortcutButton().requestFocusInWindow();

        clearCacheAction = new ClearCacheAction();
        updateCacheActionState(imageCache.getSize());

        contentPane.getClearCacheButton().setAction(clearCacheAction);

        contentPane.getShortcutTextField().getDocument().addDocumentListener(
                new DocumentListener(){
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        applyAction.setEnabled(true);
                    }
                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        applyAction.setEnabled(true);
                    }
                    @Override
                    public void changedUpdate(DocumentEvent e) {}
                });

        contentPane.getTransferFocusCheckBox().setSelected(config.shouldTransferFocusAfterInsert());

        contentPane.getTransferFocusCheckBox().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                applyAction.setEnabled(true);
            }
        });

        imageCacheListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if (ImageCache.IMAGE_STORED.equals(e.getPropertyName())) {
                    int imageCacheSize = imageCache.getSize();
                    contentPane.updateCacheItemCount(imageCacheSize);
                    updateCacheActionState(imageCacheSize);
                } else if (ImageCache.CACHE_CLEARED.equals(e.getPropertyName())) {
                    contentPane.updateCacheItemCount(0);
                    updateCacheActionState(0);
                }

            }
        };

    }

    /**
     * sets the image cache listener
     */
    public void setImageCacheListener() {
        imageCache.addPropertyChangeListener(imageCacheListener);
    }

    /**
     * removes the image cache listener
     */
    public void removeImageCacheListener() {
        imageCache.removePropertyChangeListener(imageCacheListener);
    }

    private void updateCacheActionState(int imageCacheSize) {
        if (imageCacheSize == 0 && clearCacheAction.isEnabled()) {
            clearCacheAction.setEnabled(false);
        } else if (imageCacheSize != 0 && !clearCacheAction.isEnabled()) {
            clearCacheAction.setEnabled(true);
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
            super(ClearCacheAction.class.getSimpleName(), "/images/oxygen/TrashIcon16.gif");
        }

        /* (non-Javadoc)
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            imageCache.clear();
        }
    }
}

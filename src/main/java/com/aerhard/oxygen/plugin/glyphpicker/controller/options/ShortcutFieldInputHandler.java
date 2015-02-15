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

import com.aerhard.oxygen.plugin.glyphpicker.model.Config;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 * A DocumentListener handling updates to the shortcut text field
 */
public class ShortcutFieldInputHandler implements DocumentListener {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(ShortcutFieldInputHandler.class.getName());

    /**
     * the plugin config
     */
    private final Config config;

    /**
     * The apply-shortcut button
     */
    private final Action applyShortcutAction;

    /**
     * Instantiates a new GlyphSelect object.
     *
     * @param config              the plugin config
     * @param applyShortcutAction the apply-shortcut button
     */
    public ShortcutFieldInputHandler(Config config, Action applyShortcutAction) {
        this.config = config;
        this.applyShortcutAction = applyShortcutAction;
    }

    /**
     * Enables the apply-shortcut action if the shortcut text field text doesn't match the shortcut in the config object.
     * Disables the action if it matches.
     *
     * @param e the document event
     */
    public void updateAction(final DocumentEvent e) {
        try {
            String text = e.getDocument().getText(0, e.getDocument().getLength());
            applyShortcutAction.setEnabled(!text.equals(config.getShortcut()));
        } catch (BadLocationException e1) {
            LOGGER.info(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.
     * DocumentEvent)
     */
    @Override
    public void changedUpdate(DocumentEvent e) {
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.
     * DocumentEvent)
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        updateAction(e);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.
     * DocumentEvent)
     */
    @Override
    public void removeUpdate(DocumentEvent e) {
        updateAction(e);
    }
}

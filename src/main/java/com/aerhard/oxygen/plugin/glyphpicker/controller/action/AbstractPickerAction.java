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

import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

/**
 * An abstract action with i18n support; used as superclass of all GlyphPicker
 * actions.
 */
public abstract class AbstractPickerAction extends AbstractAction {

    private static final long serialVersionUID = 1L;

    /** The Constant i18n resource bundle. */
    protected static final ResourceBundle I18N = ResourceBundle
            .getBundle("GlyphPicker");

    /** The OS dependent name of the Option / Alt key. */
    static final String MODIFIER_NAME = (System.getProperty("os.name")
            .toLowerCase().contains("mac")) ? "Option" : "Alt";

    /**
     * Instantiates a new AbstractPickerAction.
     *
     * @param className
     *            the name of the subclass; used to identify the action's label,
     *            description and mnemonic string in the language resource
     *            bundle
     */
    public AbstractPickerAction(String className) {
        super(I18N.getString(className + ".label"));

        String description = I18N.getString(className + ".description");
        String mnemonic = I18N.getString(className + ".mnemonic");

        putValue(SHORT_DESCRIPTION, description + " (" + MODIFIER_NAME + "+"
                + mnemonic + ")");
        putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    }

    /**
     * Instantiates a new AbstractPickerAction.
     *
    * @param className
     *            the name of the subclass; used to identify the action's label,
     *            description and mnemonic string in the language resource
     *            bundle
     * @param icon
     *            the icon path; may be one of the constants defined in ro.sync.ui.Icons
     */
    public AbstractPickerAction(String className, String icon) {
//        super(I18N.getString(className + ".label"), new ImageIcon(
//                AbstractPickerAction.class.getResource(icon)));

        super(I18N.getString(className + ".label"), Icons.getIcon(icon));

        String description = I18N.getString(className + ".description");
        String mnemonic = I18N.getString(className + ".mnemonic");

        putValue(SHORT_DESCRIPTION, description + " (" + MODIFIER_NAME + "+"
                + mnemonic + ")");
        putValue(MNEMONIC_KEY, KeyStroke.getKeyStroke(mnemonic).getKeyCode());
    }

    /**
     * Instantiates a new AbstractPickerAction.
     *
     * @param label
     *            the action's label
     * @param icon
     *            the action's icon
     */
    public AbstractPickerAction(String label, ImageIcon icon) {
        super(label, icon);
    }

}
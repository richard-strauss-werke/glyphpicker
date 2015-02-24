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

import org.apache.log4j.Logger;

import javax.swing.*;
import java.util.ResourceBundle;

/**
 * An abstract action with i18n support; used as superclass of all GlyphPicker
 * actions.
 */
public abstract class AbstractPickerAction extends AbstractAction {

    /**
     * Indicates if the program is run on a MAC
     */
    public static final boolean IS_MAC = (System.getProperty("os.name")
            .toLowerCase().contains("mac"));
    /**
     * The Constant i18n resource bundle.
     */
    protected static final ResourceBundle I18N = ResourceBundle
            .getBundle("GlyphPicker");
    public static final String MENU_SHORTCUT_NAME = (IS_MAC) ?
            I18N.getString("AbstractPickerAction.commandKey") :
            I18N.getString("AbstractPickerAction.controlKey");
    private static final long serialVersionUID = 1L;
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger
            .getLogger(AbstractPickerAction.class.getName());

    /**
     * Instantiates a new AbstractPickerAction.
     *
     * @param className the name of the subclass; used to identify the action's label,
     *                  description and mnemonic string in the language resource
     *                  bundle
     * @param icon      the icon path
     */
    public AbstractPickerAction(String className, String icon) {
        super(I18N.getString(className + ".label"), new ImageIcon(
                AbstractPickerAction.class.getResource(icon)));
        String description = I18N.getString(className + ".description");
        putValue(SHORT_DESCRIPTION, description);
    }

    /**
     * Instantiates a new AbstractPickerAction.
     *
     * @param accelerator the keyboard accelerator string in Windows / Linux nomenclature
     */
    public AbstractPickerAction(String accelerator) {
        String osIndependentAccelerator = (IS_MAC) ? accelerator.replace("ctrl", "meta") : accelerator;
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(osIndependentAccelerator));
    }

    /**
     * Instantiates a new AbstractPickerAction.
     *
     * @param className   the name of the subclass; used to identify the action's label and description
     *                    in the language resource bundle
     * @param icon        the icon path
     * @param accelerator the keyboard accelerator string in Windows / Linux nomenclature
     */
    public AbstractPickerAction(String className, String icon, String accelerator) {
        super(I18N.getString(className + ".label"), new ImageIcon(
                AbstractPickerAction.class.getResource(icon)));

        String description = I18N.getString(className + ".description");

        String osIndependentAccelerator = (IS_MAC) ? accelerator.replace("ctrl", "meta") : accelerator;

        String keyStrokeLabel = accelerator.replace("ctrl ", MENU_SHORTCUT_NAME + "+");

        putValue(SHORT_DESCRIPTION, description + " (" + keyStrokeLabel + ")");
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(osIndependentAccelerator));
    }

    /**
     * Instantiates a new AbstractPickerAction.
     *
     * @param label the action's label
     * @param icon  the action's icon
     */
    public AbstractPickerAction(String label, ImageIcon icon) {
        super(label, icon);
    }

    /**
     * Binds the action's accelerator to the action; the scope is the specified component
     * and all descendant components
     *
     * @param component the component
     * @param action    the action
     */
    public static void bindAcceleratorToComponent(Action action, JComponent component) {
        String className = action.getClass().getName();
        Object keyStroke = action.getValue(Action.ACCELERATOR_KEY);
        if (keyStroke instanceof KeyStroke) {
            component.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put((KeyStroke) keyStroke,
                    className);
            component.getActionMap().put(className, action);
        } else {
            LOGGER.error("Action key stroke is no instance of " + KeyStroke.class.getName());
        }
    }

}
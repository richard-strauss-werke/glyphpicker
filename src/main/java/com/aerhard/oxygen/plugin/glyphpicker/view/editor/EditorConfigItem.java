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
package com.aerhard.oxygen.plugin.glyphpicker.view.editor;

import javax.swing.JComponent;

/**
 * A class providing config information about form controls in the editor window.
 */
public class EditorConfigItem {
    
    /** The form control's component. */
    private final JComponent component;
    
    /** The form control's label. */
    private final String label;

    /**
     * Instantiates a new EditorConfigItem.
     *
     * @param label the label
     * @param component the component
     */
    public EditorConfigItem(String label, JComponent component) {
        this.component = component;
        this.label = label;
    }

    /**
     * Gets the component.
     *
     * @return the component
     */
    public JComponent getComponent() {
        return component;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }
}

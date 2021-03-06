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
package de.badw.strauss.glyphpicker.model.settings;

import javax.swing.*;

/**
 * A class providing config information about form controls in the editor window.
 */
public class FormItemConfig {

    /**
     * The form control's component.
     */
    private final JComponent component;

    /**
     * The form control's label.
     */
    private final String label;

    /**
     * the x coordinate of the item in the grid
     */
    private final int x;
    /**
     * the y coordinate of the item in the grid
     */
    private final int y;
    /**
     * the width of the item in the grid
     */
    private final int width;

    /**
     * Instantiates a new FormItemConfig.
     *
     * @param label     the label
     * @param component the component
     * @param x         the x coordinate of the item in the grid
     * @param y         the y coordinate of the item in the grid
     * @param width     the width of the item in the grid
     */
    public FormItemConfig(String label, JComponent component, int x, int y, int width) {
        this.component = component;
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
    }

    /**
     * gets the x coordinate of the item in the grid
     *
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * gets the y coordinate of the item in the grid
     *
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * gets the width of the item in the grid
     *
     * @return the width
     */
    public int getWidth() {
        return width;
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

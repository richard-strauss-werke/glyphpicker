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

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

/**
 * An action to open the data source editor and process the results if editing
 * has occurred.
 */
public class OptionsAction extends AbstractPickerAction {

    private static final long serialVersionUID = 1L;

    /**
     * The panel from which the data source editor has been opened.
     */
    private final JPanel panel;

    /**
     * The name of the class.
     */
    private static final String CLASS_NAME = OptionsAction.class.getSimpleName();

    /**
     * the ImageCacheAccess object
     */
    private final ImageCacheAccess imageCacheAccess;

    /**
     * The plugin's config
     */
    private final Config config;

    /**
     * Instantiates a new EditAction.
     *
     * @param listener         the property change listener to be added to this action
     * @param config           The plugin's config
     * @param panel            The panel from which the data source editor has been opened
     * @param imageCacheAccess the ImageCacheAccess object
     */
    public OptionsAction(PropertyChangeListener listener, Config config, JPanel panel, ImageCacheAccess imageCacheAccess) {
        super(CLASS_NAME, "/images/gear.png");
        addPropertyChangeListener(listener);
        this.panel = panel;
        this.imageCacheAccess = imageCacheAccess;
        this.config = config;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        new OptionsEditorController(
                new OptionsEditor(), panel, config, imageCacheAccess).load();
    }
}

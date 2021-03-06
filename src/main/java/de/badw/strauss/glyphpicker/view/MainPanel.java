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
package de.badw.strauss.glyphpicker.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ResourceBundle;

/**
 * The plugin's main panel.
 */
public class MainPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    /**
     * The main panel's border width.
     */
    private static final int MAIN_BORDER_THICKNESS = 2;
    /**
     * The minimum width.
     */
    private static final int MIN_WIDTH = 200;
    /**
     * The minimum height.
     */
    private static final int MIN_HEIGHT = 200;
    /**
     * The tabbed pane.
     */
    private final JTabbedPane tabbedPane;

    /**
     * Instantiates a new main panel.
     *
     * @param memorizedTabPanel the memorized tab panel
     * @param allTabPanel        the allTab panel
     * @param menuShortcutName    the OS dependent name of the menu shortcut
     */
    public MainPanel(JComponent memorizedTabPanel, JComponent allTabPanel, String menuShortcutName) {

        ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
        String className = this.getClass().getSimpleName();

        setLayout(new BorderLayout(0, 0));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new EmptyBorder(MAIN_BORDER_THICKNESS,
                MAIN_BORDER_THICKNESS, MAIN_BORDER_THICKNESS, MAIN_BORDER_THICKNESS));

        String memorizedTabLabel = i18n.getString(className
                + ".memorizedTab");
        tabbedPane.addTab(null, null, memorizedTabPanel, i18n.getString(className
                + ".memorizedTabTooltip") + " (" + menuShortcutName + "+1)");
        tabbedPane.setTabComponentAt(0, new HighlightLabel(memorizedTabLabel));

        String dataSourcesLabel = i18n.getString(className + ".allCharacters");
        tabbedPane.addTab(null, null, allTabPanel, i18n.getString(className
                + ".allCharactersTooltip") + " (" + menuShortcutName + "+2)");
        tabbedPane.setTabComponentAt(1, new HighlightLabel(dataSourcesLabel));

        add(tabbedPane, BorderLayout.CENTER);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
    }

    /**
     * Gets the tabbed pane.
     *
     * @return the tabbed pane
     */
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    /**
     * Highlights the tab title at the specified index.
     *
     * @param index the index
     */
    public void highlightTabTitle(int index) {
        ((HighlightLabel) tabbedPane.getTabComponentAt(index)).highlight();
    }

}

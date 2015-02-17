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

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;

import java.awt.Dimension;
import java.util.ResourceBundle;

/**
 * The plugin's main panel.
 */
public class MainPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * The tabbed pane.
     */
    private final JTabbedPane tabbedPane;

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
     * Instantiates a new main panel.
     *
     * @param userCollectionPanel the user collection panel
     * @param browserPanel        the browser panel
     */
    public MainPanel(JComponent userCollectionPanel, JComponent browserPanel) {

        ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
        String className = this.getClass().getSimpleName();

        setLayout(new BorderLayout(0, 0));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new EmptyBorder(MAIN_BORDER_THICKNESS,
                MAIN_BORDER_THICKNESS, MAIN_BORDER_THICKNESS, MAIN_BORDER_THICKNESS));

        String userCollectionLabel = i18n.getString(className
                + ".userCollection");
        tabbedPane.addTab(null, null, userCollectionPanel, null);
        tabbedPane.setTabComponentAt(0, new HighlightLabel(userCollectionLabel));

        String dataSourcesLabel = i18n.getString(className + ".allCharacters");
        tabbedPane.addTab(null, null, browserPanel, null);
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

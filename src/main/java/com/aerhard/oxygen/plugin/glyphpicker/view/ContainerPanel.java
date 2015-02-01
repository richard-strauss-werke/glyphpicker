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
package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import com.jidesoft.swing.DefaultOverlayable;

import javax.swing.UIManager;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.ResourceBundle;

/**
 * A container panel in a tab.
 */
public class ContainerPanel extends JPanel {


    private static final long serialVersionUID = 1L;
    
    /** The preferred height and width of the info label component. */
    private static final int INFO_LABEL_PREFERRED_SIZE = 90;

    /** The scroll pane. */
    private final JScrollPane jPane;
    
    /** The scroll pane's overlayable. */
    private final DefaultOverlayable overlayable;
    
    /** The info label. */
    private final JTextPane infoLabel;

    /** The info panel containing the info label. */
    private final JPanel infoPanel;

    /** The control panel. */
    private final ControlPanel controlPanel;

    /**
     * Instantiates a new Container Panel.
     *
     * @param controlPanel the control panel to be added
     */
    public ContainerPanel(ControlPanel controlPanel) {

        ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");

        this.controlPanel = controlPanel;

        setLayout(new BorderLayout(0, 0));

        add(controlPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel();
        add(tablePanel, BorderLayout.CENTER);
        tablePanel.setBorder(new EmptyBorder(11, 8, 7, 8));
        tablePanel.setLayout(new BorderLayout(0, 6));

        jPane = new JScrollPane();
        jPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jPane.setBorder(new EtchedBorder());

        JPanel overlayPanel = new JPanel();
        overlayPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null,
                null));
        JLabel overlayLabel = new JLabel(
                i18n.getString("ContainerPanel.loading"));
        overlayPanel.add(overlayLabel);
        overlayPanel.setBackground(UIManager.getColor("Panel.background"));

        overlayable = new DefaultOverlayable(jPane, overlayPanel);
        tablePanel.add(overlayable, BorderLayout.CENTER);

        infoPanel = new JPanel();
        infoPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        infoPanel.setBackground(UIManager.getColor("TextField.background"));
        GridBagLayout gblInfoPanel = new GridBagLayout();
        gblInfoPanel.columnWidths = new int[] { 210, 14, 0 };
        gblInfoPanel.rowHeights = new int[] { 0, 0 };
        gblInfoPanel.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
        gblInfoPanel.rowWeights = new double[] { 0.0, 0.0 };
        infoPanel.setLayout(gblInfoPanel);

        tablePanel.add(infoPanel, BorderLayout.SOUTH);

        infoLabel = new JTextPane();
        infoLabel.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        infoLabel.setEditable(false);
        infoLabel.setContentType("text/html");
        infoLabel.setPreferredSize(new Dimension(INFO_LABEL_PREFERRED_SIZE, INFO_LABEL_PREFERRED_SIZE));
        GridBagConstraints gbcInfoLabel = new GridBagConstraints();
        gbcInfoLabel.insets = new Insets(5, 8, 5, 8);
        gbcInfoLabel.gridwidth = 2;
        gbcInfoLabel.weightx = 1.0;
        gbcInfoLabel.fill = GridBagConstraints.HORIZONTAL;
        gbcInfoLabel.anchor = GridBagConstraints.NORTHWEST;
        gbcInfoLabel.gridx = 0;
        gbcInfoLabel.gridy = 0;
        infoPanel.add(infoLabel, gbcInfoLabel);

    }

    /**
     * Sets the list component.
     *
     * @param component the new list component
     */
    public void setListComponent(JComponent component) {
        jPane.setViewportView(component);
    }

    /**
     * Gets the list component.
     *
     * @return the list component
     */
    public Component getListComponent() {
        return jPane.getViewport().getComponent(0);
    }

    /**
     * Gets the control panel.
     *
     * @return the control panel
     */
    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    /**
     * Gets the info panel.
     *
     * @return the info panel
     */
    public JPanel getInfoPanel() {
        return infoPanel;
    }

    /**
     * Gets the info label.
     *
     * @return the info label
     */
    public JTextPane getInfoLabel() {
        return infoLabel;
    }

    /**
     * Turns the mask on/off.
     *
     * @param mask true to show the mask, false to hide it
     */
    public void setMask(boolean mask) {
        overlayable.setOverlayVisible(mask);
    }

    /**
     * Redraws the icon at the specified list index; called each time a new bitmap has been loaded.
     *
     * @param index the index
     */
    public void redrawIconInList(int index) {
        Component listComponent = getListComponent();
        if (index != -1) {
            if (listComponent instanceof GlyphGrid) {
                ((GlyphGrid) listComponent).repaint(((GlyphGrid) listComponent)
                        .getCellBounds(index, index));
            }

            else if (listComponent instanceof GlyphTable) {
                ((GlyphTable) listComponent)
                        .repaint(((GlyphTable) listComponent).getCellRect(
                                index, 0, true));
            }
        }
    }

}

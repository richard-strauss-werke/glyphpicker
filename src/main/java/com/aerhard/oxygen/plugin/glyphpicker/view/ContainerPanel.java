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

public class ContainerPanel extends JPanel {

    private static final int INFO_LABEL_PREFERRED_SIZE = 90;

    private static final long serialVersionUID = 1L;

    private JScrollPane jPane;
    private DefaultOverlayable overlayable;
    private JTextPane infoLabel;

    private JPanel infoPanel;

    private ControlPanel controlPanel;

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

    public void setListComponent(JComponent component) {
        jPane.setViewportView(component);
    }

    public Component getListComponent() {
        return jPane.getViewport().getComponent(0);
    }

    public ControlPanel getControlPanel() {
        return controlPanel;
    }

    public JPanel getInfoPanel() {
        return infoPanel;
    }

    public JTextPane getInfoLabel() {
        return infoLabel;
    }

    public void setMask(boolean mask) {
        overlayable.setOverlayVisible(mask);
    }

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

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

public class ContainerPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JScrollPane jPane;
    private DefaultOverlayable overlayable;
    private JTextPane infoLabel;

    private JPanel infoPanel;

    private JPanel overlayPanel;

    private ControlPanel controlPanel;

    public ContainerPanel(ControlPanel controlPanel) {
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

        overlayPanel = new JPanel();
        overlayPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        JLabel overlayLabel = new JLabel(
                "Loading data ...");
        overlayPanel.add(overlayLabel);
        overlayPanel.setBackground(UIManager.getColor("Panel.background"));
        
        overlayable = new DefaultOverlayable(jPane, overlayPanel);
        tablePanel.add(overlayable, BorderLayout.CENTER);
        
        infoPanel = new JPanel();
        infoPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        infoPanel.setBackground(UIManager.getColor("TextField.background"));
        GridBagLayout gbl_infoPanel = new GridBagLayout();
        gbl_infoPanel.columnWidths = new int[]{210, 14, 0};
        gbl_infoPanel.rowHeights = new int[] {0, 0};
        gbl_infoPanel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        gbl_infoPanel.rowWeights = new double[]{0.0, 0.0};
        infoPanel.setLayout(gbl_infoPanel);
        
        tablePanel.add(infoPanel, BorderLayout.SOUTH);
        
        infoLabel = new JTextPane();
        infoLabel.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        infoLabel.setEditable(false);
        infoLabel.setContentType("text/html");
        infoLabel.setPreferredSize(new Dimension(90,90));
        GridBagConstraints gbc_infoLabel = new GridBagConstraints();
        gbc_infoLabel.insets = new Insets(5, 8, 5, 8);
        gbc_infoLabel.gridwidth = 2;
        gbc_infoLabel.weightx = 1.0;
        gbc_infoLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_infoLabel.anchor = GridBagConstraints.NORTHWEST;
        gbc_infoLabel.gridx = 0;
        gbc_infoLabel.gridy = 0;
        infoPanel.add(infoLabel, gbc_infoLabel);
        
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
                ((GlyphGrid)listComponent).repaint(((GlyphGrid)listComponent).getCellBounds(index, index));
            }
            
            else if (listComponent instanceof GlyphTable) {
                ((GlyphTable)listComponent).repaint(((GlyphTable)listComponent).getCellRect(index, 0, true));
            }
        }
    }

}

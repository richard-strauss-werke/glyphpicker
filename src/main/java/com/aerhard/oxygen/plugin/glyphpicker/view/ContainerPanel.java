package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;

import com.jidesoft.swing.DefaultOverlayable;
import com.jidesoft.swing.JideButton;

import javax.swing.UIManager;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class ContainerPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JPanel buttonPanel;

    private JScrollPane jPane;
    private DefaultOverlayable overlayable;
    private JTextField infoLabel;

    private JPanel infoPanel;
    private JTextField infoLabel2;

    private JPanel overlayPanel;

    public ContainerPanel(JPanel controlPanel) {
        setLayout(new BorderLayout(0, 0));

        add(controlPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel();
        add(tablePanel, BorderLayout.CENTER);
        tablePanel.setBorder(new EmptyBorder(11, 8, 7, 8));
        tablePanel.setLayout(new BorderLayout(0, 6));

        jPane = new JScrollPane();
        // jPane.setLayout(new GridLayout(1,1));
        // jpane.setHorizontalScrollBarPolicy(JideScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        // jpane.setVerticalScrollBarPolicy(JideScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
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
        GridBagLayout gbl_infoPanel = new GridBagLayout();
        gbl_infoPanel.columnWidths = new int[]{210, 14, 0};
        gbl_infoPanel.rowHeights = new int[] {0, 0};
        gbl_infoPanel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        gbl_infoPanel.rowWeights = new double[]{0.0, 0.0};
        infoPanel.setLayout(gbl_infoPanel);
        
        tablePanel.add(infoPanel, BorderLayout.SOUTH);
        
        infoLabel = new JTextField(null);
        infoLabel.setEditable(false);
        infoLabel.setMargin(new Insets(2, 6, 2, 6));
        GridBagConstraints gbc_infoLabel = new GridBagConstraints();
        gbc_infoLabel.insets = new Insets(0, 0, 5, 1);
        gbc_infoLabel.gridwidth = 2;
        gbc_infoLabel.weightx = 1.0;
        gbc_infoLabel.fill = GridBagConstraints.HORIZONTAL;
        gbc_infoLabel.anchor = GridBagConstraints.NORTHWEST;
        gbc_infoLabel.gridx = 0;
        gbc_infoLabel.gridy = 0;
        infoPanel.add(infoLabel, gbc_infoLabel);
        
        infoLabel2 = new JTextField();
        infoLabel2.setEditable(false);
        infoLabel2.setMargin(new Insets(2, 6, 2, 6));
        GridBagConstraints gbc_infoLabel2 = new GridBagConstraints();
        gbc_infoLabel2.insets = new Insets(0, 0, 7, 1);
        gbc_infoLabel2.gridwidth = 2;
        gbc_infoLabel2.fill = GridBagConstraints.HORIZONTAL;
        gbc_infoLabel2.gridx = 0;
        gbc_infoLabel2.gridy = 1;
        infoPanel.add(infoLabel2, gbc_infoLabel2);
        infoLabel2.setColumns(10);

        buttonPanel = new JPanel();
        buttonPanel.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.GRAY));
        add(buttonPanel, BorderLayout.SOUTH);

    }

    public void setListComponent(JComponent component) {
        jPane.setViewportView(component);
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }

    public void addToButtonPanel(JComponent component) {
        buttonPanel.add(component);
    }

    public void addToButtonPanel(AbstractAction action) {
        buttonPanel.add(new JideButton(action));
    }

    public JPanel getInfoPanel() {
        return infoPanel;
    }

    public JTextField getInfoLabel() {
        return infoLabel;
    }
    
    public JTextField getInfoLabel2() {
        return infoLabel2;
    }
    
    public void setMask(boolean mask) {
//        if (mask) {
//            overlayPanel.setPreferredSize(overlayable.getSize());            
//        }
        overlayable.setOverlayVisible(mask);
    }

}

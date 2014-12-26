package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import java.awt.Component;
import javax.swing.Box;

public class UserCollectionPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private GlyphGrid userCollection;
    private JButton btnRemove;
    private HighlightButton btnInsert;
    private JButton btnSave;
    private JButton btnReload;
    private JComboBox<String> viewCombo;
    private Component horizontalGlue;

    public UserCollectionPanel() {

        setLayout(new BorderLayout(0, 0));

        JPanel dataSourcePanel = new JPanel();
        dataSourcePanel.setBorder(new EmptyBorder(8, 8, 0, 8));
        add(dataSourcePanel, BorderLayout.NORTH);
        GridBagLayout gbl_dataSourcePanel = new GridBagLayout();
        gbl_dataSourcePanel.columnWidths = new int[] { 62, 320, 55, 0 };
        gbl_dataSourcePanel.rowHeights = new int[] { 0, 0 };
        gbl_dataSourcePanel.columnWeights = new double[] { 0.0, 0.0, 0.0,
                Double.MIN_VALUE };
        gbl_dataSourcePanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
        dataSourcePanel.setLayout(gbl_dataSourcePanel);

        viewCombo = new JComboBox<String>();
        viewCombo.addItem("Grid");
        viewCombo.addItem("List");

        horizontalGlue = Box.createHorizontalGlue();
        GridBagConstraints gbc_horizontalGlue = new GridBagConstraints();
        gbc_horizontalGlue.fill = GridBagConstraints.BOTH;
        gbc_horizontalGlue.weightx = 1.0;
        gbc_horizontalGlue.insets = new Insets(0, 0, 0, 5);
        gbc_horizontalGlue.gridx = 1;
        gbc_horizontalGlue.gridy = 0;
        dataSourcePanel.add(horizontalGlue, gbc_horizontalGlue);
        GridBagConstraints gbc_viewCombo = new GridBagConstraints();
        gbc_viewCombo.insets = new Insets(3, 0, 0, 0);
        gbc_viewCombo.fill = GridBagConstraints.BOTH;
        gbc_viewCombo.gridx = 2;
        gbc_viewCombo.gridy = 0;
        dataSourcePanel.add(viewCombo, gbc_viewCombo);

        JPanel paletteTablePanel = new JPanel();
        paletteTablePanel.setBorder(new EmptyBorder(11, 8, 7, 8));
        add(paletteTablePanel);
        paletteTablePanel.setLayout(new BorderLayout(0, 0));

        userCollection = new GlyphGrid();

        JScrollPane scrollPane = new JScrollPane(userCollection);
        scrollPane
                .setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        paletteTablePanel.add(scrollPane);

        JPanel buttonPanel = new JPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.GRAY));

        btnInsert = new HighlightButton("Insert XML");
        btnInsert.setEnabled(false);
        buttonPanel.add(btnInsert);

        btnRemove = new JButton("Remove Item");
        btnRemove.setEnabled(false);
        buttonPanel.add(btnRemove);

        btnSave = new JButton("Save Collection");
        btnSave.setEnabled(false);
        buttonPanel.add(btnSave);

        btnReload = new JButton("Reload Collection");
        btnReload.setEnabled(false);
        buttonPanel.add(btnReload);

    }

    public void enableSelectionButtons(Boolean enable) {
        btnInsert.setEnabled(enable);
        btnRemove.setEnabled(enable);

    }

    public void enableSaveButtons(Boolean enable) {
        btnSave.setEnabled(enable);
        btnReload.setEnabled(enable);

    }

    public HighlightButton getBtnInsert() {
        return btnInsert;
    }

    public JButton getBtnRemove() {
        return btnRemove;
    }

    public JButton getBtnSave() {
        return btnSave;
    }

    public JButton getBtnReload() {
        return btnReload;
    }

    public JComboBox<String> getViewCombo() {
        return viewCombo;
    }

    public GlyphGrid getUserCollection() {
        return userCollection;
    }

}

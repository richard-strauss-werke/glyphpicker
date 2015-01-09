package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;

import java.awt.Dimension;

public class MainPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTabbedPane tabbedPane;

    // private JComboBox<String> viewCombo;
    // private JPanel panel;

    public MainPanel(JComponent browserPanel, JComponent userCollectionPanel) {

        setLayout(new BorderLayout(0, 0));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new EmptyBorder(8, 8, 8, 8));

        tabbedPane.addTab(null, null, userCollectionPanel, null);
        tabbedPane.setTabComponentAt(0, new HighlightLabel("User Collection"));

        tabbedPane.addTab(null, null, browserPanel, null);
        tabbedPane.setTabComponentAt(1, new HighlightLabel("Data Sources"));

        // panel = new JPanel();
        // panel.setBorder(new EmptyBorder(8, 8, 8, 8));
        // add(panel, BorderLayout.NORTH);
        // GridBagLayout gbl_panel = new GridBagLayout();
        // gbl_panel.columnWidths = new int[]{271, 86, 0};
        // gbl_panel.rowHeights = new int[]{20, 0};
        // gbl_panel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        // gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        // panel.setLayout(gbl_panel);

        // viewCombo = new JComboBox<String>();
        // GridBagConstraints gbc_viewCombo = new GridBagConstraints();
        // gbc_viewCombo.fill = GridBagConstraints.HORIZONTAL;
        // gbc_viewCombo.anchor = GridBagConstraints.NORTH;
        // gbc_viewCombo.gridx = 1;
        // gbc_viewCombo.gridy = 0;
        // panel.add(viewCombo, gbc_viewCombo);
        // viewCombo.addItem("Grid");
        // viewCombo.addItem("List");

        add(tabbedPane, BorderLayout.CENTER);
        setMinimumSize(new Dimension(200, 200));
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void highlightTabTitle(int index) {
        ((HighlightLabel) tabbedPane.getTabComponentAt(index)).highlight();
    }

    // public JComboBox<String> getViewCombo() {
    // return viewCombo;
    // }

}

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

    public MainPanel(JComponent browserPanel, JComponent userListPanel) {

        setLayout(new BorderLayout(0, 0));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new EmptyBorder(8, 8, 8, 8));

        tabbedPane.addTab(null, null, userListPanel, null);
        tabbedPane.setTabComponentAt(0, new HighlightLabel("Collection"));

        tabbedPane.addTab(null, null, browserPanel, null);
        tabbedPane.setTabComponentAt(1, new HighlightLabel("Data Sources"));

        add(tabbedPane, BorderLayout.CENTER);
        setMinimumSize(new Dimension(200, 200));
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void highlightTabTitle(int index) {
        ((HighlightLabel) tabbedPane.getTabComponentAt(index)).highlight();
    }

}

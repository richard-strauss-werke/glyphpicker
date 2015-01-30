package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;

import java.awt.Dimension;
import java.util.ResourceBundle;

public class MainPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTabbedPane tabbedPane;

    public MainPanel(JComponent userCollectionPanel, JComponent browserPanel) {

        ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
        String className = this.getClass().getSimpleName();

        setLayout(new BorderLayout(0, 0));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new EmptyBorder(8, 8, 8, 8));

        tabbedPane.addTab(null, null, userCollectionPanel, null);
        tabbedPane.setTabComponentAt(
                0,
                new HighlightLabel(getHtmlLabel(i18n.getString(className
                        + ".userCollection"))));

        tabbedPane.addTab(null, null, browserPanel, null);
        tabbedPane.setTabComponentAt(
                1,
                new HighlightLabel(getHtmlLabel(i18n.getString(className
                        + ".dataSources"))));

        add(tabbedPane, BorderLayout.CENTER);
        setMinimumSize(new Dimension(200, 200));
    }

    private String getHtmlLabel(String str) {
        return "<html><span style=\"text-decoration:underline\">"
                + str.substring(0, 1) + "</span>" + str.substring(1)
                + "</html>";
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void highlightTabTitle(int index) {
        ((HighlightLabel) tabbedPane.getTabComponentAt(index)).highlight();
    }

}

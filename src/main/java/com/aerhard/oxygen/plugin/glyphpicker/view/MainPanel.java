package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;

import java.awt.Dimension;
import java.util.ResourceBundle;

public class MainPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private final JTabbedPane tabbedPane;

    private static final int MAIN_BORDER_WIDTH = 8;
    private static final int MIN_WIDTH = 200;
    private static final int MIN_HEIGHT = 200;

    public MainPanel(JComponent userCollectionPanel, JComponent browserPanel) {

        ResourceBundle i18n = ResourceBundle.getBundle("GlyphPicker");
        String className = this.getClass().getSimpleName();

        setLayout(new BorderLayout(0, 0));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new EmptyBorder(MAIN_BORDER_WIDTH,
                MAIN_BORDER_WIDTH, MAIN_BORDER_WIDTH, MAIN_BORDER_WIDTH));

        String userCollectionLabel = i18n.getString(className
                + ".userCollection");
        tabbedPane.addTab(null, null, userCollectionPanel, null);
        tabbedPane.setTabComponentAt(0, new HighlightLabel(
                getHtmlLabel(userCollectionLabel)));
        tabbedPane.setMnemonicAt(0,
                KeyStroke.getKeyStroke(userCollectionLabel.substring(0, 1))
                        .getKeyCode());

        String dataSourcesLabel = i18n.getString(className + ".dataSources");
        tabbedPane.addTab(null, null, browserPanel, null);
        tabbedPane.setTabComponentAt(1, new HighlightLabel(
                getHtmlLabel(dataSourcesLabel)));
        tabbedPane.setMnemonicAt(1,
                KeyStroke.getKeyStroke(dataSourcesLabel.substring(0, 1))
                        .getKeyCode());

        add(tabbedPane, BorderLayout.CENTER);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
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

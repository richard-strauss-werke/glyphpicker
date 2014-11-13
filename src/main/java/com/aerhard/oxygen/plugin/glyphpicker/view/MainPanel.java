package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.Timer;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private JTabbedPane tabbedPane;
    private Color tabForeground = null;

    public MainPanel(JPanel browserPanel, JPanel userListPanel) {

        setLayout(new BorderLayout(0, 0));
        
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBorder(new EmptyBorder(8, 8, 8, 8));

        tabbedPane.addTab(null, null, userListPanel, null);
        tabbedPane.setTabComponentAt(0, new JLabel("User Collection"));

        tabbedPane.addTab(null, null, browserPanel, null);
        tabbedPane.setTabComponentAt(1, new JLabel("All Glyphs"));

        add(tabbedPane, BorderLayout.CENTER);
        setMinimumSize(new Dimension(200, 200));
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void highlightTabButton(int index) {
        final Component tab = getTabbedPane().getTabComponentAt(index);
        if (tabForeground == null) {
            tabForeground = tab.getForeground();
        }
        tab.setForeground(Color.GRAY);
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                tab.setForeground(tabForeground);
            }
        };
        Timer timer = new Timer(300, taskPerformer);
        timer.setRepeats(false);
        timer.start();
    }

}

package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.MatteBorder;

import com.jidesoft.swing.DefaultOverlayable;

public class ContainerPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JPanel buttonPanel;

    private JScrollPane jPane;
    private DefaultOverlayable overlayable;
    private JTextField infoLabel;

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

        overlayable = new DefaultOverlayable(jPane, new JLabel(
                "Loading data ..."));
        tablePanel.add(overlayable, BorderLayout.CENTER);

        infoLabel = new JTextField(null);
        infoLabel.setEditable(false);
        infoLabel.setMargin(new Insets(2, 6, 2, 6));
        tablePanel.add(infoLabel, BorderLayout.SOUTH);

        buttonPanel = new JPanel();
        buttonPanel.setBorder(new MatteBorder(1, 0, 0, 0, (Color) Color.GRAY));
        add(buttonPanel, BorderLayout.SOUTH);

        // add(browserButtonPanel, BorderLayout.SOUTH);
        //
        // btnInsert = new HighlightButton();
        // browserButtonPanel.add(btnInsert);
        //
        // btnAdd = new JButton();
        // browserButtonPanel.add(btnAdd);

    }

    public void setListComponent(JComponent component) {
        jPane.setViewportView(component);
        // jPane.getViewport().removeAll();
        // jPane.getViewport().add(component);
    }

    public JPanel getButtonPanel() {
        return buttonPanel;
    }

    public void addToButtonPanel(JComponent component) {
        buttonPanel.add(component);
    }

    public void addToButtonPanel(AbstractAction action) {
        buttonPanel.add(new JButton(action));
    }

    public JTextField getInfoLabel() {
        return infoLabel;
    }

    public DefaultOverlayable getOverlayable() {
        return overlayable;
    }

}

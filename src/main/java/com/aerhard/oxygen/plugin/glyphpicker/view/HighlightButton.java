package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.Timer;

public class HighlightButton extends JButton {

    private static final long serialVersionUID = 1L;
    private Color originalForeground = null;

    public HighlightButton(String text) {
        super(text);
        originalForeground = getForeground();
    }

    public void highlight() {
        setForeground(Color.GRAY);
        Timer timer = new Timer(300, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setForeground(originalForeground);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

}

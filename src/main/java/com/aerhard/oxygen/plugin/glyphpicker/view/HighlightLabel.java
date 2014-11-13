package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

public class HighlightLabel extends JLabel{

    private static final long serialVersionUID = 1L;
    private Color labelForeground = null;
    
    public HighlightLabel(String text) {
        super(text);
    }
    
    public void highlight() {
        if (labelForeground == null) {
            labelForeground = getForeground();
        }
        setForeground(Color.GRAY);
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setForeground(labelForeground);
            }
        };
        Timer timer = new Timer(300, taskPerformer);
        timer.setRepeats(false);
        timer.start();
    }
    
}

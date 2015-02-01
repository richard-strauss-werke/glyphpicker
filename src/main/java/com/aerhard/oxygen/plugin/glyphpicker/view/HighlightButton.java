package com.aerhard.oxygen.plugin.glyphpicker.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import com.jidesoft.swing.JideButton;

public class HighlightButton extends JideButton {

    private static final long serialVersionUID = 1L;
    private Color originalForeground = null;

    private static final int DELAY = 300;
    
    public HighlightButton() {
        originalForeground = getForeground();
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);
    }

    public HighlightButton(String text) {
        super(text);
        originalForeground = getForeground();
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);
    }

    public HighlightButton(AbstractAction insertAction) {
        super(insertAction);
        originalForeground = getForeground();
        setVerticalTextPosition(SwingConstants.BOTTOM);
        setHorizontalTextPosition(SwingConstants.CENTER);
    }

    public void highlight() {
        setForeground(Color.GRAY);
        Timer timer = new Timer(DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setForeground(originalForeground);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

}

/**
 * Copyright 2015 Alexander Erhard
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.badw.strauss.glyphpicker.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * A JLabel which can be highlighted.
 */
public class HighlightLabel extends JLabel {

    private static final long serialVersionUID = 1L;

    /**
     * The original foreground.
     */
    private Color originalForeground = null;

    /**
     * The duration of the highlighting.
     */
    private static final int DURATION = 300;

    /**
     * Instantiates a new HighlightLabel.
     *
     * @param text the label text
     */
    public HighlightLabel(String text) {
        super(text);
        originalForeground = getForeground();
    }

    /**
     * Highlights the label.
     */
    public void highlight() {
        setForeground(Color.GRAY);
        Timer timer = new Timer(DURATION, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                setForeground(originalForeground);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

}

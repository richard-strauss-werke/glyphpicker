package de.badw.strauss.glyphpicker;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@RunWith(MockitoJUnitRunner.class)
public class SettingsDialogActionTest {

    private static final Logger LOGGER = Logger.getLogger(SettingsDialogActionTest.class
            .getName());

    @Test
    public static void runTest() {
        TestHelper.setSystemLookAndFeel();

        JFrame frame = new JFrame("UI Test");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(80, 80));


        JButton btn = new JButton("open");

        final JPanel panel = new JPanel();
        panel.add(new JLabel("content"));
        
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //openDialog(panel);
            }
        });

        frame.add(btn);


        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                runTest();

            }
        });
    }

}

package com.aerhard.oxygen.plugin.glyphpicker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Locale;

import javax.swing.*;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.controller.main.MainController;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UITest {

    private static final Logger LOGGER = Logger.getLogger(UITest.class
            .getName());

    private static MainController mainController;

    @Test
    public void testSearchDialog() {
        LOGGER.info("UI test always passes in JUnit tests.");
    }

    private static void runTest() {
        setSystemLookAndFeel();
        
        Locale.setDefault(Locale.ENGLISH);

        String tempDir = System.getProperty("user.dir");
        File tmpFolder = new File(tempDir, "tmp");

        if (!((tmpFolder.exists() && tmpFolder.isDirectory()) || tmpFolder.mkdir())) {
            LOGGER.error(String.format("Could not create tmp folder at %s", tmpFolder.toString()));
            System.exit(-1);
        }

        String tmpFolderString = tmpFolder.toString();

        StandalonePluginWorkspace workspace = mock(StandalonePluginWorkspace.class);
        when(workspace.getPreferencesDirectory()).thenReturn(tmpFolderString);

        JFrame frame = new JFrame("UI Test");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setPreferredSize(new Dimension(600, 600));
        
        mainController = new MainController(workspace);

        frame.setLayout(new BorderLayout(0, 0));
        frame.getContentPane().add(mainController.getPanel());

        mainController.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent e) {
                if ("insert".equals(e.getPropertyName())) {
                    GlyphDefinition model = (GlyphDefinition) e.getNewValue();
                    LOGGER.info("Insertion triggered: " + model.getCharName()
                            + "\nrefString: " + model.getRefString());
                }
            }
        });
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainController.saveData();
            }
        });

        mainController.loadData();

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

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            LOGGER.error(e);
        }
    }

}

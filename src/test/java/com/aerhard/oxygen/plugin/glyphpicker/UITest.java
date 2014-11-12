package com.aerhard.oxygen.plugin.glyphpicker;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.controller.InsertListener;
import com.aerhard.oxygen.plugin.glyphpicker.controller.MainController;
import com.aerhard.oxygen.plugin.glyphpicker.model.tei.GlyphModel;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UITest {

    private static final Logger LOGGER = Logger.getLogger(UITest.class
            .getName());

    private static MainController controller;

    
    @Test
    public void testSearchDialog() {
        LOGGER.info("UI test.");
    }
    
    
    public static void main(String[] args) {
        setSystemLookAndFeel();
        
        Properties properties = new Properties();
        try {
            properties.load(UITest.class
                    .getResourceAsStream("/mock.properties"));
        } catch (IOException e) {
            LOGGER.error("Could not read \"mock.properties\".");
        }
        
        String oxyPropFolder = properties.getProperty("oxygen.propertyfolder");
        
        
        StandalonePluginWorkspace workspace = mock(StandalonePluginWorkspace.class);
        when(workspace.getPreferencesDirectory()).thenReturn(oxyPropFolder);
        

        JFrame frame = new JFrame("UI Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        controller = new MainController(workspace);

        frame.setLayout(new BorderLayout(0, 0));
        frame.getContentPane().add(controller.getMainPanel());

        controller.addListener(new InsertListener() {
            @Override
            public void insert(GlyphModel model) {
                LOGGER.info("Insertion triggered: " + model.getCharName());
            }
        });

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.getConfigController().save();
                controller.getUserListController().save();
            }
        });
        
        controller.loadData();

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            LOGGER.error(e);
        } catch (InstantiationException e) {
            LOGGER.error(e);
        } catch (IllegalAccessException e) {
            LOGGER.error(e);
        } catch (UnsupportedLookAndFeelException e) {
            LOGGER.error(e);
        }
    }

}

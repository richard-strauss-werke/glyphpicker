package com.aerhard.oxygen.plugin.glyphpicker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import com.aerhard.oxygen.plugin.glyphpicker.controller.MainController;
import com.aerhard.oxygen.plugin.glyphpicker.model.GlyphDefinition;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UITest {

    private static final Logger LOGGER = Logger.getLogger(UITest.class
            .getName());

    private static MainController mainController;

    @Test
    public void testSearchDialog() {
        LOGGER.info("UI test.");
    }

    private static void runTest() {
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

        // ArrayList<DataSource> ds =
        // mainController.getConfigLoader().getConfig().getDataSources();
        // DataSource s;
        // PathComboModel pcm =
        // mainController.getConfigLoader().getConfig().getPaths();
        // for (int i=0; i< pcm.getSize();i++) {
        // System.out.println(pcm.getElementAt(i));
        // s = new DataSource();
        //
        // s.setLabel("Edirom SMuFL-Browser");
        // s.setPath(pcm.getElementAt(i));
        // s.setFontName("BravuraText");
        // s.setDisplayMode("fit");
        // s.setTemplate("${baseUrl}#${id}");
        //
        // ds.add(s);
        // }

        // mainController.getConfigLoader().getConfig().setDataSources_(new
        // DataSourceList(mainController.getConfigLoader().getConfig().getDataSources()));

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

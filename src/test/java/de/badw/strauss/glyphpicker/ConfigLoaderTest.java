package de.badw.strauss.glyphpicker;

import de.badw.strauss.glyphpicker.controller.main.ConfigLoader;
import de.badw.strauss.glyphpicker.model.Config;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import ro.sync.exml.workspace.api.standalone.StandalonePluginWorkspace;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConfigLoaderTest {

    private static final Logger LOGGER = Logger.getLogger(ConfigLoaderTest.class
            .getName());

    /**
     * Tests if the version in the config file matches the version in pom.xml.
     */
    @Test
    public void testVersion() {
        Properties properties = new Properties();
        try {
            properties.load(ConfigLoaderTest.class
                    .getResourceAsStream("/plugin.properties"));
        } catch (Exception e) {
            LOGGER.error("Could not read \"plugin.properties\".");
            fail();
        }

        // let the path to the external config file point to a missing file
        // to let the config loader load the default config at /src/main/resources/default_config.xml
        properties.setProperty("config.filename", "missing-file.xml");

        StandalonePluginWorkspace workspace = mock(StandalonePluginWorkspace.class);
        when(workspace.getPreferencesDirectory()).thenReturn(TestHelper.getTempFolder());

        // first run: test test_config.xml
        ConfigLoader c = new ConfigLoader(workspace, properties);
        c.load();
        assertEquals("The config version given in "+properties.getProperty("default.config.path")+" must " +
                        "match the project version in /pom.xml", properties.getProperty("config.version"),
                c.getConfig().getVersion());

        // second run: test default_config.xml
        properties.setProperty("default.config.path", "/config.xml");
        c = new ConfigLoader(workspace, properties);
        c.load();
        assertEquals("The config version given in "+properties.getProperty("default.config.path")+" must " +
                        "match the project version in /pom.xml", properties.getProperty("config.version"),
                c.getConfig().getVersion());
    }

}

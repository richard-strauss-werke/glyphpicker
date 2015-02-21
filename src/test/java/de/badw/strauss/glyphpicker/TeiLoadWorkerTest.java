package de.badw.strauss.glyphpicker;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class TeiLoadWorkerTest {

    private static final Logger LOGGER = Logger.getLogger(TeiLoadWorkerTest.class
            .getName());

    /**
     * Tests if the version in the config file matches the version in pom.xml.
     */
    @Test
    public void testLoadDataFromUrl() {
        /*
        Properties properties = new Properties();
        try {
            properties.load(TeiLoadWorkerTest.class
                    .getResourceAsStream("/plugin.properties"));
        } catch (Exception e) {
            LOGGER.error("Could not read \"plugin.properties\".");
            fail();
        }

        // let the path to the external config file point to a missing file
        // to let the config loader load the default config at /src/main/resources/test_config.xml
        properties.setProperty("config.filename", "missing-file.xml");

        StandalonePluginWorkspace workspace = mock(StandalonePluginWorkspace.class);
        when(workspace.getPreferencesDirectory()).thenReturn(TestHelper.getTempFolder());

        DataSource t = new DataSource();
        t.setBasePath("http://unknown-asdf1099nsdkjds89sdfklsdf.de");

        TeiLoadWorker w = new TeiLoadWorker(t);
        w.loadDataFromUrl();
        */

    }

}

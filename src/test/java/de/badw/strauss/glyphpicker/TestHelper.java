package de.badw.strauss.glyphpicker;

import org.apache.log4j.Logger;

import java.io.File;

public class TestHelper {

    private static final Logger LOGGER = Logger.getLogger(TestHelper.class
            .getName());

    static String getTempFolder () {
        String tempDir = System.getProperty("user.dir");
        File tmpFolder = new File(tempDir, "tmp");

        if (!((tmpFolder.exists() && tmpFolder.isDirectory()) || tmpFolder.mkdir())) {
            LOGGER.error(String.format("Could not create tmp folder at %s", tmpFolder.toString()));
            System.exit(-1);
        }
        return tmpFolder.toString();
    }

}

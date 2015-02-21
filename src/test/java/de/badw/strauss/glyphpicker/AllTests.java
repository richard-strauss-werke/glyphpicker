package de.badw.strauss.glyphpicker;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ i18nTest.class, UITest.class, GlyphPickerPluginExtensionTest.class,
ConfigLoaderTest.class, TeiLoadWorkerTest.class, TeiXmlHandlerTest.class})
public class AllTests {

}

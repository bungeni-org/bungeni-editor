package plugintest;

import java.io.IOException;
import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;

import org.bungeni.plugins.translator.OdtTranslate;
import org.bungeni.translators.utility.files.FileUtility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OdtTranslateTest {
	OdtTranslate testObject = null;
	HashMap paramMap = null;
	String currentDirectory = "";
	String currentDocType = "";
	
	@Before
	public void setUp() throws Exception {
		testObject = new OdtTranslate();
		paramMap = new HashMap();
		currentDirectory = System.getProperty("user.dir");
		currentDocType = "debaterecord";
		paramMap.put("OdfFileURL", currentDirectory + "/test/testdocs/debaterecord_test.odt");
		paramMap.put("OutputFilePath", currentDirectory + "/test/testresults/debaterecord_test_plugin_anx.xml");
		paramMap.put("OutputMetalexFilePath", currentDirectory + "/test/testresults/debaterecord_test_plugin_mlx.xml");
		paramMap.put("TranslatorRootFolder", currentDirectory + "/resources/");
		paramMap.put("TranslatorConfigFile", "configfiles/configs/config_debaterecord.xml");
		paramMap.put("CurrentDocType", currentDocType);
		paramMap.put("CallerPanel", null);
		paramMap.put("PluginMode", "odt2akn");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testExec() throws IOException {
		File foutput  = new File((String) paramMap.get("OutputFilePath"));
		if (foutput.exists()) {
			foutput.delete();
		}
		testObject.setParams(paramMap);
		String sErrors = testObject.exec();
		System.out.println("Translation Errors = \n\n" + sErrors);
		File fnewout =  new File((String) paramMap.get("OutputFilePath"));
                String sOutput = FileUtility.getInstance().FileToString(fnewout.getAbsolutePath()).trim();
                String sExpected = FileUtility.getInstance().FileToString("test/testdocs/debaterecord_test_comp.xml").trim();
		assertTrue("Ooutput file was not created", fnewout.exists() == true);
                assertEquals("Expected output does not match generated output" , sExpected, sOutput);
	}

	@Test
	public final void testExec2() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetParams() {
		testObject.setParams(paramMap);
	}

}

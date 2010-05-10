/**
 * 
 */
package translatortest;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.un.bungeni.translators.globalconfigurations.GlobalConfigurations;
import org.un.bungeni.translators.odttoakn.translator.OATranslator;
import org.un.bungeni.translators.utility.files.FileUtility;


/**
 * @author Ashok Hariharan
 *
 */
public class OATranslatorTestBase 
{

    protected String m_configFilePath = "";
    protected String m_inputDocument = "";
    protected String m_outputDocument = "";
    protected String m_outputMetalex = "";
    protected String m_comparisonDocument = "";
    protected String m_pipeline = "";
    
	public OATranslatorTestBase() {
		super();
	}
	
	private OATranslator myTranslator;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		//set the application path prefix
		GlobalConfigurations.setApplicationPathPrefix("resources/");
		//GlobalConfigurations.setConfigurationFilePath("configfiles/odttoakn/TranslatorConfig_debaterecord.xml");
		GlobalConfigurations.setConfigurationFilePath(this.m_configFilePath);

		//get the instance of the translator
		myTranslator = OATranslator.getInstance();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.un.bungeni.translators.odttoakn.translator.OATranslator#getInstance()}.
	 */
	@Test
	public final void testGetInstance() 
	{
		//chek if the instance is successful instanced  
		assertNotNull(myTranslator);
	}

	/**
	 * Test method for {@link org.un.bungeni.translators.odttoakn.translator.OATranslator#translate(java.lang.String, java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public final void testTranslate() throws Exception 
	{
		//perform a translation
		HashMap<String, File> translatedFiles = myTranslator.translate(this.m_inputDocument ,GlobalConfigurations.getApplicationPathPrefix() + this.m_pipeline);
		//File translation = myTranslator.translate("resources/debaterecord_ken_eng_2008_12_17_main.odt", GlobalConfigurations.getApplicationPathPrefix() + "odttoakn/minixslt/debaterecord/pipeline.xsl");
		System.out.println("OUTPUTTING ERRORS = \n\n" + myTranslator.getValidationErrors());
		
		//input stream
		FileInputStream fis  = new FileInputStream(translatedFiles.get("anxml"));
		FileInputStream fisMlx = new FileInputStream(translatedFiles.get("metalex"));
		//output stream 
		File outFile = new File(this.m_outputDocument);
		File outMlx = new File (this.m_outputMetalex);
		//copy the file
		FileUtility.getInstance().copyFile(fis, outFile);
		FileUtility.getInstance().copyFile(fisMlx, outMlx);
		//compare the generated output with the expected outut
		String sOut = FileUtility.getInstance().FileToString(this.m_outputDocument).trim();
		String sExp = FileUtility.getInstance().FileToString(this.m_comparisonDocument).trim();
		assertEquals("Generated file did not match expected output " , sExp, sOut);
		
	}
	


}

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
import org.bungeni.translators.globalconfigurations.GlobalConfigurations;
import org.bungeni.translators.translator.OATranslator;
//import org.un.bungeni.translators.odttoakn.xslprocbuilder.OAXSLProcBuilder;
//import org.un.bungeni.translators.utility.exceptionmanager.ExceptionManager;


/**
 * @author lucacervone
 *
 */
public class OATranslatorTestDebate 
{
	private OATranslator myTranslator;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		//set the application path prefix
		//GlobalConfigurations.setApplicationPathPrefix("/Users/lucacervone/Documents/AKNTranslatorData/resources/");
		GlobalConfigurations.setApplicationPathPrefix("resources/");
		//GlobalConfigurations.setConfigurationFilePath("configfiles/metalex2akn/TranslatorConfig_debaterecord.xml");
		GlobalConfigurations.setConfigurationFilePath("configfiles/configs/TranslatorConfig_debaterecord.xml");

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
		HashMap<String, File> translatedFiles = myTranslator.translate("resources/ke_debaterecord_2009-10-14_eng.odt");
		//File translation = myTranslator.translate("resources/debaterecord_ken_eng_2008_12_17_main.odt", GlobalConfigurations.getApplicationPathPrefix() + "odttoakn/minixslt/debaterecord/pipeline.xsl");
		System.out.println("OUTPUTTING ERRORS = \n\n" + myTranslator.getValidationErrors());
		
		//input stream
		FileInputStream fis  = new FileInputStream(translatedFiles.get("anxml"));
		
		//output stream 
		FileOutputStream fos = new FileOutputStream("resources/resultAKN_debaterecord.xml");
		//copy the file
		try 
		{
			byte[] buf = new byte[1024];
		    int i = 0;
		    while ((i = fis.read(buf)) != -1) 
		    {
		            fos.write(buf, 0, i);
		    }
		} 
		catch (Exception e) 
		{
		}
		finally 
		{
		
			if (fis != null) fis.close();
		        if (fos != null) fos.close();
		}	
		
	}

}

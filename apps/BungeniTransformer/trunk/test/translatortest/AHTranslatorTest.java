package translatortest;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
//import org.un.bungeni.translators.akntohtml.configurations.AHConfigurationBuilder;
import org.bungeni.translators.akntohtml.translator.AHTranslator;
import org.bungeni.translators.globalconfigurations.GlobalConfigurations;
//import org.un.bungeni.translators.akntohtml.xslprocbuilder.AHXSLProcBuilder;
//import org.un.bungeni.translators.utility.exceptionmanager.ExceptionManager;

public class AHTranslatorTest {
        /*SVN TEST*/
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.un.bungeni.translators.odttoakn.translator.OATranslator#translate(java.lang.String, java.lang.String)}.
	 * @throws Exception 
	 */
	@Test
	public final void testTranslate() throws Exception 
	{
		//set the application path prefix
		GlobalConfigurations.setApplicationPathPrefix("");
		HashMap<String,File> transFiles = AHTranslator.getInstance().translate("resources/Act_Kenya_1980-01-01.xml","resources/akntohtml/minixslt/pipeline.xsl");
		//input stream
		FileInputStream fis  = new FileInputStream(transFiles.get("html"));
		
		//output stream 
		FileOutputStream fos = new FileOutputStream("resources/resultHTML_bill.html");
		
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
		
		//File xslt = AHTranslator.getInstance().buildXSLT("resources/akntohtml/minixslt/pipeline.xsl");
		//input stream
		/*FileInputStream fis  = new FileInputStream(xslt);
		
		//output stream 
		FileOutputStream fos = new FileOutputStream("resources/XSLTResult.xsl");
		
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
		}	*/

	}
}

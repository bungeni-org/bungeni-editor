/*
 * Copyright (C) 2013 undesa
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package translatortest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import org.apache.log4j.BasicConfigurator;
import org.bungeni.translators.globalconfigurations.GlobalConfigurations;
import org.bungeni.translators.translator.OATranslator;
import org.bungeni.translators.utility.files.FileUtility;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author Ashok Hariharan
 */
public class TestConfigParameters {

    private String m_configFilePath = "configfiles/configs/test_configparams.xml";
    private String m_inputDocument = "test/testdocs/TestConfigParameters_doc.xml";
    private String m_outputDocument = "test/testresults/TestConfigParameters_doc_out.xml";
    private String m_comparisonDocument = "test/testdocs/TestConfigParameters_compare.xml";
  
    	public TestConfigParameters() {
		super();
                BasicConfigurator.configure();
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
		//GlobalConfigurations.setConfigurationFilePath(this.getConfigFilePath());
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
                File f = new File(this.m_inputDocument);
                System.out.println("FULL PATH  for Test Document = " + f.getAbsolutePath());
                HashMap inputparams = new HashMap(){
                    {
                        put("xml-param-input","<hello><world name=\"Earth\" /></hello>");
                        put("text-param-input", "input param from transformer");
                        put("text-param-defval-or", "overriden value");
                        put("xml-param-defval-or", "<value><hello name=\"namsakar\"></hello></value>");
                    }  
                };
                HashMap<String, File> translatedFiles = myTranslator.translate(this.m_inputDocument, this.m_configFilePath, inputparams);
		System.out.println("OUTPUTTING ERRORS = \n\n" + myTranslator.getValidationErrors());
		
		//input stream
		FileInputStream fis  = new FileInputStream(translatedFiles.get("final"));
		//output stream 
		File outFile = new File(this.m_outputDocument);

		//copy the file
		FileUtility.getInstance().copyFile(fis, outFile);
		//compare the generated output with the expected outut
                boolean b = diffXml(this.m_outputDocument, this.m_comparisonDocument);
		//String sOut = FileUtility.getInstance().FileToString(this.getOutputDocument()).trim();
		//String sExp = FileUtility.getInstance().FileToString(this.getComparisonDocument()).trim();
		assertEquals("Generated file did not match expected output " , true, b);
		
	}
        private boolean diffXml(String sourceFile, String compFile) throws Exception{
            XMLUnit.setIgnoreAttributeOrder(true);
            XMLUnit.setIgnoreComments(true);
            XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
            XMLUnit.setIgnoreWhitespace(true);
            Diff d = new Diff(new FileReader(sourceFile), new FileReader(compFile));
            return d.identical();
        }


    
}

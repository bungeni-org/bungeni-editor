/**
 * 
 */
package translatortest;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import org.apache.log4j.BasicConfigurator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.bungeni.translators.globalconfigurations.GlobalConfigurations;
import org.bungeni.translators.translator.OATranslator;
import org.bungeni.translators.utility.files.FileUtility;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;


/**
 * This is the main class for the translator unit tests - debatebase and 
 * subsequently the debate test classes  are derived from this class.
 *
 * @author Ashok Hariharan
 *
 */
public class OATranslatorTestBase 
{
    private String m_configFilePath = "";
    private String m_inputDocument = "";
    private String m_outputDocument = "";
    private String m_outputMetalex = "";
    private String m_comparisonDocument = "";
    private String m_pipeline = "";
    
	public OATranslatorTestBase() {
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
                File f = new File(this.getInputDocument());
                System.out.println("FULL PATH  for Test Document = " + f.getAbsolutePath());
		HashMap<String, File> translatedFiles = myTranslator.translate(this.getInputDocument(), this.getConfigFilePath());
		//File translation = myTranslator.translate("resources/debaterecord_ken_eng_2008_12_17_main.odt", GlobalConfigurations.getApplicationPathPrefix() + "odttoakn/minixslt/debaterecord/pipeline.xsl");
		System.out.println("OUTPUTTING ERRORS = \n\n" + myTranslator.getValidationErrors());
		
		//input stream
		FileInputStream fis  = new FileInputStream(translatedFiles.get("anxml"));
		FileInputStream fisMlx = new FileInputStream(translatedFiles.get("metalex"));
		//output stream 
		File outFile = new File(this.getOutputDocument());
		File outMlx = new File (this.getOutputMetalex());
		//copy the file
		FileUtility.getInstance().copyFile(fis, outFile);
		FileUtility.getInstance().copyFile(fisMlx, outMlx);
		//compare the generated output with the expected outut
                boolean b = diffXml(getOutputDocument(), getComparisonDocument());
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


    /**
     * @return the m_configFilePath
     */
    public String getConfigFilePath() {
        return m_configFilePath;
    }

    /**
     * @param m_configFilePath the m_configFilePath to set
     */
    public void setConfigFilePath(String m_configFilePath) {
        this.m_configFilePath = m_configFilePath;
    }

    /**
     * @return the m_inputDocument
     */
    public String getInputDocument() {
        return m_inputDocument;
    }

    /**
     * @param m_inputDocument the m_inputDocument to set
     */
    public void setInputDocument(String m_inputDocument) {
        this.m_inputDocument = m_inputDocument;
    }

    /**
     * @return the m_outputDocument
     */
    public String getOutputDocument() {
        return m_outputDocument;
    }

    /**
     * @param m_outputDocument the m_outputDocument to set
     */
    public void setOutputDocument(String m_outputDocument) {
        this.m_outputDocument = m_outputDocument;
    }

    /**
     * @return the m_outputMetalex
     */
    public String getOutputMetalex() {
        return m_outputMetalex;
    }

    /**
     * @param m_outputMetalex the m_outputMetalex to set
     */
    public void setOutputMetalex(String m_outputMetalex) {
        this.m_outputMetalex = m_outputMetalex;
    }

    /**
     * @return the m_comparisonDocument
     */
    public String getComparisonDocument() {
        return m_comparisonDocument;
    }

    /**
     * @param m_comparisonDocument the m_comparisonDocument to set
     */
    public void setComparisonDocument(String m_comparisonDocument) {
        this.m_comparisonDocument = m_comparisonDocument;
    }

    /**
     * @return the m_pipeline
     */
    public String getPipeline() {
        return m_pipeline;
    }

    /**
     * @param m_pipeline the m_pipeline to set
     */
    public void setPipeline(String m_pipeline) {
        this.m_pipeline = m_pipeline;
    }
	


}

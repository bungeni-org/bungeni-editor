/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package translatortest;

/**
 *
 * @author anthony
 */
public class TestBungeniXmlQuestion2 extends OATranslatorTestBungeniXmlBase {
	public TestBungeniXmlQuestion2(){
		super();
                //TransformerFactoryImpl l = new TransformerFactoryImpl();
                setConfigFilePath("configfiles/configs/config_bungeni_parliamentaryitem.xml");
		setInputDocument("test/testdocs/bungeni_question2.xml");
		setOutputDocument("test/testresults/test_bungeni_question2.xml");
		
		setComparisonDocument("test/testdocs/test_bungeni_question_out.xml");
	}

}

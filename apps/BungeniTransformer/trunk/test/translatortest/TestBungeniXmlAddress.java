/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package translatortest;

/**
 *
 * @author anthony
 */
public class TestBungeniXmlAddress extends OATranslatorTestBungeniXmlBase {
	public TestBungeniXmlAddress(){
		super();
                //TransformerFactoryImpl l = new TransformerFactoryImpl();
                setConfigFilePath("configfiles/configs/config_bungeni_address.xml");
		setInputDocument("test/testdocs/type-address.xml");
		setOutputDocument("test/testresults/test_bungeni_address.xml");
		
		setComparisonDocument("test/testdocs/test_bungeni_question_out.xml");
	}

}
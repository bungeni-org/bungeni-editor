/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package translatortest;

/**
 *
 * @author anthony
 */
public class TestBungeniXmlDoubleEscape extends OATranslatorTestBungeniXmlBase {
	public TestBungeniXmlDoubleEscape(){
		super();
                //TransformerFactoryImpl l = new TransformerFactoryImpl();
                setConfigFilePath("configfiles/configs/config_bungeni_parliamentaryitem.xml");
		setInputDocument("test/testdocs/double-escape.xml");
		setOutputDocument("test/testresults/test_bungeni_doubleescape.xml");
		setOutputMetalex("test/testresults/test_bungeni_doubleescape.mlx");
		setComparisonDocument("test/testdocs/test_bungeni_question_out.xml");
	}

}

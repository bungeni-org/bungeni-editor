/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package translatortest;

/**
 *
 * @author anthony
 */
public class TestBungeniXmlQuestion extends OATranslatorTestBungeniXmlBase {
	public TestBungeniXmlQuestion(){
		super();
                //TransformerFactoryImpl l = new TransformerFactoryImpl();
                setConfigFilePath("configfiles/configs/config_bungeni_parliamentaryitem.xml");
		setInputDocument("test/testdocs/question-6.xml");
		setOutputDocument("test/testresults/test_bungeni_question.xml");
		setOutputMetalex("test/testresults/test_bungeni_question.mlx");
		setComparisonDocument("test/testdocs/test_bungeni_question_out.xml");
	}

}

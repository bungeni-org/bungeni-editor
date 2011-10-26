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
                setConfigFilePath("configfiles/configs/config_bungeni_question.xml");
		setInputDocument("test/testdocs/item-qn.xml");
		setOutputDocument("test/testresults/test_bungeni_question.xml");
		setOutputMetalex("test/testresults/test_bungeni_question.mlx");
		setComparisonDocument("test/testdocs/test_bungeni_question_out.xml");
	}

}

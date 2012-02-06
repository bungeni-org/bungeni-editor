/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package translatortest;

/**
 *
 * @author anthony
 */
public class TestBungeniXmlUser extends OATranslatorTestBungeniXmlBase {
	public TestBungeniXmlUser(){
		super();
                setConfigFilePath("configfiles/configs/config_bungeni_user.xml");
		setInputDocument("test/testdocs/test-user.xml");
		setOutputDocument("test/testresults/test_bungeni_user.xml");
		setOutputMetalex("test/testresults/test_bungeni_user.mlx");
		setComparisonDocument("test/testdocs/test_bungeni_user_out.xml");
	}

}

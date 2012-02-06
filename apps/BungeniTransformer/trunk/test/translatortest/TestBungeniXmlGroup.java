/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package translatortest;

/**
 *
 * @author anthony
 */
public class TestBungeniXmlGroup extends OATranslatorTestBungeniXmlBase {
	public TestBungeniXmlGroup(){
		super();
                setConfigFilePath("configfiles/configs/config_bungeni_group.xml");
		setInputDocument("test/testdocs/grp-parl.xml");
		setOutputDocument("test/testresults/test_bungeni_group.xml");
		setOutputMetalex("test/testresults/test_bungeni_group.mlx");
		setComparisonDocument("test/testdocs/test_bungeni_group_out.xml");
	}

}

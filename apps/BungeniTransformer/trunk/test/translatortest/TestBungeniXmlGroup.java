
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
		
		setComparisonDocument("test/testdocs/test_bungeni_group_out.xml");
	}

}

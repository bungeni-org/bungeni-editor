/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package translatortest;

/**
 *
 * @author anthony
 */
public class TestBungeniXmlMembership extends OATranslatorTestBungeniXmlBase {
	public TestBungeniXmlMembership(){
		super();
                setConfigFilePath("configfiles/configs/config_bungeni_membership.xml");
		setInputDocument("test/testdocs/test_membership.xml");
		setOutputDocument("test/testresults/test_bungeni_membership.xml");
		
		setComparisonDocument("test/testdocs/test_bungeni_membership_out.xml");
	}

}

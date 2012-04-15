package translatortest;


/***
 * Testing debates
 *
 * 14 May 2010
 * @author Ashok Hariharan
 */
public class TestAn1toAn2 extends OATranslatorTestBungeniXmlBase {
	public TestAn1toAn2(){
		super();
                setConfigFilePath("configfiles/configs/config_an1_to_an2.xml");
		setInputDocument("test/testdocs/an1_act01.xml");
		setOutputDocument("test/testresults/out_an1_act01.xml");
		setOutputMetalex("test/testresults/out_an1_act01.mlx");
		setComparisonDocument("test/testdocs/test_bungeni_out.xml");
	}
	
}

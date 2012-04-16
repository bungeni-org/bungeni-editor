package translatortest;


/***
 * Testing debates
 *
 * 14 May 2010
 * @author Ashok Hariharan
 */
public class TestAn1toAn2Constitution extends OATranslatorTestBungeniXmlBase {
	public TestAn1toAn2Constitution(){
		super();
                setConfigFilePath("configfiles/configs/config_an1const_an2const.xml");
		setInputDocument("test/testdocs/constitution_final.xml");
		setOutputDocument("test/testresults/out_constitution_final.xml");
		setOutputMetalex("test/testresults/out_constitution_final.mlx");
		setComparisonDocument("test/testdocs/comp_out_constitution_final.xml");
	}
	
}

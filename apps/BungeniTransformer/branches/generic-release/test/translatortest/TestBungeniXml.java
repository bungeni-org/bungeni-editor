package translatortest;


/***
 * Testing debates
 *
 * 14 May 2010
 * @author Ashok Hariharan
 */
public class TestBungeniXml extends OATranslatorTestBungeniXmlBase {
	public TestBungeniXml(){
		super();
		setInputDocument("test/testdocs/grp-parl.xml");
		setOutputDocument("test/testresults/test_bungeni.xml");
		setOutputMetalex("test/testresults/test_bungeni.mlx");
		setComparisonDocument("test/testdocs/test_bugneni_out.xml");
	}
	
}

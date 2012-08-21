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
                setConfigFilePath("configfiles/configs/config_bungeni_question.xml");
		setInputDocument("test/testdocs/item-qn.xml");
		setOutputDocument("test/testresults/test_bungeni.xml");
		
		setComparisonDocument("test/testdocs/test_bungeni_out.xml");
	}
	
}

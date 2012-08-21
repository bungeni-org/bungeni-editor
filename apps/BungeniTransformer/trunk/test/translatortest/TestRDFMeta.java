package translatortest;


/***
 * Testing debates
 *
 * 14 May 2010
 * @author Ashok Hariharan
 */
public class TestRDFMeta extends OATranslatorTestDebateBase {
	public TestRDFMeta(){
		super();
		setInputDocument("test/testdocs/ke_debaterecord_2011-9-23_en.odt");
		setOutputDocument("test/testresults/test_rdfmeta.xml");
		
		setComparisonDocument("test/testdocs/test_rdfmeta_out.xml");
	}
	
}

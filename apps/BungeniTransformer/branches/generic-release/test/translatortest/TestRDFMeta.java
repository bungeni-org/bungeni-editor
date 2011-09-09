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
		setInputDocument("test/testdocs/test_rdfmeta.odt");
		setOutputDocument("test/testresults/test_rdfmeta.xml");
		setOutputMetalex("test/testresults/test_rdfmeta.mlx");
		setComparisonDocument("test/testdocs/test_rdfmeta_out.xml");
	}
	
}

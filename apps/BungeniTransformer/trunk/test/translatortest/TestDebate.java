package translatortest;


/***
 * Testing debates
 *
 * 14 May 2010
 * @author Ashok Hariharan
 */
public class TestDebate extends OATranslatorTestDebateBase {
	public TestDebate(){
		super();
		setInputDocument("test/testdocs/debaterecord_test.odt");
		setOutputDocument("test/testresults/debaterecord_test_out_anx.xml");
		
		setComparisonDocument("test/testdocs/debaterecord_test_comp.xml");
	}
	
}

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
		setInputDocument("test/testdocs/test_debaterecord_2.odt");
		setOutputDocument("test/testresults/test_debaterecord_2.xml");
		setOutputMetalex("test/testresults/test_debaterecord_2.mlx");
		setComparisonDocument("test/testdocs/test_debaterecord_out.xml");
	}
	
}

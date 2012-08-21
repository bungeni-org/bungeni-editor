package translatortest;


/***
 * Testing debates
 *
 * 14 May 2010
 * @author Ashok Hariharan
 */
public class TestDebateInlineRef extends OATranslatorTestDebateBase {
	public TestDebateInlineRef(){
		super();
		setInputDocument("test/testdocs/test_debate_inline_ref.odt");
		setOutputDocument("test/testresults/test_debate_inline_ref.xml");
		
		setComparisonDocument("test/testdocs/debaterecord_test_comp.xml");
	}
	
}

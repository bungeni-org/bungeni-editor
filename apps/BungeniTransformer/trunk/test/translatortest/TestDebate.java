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
		this.m_inputDocument = "test/testdocs/test_debaterecord.odt";
		this.m_outputDocument = "test/testresults/test_debaterecord.xml";
		this.m_outputMetalex = "test/testresults/test_debaterecord.mlx";
		this.m_comparisonDocument = "test/testdocs/test_debaterecord_out.xml";
	}
	
}

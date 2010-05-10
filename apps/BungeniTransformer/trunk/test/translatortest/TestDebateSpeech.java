package translatortest;

/**
 * 
 * Tests the <speech> element translation
 * @author Ashok
 *
 */
public class TestDebateSpeech  extends OATranslatorTestDebateBase {
		public TestDebateSpeech(){
			super();
			this.m_inputDocument = "tests/testdocs/test_speech.odt";
			this.m_outputDocument = "tests/testresults/test_speech.xml";
			this.m_comparisonDocument = "tests/testdocs/test_speech_out.xml";
		}
		
		
}

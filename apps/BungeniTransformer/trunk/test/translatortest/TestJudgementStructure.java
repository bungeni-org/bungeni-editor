package translatortest;

public class TestJudgementStructure extends OATranslatorTestJudgementBase {
	public TestJudgementStructure(){
		super();
		this.m_inputDocument = "tests/testdocs/test_judgement_structural_translation.odt";
		this.m_outputDocument = "tests/testresults/test_judgement_structural_translation.xml";
		this.m_comparisonDocument = "tests/testdocs/test_judgement_structural_translation_out.xml";
		
	}
}

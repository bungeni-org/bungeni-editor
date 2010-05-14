package translatortest;

public class TestJudgementStructure extends OATranslatorTestJudgementBase {
	public TestJudgementStructure(){
		super();
		setInputDocument("tests/testdocs/test_judgement_structural_translation.odt");
		setOutputDocument("tests/testresults/test_judgement_structural_translation.xml");
		setComparisonDocument("tests/testdocs/test_judgement_structural_translation_out.xml");
		
	}
}

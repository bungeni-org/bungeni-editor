package translatortest;

public class TestJudgementHeader extends OATranslatorTestJudgementBase {
	public TestJudgementHeader(){
		super();
		setInputDocument("tests/testdocs/test_judgement_header.odt");
		setOutputDocument("tests/testresults/test_judgement_header.xml");
		
		setComparisonDocument("tests/testdocs/test_judgement_header_out.xml");
		
	}
}

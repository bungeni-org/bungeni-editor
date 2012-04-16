package translatortest;

public class TestJudgementHeader extends OATranslatorTestJudgementBase {
	public TestJudgementHeader(){
		super();
		setInputDocument("tests/testdocs/test_judgement_header.odt");
		setOutputDocument("tests/testresults/test_judgement_header.xml");
		setOutputMetalex("tests/testresults/test_judgement_header.mlx");
		setComparisonDocument("tests/testdocs/test_judgement_header_out.xml");
		
	}
}

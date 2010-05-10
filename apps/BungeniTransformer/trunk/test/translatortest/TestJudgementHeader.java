package translatortest;

public class TestJudgementHeader extends OATranslatorTestJudgementBase {
	public TestJudgementHeader(){
		super();
		this.m_inputDocument = "tests/testdocs/test_judgement_header.odt";
		this.m_outputDocument = "tests/testresults/test_judgement_header.xml";
		this.m_outputMetalex = "tests/testresults/test_judgement_header.mlx";
		this.m_comparisonDocument = "tests/testdocs/test_judgement_header_out.xml";
		
	}
}

package translatortest;

public class TestDebateBillReference extends OATranslatorTestDebateBase {
	public TestDebateBillReference(){
		super();
		this.m_inputDocument = "tests/testdocs/test_debaterecord_bill_ref.odt";
		this.m_outputDocument = "tests/testresults/test_debaterecord_bill_ref.xml";
		this.m_outputMetalex = "tests/testresults/test_debaterecord_bill_ref.mlx";
		this.m_comparisonDocument = "tests/testdocs/test_debaterecord_bill_ref_out.xml";
	}
	
}

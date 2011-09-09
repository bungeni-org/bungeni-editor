package translatortest;

public class TestDebateBillReference extends OATranslatorTestDebateBase {
	public TestDebateBillReference(){
		super();
		setInputDocument("tests/testdocs/test_debaterecord_bill_ref.odt");
		setOutputDocument("tests/testresults/test_debaterecord_bill_ref.xml");
		setOutputMetalex("tests/testresults/test_debaterecord_bill_ref.mlx");
		setComparisonDocument("tests/testdocs/test_debaterecord_bill_ref_out.xml");
	}
	
}

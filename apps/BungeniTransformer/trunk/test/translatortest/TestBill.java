package translatortest;


/***
 * Testing debates
 *
 * 14 May 2010
 * @author Ashok Hariharan
 */
public class TestBill extends OATranslatorTestBillBase {
	public TestBill(){
		super();
		setInputDocument("test/testdocs/bill_test.odt");
		setOutputDocument("test/testresults/bill_test_out_anx.xml");
		
		setComparisonDocument("test/testdocs/debaterecord_test_comp.xml");
	}
	
}

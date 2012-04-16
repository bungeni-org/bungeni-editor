package translatortest;


/***
 * Testing debates
 *
 * 14 May 2010
 * @author Ashok Hariharan
 */
public class TestJudgement extends OATranslatorTestJudgementBase {
	public TestJudgement(){
		super();
		setInputDocument("test/testdocs/test_judgement.odt");
		setOutputDocument("test/testresults/test_judgement.xml");
		setOutputMetalex("test/testresults/test_judgement.mlx");
		setComparisonDocument("test/testdocs/test_judgement_out.xml");
	}
	
}

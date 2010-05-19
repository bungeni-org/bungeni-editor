package translatortest;

/**
 *This is the base class used for creating unit tests for judgement type documents.
 * The base class sets up the the translator configuration and the piple for the translation.
 * Any deriving unit test class need only set :
 *
 * 	m_inputDocument = "test/testdocs/test_judgement.odt";
	m_outputDocument = "test/testresults/test_judgement.xml";
	m_outputMetalex = "test/testresults/test_judgement.mlx";
	m_comparisonDocument = "test/testdocs/test_judgement_out.xml";
 *
 * @author Ashok Hariharan
 */
public class OATranslatorTestJudgementBase extends OATranslatorTestBase {

	public OATranslatorTestJudgementBase() {
		super();
		setConfigFilePath("configfiles/odttoakn/TranslatorConfig_judgement.xml");
		setPipeline("odttoakn/minixslt/judgement/pipeline.xsl");
	}
}


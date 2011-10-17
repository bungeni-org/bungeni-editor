package translatortest;


/**
 *This is the base class used for creating unit tests for debaterecord type documents.
 * The base class sets up the the translator configuration and the piple for the translation.
 * Any deriving unit test class need only set :
 *
 * 	m_inputDocument = "test/testdocs/test_debaterecord.odt";
	m_outputDocument = "test/testresults/test_debaterecord.xml";
	m_outputMetalex = "test/testresults/test_debaterecord.mlx";
	m_comparisonDocument = "test/testdocs/test_debaterecord_out.xml";
 *
 * @author Ashok Hariharan
 */
public class OATranslatorTestBungeniXmlBase extends OATranslatorTestBase {

	public OATranslatorTestBungeniXmlBase() {
		super();
		setConfigFilePath("configfiles/configs/config_bungeni_group.xml");
	}
}


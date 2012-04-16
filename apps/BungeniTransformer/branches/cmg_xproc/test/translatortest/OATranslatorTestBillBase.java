package translatortest;

public class OATranslatorTestBillBase extends OATranslatorTestBase {

	public OATranslatorTestBillBase() {
		super();
		setConfigFilePath("configfiles/configs/TranslatorConfig_bill.xml");
		setPipeline("metalex2akn/minixslt/bill/pipeline.xsl");
		// TODO Auto-generated constructor stub
	}
}


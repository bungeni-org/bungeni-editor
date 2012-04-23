/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package translatortest;

/**
 *
 * @author anthony
 */
public class TestBungeniXmlAgendaItem extends OATranslatorTestBungeniXmlBase {
	public TestBungeniXmlAgendaItem(){
		super();
                //TransformerFactoryImpl l = new TransformerFactoryImpl();
                setConfigFilePath("configfiles/configs/config_bungeni_parliamentaryitem.xml");
		setInputDocument("test/testdocs/bungeni_agendaitem.xml");
		setOutputDocument("test/testresults/test_bungeni_agendaitem.xml");
		setOutputMetalex("test/testresults/test_bungeni_agendaitem.mlx");
		setComparisonDocument("test/testresults/test_bungeni_agendaitem_out.xml");
	}

}

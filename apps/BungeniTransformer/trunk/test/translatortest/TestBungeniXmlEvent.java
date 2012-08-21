/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package translatortest;

/**
 *
 * @author anthony
 */
public class TestBungeniXmlEvent extends OATranslatorTestBungeniXmlBase {
	public TestBungeniXmlEvent(){
		super();
                //TransformerFactoryImpl l = new TransformerFactoryImpl();
                setConfigFilePath("configfiles/configs/config_bungeni_parliamentaryitem.xml");
		setInputDocument("test/testdocs/bungeni_event.xml");
		setOutputDocument("test/testresults/test_bungeni_event.xml");
		
		setComparisonDocument("test/testresults/test_bungeni_event_out.xml");
	}

}

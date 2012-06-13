/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package translatortest;

/**
 *
 * @author bzuadmin
 */
public class TestActInlineRef extends OATranslatorTestDebateBase {
	public TestActInlineRef(){
		super();
		setInputDocument("test/testdocs/test_act_inline_ref.odt");
		setOutputDocument("test/testresults/test_act_inline_ref.xml");
		setOutputMetalex("test/testresults/test_act_inline_ref_mlx.xml");
		setComparisonDocument("test/testdocs/act_test_comp.xml");
	}
    
}

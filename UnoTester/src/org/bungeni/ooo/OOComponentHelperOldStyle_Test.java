/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.ooo;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.editor.test.UnoTest;

/**
 *
 * @author undesa
 */
public class OOComponentHelperOldStyle_Test extends UnoTest {

   public void testSetSectionMetadataAttributes_OldStyle() {
        System.out.println("setSectionMetadataAttributes_OldStyle");
        OOComponentHelper.USE_OLD_STYLE_METADATA = true;

        HashMap<String,String> metaMap = new HashMap<String,String>();
        metaMap.put("Type", "Clause");
        metaMap.put("UUID", "9999-38383-38383-2772");
        ooComponent.setSectionMetadataAttributes("Section2", metaMap);
        HashMap<String,String> returnMap = new HashMap<String,String>();
        returnMap = ooComponent.getSectionMetadataAttributes("Section2");

        for (String keyName : returnMap.keySet()) {
            p(keyName + " : " + returnMap.get(keyName));
        }
        p("test assertions ...");
        assertNotTrue(returnMap.containsKey("Type") && returnMap.containsKey("UUID"), "testSetSectionMetadataAttributes_OldStyle", "matching meta names");
        assertNotTrue(returnMap.get("Type").equals("Clause") && returnMap.get("UUID").equals("9999-38383-38383-2772"), "testSetSectionMetadataAttributes_OldStyle", "matching meta values");

    }


    public void testGetSectionType_OldStyle(){
            System.out.println("testGetSectionType_OldStyle");
            String s = ooComponent.getSectionType("Section2");
            assertNotTrue(s.equals("Clause"), "testGetSectionType_OldStyle", "matching section Types");
     }


@Override
    public void runTests(){
        try {
            super.runTests();

            this.testSetSectionMetadataAttributes_OldStyle();
            this.testGetSectionType_OldStyle();

        } catch (Exception ex) {
            Logger.getLogger(OOComponentHelperOldStyle_Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

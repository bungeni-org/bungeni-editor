
package org.bungeni.ooo;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.editor.test.UnoTest;

/**
 *
 * @author Ashok Hariharan
 */
public class OOComponentHelperTest extends UnoTest {

     public void testSetSectionMetadataAttributes() {
        System.out.println("setSectionMetadataAttributes");
        HashMap<String,String> metaMap = new HashMap<String,String>();
        metaMap.put("Type", "Article");
        metaMap.put("UUID", "3838-38383-38383-2772");
        ooComponent.setSectionMetadataAttributes("Section1", metaMap);
        HashMap<String,String> returnMap = new HashMap<String,String>();
        returnMap = ooComponent.getSectionMetadataAttributes("Section1");

        for (String keyName : returnMap.keySet()) {
            p(keyName + " : " + returnMap.get(keyName));
        }
        p("test assertions ...");
        assertNotTrue(returnMap.containsKey("Type") && returnMap.containsKey("UUID"), "testSetSectionMetadataAttributes", "matching meta names");
        assertNotTrue(returnMap.get("Type").equals("Article") && returnMap.get("UUID").equals("3838-38383-38383-2772"), "testSetSectionMetadataAttributes", "matching meta values");

    }

     public void testGetSectionType(){
            System.out.println("testGetSectionType");
            String s = ooComponent.getSectionType("Section1");
            assertNotTrue(s.equals("Article"), "testGetSectionType", "matching section Types");
     }




@Override
    public void runTests(){
        try {
            super.runTests();

            this.testSetSectionMetadataAttributes();
            this.testGetSectionType();
            
        } catch (Exception ex) {
            Logger.getLogger(OOComponentHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   


}

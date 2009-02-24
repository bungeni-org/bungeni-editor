/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.semantics.documents;

import java.util.HashMap;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.semantics.BaseSemanticCheck;

/**
 *
 * @author undesa
 */
public class validateSectionTypeExists extends BaseSemanticCheck {

/**
 *
 * @author Ashok Hariharan
 */
    @Override
      public boolean validateSemantic(OOComponentHelper ooDocument, HashMap<String,Object> paramMap) {
        super.validateSemantic(ooDocument, paramMap);
        boolean bState = processSemantic();
        return bState;
    }

    private boolean processSemantic() {
        if (parameterMap.containsKey("ROOT_SECTION_NAME")) {
            if (parameterMap.containsKey("SECTION_TYPE")) {
                String rootSection =  (String) parameterMap.get("ROOT_SECTION_NAME");
                String findSectionType = (String) parameterMap.get("SECTION_TYPE");
                String foundSection = ooDocument.getChildSectionByType(rootSection, findSectionType);
                if (foundSection == null) {
                    return false;
                } else
                    return true;
            }
        }
        return false;
    }

}

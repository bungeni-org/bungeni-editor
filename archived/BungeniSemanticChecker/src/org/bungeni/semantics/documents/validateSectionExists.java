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
 * @author Ashok Hariharan
 */
public class validateSectionExists extends BaseSemanticCheck {
    @Override
      public boolean validateSemantic(OOComponentHelper ooDocument, HashMap<String,Object> paramMap) {
        super.validateSemantic(ooDocument, paramMap);
        boolean bState = processSemantic();
        return bState;
    }

    private boolean processSemantic(){
      if (parameterMap.containsKey("SECTION_NAME")) {
          String strSectionName = (String) parameterMap.get("SECTION_NAME");
          if (ooDocument.hasSection(strSectionName))
              return true;
          else
              return false;
      } else return false;
    }
}

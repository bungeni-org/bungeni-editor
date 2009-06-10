/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.semantics;

import java.util.HashMap;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public class BaseSemanticCheck implements ISemanticCheck {
    protected OOComponentHelper ooDocument ; 
    protected HashMap<String,Object> parameterMap = new HashMap<String, Object>();
    
    public boolean validateSemantic(OOComponentHelper ooDocument, HashMap<String,Object> paramMap) {
        this.ooDocument = ooDocument;
        this.parameterMap = paramMap;
        return true;
    }

}

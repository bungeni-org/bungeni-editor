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
public interface ISemanticCheck {
    public boolean validateSemantic (OOComponentHelper ooDocument, HashMap<String, Object> paramMap);
}

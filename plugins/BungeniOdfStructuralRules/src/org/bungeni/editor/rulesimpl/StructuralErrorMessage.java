/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.rulesimpl;

import java.text.MessageFormat;

/**
 *
 * @author undesa
 */
public class StructuralErrorMessage {
    public static String toErrorMessage(StructuralError error) {
       String msg = "";
               Object[] arguments = {error.parentSectionName, error.parentSectionType, error.childSectionType, error.childSectionName  };

       if (error.failRuleType.equals("AllowedChildSections")) {
            msg = "The document section ''{0}'' of type ''{1}'' cannot contain \n a document " +
                "section of type ''{2}'' with document section name ''{3}'' ." ;
       } else {
            msg = "The order of section ''{3}'' of type ''{2}'' has been placed in the wrong order \n inside  " +
                "section name ''{0}'' of type ''{1}'' ." ;

       }
         msg =   MessageFormat.format(msg, arguments);
        return msg;
    }

}

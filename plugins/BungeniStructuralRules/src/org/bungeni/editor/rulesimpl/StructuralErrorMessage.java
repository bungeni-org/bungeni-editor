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
        String msg = "The document section {0} of type {1} cannot contain \n a document " +
                "section of type {2} with document section name {3} ." ;
        Object[] arguments = {error.parentSectionName, error.parentSectionType, error.childSectionType, error.childSectionName  };
         msg =   MessageFormat.format(msg, arguments);
        return msg;
    }

    public static void main (String[] args) {
       StructuralError err = new StructuralError();
       err.parentSectionType = "parent section";
       err.parentSectionName = "parent name";
       err.childSectionType = "child section";
       err.childSectionName = "child section name";
       System.out.println(StructuralErrorMessage.toErrorMessage(err));

    }
}

/*
 * validatorFactory.java
 *
 * Created on March 9, 2008, 10:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.validators;

import org.bungeni.editor.actions.validators.IBungeniActionValidator;
import org.bungeni.editor.actions.toolbarSubAction;

/**
 *
 * @author Administrator
 */
public class validatorFactory {
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(validatorFactory.class.getName());
 
    /** Creates a new instance of validatorFactory */
    public validatorFactory() {
    }
    
    public static IBungeniActionValidator getValidatorClass(toolbarSubAction subAction) {
             IBungeniActionValidator validator = null;
       try {
             log.debug("getValidatorClass: creating event class"+ subAction.validator_class());
             Class validatorClass;
             validatorClass = Class.forName(subAction.validator_class());
             validator = (IBungeniActionValidator) validatorClass.newInstance();
       } catch (ClassNotFoundException ex) {
           log.error("getValidatorClass:"+ ex.getMessage());
        } finally {
             return validator;
        }
    }
    
}

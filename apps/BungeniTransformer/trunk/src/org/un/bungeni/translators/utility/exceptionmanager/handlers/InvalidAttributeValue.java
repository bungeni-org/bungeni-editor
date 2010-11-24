/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.un.bungeni.translators.utility.exceptionmanager.handlers;

/**
 *
 * @author undesa
 */
public class InvalidAttributeValue extends MissingAttribute{
    @Override
    public String getLocalizedMessage(String attribute, String element){
        String localizedMessage = null;
        localizedMessage = 
                resourceBundle.getString("INVALID_ATTRIBUTE_VALUE_LEFT") +
                attribute +
                resourceBundle.getString("INVALID_ATTRIBUTE_VALUE_CENTER") +
                element +
                resourceBundle.getString("INVALID_ATTRIBUTE_VALUE_RIGHT");
        return localizedMessage;
    }
}

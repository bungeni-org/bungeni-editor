/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.translators.utility.exceptionmanager.handlers;

/* JDK imports */
import java.util.ResourceBundle;

/* Non JDK imports */
import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.SAXParseException;

/**
 * This is the base exception handler. It implements the initialize method
 * defined in the interface <code>IExceptionHandler</code>.
 * 
 * @author murithi
 */
abstract class BaseExceptionHandler implements IExceptionHandler{
    
    SAXParseException exception;
    ResourceBundle resourceBundle;
    DOMParser parser;
    String ODFSectionString;

    public void initialize(SAXParseException ex,
            ResourceBundle resourceBundle,
            DOMParser parser,
            String ODFSectionString){
        this.exception = ex;
        this.resourceBundle = resourceBundle;
        this.parser = parser;
        this.ODFSectionString = ODFSectionString;
    }

    public String getLocalizedMessage(String attribute, String element){
        return null;
    }
    
}

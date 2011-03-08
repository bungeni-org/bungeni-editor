package org.bungeni.translators.utility.exceptionmanager;

import java.util.ArrayList;
import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 *
 * @author murithi
 */
public interface IExceptionManager extends ErrorHandler{
    /*
     * Interface for SAX Parser exception manager. Exeptions raised during
     * transformation of ODT documents are matched using configured regular
     * expressions and a matchin handler is invoked.
     */
    public void setDOMParser(DOMParser parser);
    public void setODFDocument(String anOdfDocumentPath);
    abstract String getStartingWords(String anId);
    public ArrayList<ValidationError> getValidationErrors();
    public void init();
    abstract void processExceptions(SAXParseException ex);
    abstract String getConfigurationFilePath();
}

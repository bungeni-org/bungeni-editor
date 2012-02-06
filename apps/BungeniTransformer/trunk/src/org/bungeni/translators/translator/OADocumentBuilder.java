package org.bungeni.translators.translator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;

/**
 *
 * @author Ashok
 */
public class OADocumentBuilder {

    private static org.apache.log4j.Logger log =
            Logger.getLogger(OADocumentBuilder.class.getName());
    
    private static OADocumentBuilder instance = null;
    private DocumentBuilder dbuilder = null;

    private OADocumentBuilder() throws ParserConfigurationException {
        dbuilder = OADocumentBuilderFactory.getInstance().getDBF().newDocumentBuilder();
    }

    public static OADocumentBuilder getInstance() {
        if (instance == null) {
            try {
                instance = new OADocumentBuilder();
            } catch (ParserConfigurationException ex) {
                log.error("Error whil setting up document builder factory", ex);
            }
        }
        return instance;
    }

    public DocumentBuilder getDocumentBuilder() {
        return this.dbuilder;
    }
}

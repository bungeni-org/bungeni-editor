package org.bungeni.translators.translator;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Singleton for DocumentBuilderFactory
 * @author Ashok
 */
public class OADocumentBuilderFactory {

    private DocumentBuilderFactory dbfInstance = null;

    private static OADocumentBuilderFactory instance = null;
    
    private OADocumentBuilderFactory(){
        dbfInstance = DocumentBuilderFactory.newInstance();
    }

    public static OADocumentBuilderFactory getInstance(){
        if (instance == null) {
            instance = new OADocumentBuilderFactory();
        }
        return instance;
    }

    public DocumentBuilderFactory getDBF(){
        return this.dbfInstance;
    }
}

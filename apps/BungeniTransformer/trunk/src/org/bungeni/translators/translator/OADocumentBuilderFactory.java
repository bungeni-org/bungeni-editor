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
        //!+BLANK_NS(2013-03-15, enabling namespaceware = true to prevent blank xmlns attributes
        //during adoption and import of nodes
        //!+WARNING+WARNING - possible breakages, side-effects !
        dbfInstance.setNamespaceAware(true);
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

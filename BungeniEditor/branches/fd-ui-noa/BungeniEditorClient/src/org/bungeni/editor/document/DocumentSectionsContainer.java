/*
 * DocumentSectionsContainer.java
 *
 * Created on May 23, 2008, 12:06 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.document;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import org.bungeni.db.BungeniClientDB;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.db.QueryResults;
import org.bungeni.db.SettingsQueryFactory;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;

/**
 *
 * @author Administrator
 */
public class DocumentSectionsContainer {
    
    private static HashMap<String, DocumentSection> documentSections = new HashMap<String, DocumentSection>();
    /** Creates a new instance of DocumentSectionsContainer */
    public DocumentSectionsContainer() {
    }
    
    public static DocumentSection getDocumentSectionByType(String type ) {
        if (getDocumentSectionsContainer().containsKey(type)) {
            return getDocumentSectionsContainer().get(type);
        } else
            return null;
    }
    
    /*
     *
     *  A sectionType is unique within a document type
     *
     *
     */
    public static HashMap<String,DocumentSection> getDocumentSectionsContainer(){
       if (documentSections.size() == 0 ) { 
        String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
        BungeniClientDB db =  new BungeniClientDB(DefaultInstanceFactory.DEFAULT_INSTANCE(), DefaultInstanceFactory.DEFAULT_DB());
        QueryResults qr = db.ConnectAndQuery(SettingsQueryFactory.Q_FETCH_DOCUMENT_SECTION_TYPES(docType));
      
        if (qr.hasResults()) {
             for (Vector<String> row :  qr.theResults()){
                DocumentSection section = new DocumentSection( qr, row);
                documentSections.put(section.getSectionType(), section);
             }
        }
        }
        return documentSections;
    }

    public static void main(String[] args) {
       Set<String> Keys =  DocumentSectionsContainer.getDocumentSectionsContainer().keySet();
        for (String key : Keys) {
            System.out.println(key);
        }
    }
}

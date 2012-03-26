package org.bungeni.editor.document;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.bungeni.editor.config.SectionTypesReader;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 *
 * @author Ashok Hariharan
 */
public class DocumentSectionsContainer {

    private static org.apache.log4j.Logger log =
            org.apache.log4j.Logger.getLogger(DocumentSectionsContainer.class.getName());
    private static HashMap<String, DocumentSection> documentSections = new HashMap<String, DocumentSection>();

    /** Creates a new instance of DocumentSectionsContainer */
    public DocumentSectionsContainer() {
    }

    public static DocumentSection getDocumentSectionByType(String type) {
        System.out.println("Document Section By Type : " + type);
        if (getDocumentSectionsContainer().containsKey(type)) {
            return getDocumentSectionsContainer().get(type);
        } else {
            return null;
        }
    }

    /*
     *
     *  A sectionType is unique within a document type
     * !+CONFIG_IN_XML(ah,jan-2012) all section type configs moved to xml
     *
     */
    public static HashMap<String, DocumentSection> getDocumentSectionsContainer() {
        if (documentSections.isEmpty()) {
            try {
                String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
                List<Element> arrSectionTypes = SectionTypesReader.getInstance().getSectionTypes();

                for(Element elemSectionType : arrSectionTypes) {
                    DocumentSection aSection = new DocumentSection(elemSectionType, docType);
                    documentSections.put(aSection.getSectionType(), aSection);
                }
            } catch (JDOMException ex) {
                log.error("Error while getting document section types", ex);
            } catch (IOException ex) {
                log.error("Error while getting document section types", ex);
            }
        }
        return documentSections;
    }

    public static void main(String[] args) {
        Set<String> Keys = DocumentSectionsContainer.getDocumentSectionsContainer().keySet();
        for (String key : Keys) {
            System.out.println(key);
        }
    }
}

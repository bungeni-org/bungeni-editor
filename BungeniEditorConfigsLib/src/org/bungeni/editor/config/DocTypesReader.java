/*
 *  Copyright (C) 2012 Africa iParliaments
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package org.bungeni.editor.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.bungeni.extutils.CommonEditorXmlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 * Configuration access class that provides access to configuration
 * @author Ashok Hariharan
 */
public class DocTypesReader extends BaseConfigReader {
    private static Logger log = Logger.getLogger(DocTypesReader.class.getName());

    public final static String SETTINGS_FOLDER = configsFolder();
    public final static String DOCTYPES_FILE = "doc_types.xml";
    public final static String RELATIVE_PATH_TO_SYSTEM_PARAMETERS_FILE = SETTINGS_FOLDER + File.separator + DOCTYPES_FILE;

    private static DocTypesReader thisInstance = null;

    private Document doctypesDocument = null;

    private XPath xpathInstance = null;

    private DocTypesReader(){

    }

    /**
     * Singleton object accessor
     * @return
     */
    public static DocTypesReader getInstance(){
        if (null == thisInstance) {
            thisInstance = new DocTypesReader();
        }
        return thisInstance;
    }

    /**
     * Returns all the doctypes in configuration as Elements
     * @return
     * @throws JDOMException
     */
    public List<Element> getDocTypes() throws JDOMException{
      if (null != getDocument()) {
           List<Element> doctypeElements =  getXPathInstance().selectNodes(getDocument(),"//doctypes/doctype");
           return doctypeElements;
      } else {
          log.error("Doctypes code file could not be loaded !");
          return null;
      }
    }
    

    /**
     * Returns all the active Doctype elements in configuration
     * @return
     * @throws JDOMException
     */
    public List<Element> getActiveDocTypes() throws JDOMException{
      if (null != getDocument()) {
           List<Element> doctypeElements =  getXPathInstance().selectNodes(getDocument(),"//doctypes/doctype[@state='1']");
           return doctypeElements;
      } else {
          log.error("Locale code file could not be loaded !");
          return null;
      }
    }

    /**
     * Returns the doctype element for a doctype name
     * @param docType
     * @return
     * @throws JDOMException
     */
    public Element getDocTypeByName(String docType) throws JDOMException {
        if (null != getDocument()) {
            Element doctypeElement = (Element) getXPathInstance().selectSingleNode(getDocument(), "//doctypes/doctype[@name='"+ docType + "']");
            return doctypeElement;
        } else {
            log.error("Error getting doctype element");
            return null;
        }
     }

    /**
     * Gets a doctype element 
     * @param docType
     * @return G
     */
    public Element getDocTypeByNameClone(String docType) {
        Element docTypeElem = null;
        try {
            docTypeElem = getDocTypeByName(docType);
        } catch (JDOMException ex) {
            log.error("Error while getting doctype Configuration");
        }
        if (docTypeElem != null){
            return (Element)docTypeElem.clone();
        }
        return null;
    }


    /**
     * Returns the doctypes/output element
     * @return 
     */
    public Element getOutputsBlock(){
      if (null != getDocument()) {
            Element doctypeOutput = null;
            try {
                doctypeOutput = (Element) this.getXPathInstance().selectSingleNode(
                getDocument(),
                "//doctypes/outputs"
                );
            } catch (JDOMException ex) {
                log.error("Error while accessing outputs element");
            }
           return doctypeOutput;
      } else {
          log.error("Doctypes code file could not be loaded !");
          return null;
      }
    }
     /**
      * Get root Section type for a doctype
      * @param docType
      * @return
      */
    public String getRootForDocType(String docType) {
        String sRootSectionType = "";
        try {
            Element elemDocType = getDocTypeByName(docType);
            sRootSectionType = elemDocType.getAttributeValue("root");
        } catch (JDOMException ex) {
            log.error("Error getting doctype element for : " + docType , ex);
        }
        return sRootSectionType;
    }

    public String getWorkUriForDocType(Element doctypeElem) {
        Element uriWork = null;
        String sUriWork = "";
        try {
           uriWork = (Element) getXPathInstance().selectSingleNode(doctypeElem, "./uri[@type='work']");
           if (null != uriWork) {
                sUriWork = uriWork.getTextNormalize();
           }
        } catch (JDOMException ex) {
            log.error("Error getting work uri element");
        }
        return sUriWork;
    }


    public String getExpUriForDocType(Element doctypeElem) {
        Element uriExp = null;
        String sUriExpr = "";
        try {
           uriExp = (Element) getXPathInstance().selectSingleNode(doctypeElem, "./uri[@type='expression']");
           if (null != uriExp) {
                sUriExpr = uriExp.getTextNormalize();
           }
        } catch (JDOMException ex) {
            log.error("Error getting work uri element");
        }
        return sUriExpr;
    }

    public String getFileNameSchemeForDocType(Element doctypeElem) {
        return doctypeElem.getChildTextNormalize("file-name-scheme");
    }

    public List<Element> getMetadataModelEditorsForDocType(Element doctypeElem) {
        Element metadataModelEditorsElem = doctypeElem.getChild("metadata-editors");
        List<Element> listModelEditors = null;
        if (null != metadataModelEditorsElem) {
            listModelEditors = metadataModelEditorsElem.getChildren("metadata-editor");
        } else {
            log.error("Error while getting metadata-model-editors !");
        }
        return listModelEditors;
    }

    public List<Element> getPartsForDocType(Element doctypeElem) {
        Element partsElem = doctypeElem.getChild("parts");
        List<Element> listParts = null;
        if (null != partsElem) {
            listParts = partsElem.getChildren("part");
        } else {
            log.error("Error while getting <parts> !");
        }
        return listParts;
    }

    private XPath getXPathInstance() throws JDOMException {
        if (this.xpathInstance == null) {
            this.xpathInstance = XPath.newInstance("//locale");
        }
        return this.xpathInstance;
    }

    private Document getDocument() {
       if (this.doctypesDocument == null) {
            try {
                this.doctypesDocument = CommonEditorXmlUtils.loadFile(RELATIVE_PATH_TO_SYSTEM_PARAMETERS_FILE);
            } catch (FileNotFoundException ex) {
                log.error("file not found", ex);
            } catch (UnsupportedEncodingException ex) {
                log.error("encoding error", ex);
            } catch (JDOMException ex) {
                log.error("dom error", ex);
            } catch (IOException ex) {
                log.error("io error", ex);
            }
        }
       return this.doctypesDocument;
    }

   /**
    * Returns a string list of Active doctype names
    * @return
    */
    public List<String> getDocTypeNames(){
        List<Element> docTypeElements = new ArrayList<Element>(0);
        List<String> docTypeNames = new ArrayList<String>(0);
        try {
            docTypeElements = getActiveDocTypes();
        } catch (JDOMException ex) {
            log.error("Failed getting docTypes", ex);
        }
        for (Element docType : docTypeElements) {
            docTypeNames.add(
                docType.getAttributeValue("name")
                );
        }
        return docTypeNames;
    }

}

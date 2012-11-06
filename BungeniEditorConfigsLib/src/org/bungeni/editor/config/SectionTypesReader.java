/*
 *  Copyright (C) 2012 Africa i-Parliaments
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
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.bungeni.extutils.CommonEditorXmlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 *
 * @author Ashok Hariharan
 */
public class SectionTypesReader extends BaseConfigReader {

       private static Logger log = Logger.getLogger(SectionTypesReader.class.getName());

 private static SectionTypesReader thisInstance = null;

    private final static String SETTINGS_FOLDER = CONFIGS_FOLDER + File.separator + "section_types";

    private HashMap<String,Document> cachedTypes = new HashMap<String,Document>();


    private SectionTypesReader() {
    }

    public static SectionTypesReader getInstance() {
        if (thisInstance == null ) {
            thisInstance = new SectionTypesReader();
        }
        return thisInstance ;
    }

    public List getSectionTypes(String docType) throws JDOMException, IOException {
       if (null != getDocument(docType)) {
            XPath xPath = XPath.newInstance("//sectionTypes[@for='" + docType + "']/sectionType");
            return (List) xPath.selectNodes(getDocument(docType));
       }
       return null;
    }

    private Document getDocument(String docType)
            throws FileNotFoundException,
            UnsupportedEncodingException,
            JDOMException,
            IOException {
       if (!this.cachedTypes.containsKey(docType)) {
            String docSectionsFolder = SETTINGS_FOLDER;
            String docSectionsFile = docSectionsFolder + File.separator + docType + ".xml";
            this.cachedTypes.put(docType, CommonEditorXmlUtils.loadFile(docSectionsFile));
       }
       return this.cachedTypes.get(docType);
    }

    public List getSectionTypesClone(String docType) {
        List listSectionTypes = new ArrayList();
        try {
            listSectionTypes =  getDocument(docType).cloneContent();
        } catch (FileNotFoundException ex) {
            log.error("unable to getSectionTypes list", ex);
        } catch (UnsupportedEncodingException ex) {
            log.error("unable to getSectionTypes list", ex);
        } catch (JDOMException ex) {
            log.error("unable to getSectionTypes list", ex);
        } catch (IOException ex) {
            log.error("unable to getSectionTypes list", ex);
        }
        return listSectionTypes;
    }


    /**
     * (rm, feb 2012) - this method obtains the numbering tag for a section type
     * @param sectionTypeName
     * @return
     */
    public Element getSectionTypeNumbering(String docType, String sectionTypeName) throws JDOMException, IOException {
               
       if (!this.cachedTypes.containsKey(docType)) {
            String docSectionsFolder = SETTINGS_FOLDER;
            String docSectionsFile = docSectionsFolder + File.separator + docType + ".xml";
            this.cachedTypes.put(docType, CommonEditorXmlUtils.loadFile(docSectionsFile));
        }

        XPath xPath = XPath.newInstance("//sectionType[@name='"+ sectionTypeName +"']/numbering");
        return (Element) xPath.selectSingleNode(this.cachedTypes.get(docType));
    }


}

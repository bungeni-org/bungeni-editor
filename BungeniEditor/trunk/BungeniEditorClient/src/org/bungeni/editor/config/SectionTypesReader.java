/*
 *  Copyright (C) 2012 windows
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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.extutils.CommonFileFunctions;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

/**
 *
 * @author Ashok Hariharan
 */
public class SectionTypesReader {

 private static SectionTypesReader thisInstance = null;

    private SAXBuilder saxBuilder ;

    private HashMap<String,Document> cachedTypes = new HashMap<String,Document>();


    private SectionTypesReader() {
        saxBuilder = new SAXBuilder("org.apache.xerces.parsers.SAXParser",
                        false);

    }

    public static SectionTypesReader getInstance() {
        if (thisInstance == null ) {
            thisInstance = new SectionTypesReader();
        }
        return thisInstance ;
    }

    public List getSectionTypes() throws JDOMException, IOException {
       String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
       if (!this.cachedTypes.containsKey(docType)) {
            String docSectionsFolder = BungeniEditorProperties.get("sectionTypesFolderRoot");
            String docSectionsFile = CommonFileFunctions.convertRelativePathToFullPath(docSectionsFolder) + File.separator + docType + ".xml";
            this.cachedTypes.put(docType, saxBuilder.build(new File(docSectionsFile)));
        }
        XPath xPath = XPath.newInstance("//sectionTypes[@for='" + docType + "']/sectionType");
        return (List) xPath.selectNodes(this.cachedTypes.get(docType));

    }


    /**
     * (rm, feb 2012) - this method obtains the numbering tag for a section type
     * @param sectionTypeName
     * @return
     */
    public Element getSectionTypeNumbering(String sectionTypeName) throws JDOMException, IOException {
               
       String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
       if (!this.cachedTypes.containsKey(docType)) {
            String docSectionsFolder = BungeniEditorProperties.get("sectionTypesFolderRoot");
            String docSectionsFile = CommonFileFunctions.convertRelativePathToFullPath(docSectionsFolder) + File.separator + docType + ".xml";
            this.cachedTypes.put(docType, saxBuilder.build(new File(docSectionsFile)));
        }

        XPath xPath = XPath.newInstance("//sectionType[@name='"+ sectionTypeName +"']/numbering");
        return (Element) xPath.selectSingleNode(this.cachedTypes.get(docType));
    }


}

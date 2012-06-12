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
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.extutils.CommonXmlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 * This Reads the sectionType configuration from section_types
 * @author Ashok Hariharan
 */
public class InlineTypesReader extends BaseConfigReader {

 private static InlineTypesReader thisInstance = null;

    private static final Logger log = Logger.getLogger(InlineTypesReader.class.getName());

    private final static String SETTINGS_FOLDER = CONFIGS_FOLDER + File.separator + "inline_types";

    private HashMap<String,Document> cachedTypes = new HashMap<String,Document>();


    private InlineTypesReader() {
    }

    public static InlineTypesReader getInstance() {
        if (thisInstance == null ) {
            thisInstance = new InlineTypesReader();
        }
        return thisInstance ;
    }

    private Document getDocument() {
       String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
       if (!this.cachedTypes.containsKey(docType)) {
            try {
                String docSectionsFolder = SETTINGS_FOLDER;
                String docSectionsFile = docSectionsFolder + File.separator + docType + ".xml";
                this.cachedTypes.put(docType, CommonXmlUtils.loadFile(docSectionsFile));
            } catch (FileNotFoundException ex) {
                log.error("File not found", ex);
            } catch (UnsupportedEncodingException ex) {
                log.error("Wrong encodinig", ex);
            } catch (JDOMException ex) {
                log.error("Jdom error", ex);
            } catch (IOException ex) {
                log.error("IO error", ex);
            }
        }
        if (this.cachedTypes.containsKey(docType))
             return this.cachedTypes.get(docType);
        else
            return null;
    }

    public List getInlineTypes() throws JDOMException, IOException {
       String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
       if (null != getDocument()) {
        XPath xPath = XPath.newInstance("//inlineTypes[@for='" + docType + "']/inlineType");
        return (List) xPath.selectNodes(this.cachedTypes.get(docType));
        } else {
           log.error("Unabel to get inline types");
           return null;
        }
    }


  /**
   * This gets the metadata applicable for a sectiontype
   * @param inlineTypeName
   * @return
   * @throws FileNotFoundException
   * @throws UnsupportedEncodingException
   * @throws JDOMException
   * @throws IOException
   */
  public List getInlineTypeMetadata(String inlineTypeName) throws FileNotFoundException, UnsupportedEncodingException, JDOMException, IOException {
       String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
       if (null != getDocument()) {
          XPath xPath = XPath.newInstance("//inlineType[@name='"+ inlineTypeName +"']/metadatas/metadata");
          return xPath.selectNodes(this.cachedTypes.get(docType));
       } else {
           log.error("Error getting inline type metadata");
           return null;
       }
  }

 /**
  * This gets the output template applicable for a section type
  * @param inlineTypeName
  * @return
  * @throws JDOMException
  */
  public Element getInlineTypeOutputTemplate(String inlineTypeName) throws JDOMException{
       String docType = BungeniEditorPropertiesHelper.getCurrentDocType();
       if (null != getDocument()) {
          XPath xPath = XPath.newInstance("//inlineType[@name='"+ inlineTypeName +"']/output");
          return (Element) xPath.selectSingleNode(this.cachedTypes.get(docType));
       } else {
           log.error("Error getting inline type metadata");
           return null;
       }
  }


}

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
import java.util.List;
import org.apache.log4j.Logger;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.extutils.CommonXmlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 *
 * @author Ashok
 */
public class DocumentMetadataReader extends BaseConfigReader {
    private static Logger log = Logger.getLogger(DocumentMetadataReader.class.getName());

    public final static String SETTINGS_FOLDER = CONFIGS_FOLDER + File.separator + "metadata";
    public final static String METADATA_FILE = BungeniEditorPropertiesHelper.getCurrentDocType() + ".xml";
    public final static String RELATIVE_PATH_TO_SYSTEM_PARAMETERS_FILE = SETTINGS_FOLDER + File.separator + METADATA_FILE;

    private static DocumentMetadataReader thisInstance = null;

    private Document metadataDocument = null;

    private XPath xpathInstance = null;

    private DocumentMetadataReader(){

    }

    public static DocumentMetadataReader getInstance(){
        if (null == thisInstance) {
            thisInstance = new DocumentMetadataReader();
        }
        return thisInstance;
    }

    public List<Element> getVisibleMetadatas()  throws JDOMException{
      if (null != getDocument()) {
           List<Element> doctypeElements =  getXPath().selectNodes(getDocument(),
                   "//metadatas[@for='"+
                     BungeniEditorPropertiesHelper.getCurrentDocType() +
                   "']/metadata[@visible='1']");
           return doctypeElements;
      } else {
          log.error("Metadatas code file could not be loaded !");
          return null;
      }
    }

    public List<Element> getMetadatas() throws JDOMException{
      if (null != getDocument()) {
           List<Element> doctypeElements =  getXPath().selectNodes(getDocument(),
                   "//metadatas[@for='"+
                     BungeniEditorPropertiesHelper.getCurrentDocType() +
                   "']/metadata");
           return doctypeElements;
      } else {
          log.error("Metadatas code file could not be loaded !");
          return null;
      }
    }

    public Element getMetadataByName(String metaName) throws JDOMException {
        if (null != getDocument()) {
                 Element metadataElem =  (Element) getXPath().selectSingleNode(getDocument(),
                   "//metadatas[@for='"+
                     BungeniEditorPropertiesHelper.getCurrentDocType() +
                   "']/metadata[@name='" + metaName + "']");
                 return metadataElem;
        } else {
            log.error("Unable to load metadata document getMetadataByName()");
            return null;
        }
    }

   

  
    private XPath getXPath() throws JDOMException {
        if (this.xpathInstance == null) {
            this.xpathInstance = XPath.newInstance("//locale");
        }
        return this.xpathInstance;
    }

    private Document getDocument() {
       if (this.metadataDocument == null) {
            try {
                this.metadataDocument = CommonXmlUtils.loadFile(RELATIVE_PATH_TO_SYSTEM_PARAMETERS_FILE);
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
       return this.metadataDocument;
    }

}

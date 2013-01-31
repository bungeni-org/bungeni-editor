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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.bungeni.extutils.CommonEditorXmlUtils;
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

    public final static String SETTINGS_FOLDER = configsFolder() + File.separator + "metadata";

    private static DocumentMetadataReader thisInstance = null;

    private Map<String,Document> cachedMetadatas = new HashMap<String,Document>();

    private XPath xpathInstance = null;

    private DocumentMetadataReader(){

    }

    public static DocumentMetadataReader getInstance(){
        if (null == thisInstance) {
            thisInstance = new DocumentMetadataReader();
        }
        return thisInstance;
    }

    public List<Element> getVisibleMetadatas(String docType)  throws JDOMException{
      if (null != getDocument(docType)) {
           List<Element> doctypeElements =  getXPath().selectNodes(getDocument(docType),
                   "//metadatas[@for='"+
                   docType +
                   "']/metadata[@visible='1']");
           return doctypeElements;
      } else {
          log.error("Metadatas code file could not be loaded !");
          return null;
      }
    }

    public List<Element> getMetadatas(String docType) throws JDOMException{
      if (null != getDocument(docType)) {
           List<Element> doctypeElements =  getXPath().selectNodes(getDocument(docType),
                   "//metadatas[@for='"+
                     docType +
                   "']/metadata");
           return doctypeElements;
      } else {
          log.error("Metadatas code file could not be loaded !");
          return null;
      }
    }

    public Element getMetadataByName(String docType, String metaName) throws JDOMException {
        if (null != getDocument(docType)) {
                 Element metadataElem =  (Element) getXPath().selectSingleNode(
                     getDocument(docType),
                     "//metadatas[@for='"+
                     docType +
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

    public Document getDocument(String docType) {
       if (!this.cachedMetadatas.containsKey(docType)){
            Document doc;
            try {
                doc = CommonEditorXmlUtils.loadFile(SETTINGS_FOLDER + File.separator + docType + ".xml");
                this.cachedMetadatas.put(docType, doc);
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
       return this.cachedMetadatas.get(docType);
    }

}

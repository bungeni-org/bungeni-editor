/*
 *  Copyright (C) 2012 UN/DESA Africa i-Parliaments Action Plan
 * 
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
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
public class ConditionsReader extends BaseConfigReader {
    private static Logger log = Logger.getLogger(ConditionsReader.class.getName());

    public final static String SETTINGS_FOLDER = configsFolder() + File.separator + "actions"  + File.separator + "conditions";
    
    private static ConditionsReader thisInstance = null;

    private Document conditionsDocument = null;

    private XPath xpathInstance = null;

    private ConditionsReader(){

    }

    public static ConditionsReader getInstance(){
        if (null == thisInstance) {
            thisInstance = new ConditionsReader();
        }
        return thisInstance;
    }

    public Element getConditionByName(String docType, String conditionName) throws JDOMException{
      if (null != getDocument(docType)) {
           Element conditionElem =  (Element) getXPath().selectSingleNode(getDocument(docType),"//conditions[@for='"+ docType+"']/condition[@name='" + conditionName + "']");
           return conditionElem;
      } else {
          log.error("Locale code file could not be loaded !");
          return null;
      }
    }


    private XPath getXPath() throws JDOMException {
        if (this.xpathInstance == null) {
            this.xpathInstance = XPath.newInstance("//locale");
        }
        return this.xpathInstance;
    }

    private Document getDocument(String docType) {
       if (this.conditionsDocument == null) {
            try {
                this.conditionsDocument = CommonEditorXmlUtils.loadFile(SETTINGS_FOLDER + File.separator + docType + ".xml");
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
       return this.conditionsDocument;
    }

}

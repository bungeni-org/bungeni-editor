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
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.bungeni.extutils.CommonXmlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 *
 * @author Ashok
 */
public class LanguageCodesReader {
    private static Logger log = Logger.getLogger(LanguageCodesReader.class.getName());

    public final static String SETTINGS_FOLDER = "settings" + File.separator + "locales";
    public final static String LANGUAGE_CODES_FILE = "languages_a2.xml";
    public final static String RELATIVE_PATH_TO_SYSTEM_PARAMETERS_FILE = SETTINGS_FOLDER + File.separator + LANGUAGE_CODES_FILE;

    private static LanguageCodesReader thisInstance = null;

    private Document languageCodesDocument = null;

    private XPath xpathInstance = null;

    private LanguageCodesReader(){

    }

    public static LanguageCodesReader getInstance(){
        if (null == thisInstance) {
            thisInstance = new LanguageCodesReader();
        }
        return thisInstance;
    }

    public List<Element> getLanguageCodeElements() throws JDOMException{
      if (null != getDocument()) {
           List<Element> langCodeElements =  getXPath().selectNodes(getDocument(),"//language");
           return langCodeElements;
      } else {
          log.error("Lang code file could not be loaded !");
          return null;
      }
    }

    public String getLanguageName(Element language, String deflang){
        String sLangName = "UNKNOWN";
        try {
          Element elemLangName =   (Element) getXPath().selectSingleNode(language, "./name[@lang='" + deflang + "']");
          sLangName = elemLangName.getTextNormalize();
        } catch (JDOMException ex) {
            log.error("Error getting language name ", ex);
        }
        return sLangName;
    }

    private XPath getXPath() throws JDOMException {
        if (this.xpathInstance == null) {
            this.xpathInstance = XPath.newInstance("//language");
        }
        return this.xpathInstance;
    }

    private Document getDocument() {
       if (this.languageCodesDocument == null) {
            try {
                this.languageCodesDocument = CommonXmlUtils.loadFile(RELATIVE_PATH_TO_SYSTEM_PARAMETERS_FILE);
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
       return this.languageCodesDocument;
    }

}

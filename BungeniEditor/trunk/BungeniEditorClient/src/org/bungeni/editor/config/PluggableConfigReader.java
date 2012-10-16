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
import java.util.List;
import org.apache.log4j.Logger;
import org.bungeni.extutils.CommonXmlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 *
 * @author Ashok Hariharan
 */
public class PluggableConfigReader {

    private static Logger log = Logger.getLogger(PluggableConfigReader.class.getName());

    private Document pluggableConfigDocument = null;
    private static final String PLUGGABLE_CONFIGS_FILE = "configs.xml";
    private static PluggableConfigReader instance = null;
    private XPath xpathInstance = null;

    private PluggableConfigReader(){
        pluggableConfigDocument = getDocument();
    }

    public static PluggableConfigReader getInstance(){
        if (null == instance){
            instance = new PluggableConfigReader();
        }
        return instance;
    }

   public List<Element> getConfigs(){
       return 
         this.pluggableConfigDocument.getRootElement().getChildren("configs");
   }

   public Element getDefaultConfig() throws JDOMException{
       XPath xpath = XPath.newInstance("//config[@default='true']");
       Element elementConfig = (Element) xpath.selectSingleNode(getDocument());
       return elementConfig;
   }

    private XPath getXPath() throws JDOMException {
        if (this.xpathInstance == null) {
            this.xpathInstance = XPath.newInstance("//locale");
        }
        return this.xpathInstance;
    }

   private Document getDocument() {
       if (this.pluggableConfigDocument == null) {
            try {
                this.pluggableConfigDocument = CommonXmlUtils.loadFile(
                        BaseConfigReader.BASE_SETTINGS_FOLDER + 
                        File.separator + 
                        PLUGGABLE_CONFIGS_FILE
                        );
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
       return this.pluggableConfigDocument;
    }
}

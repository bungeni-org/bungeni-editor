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
import java.util.List;
import org.apache.log4j.Logger;
import org.bungeni.utils.CommonEditorXmlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 * Reads pluggable Configuration
 * @author Ashok Hariharan
 */
public class PluggableConfigReader {

    private static Logger log = Logger.getLogger(PluggableConfigReader.class.getName());

    private Document pluggableConfigDocument = null;
    private static final String PLUGGABLE_CONFIGS_FILE = "configs.xml";
    private static PluggableConfigReader instance = null;
    private XPath xpathInstance = null;

    public static List<PluggableConfig> asList(List<Element> configs){
        List<PluggableConfig> pluggables = new ArrayList<PluggableConfig>(0);
        for (Element element : configs) {
            pluggables.add(new PluggableConfig(element));
        }
        return pluggables;
    }

    public static class PluggableConfig {
        public final  String name;
        public final String title;
        public final String url;
        public final String folderBase;
        public final String receiverClass ;
        public final  boolean configDefault;
        public final boolean customConfig ;
        public final Element customConfigElement ;

        public PluggableConfig(Element configElement) {
            this.name = configElement.getAttributeValue("name");
            this.title = configElement.getAttributeValue("title");
            this.url = configElement.getAttributeValue("url");
            this.folderBase = configElement.getAttributeValue("folder-base");
            this.receiverClass = configElement.getAttributeValue("receiver").trim();
            String sDefault = configElement.getAttributeValue("default");
            Element customChild = configElement.getChild("custom");
            if (null == customChild) {
               this.customConfigElement = null;
               this.customConfig = false;
            } else {
                this.customConfig = true;
                this.customConfigElement = customChild;
            }
            if (sDefault == null ) {
                this.configDefault = false;
            } else {
                this.configDefault = Boolean.parseBoolean(sDefault);
            }
        }

        @Override
        public String toString() {
            return this.name + " - " + this.title;
        }

    }

    private PluggableConfigReader(){
        pluggableConfigDocument = getDocument();
    }

    public static PluggableConfigReader getInstance(){
        if (null == instance){
            instance = new PluggableConfigReader();
        }
        return instance;
    }

   public List<PluggableConfig> getConfigs(){
       return
         PluggableConfigReader.asList(
             this.pluggableConfigDocument.
                getRootElement().
                    getChildren("config")
         );
   }

   public PluggableConfig getDefaultConfig() throws JDOMException{
       XPath xpath = XPath.newInstance("//config[@default='true']");
       Element elementConfig = (Element) xpath.selectSingleNode(getDocument());
       return new PluggableConfig(elementConfig);
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
                this.pluggableConfigDocument = CommonEditorXmlUtils.loadFile(
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

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
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.log4j.Logger;
import org.bungeni.extutils.CommonEditorXmlUtils;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

/**
 * Class to Read system-parameter.xml 
 * @author Ashok Hariharan
 */
public class SystemParameterReader extends BaseConfigReader {

    private static Logger log = Logger.getLogger(SystemParameterReader.class.getName());

    public final static String SETTINGS_FOLDER = CONFIGS_FOLDER;
    public final static String SYSTEM_PARAMETER_FILE_NAME = "system-parameters.xml";
    public final static String PATH_TO_SYSTEM_PARAMETERS_FILE = SETTINGS_FOLDER + File.separator + SYSTEM_PARAMETER_FILE_NAME;

    private static SystemParameterReader thisInstance = null;

    private Document systemParametersDocument = null;

    private XPath xpathInstance = null;

    private SystemParameterReader(){
        
    }

    public static SystemParameterReader getInstance(){
        if (null == thisInstance) {
            thisInstance = new SystemParameterReader();
        }
        return thisInstance;
    }

    private Element getPropertyElement(String key) throws JDOMException{
            Element propElement = (Element) getXPath().selectSingleNode(this.systemParametersDocument, "//props/prop[@name='" + key + "']");
            return propElement;
    }

    public String getParameter(String key) {
        String propValue = null;
        if (getDocument() != null) {
                Element propElement = null;
                try {
                    propElement =  getPropertyElement(key);
                } catch (JDOMException ex) {
                    log.error("Dom error getting property", ex);
                }
                if (propElement != null) {
                   propValue =  propElement.getAttributeValue("value");
                } else {
                    log.error("Value was not found, returning null");
                }
            } else {
                log.error("system parameters document is null");
        }
        return propValue;
    }

    public void setParameter(String key, String value){
        if (null != getDocument()) {
            try {
                Element propElement = getPropertyElement(key);
                Attribute attr = propElement.getAttribute("value");
                attr.setValue(value);
            } catch (JDOMException ex) {
                log.error("Error getting parameter for setting", ex);
            }
        } else {
            log.error("Unable to get doument handle");
        }
    }

    public void save() throws IOException{
        XMLOutputter xmlout = new XMLOutputter();
        xmlout.setFormat(Format.getRawFormat());
        FileWriter fw=new FileWriter(SystemParameterReader.PATH_TO_SYSTEM_PARAMETERS_FILE);
        xmlout.output(getDocument(), fw);
        fw.flush();
        fw.close();
    }

    public void reload() {
        this.systemParametersDocument = null;
        this.xpathInstance = null;
    }

    private XPath getXPath() throws JDOMException {
        if (this.xpathInstance == null) {
            this.xpathInstance = XPath.newInstance("//props");
        }
        return this.xpathInstance;
    }

    private Document getDocument() {
       if (this.systemParametersDocument == null) {
            try {
                this.systemParametersDocument = CommonEditorXmlUtils.loadFile(PATH_TO_SYSTEM_PARAMETERS_FILE);
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
       return this.systemParametersDocument;
    }
}

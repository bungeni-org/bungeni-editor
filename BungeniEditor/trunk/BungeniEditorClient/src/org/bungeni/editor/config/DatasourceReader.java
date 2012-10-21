/*
 * Copyright (C) 2012 test
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.bungeni.editor.config;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.log4j.Logger;
import org.bungeni.extutils.CommonXmlUtils;
import org.bungeni.utils.CommonEditorXmlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 *
 * @author bzuadmin
 */
//!+FIX_THIS(ah,01-05-2012) This should be done using BungeniConnector and not using
//config reader.
@Deprecated
public class DatasourceReader extends BaseConfigReader {
    
    private static Logger log = Logger.getLogger(DocTypesReader.class.getName());

    public final static String XMLDATASOURCES_FOLDER = "settings" + File.separator + "datasource" + File.separator + "xml";
    public static String DATASOURCE_FILE = null;

    private static DatasourceReader thisInstance = null;

    private Document localesDocument = null;

     private DatasourceReader(){

    }
     
     public static DatasourceReader getInstance(){
        if (null == thisInstance) {
            thisInstance = new DatasourceReader();
        }
        return thisInstance;
    }
    
    public List<Element> getDatasourceElements(String packagAlias, String alias) throws FileNotFoundException {
        DATASOURCE_FILE =  XMLDATASOURCES_FOLDER + File.separator + packagAlias + ".xml";
        List<Element> datasourceElements = null;
        
        try {
            datasourceElements = (List<Element>) XPath.selectNodes(getDocument(),"//"+packagAlias+"/"+alias+"");
        } catch (JDOMException ex) {
            log.error("getDatasourceElements : " + ex.getMessage());
        } finally {
            return datasourceElements;
        }
    }
    

    private Document getDocument() {
            try {
            this.localesDocument = CommonEditorXmlUtils.loadFile(DATASOURCE_FILE);
        } catch (FileNotFoundException ex) {
            log.error("file not found", ex);
        } catch (UnsupportedEncodingException ex) {
            log.error("encoding error", ex);
        } catch (JDOMException ex) {
            log.error("dom error", ex);
        } catch (IOException ex) {
            log.error("io error", ex);
        }
       return this.localesDocument;
    }
}



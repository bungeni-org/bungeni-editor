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

package org.bungeni.editor.system;

import java.io.File;
import java.io.FileNotFoundException;
import javax.xml.transform.Source;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.bungeni.editor.config.SysTransformsReader;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.output.DOMOutputter;

/**
 *
 * @author Ashok Hariharan
 */
public class TransformerGenerator {

 private static final Logger log =
     Logger.getLogger(TransformerGenerator.class.getName());

    private static TransformerGenerator instance = null;

    private TransformerFactory thisTransFactory = null;
    //private Transformer thisTransformer = null;

    private TransformerGenerator() throws TransformerConfigurationException{
        thisTransFactory = net.sf.saxon.TransformerFactoryImpl.newInstance();
       // thisTransformer = thisTransFactory.newTransformer();
    }

    public static TransformerGenerator getInstance(){
        if (null == instance) {
            try {
                instance = new TransformerGenerator();
            } catch (TransformerConfigurationException ex) {
              log.error("Error while initializaing TransformerGenerator object", ex);
            }
        }
        return instance;
    }

    public File typeGeneratorTemplate() throws JDOMException {
        StreamSource xsltTypeGenerator = null;
        File ftypeGenerator =   new File(
                       SysTransformsReader.SYSTEM_TRANS_CACHE + File.separator +
                       "type_transform.xsl"
                       );
        try {
            ConfigurationProvider configs = ConfigurationProvider.getInstance();
            Document mergedConfigs = configs.getMergedDocument();
            //Conver to a w3c dom document
            DOMOutputter dout = new DOMOutputter();
            org.w3c.dom.Document domDoc = dout.output(mergedConfigs);
            xsltTypeGenerator = SysTransformsReader.getInstance().getXslt("typeGenerator.xsl");
            Transformer transformer = thisTransFactory.newTransformer(xsltTypeGenerator);
            StreamResult outputResult = new StreamResult(
                  ftypeGenerator
                  );
            transformer.transform( new DOMSource(domDoc), outputResult);
        } catch (TransformerException ex) {
             log.error("Transform exception !", ex);
        } catch (FileNotFoundException ex) {
            log.error("XSLT not found !", ex);
        }
        return ftypeGenerator;
    }



}

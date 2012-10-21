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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.bungeni.editor.config.SysTransformsReader;
import org.bungeni.extutils.CommonXmlUtils;
import org.bungeni.utils.CommonEditorXmlUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.DOMOutputter;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

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


    private void createNewFileWhenPathNotExists(File ftypeGenerator) throws IOException {
       if (!ftypeGenerator.exists()) {
             (
             new File(
             ftypeGenerator.getParent()
              )
          ).mkdirs();
          ftypeGenerator.createNewFile();
        }
    }

    public File typeTlcGeneratorTemplate(ConfigGeneratorError err, Document mergedConfigs, String docType) throws IOException {
        StreamSource xsltTypeGenerator = null;
        File ftypeGenerator =   new File(
            BaseSystemConfig.SYSTEM_TEMPLATES + File.separator +
            "type_tlc_transform_" + docType  + ".xsl"
        );
        createNewFileWhenPathNotExists(ftypeGenerator);
        log.info("Full path to xslt file : " + ftypeGenerator.getAbsolutePath());
        try {
            //Conver to a w3c dom document
            DOMOutputter dout = new DOMOutputter();
            org.w3c.dom.Document domDoc = dout.output(mergedConfigs);
            xsltTypeGenerator = SysTransformsReader.getInstance().getXslt("type_tlc_generator.xsl");
            Transformer transformer = thisTransFactory.newTransformer(xsltTypeGenerator);
            FileWriter fwTypeGen = new FileWriter(ftypeGenerator);
            StreamResult outputResult = new StreamResult(
                  fwTypeGen
                  );
            transformer.transform( new DOMSource(domDoc), outputResult);
            fwTypeGen.flush();
        } catch (TransformerException ex) {
             log.error("Transform exception !", ex);
        } catch (FileNotFoundException ex) {
            log.error("Full path to xslt file : " + ftypeGenerator.getAbsolutePath());
            log.error("XSLT not found !", ex);
        } catch (JDOMException ex) {
            log.error("There was an error " , ex);
        }
        return ftypeGenerator;
    }

    public File typeGeneratorTemplate(ConfigGeneratorError err, Document mergedConfigs, String docType) throws JDOMException, IOException {
        StreamSource xsltTypeGenerator = null;
        File ftypeGenerator =   new File(
            BaseSystemConfig.SYSTEM_TEMPLATES + File.separator +
            "type_transform_" + docType  + ".xsl"
        );
        createNewFileWhenPathNotExists(ftypeGenerator);
        log.info("Full path to xslt file : " + ftypeGenerator.getAbsolutePath());
        try {
            //Conver to a w3c dom document
            DOMOutputter dout = new DOMOutputter();
            org.w3c.dom.Document domDoc = dout.output(mergedConfigs);
            xsltTypeGenerator = SysTransformsReader.getInstance().getXslt("type_generator.xsl");
            Transformer transformer = thisTransFactory.newTransformer(xsltTypeGenerator);
            FileWriter fwTypeGen = new FileWriter(ftypeGenerator);
            StreamResult outputResult = new StreamResult(
                  fwTypeGen
                  );
            transformer.transform( new DOMSource(domDoc), outputResult);
            fwTypeGen.flush();
        } catch (TransformerException ex) {
             log.error("Transform exception !", ex);
        } catch (FileNotFoundException ex) {
            log.error("Full path to xslt file : " + ftypeGenerator.getAbsolutePath());
            log.error("XSLT not found !", ex);
        }

        //now merge type_static_templates.xsl at the end of this template.

        Document typeStaticXSL = CommonEditorXmlUtils.loadFile(BaseSystemConfig.SYSTEM_GENERATOR +
                File.separator + "type_static_templates.xsl");
        Document typeCurrentXSL = CommonEditorXmlUtils.loadFile(ftypeGenerator);
        List children = typeStaticXSL.getRootElement().getChildren();
        for (int i=0 ; i < children.size() ; i++ ) {
            Element child = (Element) children.get(i);
            Element childClone= (Element) child.clone();
            childClone.detach();
            typeCurrentXSL.getRootElement().addContent(childClone);
        }
        FileWriter fwXSLTgen = new FileWriter(ftypeGenerator);
        XMLOutputter xout = new XMLOutputter(Format.getPrettyFormat());
        xout.output(typeCurrentXSL, fwXSLTgen);
        fwXSLTgen.flush();
        return ftypeGenerator;
    }

public File typeMetaIdentifierGenerator(ConfigGeneratorError err, Document metadataDocument, String docType) throws JDOMException, IOException {
        StreamSource xsltTypeGenerator = null;
        
        String generatorName = "type_meta_ident_publi";

        File ftypeGenerator =   new File(
            BaseSystemConfig.SYSTEM_TEMPLATES + File.separator +
            generatorName + "_" + docType  + ".xsl"
        );
        createNewFileWhenPathNotExists(ftypeGenerator);
        log.info("Full path to xslt file : " + ftypeGenerator.getAbsolutePath());
        try {
            //Conver to a w3c dom document
            DOMOutputter dout = new DOMOutputter();
            org.w3c.dom.Document domDoc = dout.output(
                   metadataDocument
            );
            xsltTypeGenerator = SysTransformsReader.getInstance().getXslt(
                    generatorName +
                    "_generator.xsl"
               );
            Transformer transformer = thisTransFactory.newTransformer(xsltTypeGenerator);
            FileWriter fwTypeGen = new FileWriter(ftypeGenerator);
            StreamResult outputResult = new StreamResult(
                  fwTypeGen
                  );
            transformer.transform( new DOMSource(domDoc), outputResult);
            fwTypeGen.flush();
        } catch (TransformerException ex) {
             log.error("Transform exception !", ex);
        } catch (FileNotFoundException ex) {
            log.error("Full path to xslt file : " + ftypeGenerator.getAbsolutePath());
            log.error("XSLT not found !", ex);
        }
        return ftypeGenerator;
    }


    public List<File> typeGenerators(ConfigGeneratorError err, String docType) throws JDOMException, IOException {
        List<File> genFiles = new ArrayList<File>(0);
        ConfigurationProvider cp = ConfigurationProvider.getInstance();
        cp.generateMergedConfiguration(err, docType);
        Document doc = cp.getMergedDocument();
        File f1 = typeGeneratorTemplate(err, doc, docType);
        File f2 = typeTlcGeneratorTemplate(err, doc, docType);
        return genFiles;
    }


}

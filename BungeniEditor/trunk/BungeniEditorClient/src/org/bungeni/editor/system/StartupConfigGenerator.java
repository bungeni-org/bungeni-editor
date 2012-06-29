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

import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.bungeni.editor.config.DocTypesReader;
import org.bungeni.editor.config.DocumentMetadataReader;
import org.bungeni.extutils.CommonXmlUtils;
import org.bungeni.translators.configurations.steps.OAXSLTStep;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

/**
 *
 * @author Ashok hariharan
 */
public class StartupConfigGenerator {

   private static final Logger log =
             Logger.getLogger(StartupConfigGenerator.class.getName());

    private StartupConfigGenerator(){

    }

    private static StartupConfigGenerator thisInstance = null;

    public static StartupConfigGenerator getInstance(){
        if (null == thisInstance) {
            thisInstance = new StartupConfigGenerator();
        }
        return thisInstance;
    }


    
    private void createODFMetaMasterXSLT(String docType,
            File baseTemplate,
            File templateToMerge
            ) throws IOException, TemplateException, JDOMException, CloneNotSupportedException {
        /**
         * The first template is the root template,
         * the content of the following templates is merged into the root template
         * 
         */
        Document docXSLTtoMergeInto = null;
        try {
        //get the template XSLT
            docXSLTtoMergeInto  = CommonXmlUtils.loadFile(
                baseTemplate
                );
        } catch (FileNotFoundException ex) {
            log.error("Error while loading source template xslt " , ex);
        } catch (UnsupportedEncodingException ex) {
            log.error("Error while loading source template xslt " , ex);
        } catch (JDOMException ex) {
            log.error("Error while loading source template xslt " , ex);
        }

        Document docXSLTtoMergeFrom = null;
         try {
            docXSLTtoMergeFrom = CommonXmlUtils.loadFile(
                    templateToMerge
                    );
        } catch (FileNotFoundException ex) {
            log.error("Error while loading target template xslt " , ex);
        } catch (UnsupportedEncodingException ex) {
            log.error("Error while loading target template xslt " , ex);
        } catch (JDOMException ex) {
            log.error("Error while loading target template xslt " , ex);
        }
        if (docXSLTtoMergeFrom != null ) {

            String strXpathToMergeInto =
                    "//xsl:template[@match='office:meta']/mcontainer[@name='meta']";
            String strXpathToMergeChildrenFrom =
                    "//xsl:template[@match='office:meta']";

            XPath xpathToMergeInto = XPath.newInstance(strXpathToMergeInto);
            XPath xpathToMergeChildenFrom = XPath.newInstance(strXpathToMergeChildrenFrom);

            boolean mergeRootElementsForDocXSLTtoMergeFrom = true;

            Element elemToMergeInto = (Element) xpathToMergeInto.selectSingleNode(
                    docXSLTtoMergeInto
                    );

            Element elemMergeChildrenFrom = (Element) xpathToMergeChildenFrom.selectSingleNode(
                    docXSLTtoMergeFrom
                    );

            List childSourceMergeChildren = elemMergeChildrenFrom.getChildren();
            Collection cl ;
            for (int i = 0 ; i < childSourceMergeChildren.size(); i++ ){
                elemToMergeInto.addContent(
                        (Content)(
                             (Content) childSourceMergeChildren.get(i)
                            ).clone()
                        );
            }


            if (true == mergeRootElementsForDocXSLTtoMergeFrom) {
                String strXpathSelf = "";
                if (strXpathToMergeChildrenFrom.startsWith("//")) {
                    strXpathSelf = strXpathToMergeChildrenFrom.substring(2);
                } else if (strXpathToMergeChildrenFrom.startsWith("/")) {
                    strXpathSelf = strXpathToMergeChildrenFrom.substring(1);
                } else {
                    strXpathSelf = strXpathToMergeChildrenFrom;
                }
                XPath xpathOtherRootChildren = XPath.newInstance(
                        "/xsl:stylesheet/*[not(self::" + strXpathSelf + ")]"
                        );
                List elemsOtherChildren = xpathOtherRootChildren.selectNodes(
                        docXSLTtoMergeFrom.getRootElement()
                        );

                for (int j=0 ; j < elemsOtherChildren.size(); j++) {
                    docXSLTtoMergeInto.getRootElement().addContent(
                            0,
                            (Content)
                              ((Content)elemsOtherChildren.get(j)).clone()
                      );
                }
            }

            XMLOutputter xout = new XMLOutputter();
            FileWriter fw = new FileWriter (new File(BaseSystemConfig.SYSTEM_CACHE + 
                    File.separator + "odf_meta_config_" + docType + ".xsl"));
            xout.output( docXSLTtoMergeInto, fw);
            fw.flush();
        }
    
    }

    public void startupGenerate(){
        DocTypesReader reader = DocTypesReader.getInstance();
        List<String> doctypeNames = reader.getDocTypeNames();
        for (String doctypeName : doctypeNames) {
            // process generators per type
            try {
                ConfigurationProvider cp = ConfigurationProvider.getInstance();
                cp.generateMergedConfiguration(doctypeName);
                Document mergedConfigs = cp.getMergedDocument();
                // generate configuraiton pipeline
                ConfigTemplateGenerator ctg = new ConfigTemplateGenerator();
                List<OAXSLTStep> inputSteps = new ArrayList<OAXSLTStep>(0);
                List<OAXSLTStep> outputSteps = new ArrayList<OAXSLTStep>(0);
                ctg.process(doctypeName, doctypeName, false, inputSteps, outputSteps);
                // generate type transformation
                File typeGeneratorTemplate = TransformerGenerator.getInstance().
                        typeGeneratorTemplate(
                            mergedConfigs,
                            doctypeName
                        );
                // generate tlc generator template
                File typeTlcGeneratorTemplate = TransformerGenerator.getInstance().
                        typeTlcGeneratorTemplate(
                            mergedConfigs,
                            doctypeName
                        );
                // generate identity metadata generate template
                File typeMetaIdentPubliGeneratorTemplate = TransformerGenerator.getInstance().
                        typeMetaIdentifierGenerator(
                            DocumentMetadataReader.getInstance().getDocument(doctypeName),
                            doctypeName
                        );
                this.createODFMetaMasterXSLT(
                        doctypeName,
                        typeMetaIdentPubliGeneratorTemplate,
                        typeTlcGeneratorTemplate
                        );

            } catch (Exception ex) {
                log.error("Exception generating config for doctype :  " + doctypeName, ex);
            } 

        }
    }

}

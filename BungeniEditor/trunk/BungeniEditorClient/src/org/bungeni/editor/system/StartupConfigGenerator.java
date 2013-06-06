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

package org.bungeni.editor.system;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;
import org.bungeni.editor.config.BaseSystemConfig;
import org.bungeni.editor.config.DocTypesReader;
import org.bungeni.editor.config.DocumentMetadataReader;
import org.bungeni.extutils.CommonEditorXmlUtils;
import org.bungeni.extutils.CommonFileFunctions;
import org.bungeni.translators.configurations.steps.OAXSLTStep;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

/**
 *This class generates the startup config generation
 * @author Ashok hariharan
 */
public class StartupConfigGenerator {

    private static final Logger log =
             Logger.getLogger(StartupConfigGenerator.class.getName());

    ConfigGeneratorError generatorError = null;

    public StartupConfigGenerator(){
         generatorError = new ConfigGeneratorError();
         init();
    }

    private void init(){
        //create cache folder if it doesnt exist
        File fcacheTrans = new File(BaseSystemConfig.SYSTEM_TRANSFORMER_CACHE);
        if (!fcacheTrans.exists()){
            fcacheTrans.mkdirs();
        }
        File fcacheGen = new File(BaseSystemConfig.SYSTEM_GENERATOR_CACHE);
        if (!fcacheGen.exists()){
            fcacheGen.mkdirs();
        }

    }

    public void startupGenerate(){
        DocTypesReader reader = DocTypesReader.getInstance();
        List<String> doctypeNames = reader.getDocTypeNames();
        for (String doctypeName : doctypeNames) {
            // process generators per type
            try {
                //A merged configuration Document is created
                ConfigurationProvider cp = ConfigurationProvider.getInstance();
                cp.generateMergedConfiguration(this.generatorError, doctypeName);
                //!+PROPRIETARY_METADATA(AH,03-12-2012)
                //write merged configuration for test purposes
                // !+PIPE_INPUT: debaterecord_cfg.xml
                cp.writeMergedConfig(
                        new File(
                            BaseSystemConfig.SYSTEM_GENERATOR_CACHE +
                            File.separator +
                            doctypeName+"_cfg.xml"
                        )
                );
                Document mergedConfigs = cp.getMergedDocument();
                // generate configuraiton pipeline
                // generate type transformation
                // !+PIPE_ITEM : TYPE_TRANSFORM_<TYPE>.XSL
                File typeGeneratorTemplate = TransformerGenerator.getInstance().
                        typeGeneratorTemplate(
                            this.generatorError,
                            mergedConfigs,
                            doctypeName
                        );
                // generate tlc generator template
                // !+PIPE_ITEM : TYPE_TLC_TRANSFORM_<TYPE>.XSL
                // This is used to create the master template
                File typeTlcGeneratorTemplate = TransformerGenerator.getInstance().
                        typeTlcGeneratorTemplate(
                            this.generatorError,
                            mergedConfigs,
                            doctypeName
                        );
                // generate identity metadata generate template
                // !+PIPE_ITEM : TYPE_META_IDENTI_PUBLI_<TYPE>.XSL
                // This is used to create the master template
                File typeMetaIdentPubliGeneratorTemplate = TransformerGenerator.getInstance().
                        typeMetaIdentifierGenerator(
                            this.generatorError,
                            DocumentMetadataReader.getInstance().getDocument(doctypeName),
                            doctypeName
                        );
                
                //This is the odf to meta master template
                // !+PIPE_ITEM : ODF_META_CONFIG_<TYPE>.XSL 
                // THis is the master template
                File odfMetaMasterXSLT = this.createODFMetaMasterXSLT(
                        doctypeName,
                        typeMetaIdentPubliGeneratorTemplate,
                        typeTlcGeneratorTemplate
                        );
                ConfigTemplateGenerator ctg = new ConfigTemplateGenerator();
                List<OAXSLTStep> inputSteps = new ArrayList<OAXSLTStep>(0);
                List<OAXSLTStep> outputSteps = new ArrayList<OAXSLTStep>(0);

                if (odfMetaMasterXSLT != null ) {

                    inputSteps.add(
                        new OAXSLTStep(
                            "odf_meta_lang_master",
                            odfMetaMasterXSLT.toURI().toURL().toExternalForm(),
                            99
                            )
                       );

                    outputSteps.add(
                        new OAXSLTStep(
                            "full_convert",
                            typeGeneratorTemplate.toURI().toURL().toExternalForm(),
                            98
                            )
                       );

                }
                // this will generate the config_XXTYPEXX.xml from config_tmpl.xml
                ctg.process(doctypeName, doctypeName, false, inputSteps, outputSteps);
                
                //!+PROPRIETARY(ah, 05-12-2012) 
                generateCustomXSLTfromTemplates();
                
                //Rewrite xsl:imports to full pathts
                rewriteXSLImportsToFullPathsInCustom();
                
            } catch (Exception ex) {
                log.error(ex);
                generatorError.add(
                    doctypeName,
                    "Error while generating configuration file ",
                    ex
                    );
            } 

        }
        this.generatorError.saveFile();
        checkConfigErrors();
    }
    
    
    private File createODFMetaMasterXSLT(String docType,
            File baseTemplate,
            File templateToMerge
            ) throws IOException, TemplateException, JDOMException, CloneNotSupportedException {
        /**
         * The first template is the root template,
         * the content of the following templates is merged into the root template
         * 
         */
        File outputFile = null;

        Document docXSLTtoMergeInto = null;
        try {
        //get the template XSLT
            docXSLTtoMergeInto  = CommonEditorXmlUtils.loadFile(
                baseTemplate
                );
        } catch (FileNotFoundException ex) {
            log.error(ex);
            generatorError.add(
                    docType,
                    "File not found Error while adding source Template file :" + baseTemplate,
                    ex
                    );
        } catch (UnsupportedEncodingException ex) {
            log.error(ex);
            generatorError.add(
                    docType,
                    "Encoding error while loading source Template file :" + baseTemplate,
                    ex
                    );
        } catch (JDOMException ex) {
            log.error(ex);
            generatorError.add(
                    docType,
                    "DOM loading error while loading source Template file :" + baseTemplate,
                    ex
                    );
        }

        Document docXSLTtoMergeFrom = null;
        try {
            docXSLTtoMergeFrom = CommonEditorXmlUtils.loadFile(
                    templateToMerge
                    );
        } catch (FileNotFoundException ex) {
            log.error(ex);
            generatorError.add(
                docType,
                "File not found error while loading XSLT template to merge file :" + templateToMerge,
                ex
                );
        } catch (UnsupportedEncodingException ex) {
            log.error(ex);
            generatorError.add(
                docType,
                "Encoding error while loading Template to merge file :" + templateToMerge,
                ex
                );
        } catch (JDOMException ex) {
            log.error(ex);
            generatorError.add(
                docType,
                "DOM while loading Template to merge file :" + templateToMerge,
                ex
                );
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
            outputFile = new File(BaseSystemConfig.SYSTEM_TRANSFORMER_CACHE +
                    File.separator + "odf_meta_config_" + docType + ".xsl");
            FileWriter fw = new FileWriter (outputFile);
            xout.output( docXSLTtoMergeInto, fw);
            fw.flush();
        }

        return outputFile;
    }

    /**
     * We need to do this because - the xsl:import references in the custom override templates
     * in custom/xsl use relative references to the systm template. 
     * We rewrite this to absolute paths to the system templates at runtime since the base path
     * is not known before runtime.
     */
    private void rewriteXSLImportsToFullPathsInCustom(){
        //rewriteXSLImportInFolderRelativeToSystemFolder(
        //         new File(BaseSystemConfig.CUSTOM_GENERATOR),
        //         new File(BaseSystemConfig.SYSTEM_GENERATOR)
        //        );
        rewriteXSLImportInFolderRelativeToSystemFolder(
                 new File(BaseSystemConfig.CUSTOM_TRANSFORMER_XSL),
                 new File(BaseSystemConfig.SYSTEM_TRANSFORMER_XSL)
                );
    }

    private void rewriteXSLImportInFolderRelativeToSystemFolder (File origFolder, File systemFolder) {
        FileFilter fileFilter = new WildcardFileFilter("*.xsl");
        File[] files = origFolder.listFiles(fileFilter);
        for (int i = 0; i < files.length; i++) {
           rewriteXSLImport(files[i], systemFolder.getAbsolutePath());
        }  
    }
    
    private void rewriteXSLImport(File fxsl, String absolutePath) {
        try {
            String fileName = fxsl.getName();
            File fsystemXsl = new File(absolutePath + File.separator + fileName);
            String sURLPath = fsystemXsl.toURI().toURL().toExternalForm();
            Document doc = CommonEditorXmlUtils.loadFile(fxsl);
            XPath xPath = XPath.newInstance("//xsl:import");
            Element xslImport = (Element) xPath.selectSingleNode(doc);
            xslImport.setAttribute("href", sURLPath );
            XMLOutputter xout  = new XMLOutputter();
            File ftransCacheCopy = new File(BaseSystemConfig.SYSTEM_TRANSFORMER_CACHE + File.separator + fileName);
            FileWriter fw = new FileWriter(ftransCacheCopy);
            xout.output(doc, fw);
            fw.flush();
        } catch (Exception ex) {
            generatorError.add(
                    "ALL",
                    "Error will rewriting import on custom template : " + fxsl.getName(),
                    ex);

            log.error("Error while loading XSL " + fxsl.getName());
        }
    }
    
    private void generateCustomXSLTfromTemplates(){
        String metalangmlxTmplName = "meta_language_add_mlx_ns.xsl.tmpl";
        try {
            BaseTransformTemplateGenerator ttgen = BaseTransformTemplateGenerator.getInstance();
            Template metalangmlxTmpl = ttgen.getTemplate("meta_language_add_mlx_ns.xsl.tmpl");
            Map<String,Object> objMap = new HashMap<String,Object>();
            objMap.put("namespace_uris", getProprietaryNSList());
            FileWriter fw = new FileWriter(BaseSystemConfig.SYSTEM_TRANSFORMER_CACHE +
                    File.separator + 
                    "meta_language_add_mlx_ns.xsl"
                    );
            metalangmlxTmpl.process(objMap, fw);
            fw.flush();
            fw.close();
        } catch (TemplateException ex) {
            generatorError.add(
                    "ALL",
                    "Error will processing template : " + metalangmlxTmplName,
                    ex
                    );
            log.error(
                    "Error will processing template : " + metalangmlxTmplName
                    );
        } catch (IOException ex) {
            generatorError.add(
                    "ALL",
                    "Error will getting template : " + metalangmlxTmplName,
                    ex);
            log.error(
                    "Error while getting template : " + metalangmlxTmplName
                    );
        }
    }
     
    /**
    <namespace prefix="an" type="main" desc="Akoma Ntoso" uri="http://www.akomantoso.org/2.0" />
    <namespace prefix="bg" type="proprietary" desc="Proprietary" uri="http://www.proprietary.org" />   
     * @return 
     */
    /**
    private String getProprietaryNSList(){
        Element outputsBlock = DocTypesReader.getInstance().getOutputsBlock();
        List<Element> nsElements = outputsBlock.getChildren("namespace");
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < nsElements.size(); i++) {
            Element nsElement = nsElements.get(i);
            String uri = nsElement.getAttributeValue("uri");
            // (namespace-uri() ne 'http://editor.bungeni.org/1.0/anx') 
            sb.append(" and \n");
            sb.append("(namespace-uri() ne '").append(uri).append("')");
        }
        return sb.toString();
    }**/
    
      private List<String> getProprietaryNSList(){
        List<String> nsList = new ArrayList<String>(0);
        Element outputsBlock = DocTypesReader.getInstance().getOutputsBlock();
        List<Element> nsElements = outputsBlock.getChildren("namespace");
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < nsElements.size(); i++) {
            Element nsElement = nsElements.get(i);
            String uri = nsElement.getAttributeValue("uri");
            // (namespace-uri() ne 'http://editor.bungeni.org/1.0/anx') 
            nsList.add(uri);
        }
       return nsList;
    }

    private boolean checkConfigErrors() {
      URL[] urls = CommonFileFunctions.findInFiles(
              BaseSystemConfig.SYSTEM_BASE,
              "*.xsl",
              "xsl:error"
              );

      if (urls.length > 0 ) {
          return true;
      } else {
          return false;
      }

    }


}

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
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import org.bungeni.editor.config.DocTypesReader;
import org.bungeni.editor.config.DocumentMetadataReader;
import org.bungeni.extutils.CommonXmlUtils;
import org.bungeni.translators.configurations.steps.OAXSLTStep;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;

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

    private void createODFMetaMasterXSLT(File baseTemplate, File templateToMerge) throws IOException, TemplateException {
        /**
         * The first template is the root template,
         * the content of the following templates is merged into the root template
         * 
         */
        //get the template XSLT
        BaseTransformTemplateGenerator inst = BaseTransformTemplateGenerator.getInstance();
        Template tmplXSLT = inst.getTemplate("type_meta_ident_publi_debaterecord.xsl");

        HashMap<String, Object> map = new HashMap<String,Object>();
        Document docXSLT = null;
                try {
            docXSLT = CommonXmlUtils.loadFile(
                    BaseSystemConfig.SYSTEM_TEMPLATES +
                    File.separator +
                    "type_tlc_transform_debaterecord.xsl"
                    );
        } catch (FileNotFoundException ex) {
            log.error("Error while loading template xslt " , ex);
        } catch (UnsupportedEncodingException ex) {
            log.error("Error while loading template xslt " , ex);
        } catch (JDOMException ex) {
            log.error("Error while loading template xslt " , ex);
        }
        if (docXSLT != null ) {
            
            List childElements = docXSLT.getRootElement().
                    getChildren();
            XMLOutputter xout = new XMLOutputter();
            StringWriter swout = new StringWriter();
            xout.output(childElements, swout);
            map.put("MCONTAINER_REFERENCES", swout.toString());
            FileWriter fw = new FileWriter (new File(BaseSystemConfig.SYSTEM_CACHE + 
                    File.separator + "odf_meta_config_debaterecord.xsl"));
            tmplXSLT.process(map, fw);
            fw.flush();
        }
    
    }

    public void startupGenerate(){
        DocTypesReader reader = DocTypesReader.getInstance();
        List<String> doctypeNames = reader.getDocTypeNames();
        for (String doctypeName : doctypeNames) {
            try {
                ConfigurationProvider cp = ConfigurationProvider.getInstance();
                cp.generateMergedConfiguration(doctypeName);
                Document mergedConfigs = cp.getMergedDocument();
                // generate configuraiton
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
                File typeTlcGeneratorTemplate = TransformerGenerator.getInstance().
                        typeTlcGeneratorTemplate(
                            mergedConfigs,
                            doctypeName
                        );
                File typeMetaIdentPubliGeneratorTemplate = TransformerGenerator.getInstance().
                        typeMetaIdentifierGenerator(
                            DocumentMetadataReader.getInstance().getDocument(doctypeName),
                            doctypeName
                        );
                this.createODFMetaMasterXSLT(typeMetaIdentPubliGeneratorTemplate, typeTlcGeneratorTemplate);

            } catch (Exception ex) {
                log.error("Exception generating config for doctype :  " + doctypeName, ex);
            } 

        }
    }

}

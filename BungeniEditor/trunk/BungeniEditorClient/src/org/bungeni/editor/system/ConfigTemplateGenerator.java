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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.bungeni.translators.configurations.steps.OAXSLTStep;

/**
 * This is the Main configuration template pipeline generator
 * It uses freemarker to generate the pipeline file for a doctype
 * @author Ashok
 */
public class ConfigTemplateGenerator {


    public ConfigTemplateGenerator(){
    }

    /**
     * This is the main pipeline template generator
     * @param configName
     * @param docType
     * @param cachePipeline
     * @param prefixPath
     * @param customInputSteps
     * @param customOutputSteps
     * @throws IOException
     * @throws TemplateException
     */
    public void process(String configName, 
            String docType,
            boolean cachePipeline,
            List<OAXSLTStep> customInputSteps,
            List<OAXSLTStep> customOutputSteps
            ) throws IOException, TemplateException {
        BaseTransformTemplateGenerator instance = BaseTransformTemplateGenerator.getInstance();
        Template tmpl = instance.getTemplate("config_tmpl.xml");
        Map<String,Object> objectMap = new HashMap<String,Object>();
        objectMap.put("configname", configName);
        objectMap.put("doctype", docType);
        objectMap.put("cache_pipeline", Boolean.valueOf(cachePipeline));
        objectMap.put("custom_trans_path", BaseSystemConfig.getHrefCache());
        objectMap.put("sys_trans_path", BaseSystemConfig.getHrefTransformer());
        objectMap.put("input_xml_source", "ODF");
        objectMap.put("input_steps", customInputSteps);
        objectMap.put("output_steps", customOutputSteps);

        File fcache = new File(BaseSystemConfig.SYSTEM_CACHE);
        if (!fcache.exists()){
            fcache.mkdirs();
        }
        String configFileName = BaseSystemConfig.SYSTEM_CACHE +
                File.separator +
                "config_" +
                docType +
                ".xml";
        FileWriter fwoutput = new FileWriter(configFileName);
        tmpl.process(objectMap, fwoutput);
        fwoutput.flush();
        //remove merge tags
        removeMergeTags(configFileName);
    }

    public static void removeMergeTags(String configFileName) throws IOException{
        String fileContent = IOUtils.toString(new FileInputStream(configFileName), "UTF-8");
        String replOpen = fileContent.replace("<!--{UNCOMMENT_MERGE_OPEN}", "");
        String replClose = replOpen.replace("{UNCOMMENT_MERGE_CLOSE}-->", "");
        IOUtils.write(replClose, new FileOutputStream(configFileName), "UTF-8");
    }

}

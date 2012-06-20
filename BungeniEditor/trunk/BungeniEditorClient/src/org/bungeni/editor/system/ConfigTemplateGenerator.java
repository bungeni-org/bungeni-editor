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
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ashok
 */
public class ConfigTemplateGenerator {


    public ConfigTemplateGenerator(){
    }

    public void process(String configName, 
            String docType,
            boolean cachePipeline,
            String prefixPath,
            String customInputSteps,
            String customOutputSteps
            ) throws IOException, TemplateException {
        BaseTransformTemplateGenerator instance = BaseTransformTemplateGenerator.getInstance();
        Template tmpl = instance.getTemplate("config_tmpl.xml");

        Map objectMap = new HashMap();
        objectMap.put("configname", configName);
        objectMap.put("cache_pipeline", Boolean.valueOf(cachePipeline));
        objectMap.put("trans_path", BaseTransformTemplateGenerator.CONFIG_TEMPLATES_OUTPUT);
        objectMap.put("input_xml_source", "ODF");
        FileWriter fwoutput = new FileWriter(BaseTransformTemplateGenerator.CONFIG_TEMPLATES_OUTPUT + 
                File.separator +
                "config_" +
                docType +
                ".xml");
        tmpl.process(objectMap, fwoutput);
        fwoutput.flush();
    }

    public static void main(String[] args) throws IOException, TemplateException{
        ConfigTemplateGenerator cfg = new ConfigTemplateGenerator();
        cfg.process("debatecommon", "debate", true, "/home/undesa", "custom", "custom2");

    }
 

}

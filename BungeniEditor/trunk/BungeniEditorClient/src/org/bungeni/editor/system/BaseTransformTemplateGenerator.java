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

import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Loads XSLT generator templates out of Freemarker templates
 * 
 * @author Ashok Hariharan
 */
public  class BaseTransformTemplateGenerator {
    private static Logger log = Logger.getLogger(BaseTransformTemplateGenerator.class.getName());


    private static BaseTransformTemplateGenerator instance = null;
    protected Configuration templateConfig = null;

    private BaseTransformTemplateGenerator() throws IOException {
        templateConfig = new Configuration();
        File fsystemGen = new File(BaseSystemConfig.SYSTEM_TEMPLATES);
        templateConfig.setDirectoryForTemplateLoading(
                    fsystemGen
                );
        log.info("Setting freemarker template folder to :" + fsystemGen.getAbsolutePath());
    }


    public static BaseTransformTemplateGenerator getInstance(){
        if (null == instance) {
            try {
                instance = new BaseTransformTemplateGenerator();
            } catch (IOException ex) {
                log.error("Unable to get transform template generator", ex);
            }
       }
        return instance;
    }

    public Template getTemplate(String templateName) throws IOException {
        return this.templateConfig.getTemplate(templateName);
    }

}

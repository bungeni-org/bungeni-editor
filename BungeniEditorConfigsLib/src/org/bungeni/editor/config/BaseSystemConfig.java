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
import java.net.MalformedURLException;
import org.bungeni.extutils.CommonFileFunctions;

/**
 * All system related configuration is stored here --
 *
 * NOT Intended for human editing
 * 
 * @author Ashok Hariharan
 */
public class BaseSystemConfig {
   
    /**
     * configs/system
     */
    public static final String SYSTEM_BASE =
            BaseConfigReader.CONFIGS_FOLDER + File.separator + "system";

    /**
     * This has all the system XSLT generator templates
     * Generator templates generate other XSLT templates
     * configs/system/generators
     */
    public final static String SYSTEM_GENERATOR =
            BaseSystemConfig.SYSTEM_BASE + File.separator +
            "generators";

    /**
     * The Templates generated from system XSLT generator templates
     * are cached in this folder
     * configs/system/cache
     */
    public final static String SYSTEM_CACHE =
            BaseSystemConfig.SYSTEM_BASE + File.separator +
            "cache";

    /**
     * 
     */
    public final static String SYSTEM_TEMPLATES =
            BaseSystemConfig.SYSTEM_BASE + File.separator +
            "templates";

    public final static String SYSTEM_TRANSFORMER =
            BaseSystemConfig.SYSTEM_BASE + File.separator +
            "transformer";

    public final static String SYSTEM_TRANSFORMER_ERROR =
            BaseSystemConfig.SYSTEM_BASE + File.separator +
            "errors";


    public static String getHrefTemplates() throws MalformedURLException{
        return CommonFileFunctions.getURLPath(SYSTEM_TEMPLATES);
    }

    public static String getHrefCache() throws MalformedURLException{
        return CommonFileFunctions.getURLPath(SYSTEM_CACHE);
    }

    public static String getHrefTransformer() throws MalformedURLException{
        return CommonFileFunctions.getURLPath(SYSTEM_TRANSFORMER);
    }

}

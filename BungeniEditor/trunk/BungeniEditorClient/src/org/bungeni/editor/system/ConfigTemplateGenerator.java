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

import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

/**
 * Generates the pipeline configuration template
 * Uses the antlr StringTemplate library for the template generation
 * @author Ashok Hariharan
 */
public class ConfigTemplateGenerator {

    private ConfigTemplateGenerator(){
        templateGroupFile = new STGroupFile("config_tmpl.xml.st");
    }

    private static ConfigTemplateGenerator instance = null;

    private STGroupFile templateGroupFile = null;

    public static ConfigTemplateGenerator getInstance(){
        if (null == instance) {
            instance = new ConfigTemplateGenerator();
        }
        return instance;
    }

    

}

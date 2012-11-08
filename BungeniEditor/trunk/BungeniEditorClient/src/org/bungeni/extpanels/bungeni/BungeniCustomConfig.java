/*
 * Copyright (C) 2012 Africa i-Parliaments
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.bungeni.extpanels.bungeni;

import org.bungeni.editor.config.PluggableConfigReader.PluggableConfig;
import org.jdom.Element;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniCustomConfig {
    public final    String userName ;
    public final    String userPassword ;
    public final    String serverHost   ;
    public final    String serverPort     ;
    public final    String serverloginUri ;
    
    public BungeniCustomConfig(PluggableConfig config){
        Element bungeniConfig =  config.customConfigElement.getChild("bungeni");
        if (bungeniConfig != null) {
            this.userName  = bungeniConfig.getAttributeValue("user");
            this.userPassword = bungeniConfig.getAttributeValue("password");
            this.serverHost    = bungeniConfig.getAttributeValue("host");
            this.serverPort    = bungeniConfig.getAttributeValue("port");
            this.serverloginUri = bungeniConfig.getAttributeValue("loginuri");
         } else {
            this.userName = this.userPassword = this.serverHost = this.serverPort = this.serverloginUri = null;
        }
    }
    
}

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

import java.util.HashMap;
import javax.swing.JFrame;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bungeni.editor.config.PluggableConfigReader;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniDocumentExporter {
    
    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(BungeniDocumentExporter.class.getName());
    BungeniAppConnector appConnector = null;
    DefaultHttpClient   client       = null;
    public BungeniDocumentExporter(){
        
    }
    
 
    public String sendDocument(String sPostURL, final JFrame parentFrame, final PluggableConfigReader.PluggableConfig customConfig, HashMap inputParams) {
      
        
        return "";  //send(parentFrame, customConfig, sDocURL);
    }
}

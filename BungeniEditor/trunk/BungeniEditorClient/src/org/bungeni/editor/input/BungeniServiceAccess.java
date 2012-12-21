/*
 * Copyright (C) 2012 Africa i-Parliaments Action Plan
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
package org.bungeni.editor.input;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bungeni.extpanels.bungeni.BungeniAppConnector;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniServiceAccess {
    
    private static BungeniServiceAccess instance = null;
    
    
    BungeniAppConnector appConnector = null;
    DefaultHttpClient client = null;
    
    public static BungeniServiceAccess getInstance(){
        if (null == instance) {
            instance = new BungeniServiceAccess();
        }
        return instance;
    }
   
    public DefaultHttpClient login(String appServer, String appPort, String appBase, String user, String password) throws UnsupportedEncodingException, IOException {
        if (null == appConnector) {
            appConnector = new BungeniAppConnector(appServer, appPort, appBase, user, password);
            client =  appConnector.login();
            return client;
        }
        return null;
    }
   
    public List searchDocuments(String searchServer, String docType, String status) {
        return null;
    }
    
    public boolean authenticateDocument(String urlDocument) {
        if (client != null ) {
           
        }
        return true;
    }
    
    public List getDocumentAttachments(String urlDocument) {
        return null;
    }
    
   
    
}

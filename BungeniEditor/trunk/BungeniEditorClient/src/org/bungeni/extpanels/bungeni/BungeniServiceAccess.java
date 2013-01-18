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
package org.bungeni.extpanels.bungeni;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bungeni.extpanels.bungeni.BungeniAppConnector;
import org.bungeni.extpanels.bungeni.BungeniAppConnector.WebResponse;
import org.bungeni.extpanels.bungeni.BungeniDocument;
import org.bungeni.extpanels.bungeni.BungeniListDocuments;
import org.bungeni.extpanels.bungeni.BungeniListDocuments.BungeniListDocument;
import org.bungeni.extutils.MessageBox;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *  This is the class that implements all interaction with Bungeni and abstracts that
 * interaction to the rest of the Bungeni Editor.
 * @author Ashok Hariharan
 */
public class BungeniServiceAccess {
    
    private static BungeniServiceAccess instance = null;
    
    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(BungeniServiceAccess.class.getName());
    

    
    BungeniAppConnector appConnector = null;
    DefaultHttpClient client = null;
    
    public static BungeniServiceAccess getInstance(){
        if (null == instance) {
            instance = new BungeniServiceAccess();
        }
        return instance;
    }
   
    public BungeniAppConnector getAppConnector(){
        return this.appConnector;
    }
    
    public DefaultHttpClient login(String appServer, String appPort, String appBase, String user, String password) throws UnsupportedEncodingException, IOException {
        if (null == appConnector) {
            this.appConnector = new BungeniAppConnector(appServer, appPort, appBase, user, password);
            this.client =  appConnector.login();
            return this.client;
        }
        return null;
    }
    
     
   
    public List<BungeniListDocument> availableDocumentsForEditing(String sSearchBungeniURL) {
           List<BungeniListDocument> bungeniDocs = new ArrayList<BungeniListDocument>(0);
           WebResponse wr = appConnector.getUrl(sSearchBungeniURL, true);
           if (wr != null) {
               if (wr.getStatusCode() == 200 ) {
                   String sResponseBody = wr.getResponseBody();
                   BungeniListDocuments bld = new BungeniListDocuments(sSearchBungeniURL, sResponseBody);
                   bungeniDocs = bld.getListDocuments();
               }
           }
           return bungeniDocs;
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

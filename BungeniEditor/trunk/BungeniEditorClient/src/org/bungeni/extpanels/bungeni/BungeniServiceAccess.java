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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.bungeni.editor.config.BungeniEditorPropertiesHelper;
import org.bungeni.extpanels.bungeni.BungeniAppConnector.WebResponse;
import org.bungeni.extpanels.bungeni.BungeniListDocuments.BungeniListDocument;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.document.properties.BungeniOdfPropertiesHelper;
import org.bungeni.odfdom.section.BungeniOdfSectionHelper;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.element.text.TextSectionElement;
import org.w3c.dom.NodeList;

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
    
    
    public WebResponse doTransition(Transition transition) {
        WebResponse wr = appConnector.getUrl(transition.url, false);
        if (wr != null) {
            if (wr.getStatusCode() == 200 ) {
                // transition happened
                return wr;
            }
        }
        return wr;
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
    
   /**
    * Service api called from BungeniAttLoadingPanel
    * @param fodf
    * @param aDocument
    * @return
    * @throws Exception 
    */ 
   public File checkOdfDocument(File fodf, BungeniDocument aDocument) throws Exception {
            OdfDocument odf = OdfDocument.loadDocument(fodf);
            BungeniOdfDocumentHelper odfhelper = new BungeniOdfDocumentHelper(odf);
            BungeniOdfPropertiesHelper propshelper = odfhelper.getPropertiesHelper();
            //check if the document has been edited in bungeni editor .. look for some properties
            HashMap<String,String> docPropsMap = propshelper.getUserDefinedPropertyValues();
            //check for root section
            boolean rootSectionExists = false;
            BungeniOdfSectionHelper sechelper = odfhelper.getSectionHelper();
            NodeList sections = sechelper.getDocumentSections();
            if (sections.getLength() > 0 ) {
                TextSectionElement sectionElement = (TextSectionElement)sections.item(0);
                //check for body section Type
                String sBody = sechelper.getSectionMetadataValue(sectionElement, "BungeniSectionType");
                if (sBody.equals("body")) {
                    rootSectionExists = true;
                }
            }
             
            if (docPropsMap.containsKey("BungeniDocType") && rootSectionExists ) {
                // this is a bungeni document ... load for editing
            } else {
                //first prepare the document
                BungeniDocument.Attachment att = aDocument.getSelectedAttachment();
                propshelper.setUserDefinedPropertyValue("BungeniDocType", BungeniEditorPropertiesHelper.getCurrentDocType());
                propshelper.setUserDefinedPropertyValue("DocSource", "BungeniPortal");
                propshelper.setUserDefinedPropertyValue("DocInit", "False");
                propshelper.setUserDefinedPropertyValue("PortalSourceDoc", aDocument.getStatus());
                propshelper.setUserDefinedPropertyValue("PortalSourceTitle", aDocument.getTitle());
                propshelper.setUserDefinedPropertyValue("PortalSourceURL", aDocument.getURL());
                propshelper.setUserDefinedPropertyValue("PortalAttSource", att.url);
                propshelper.setUserDefinedPropertyValue("PortalAttFileName", att.fileName);
                propshelper.setUserDefinedPropertyValue("PortalAttTitle", att.title);
                propshelper.setUserDefinedPropertyValue("PortalAttType", att.attType);
                propshelper.setUserDefinedPropertyValue("PortalAttMimeType", att.mimeType);
                propshelper.setUserDefinedPropertyValue("PortalAttLang", att.language);
                propshelper.setUserDefinedPropertyValue("PortalAttStatus", att.status);
                propshelper.setUserDefinedPropertyValue("PortalAttStatusDate", att.statusDate);
                propshelper.setUserDefinedPropertyValue("PortalAttDownURL", att.downloadUrl);
                propshelper.setUserDefinedPropertyValue("PortalAttDesc", att.description);
                propshelper.setUserDefinedPropertyValue("PortalAttTransCount", Integer.toString(att.transitions.size()));

                int i=0;
                for (Transition transition :att.transitions){
                    i++;
                    //Not more than 99 transitions !!!
                    String snum = String.format("%02d", i);
                    propshelper.setUserDefinedPropertyValue("PortalAttTransName"+snum, transition.title);
                    propshelper.setUserDefinedPropertyValue("PortalAttTransURL"+snum, transition.url);
                }
                                
                odfhelper.saveDocument();
                // create the root section after opening and set initial metadata properties
            }
            return fodf;
        }

    
   
    
    
}

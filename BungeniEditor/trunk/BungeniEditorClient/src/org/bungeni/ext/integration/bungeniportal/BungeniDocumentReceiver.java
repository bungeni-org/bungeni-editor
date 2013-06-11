/*
 *  Copyright (C) 2012 UN/DESA Africa i-Parliaments Action Plan
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 3
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



package org.bungeni.ext.integration.bungeniportal;

//~--- non-JDK imports --------------------------------------------------------

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFrame;
import org.bungeni.editor.config.PluggableConfigReader.PluggableConfig;
import org.bungeni.editor.input.IInputDocumentReceiver;
import org.bungeni.ext.integration.bungeniportal.OAuthProperties.OAuthState;
import org.bungeni.ext.integration.bungeniportal.docimpl.BungeniDoc;
import org.bungeni.ext.integration.bungeniportal.docimpl.BungeniDocument;
import org.bungeni.ext.integration.bungeniportal.docimpl.BungeniListDocuments.BungeniListDocument;
import org.bungeni.ext.integration.bungeniportal.panels.BungeniAttLoadingPanel;
import org.bungeni.ext.integration.bungeniportal.panels.BungeniDocumentAttListPanel;
import org.bungeni.ext.integration.bungeniportal.panels.BungeniLoginPanel;
import org.bungeni.ext.integration.bungeniportal.panels.BungeniSelectDocumentPanel;
import org.bungeni.extutils.FrameLauncher;
import org.bungeni.utils.BungeniDialog;
import org.jdom.Element;

/**
 * Reciever class for importing files from Bungeni
 * @author Ashok
 */
public class BungeniDocumentReceiver implements IInputDocumentReceiver {
    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(BungeniDocumentReceiver.class.getName());
    
    public String receiveDocument(final JFrame parentFrame, final PluggableConfig customConfig, HashMap inputParams) {
        //String sDocURL = (String) JOptionPane.showInputDialog(parentFrame, "Enter the URL of the document to Import",
        //                     "Import document from Bungeni", JOptionPane.QUESTION_MESSAGE);
        if (redirectLogin(parentFrame, customConfig)) {
            List<BungeniListDocument> listDocs = listDocuments(parentFrame, customConfig);
            if(listDocs.size() > 0 ) {
                //show list documents
                BungeniListDocument selectedDocument =  selectDocument(parentFrame, listDocs);
                if (selectedDocument != null){
                    BungeniDocument bungeniDoc = selectAttachment(parentFrame, selectedDocument, customConfig);
                    if (null != bungeniDoc) {
                        File fodf = loadAttachment(parentFrame, bungeniDoc, customConfig);
                        if (null != fodf ) {
                             return fodf.getAbsolutePath();   
                        }
                    }
                }
            }
        }   
        return null;
    }

    
    private boolean redirectLogin(JFrame parentFrame, PluggableConfig customConfig ) {
        // get the custom login configuration info
        LoginInfo linfo = loginInfo(customConfig);
        // check the oauth cache
        OAuthState oauthState = OAuthProperties.getInstance().queryCache();
        // if invalid attempt to login
        if (OAuthState.INVALID == oauthState) {
            return login(parentFrame, customConfig);
        } else if (OAuthState.EXPIRED == oauthState) {
            // perhaps run this in a worker thread 
            return loginViaOauthToken(linfo);
        } else if (OAuthState.VALID == oauthState) {
            // just initialize the app connector and return a thread safe client
            return loginBlind(linfo);
        } else 
            return false;
    }
    
    private boolean loginBlind(LoginInfo linfo) {
        Object client = BungeniServiceAccess.getInstance().loginBlind(linfo);
        if (null != client) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean loginViaOauthToken(LoginInfo linfo){
        Object client = BungeniServiceAccess.getInstance().loginViaOauthToken(linfo);
        if (client != null) {
            return true;
        } else {
            return false;
        }
    }
    
    private boolean login(JFrame parentFrame, PluggableConfig customConfig) {
        LoginInfo linfo = loginInfo(customConfig);
        BungeniDialog frm = new BungeniDialog(parentFrame, "Login", true);
        frm.initFrame();
        BungeniLoginPanel login = new BungeniLoginPanel(frm, linfo);
        frm.getContentPane().add(login);
        frm.pack();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        return login.loginSuccessful();
    }

    
    private LoginInfo loginInfo(PluggableConfig customConfig) {
        Element loginElem = customConfig.customConfigElement.getChild("login");
        Element oauthElem = customConfig.customConfigElement.getChild("oauth");
        LoginInfo li;
        li = new LoginInfo(
                loginElem.getAttributeValue("server"),
                loginElem.getAttributeValue("port"),
                loginElem.getAttributeValue("baseurl"),
                oauthElem.getAttributeValue("app-id"),
                oauthElem.getAttributeValue("secret"),
                oauthElem.getAttributeValue("authorize-uri"),
                oauthElem.getAttributeValue("authorize-form-uri"),
                oauthElem.getAttributeValue("access-token-uri"),
                oauthElem.getAttributeValue("refresh-token-uri")
              );
        return li;
    }
    
    
    
    private BungeniListDocument selectDocument(final JFrame parentFrame, List<BungeniListDocument> listDocuments) {
        BungeniDialog               dlgdocs = new BungeniDialog(parentFrame, "Select a Bungeni Document", true);
        BungeniSelectDocumentPanel       panelSelectDocument = new BungeniSelectDocumentPanel(dlgdocs, listDocuments);
        dlgdocs.getContentPane().add(panelSelectDocument);
        dlgdocs.pack();
        FrameLauncher.CenterFrame(dlgdocs);
        dlgdocs.setVisible(true);
        return panelSelectDocument.getSelectedListDocument();
    }
    
    private BungeniDocument selectAttachment(final JFrame parentFrame, BungeniListDocument selectedDocument, final PluggableConfig customConfig) {
         String docUrlBase = this.documentURLBase(customConfig);
         BungeniDialog               dlgdoc = new BungeniDialog(parentFrame, selectedDocument.title, true);
         BungeniDocumentAttListPanel  panelShowDocument = new BungeniDocumentAttListPanel(
                    dlgdoc, 
                    selectedDocument,
                    docUrlBase + "/" + selectedDocument.documentId()
                    );
         panelShowDocument.init();
         //!+CONTINUE_HERE
         dlgdoc.view(panelShowDocument);
         BungeniDoc aDoc = panelShowDocument.getDocument();
         /** FIX_API
         if (aDoc != null ){
             BungeniAttachment attDoc = aDoc.getSelectedAttachment();
             if (attDoc != null) {
                 return aDoc;
             }
         }**/
         return null;
    }
    
    public File loadAttachment(JFrame pFrame, BungeniDocument bungeniDocument, final PluggableConfig customConfig){
        //first get the properties of the document 
         BungeniDialog  dlgatt = new BungeniDialog(pFrame, bungeniDocument.getSelectedAttachment().title, true);
         BungeniAttLoadingPanel  panelShowAtt = new BungeniAttLoadingPanel(
                 dlgatt,
                 bungeniDocument
                 );
         panelShowAtt.init();
         dlgatt.view(panelShowAtt);
         File fOdf = panelShowAtt.getOdfDocument();
        return fOdf;
    }
    
    private String searchURL(final PluggableConfig customConfig){
        Element searchElem = customConfig.customConfigElement.getChild("search");
        String sSearchBase = searchElem.getAttributeValue("base");
        String sStates = searchElem.getChild("states").getTextTrim();
        String sTypes = searchElem.getChild("type").getTextTrim();
        Object[] arguments = { 
                  sStates,
                  sTypes
                };
        String sSearchUrl = MessageFormat.format(
                sSearchBase, 
                arguments
                );
        return sSearchUrl;
    }

    private String documentURLBase(final PluggableConfig customConfig){
        Element docElem = customConfig.customConfigElement.getChild("document");
        String sDocBase = docElem.getAttributeValue("base");
        return sDocBase;
    }

    
    private List<BungeniListDocument> listDocuments(JFrame parentFrame, final PluggableConfig customConfig) {
        String sSearchBungeniURL = searchURL(customConfig);
        // access the input URL
        List<BungeniListDocument> listDocuments = null;
        try {
            listDocuments = 
                BungeniServiceAccess.getInstance().availableDocumentsForEditing(sSearchBungeniURL);
            
        } catch (Exception ex) {
            log.error("Error while accessin url : " + sSearchBungeniURL , ex);
        }
        return listDocuments;
    }


}


//~ Formatted by Jindent --- http://www.jindent.com

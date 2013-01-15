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



package org.bungeni.editor.input;

//~--- non-JDK imports --------------------------------------------------------

import java.awt.Cursor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bungeni.editor.config.PluggableConfigReader.PluggableConfig;
import org.bungeni.editor.panels.impl.IMainContainerPanel;
import org.bungeni.editor.panels.impl.ITabbedPanel;
import org.bungeni.extpanels.bungeni.BungeniAppConnector;
import org.bungeni.extpanels.bungeni.BungeniAttachment;
import org.bungeni.extpanels.bungeni.BungeniDocument;
import org.bungeni.extpanels.bungeni.BungeniDocument.Attachment;
import org.bungeni.extpanels.bungeni.BungeniDocumentAttListPanel;
import org.bungeni.extpanels.bungeni.BungeniListDocuments;
import org.bungeni.extpanels.bungeni.BungeniListDocuments.BungeniListDocument;
import org.bungeni.extpanels.bungeni.BungeniSelectDocument;
import org.bungeni.extutils.FrameLauncher;
import org.bungeni.extutils.MessageBox;
import org.bungeni.extutils.TempFileManager;
import org.bungeni.utils.BungeniDialog;
import org.bungeni.utils.CommonEditorInterfaceFunctions;
import org.jdom.Element;
import org.jsoup.Jsoup;

/**
 *
 * @author Ashok
 */
public class BungeniDocumentReceiver implements IInputDocumentReceiver {
    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(BungeniDocumentReceiver.class.getName());
    
    public String receiveDocument(final JFrame parentFrame, final PluggableConfig customConfig, HashMap inputParams) {
        //String sDocURL = (String) JOptionPane.showInputDialog(parentFrame, "Enter the URL of the document to Import",
        //                     "Import document from Bungeni", JOptionPane.QUESTION_MESSAGE);
        if (login(parentFrame)) {
            List<BungeniListDocument> listDocs = listDocuments(parentFrame, customConfig);
            if(listDocs.size() > 0 ) {
                //show list documents
                BungeniListDocument selectedDocument =  selectDocument(parentFrame, listDocs);
                if (selectedDocument != null){
                    selectAttachment(parentFrame, selectedDocument, customConfig);
                    
                }
            }
        }   
        
        return null;
        //return receive(parentFrame, customConfig, sDocURL);
    }
    
    
    private BungeniListDocument selectDocument(final JFrame parentFrame, List<BungeniListDocument> listDocuments) {
        BungeniDialog               dlgdocs = new BungeniDialog(parentFrame, "Select a Bungeni Document", true);
        BungeniSelectDocument       panelSelectDocument = new BungeniSelectDocument(dlgdocs, listDocuments);
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
                    docUrlBase + selectedDocument.idBase
                    );
         //!+CONTINUE_HERE
         dlgdoc.view(panelShowDocument);
         
         return null;
    }
    
    private String searchURL(final PluggableConfig customConfig){
        Element searchElem = customConfig.customConfigElement.getChild("search");
        String sSearchBase = searchElem.getAttributeValue("base");
        return sSearchBase;
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
        //  BungeniDocument          jdoc   = new BungeniDocument(sDocURL, doc);
          //  Attachment               attDoc = promptForAttachment(parentFrame, jdoc);

       // if (attDoc != null) {
       //     importAttachment(jdoc, attDoc);
       // }
    }

    private boolean login(JFrame parentFrame) {
        BungeniDialog frm = new BungeniDialog(parentFrame, "Login", true);
        frm.initFrame();
        BungeniLogin login = new BungeniLogin(frm);
        frm.getContentPane().add(login);
        frm.pack();
        frm.setLocationRelativeTo(null);
        frm.setVisible(true);
        return login.loginSuccessful();
    }

   /**
    private String receive(final JFrame parentFrame, PluggableConfig config, final String sDocURL) {

        // If a string was returned, say so.
        if ((sDocURL != null) && (sDocURL.length() > 0)) {

            // open document here
            // set the wait cursor
            parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            if (config.customConfigElement != null) {
                Element bungeniConfig = config.customConfigElement.getChild("bungeni");

                if (bungeniConfig != null) {
                    String username  = bungeniConfig.getAttributeValue("user");
                    String upassword = bungeniConfig.getAttributeValue("password");
                    String uhost     = bungeniConfig.getAttributeValue("host");
                    String uport     = bungeniConfig.getAttributeValue("port");
                    String uloginUri = bungeniConfig.getAttributeValue("loginuri");

                    if (null == appConnector) {
                        appConnector = new BungeniAppConnector(uhost, uport, uloginUri, username, upassword);
                }
                }

                // call the swingworker thread for the button event
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {

                        // login
                        if (client == null) {
                            try {
                                client = appConnector.login();
                            } catch(Exception ex) {
                                
            }
        }

        return "";
    }
 
    private Attachment promptForAttachment(JFrame parentFrame, BungeniDocument jdoc) {
        BungeniDialog               dlgatts = new BungeniDialog(parentFrame, "Import an Attachment", true);
        BungeniDocumentAttListPanel docAtts = new BungeniDocumentAttListPanel(client, dlgatts, jdoc);

        docAtts.init();
        dlgatts.getContentPane().add(docAtts);
        dlgatts.pack();
        FrameLauncher.CenterFrame(dlgatts);
        dlgatts.setVisible(true);

        if (docAtts.getSelectedAttachment() != null) {
            Attachment att = docAtts.getSelectedAttachment();

            return att;
        } else {
            return null;
        }
    }

    private void importAttachment(final BungeniDocument doc, final Attachment selectedAttachment) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                String                  sAttURL         = selectedAttachment.url;
                HttpGet                 hget            = new HttpGet(sAttURL);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String                  responseBody    = "";

                try {
                    responseBody = client.execute(hget, responseHandler);
                } catch (IOException ex) {
                    log.error("Error getting response body", ex);
                }

                // parse the attachment body
                final org.jsoup.nodes.Document attsoupdoc = Jsoup.parse(responseBody);
                final BungeniAttachment        attDoc     = new BungeniAttachment(sAttURL, attsoupdoc);
                HashMap                        objMap     = new HashMap() {
                    {
                        put("MAIN_DOC", doc);
                        put("ATT_DOC", attDoc);
                    }
                };
                IMainContainerPanel panel = CommonEditorInterfaceFunctions.getMainPanel();

                for (ITabbedPanel ipanel : panel.getTabbedPanels()) {
                    ipanel.setCustomObjectMap(objMap);
                }

                File fattFile = importAttachmentFile(doc, attDoc);

                panel.loadDocumentInPanel(fattFile);
            }
        });
    }

    private File importAttachmentFile(BungeniDocument doc, BungeniAttachment attDoc) {

        // download the file --
        File         fdownTemp      = null;
        String       binaryFileUrl  = attDoc.getDownloadURL();
        String       binaryFileName = attDoc.getAttachmentName();
        HttpGet      fileGet        = new HttpGet(binaryFileUrl);
        InputStream  input          = null;
        OutputStream output         = null;

        try {
            HttpResponse downloadResponse = (HttpResponse) client.execute(fileGet);
            byte[]       buffer           = new byte[1024];
            String       filenameRandom   = RandomStringUtils.randomAlphanumeric(14);

            input     = downloadResponse.getEntity().getContent();
            fdownTemp = TempFileManager.createTempFile(filenameRandom, ".odt");
            output    = new FileOutputStream(fdownTemp);

            for (int length; (length = input.read(buffer)) > 0; ) {
                output.write(buffer, 0, length);
            }
        } catch (Exception ex) {
            log.error("Error while attempting to download attachmebt");
        } finally {
            try {
                if (input != null) {
                    input.close();
                }

                if (output != null) {
                    output.close();
                }
            } catch (IOException ex) {
                log.error("Error while closing streams");
            }

            return fdownTemp;
        }
    }
    ***/
}


//~ Formatted by Jindent --- http://www.jindent.com

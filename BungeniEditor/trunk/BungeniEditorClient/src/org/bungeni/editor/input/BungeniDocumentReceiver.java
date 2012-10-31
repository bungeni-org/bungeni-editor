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

import java.awt.Cursor;
import java.io.IOException;
import org.bungeni.editor.input.IInputDocumentReceiver;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.bungeni.editor.config.PluggableConfigReader.PluggableConfig;
import org.bungeni.extpanels.bungeni.BungeniDocumentAttListPanel;

import org.bungeni.extpanels.bungeni.BungeniAppConnector;
import org.bungeni.extpanels.bungeni.BungeniDocument;
import org.bungeni.extpanels.bungeni.BungeniDocument.Attachment;
import org.bungeni.extutils.FrameLauncher;
import org.bungeni.utils.BungeniDialog;
import org.jdom.Element;
import org.jsoup.Jsoup;

/**
 *
 * @author Ashok
 */
public class BungeniDocumentReceiver implements IInputDocumentReceiver {
    BungeniAppConnector appConnector = null;

    public String receiveDocument(final JFrame parentFrame, final PluggableConfig customConfig,  HashMap inputParams) {

             String sDocURL = (String)JOptionPane.showInputDialog(
                    parentFrame,
                    "Enter the URL of the document to Import",
                    "Import document from Bungeni",
                    JOptionPane.QUESTION_MESSAGE);
            return receive(parentFrame, customConfig,  sDocURL);
    }

    private String receive(final JFrame parentFrame, PluggableConfig config, final String sDocURL){
           //If a string was returned, say so.
            if ((sDocURL != null) && (sDocURL.length() > 0)) {
                //open document here
                  //set the wait cursor
              parentFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
              if (config.customConfigElement != null ){
                  Element bungeniConfig = config.customConfigElement.getChild("bungeni");
                  if (bungeniConfig != null) {
                  String username = bungeniConfig.getAttributeValue("user");
                  String upassword = bungeniConfig.getAttributeValue("password");
                  String uhost = bungeniConfig.getAttributeValue("host");
                  String uport =  bungeniConfig.getAttributeValue("port");
                  String uloginUri = bungeniConfig.getAttributeValue("loginuri");
                  if (null == appConnector) {
                       appConnector = new BungeniAppConnector(
                                uhost,
                                uport,
                                uloginUri,
                                username,
                                upassword
                                );

                  }
                  }
                   //call the swingworker thread for the button event
               SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    //login
                    DefaultHttpClient client = appConnector.login();
                    //access the input URL
                    final HttpGet geturl = new HttpGet(sDocURL);
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    String responseBody = "";
                    try {
                        responseBody = client.execute(geturl, responseHandler);
                    } catch (IOException ex) {

                    }
                   //parse response Body
                   parentFrame.setCursor(Cursor.getDefaultCursor());
                   //retrieve the attachments
                   org.jsoup.nodes.Document doc = Jsoup.parse(responseBody);
                   BungeniDocument jdoc = new BungeniDocument(sDocURL, doc);
                   BungeniDialog dlgatts = new BungeniDialog(
                            parentFrame ,
                            "Import an Attachment",
                            true
                            );
                    BungeniDocumentAttListPanel docAtts =
                                new BungeniDocumentAttListPanel(client, dlgatts, jdoc);
                        docAtts.init();
                        dlgatts.getContentPane().add(docAtts);
                        dlgatts.pack();
                        FrameLauncher.CenterFrame(dlgatts);
                        dlgatts.setVisible(true);
                        if (docAtts.getSelectedAttachment() != null) {
                            Attachment att = docAtts.getSelectedAttachment();
                            JOptionPane.showMessageDialog(null, att);
                        }


                    //sourceButton.setEnabled(true);
                }
            });
         }

              }

             
            return "";
    }

}

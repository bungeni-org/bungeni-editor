/*
 *  Copyright (C) 2012 PC
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

package org.bungeni.editor.dialogs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniDocumentSource {

   private static org.apache.log4j.Logger log =
           Logger.getLogger(BungeniDocumentSource.class.getName());


    private final String loginUrl ;
 
    private final String user ;
    private final String password ;
    private final String serverName;
    private final String serverPort;
    private final String loginPageURI;

    private DefaultHttpClient client = null;


    private List<BungeniDocuments> bungeniDocuments =
            new ArrayList<BungeniDocuments>(){
                {
                    add(
                     new BungeniDocuments(
                       "Bill - cosignatory - scheduling first reading p1_04 [first reading pending]",
                       "http://10.0.2.2:8081/workspace/my-documents/inbox/bill-299/"
                       )
                    );
                    add(
                     new BungeniDocuments(
                       "Bill as Clerk - cosignatory - status allow scheduling first reading p1_04 [first reading pending] ",
                       "http://10.0.2.2:8081/workspace/my-documents/inbox/bill-294/"
                      )
                    );
                }
    };

    class BungeniDocuments {
        public String title = "";
        public String url = "";

        public BungeniDocuments(String title, String url ){
            this.title = title;
            this.url = url;
        }

        @Override
        public String toString(){
            return this.title;
        }
    }

    public BungeniDocumentSource(
            String serverName,
            String serverPort,
            String loginPageURI,
            String user,
            String password) {
        this.loginPageURI = loginPageURI;
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.password = password;
        this.user = user;
        this.loginUrl = "http://" + this.serverName + ":" + this.serverPort + "/" + loginPageURI;
    }

    public List<BungeniDocuments> getBungeniDocuments(){
        return this.bungeniDocuments;
    }

    public DefaultHttpClient login(){
        if (client != null) {
            return client;
        }
        client = new DefaultHttpClient();
        try {
            final HttpPost post = new HttpPost(loginUrl);
            final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("login", this.user));
            nameValuePairs.add(new BasicNameValuePair("password", this.password));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            client.execute(post, responseHandler);
        } catch (IOException ex) {
            log.error("Extended exception while logging in ", ex);
        }
        return client;
    }

    public void selectDocument(){
        
    }







}

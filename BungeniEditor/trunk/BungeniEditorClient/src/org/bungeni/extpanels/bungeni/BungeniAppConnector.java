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

package org.bungeni.extpanels.bungeni;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.bungeni.extutils.TempFileManager;

/**
 *
 * @author Ashok Hariharan
 */
public class BungeniAppConnector {

    private static org.apache.log4j.Logger log =
           Logger.getLogger(BungeniAppConnector.class.getName());


    private final String loginUrl ;

    private final String user;
    private final String password;
    private final String serverName ;
    private final String serverPort ;
    private final String loginPageURI ;
    private final String urlBase ; 
    
    private DefaultHttpClient client = null;

   public  BungeniAppConnector(
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
        this.urlBase = "http://" + this.serverName + ":" + this.serverPort ;
        loginUrl = this.urlBase + "/" + loginPageURI;
        log.info("BungeniAppConnector : LOGIN:" + loginUrl + " user : " + this.user + " password :" +  this.password);
    }
   
   
    /**
     * Thread safe client ,since we access the same client across threads
     * @return 
     */
    public DefaultHttpClient getThreadSafeClient()  {
        PoolingClientConnectionManager cxMgr = new PoolingClientConnectionManager( SchemeRegistryFactory.createDefault());
        cxMgr.setMaxTotal(100);
        cxMgr.setDefaultMaxPerRoute(20);
        
        DefaultHttpClient aClient = new DefaultHttpClient();
        HttpParams params = aClient.getParams();
        return new DefaultHttpClient(cxMgr, params);
    }
   

       public DefaultHttpClient login() throws UnsupportedEncodingException, IOException{
            if (getClient() != null) {
                return getClient();
            }
            client = getThreadSafeClient();
      
            final HttpPost post = new HttpPost(loginUrl);
            final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("login", this.getUser()));
            nameValuePairs.add(new BasicNameValuePair("password", this.getPassword()));
            post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            HttpResponse response = getClient().execute(post);
            responseHandler.handleResponse(response);
            consumeContent(response.getEntity());
        return getClient();
    }

    public class WebResponse {
        private String responseBody;
        private int statusCode;
        
        public WebResponse(int statusCode, String responseBody) {
            this.statusCode = statusCode;
            this.responseBody = responseBody;
        }
        
        public String getResponseBody(){
            return this.responseBody;
        }
        
        public int getStatusCode(){
            return this.statusCode;
        }
    }
    
    
    

    public File getDownloadUrl(String sPage, boolean prefix) {
        
        String pageURL = (
                prefix ? 
                    this.urlBase + sPage :
                    sPage
                );

        final HttpGet           geturl          = new HttpGet(pageURL);
        
        try {
            String       filenameRandom   = RandomStringUtils.randomAlphanumeric(8);
            File fTempODT = TempFileManager.createTempFile(filenameRandom, ".odt");
            HttpResponse response = client.execute(geturl);
            int nStatusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (entity != null && nStatusCode == 200 ) {
                InputStream istream = entity.getContent();
                DataOutputStream dos = new DataOutputStream(
                        new BufferedOutputStream(
                            new FileOutputStream(fTempODT)
                        )
                   );
                //FileWriter fw = new FileWriter(fTempODT);
                byte[] rawFiles = IOUtils.toByteArray(istream);
                dos.write(rawFiles);
                dos.close();
            }
            consumeContent(entity);
            return fTempODT;
        } catch (IOException ex) {
            log.error("Error while accessin url : " + pageURL , ex);
           
        }
        return null;
    }
    
    private void consumeContent(HttpEntity entity) {
        try {
            if (entity != null) {
                EntityUtils.consume(entity);
            }
        } catch (IOException ex) {
            log.error("Error while releasing connection");
        }
    }
    
    public WebResponse multipartPostUrl(String sPage, boolean prefix, HashMap<String, ContentBody> nameValuePairs ) throws UnsupportedEncodingException  {
       WebResponse wr = null;
         String pageURL = (
                prefix ? 
                    this.urlBase + sPage :
                    sPage
                );
        MultipartEntity entity = 
                new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        
        Set<String> fields = nameValuePairs.keySet();
        for (String fieldName : fields) {
            entity.addPart(fieldName, nameValuePairs.get(fieldName));
        }
        return doMultiPartPost(pageURL, entity);
    }
    
    
    
    public WebResponse multipartPostUrl(String sPage, boolean prefix, List<BasicNameValuePair> nameValuePairs ) throws UnsupportedEncodingException  {
        WebResponse wr = null;
        String pageURL = (
                prefix ? 
                    this.urlBase + sPage :
                    sPage
                );
        MultipartEntity entity = 
                new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        for (BasicNameValuePair nv : nameValuePairs) {
            entity.addPart(nv.getName(), new StringBody(nv.getValue()));
        }
        
        return this.doMultiPartPost(pageURL, entity);
        /*
        HttpPost webPost = new HttpPost(pageURL);
        webPost.setEntity(entity);
        HttpResponse response = null ;
        try {
            response = client.execute(webPost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String sBody = responseHandler.handleResponse(response);
            wr = new WebResponse(response.getStatusLine().getStatusCode(), sBody);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            if (response != null) {
                consumeContent(
                    response.getEntity()
                );
            }
        }
        return wr; */
    }
    private WebResponse doMultiPartPost(String pageURL, MultipartEntity entity){
        WebResponse wr = null;
        HttpPost webPost = new HttpPost(pageURL);
        webPost.setEntity(entity);
        HttpResponse response = null ;
        try {
            response = client.execute(webPost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String sBody = responseHandler.handleResponse(response);
            wr = new WebResponse(response.getStatusLine().getStatusCode(), sBody);
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            if (response != null) {
                consumeContent(
                    response.getEntity()
                );
            }
        }
        return wr;
    }

    
    public WebResponse postUrl(String sPage, boolean prefix, List<BasicNameValuePair> nameValuePairs ) throws UnsupportedEncodingException {
        WebResponse wr = null;

        String pageURL = (
                prefix ? 
                    this.urlBase + sPage :
                    sPage
                );
        HttpPost webPost = new HttpPost(pageURL);
        HttpEntity postEntity = new UrlEncodedFormEntity(nameValuePairs);
        webPost.setEntity(postEntity);
        HttpResponse response = null;
        try {
            response = getClient().execute(webPost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String sBody = responseHandler.handleResponse(response);
            wr = new WebResponse(response.getStatusLine().getStatusCode(), sBody);
        } catch (IOException ex) {
            log.error("Error while posting transition " , ex);
        } finally {
            consumeContent(response.getEntity());
        }
        return wr;
    }
    
    
    public WebResponse getUrl(String sPage,  boolean prefix) {
        WebResponse wr = null;
        
        String pageURL = (
                prefix ? 
                    this.urlBase + sPage :
                    sPage
                );

        final HttpGet           geturl          = new HttpGet(pageURL);
        boolean bState = false;
        try {
            HttpResponse response = client.execute(geturl);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = responseHandler.handleResponse(response);
            int nStatusCode = response.getStatusLine().getStatusCode();
            consumeContent(response.getEntity());
            wr = new WebResponse(nStatusCode, responseBody);
        } catch (IOException ex) {
            bState = false;
            log.error("Error while accessin url : " + pageURL , ex);
        }
        return wr;
    
    }   
       
       
    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the serverName
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * @return the serverPort
     */
    public String getServerPort() {
        return serverPort;
    }

    /**
     * @return the loginPageURI
     */
    public String getLoginPageURI() {
        return loginPageURI;
    }

    /**
     * @return the client
     */
    public DefaultHttpClient getClient() {
        return client;
    }

    /**
     * 
     * @return the url base  
     */
    public String getUrlBase(){
        return this.urlBase;
    }


}

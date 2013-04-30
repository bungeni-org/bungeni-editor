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
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
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
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
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
    private final String oauthLoginUrl; 
    
    private final String user;
    private final String password;
    private final String serverName ;
    private final String serverPort ;
    private final String loginPageURI ;
    private final String urlBase ; 
    private final OAuthCredentials oauthCredentials ; 
    private DefaultHttpClient client = null;

   public  BungeniAppConnector(
            String serverName,
            String serverPort,
            String loginPageURI,
            String user,
            String password,
            OAuthCredentials oauthCredentials) {
        this.loginPageURI = loginPageURI;
        this.serverName = serverName;
        this.serverPort = serverPort;
        this.password = password;
        this.user = user;
        this.urlBase = "http://" + this.serverName + ":" + this.serverPort ;
        this.loginUrl = this.urlBase + "/" + loginPageURI;
        this.oauthCredentials = oauthCredentials; 
        this.oauthLoginUrl = this.urlBase + oauthCredentials.authUri();
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
   

    
    
    public DefaultHttpClient oauthLogin() throws UnsupportedEncodingException, IOException {
        if (getClient() != null) {
            return getClient();
        }
        client = getThreadSafeClient();
        String oauthForwardURL = oauthNegotiate();
        if (oauthForwardURL != null ) {
            //oauthForwardURL = URLDecoder.decode(oauthForwardURL, "UTF-8");
            String oauthAuthorizeURL = oauthAuthenticate(oauthForwardURL, this.oauthLoginUrl );
            if (oauthAuthorizeURL != null) {
                oauthAuthorize(oauthAuthorizeURL);
            }
            
        }
        return getClient();
    }
    
    private String oauthAuthenticate(String oauthForwardURL, String oauthCameFromURL) throws UnsupportedEncodingException, IOException{
        
        final HttpPost post = new HttpPost(oauthForwardURL);
        final HashMap<String, ContentBody> nameValuePairs = new HashMap<String, ContentBody>();
        //final List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);
        nameValuePairs.put("login", new StringBody(this.getUser()));
        nameValuePairs.put("password", new StringBody(this.getPassword()));
        nameValuePairs.put("camefrom", new StringBody(oauthCameFromURL));
        nameValuePairs.put("actions.login", new StringBody("login"));
        MultipartEntity entity = 
                new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        Set<String> fields = nameValuePairs.keySet();
        for (String fieldName : fields) {
            entity.addPart(fieldName, nameValuePairs.get(fieldName));
        }
        HttpContext context= new BasicHttpContext();
        post.setEntity(entity);
        HttpResponse oauthResponse = client.execute(post, context);
                // if the OAuth page retrieval failed throw an exception
        if (oauthResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new IOException(oauthResponse.getStatusLine().toString());
        }
        String currentUrl = getRequestEndContextURL(context);
        // consume the response
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String sBody = responseHandler.handleResponse(oauthResponse);
        consumeContent(oauthResponse.getEntity());
        return currentUrl;
    }
    
    private boolean oauthAuthorize(String oauthAuthorizeURL){
        System.out.println(oauthAuthorizeURL);
        return true;
    }
    
    private String oauthNegotiate() throws IOException{
        final HttpGet hget = new HttpGet(this.oauthLoginUrl);
        HttpContext context = new BasicHttpContext(); 
        HttpResponse oauthResponse = getClient().execute(hget, context); 
        // if the OAuth page retrieval failed throw an exception
        if (oauthResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new IOException(oauthResponse.getStatusLine().toString());
        }
        // if the OAuth page retrieval succeeded we get the redirected page, 
        // which in this case is the login page
        String currentUrl = getRequestEndContextURL(context);
        // consume the response
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        responseHandler.handleResponse(oauthResponse);
        consumeContent(oauthResponse.getEntity());
        return currentUrl;
    }
    
    private String getRequestEndContextURL(HttpContext context){
        HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute( 
                ExecutionContext.HTTP_REQUEST
                );
        HttpHost currentHost = (HttpHost)  context.getAttribute( 
                ExecutionContext.HTTP_TARGET_HOST
                );
        // this gives the login page
        String currentUrl = (
                currentReq.getURI().isAbsolute()) ? 
                    currentReq.getURI().toString() : 
                    (currentHost.toURI() + currentReq.getURI()
                );
        return currentUrl;
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
            String sBody = responseHandler.handleResponse(response);
            System.out.println(sBody);
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

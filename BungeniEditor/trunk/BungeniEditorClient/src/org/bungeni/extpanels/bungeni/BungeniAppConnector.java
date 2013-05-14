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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.Header;
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
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.bungeni.editor.config.BaseConfigReader;
import org.bungeni.extutils.TempFileManager;
import org.jdom.JDOMException;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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
    private final String oauthAuthFormUrl;

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
        this.oauthAuthFormUrl = this.urlBase + oauthCredentials.authFormUri();
        //this.oauthTokenUrl   = this.urlBase + oauthCredentials.authTokenUri();
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
   

    
    
    public DefaultHttpClient oauthLogin() throws UnsupportedEncodingException, IOException, JDOMException {
        if (getClient() != null) {
            return getClient();
        }
        client = getThreadSafeClient();
        
        // check if acess code exists !
       boolean access_code_exists = false; 
       Properties props = new Properties() ; 
       try {
            props = this.getOauthProperties();
            if (props.containsKey("access_token")) {
                 // the below API checks for expiryfo the access code and 
                 // gets a new one if it exists
                 oauthCheckAccessCode(props);
                 access_code_exists = true;
            }
           } catch(IOException ex) {
             log.info("ACCESS_TOKEN not found, will to create a new one");  
           }
      
       
       if (access_code_exists) {
           // attempt to use the access code 
           // get the oauth properties
           Properties authProps = this.getOauthProperties();
       } else {
         //negotiate to the login url
         String oauthForwardURL = oauthNegotiate();
         if (oauthForwardURL != null ) {
            // now authenticate , this will return the authorize URL 
            String oauthAuthorizeURL = oauthAuthenticate(oauthForwardURL, this.oauthLoginUrl );
            if (oauthAuthorizeURL != null) {
                // now attempt to authorize, this will provide an oauth refresh token
                OAuthToken token = oauthAuthorize(oauthAuthorizeURL);
                if (token != null) {
                    this.oauthCredentials.setRefreshCode(token.getCode());
                    this.oauthCredentials.setRefreshState(token.getState());
                    oauthTokenAccess(token);
                }
            }
        }
       }
        return getClient();
    }
    
    private boolean oauthCheckAccessCode(Properties props) throws FileNotFoundException, IOException{
        String sRefreshDateTime = (String) props.get("authorization_time");
        try {
            if (isAccessTokenExpired(sRefreshDateTime)) {
                // if expired get new access code
                try {
                    boolean bState =  oauthNewAccessToken(props);   
                    return bState;
                } catch (Exception ex) {
                    log.error("Error while getting new access token ", ex);
                    return false;
                }
            }
        } catch(ParseException ex) {
            try {
                // still 
                boolean bState =  oauthNewAccessToken(props);
                return bState;
            } catch(Exception ex2) {
                log.error("Error while getting new access token ", ex2);
                return false;
            }
        }
        return false;
    }
    
    private DateFormat getOauthRefreshDateFormat(){
      return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    
    private String getCurrentDateTime(){
      DateFormat df = getOauthRefreshDateFormat();
      Date dt = new Date();
      return df.format(dt);
    }
    
    private boolean isAccessTokenExpired(String sRefreshTime) throws ParseException {
        long expiryTime = 3600 ; 
        // parse the refresh date
        DateFormat df = getOauthRefreshDateFormat();
        Date dtOriginal = df.parse(sRefreshTime);
        Calendar c = Calendar.getInstance();
        Date dtCurrent = c.getTime();
        long timeDiff = Math.abs( 
                (dtCurrent.getTime() - dtOriginal.getTime()) / 1000 
                ) ;
        if (timeDiff >= expiryTime ) {
            return true;
        }
        return false;
    }
    
    private HashMap<String,String> parseJSonStream(String responseBody){
        JSONObject jsonObject = (JSONObject) JSONValue.parse(responseBody);
        HashMap<String,String> jsonTokens = new HashMap<String, String>();
        Set<String> keys = jsonObject.keySet();
        for (String key : keys) {
            jsonTokens.put(key, jsonObject.get(key).toString());
        }
        jsonTokens.put("authorization_time", getCurrentDateTime() );
        return jsonTokens;
    }
    
    private boolean oauthTokenAccess(OAuthToken token) {
        boolean bstate = false;
        try {
            final HttpGet hget = new HttpGet(this.urlBase + this.oauthCredentials.accessTokenUri());
            HttpContext context = new BasicHttpContext(); 
            HttpResponse oauthResponse = getClient().execute(hget, context); 
            // if the OAuth page retrieval failed throw an exception
            if (
                 (oauthResponse.getStatusLine().getStatusCode() != HttpStatus.SC_OK) && 
                    ! (
                    "application/json".equals(
                        oauthResponse.getEntity().getContentType().getValue()
                        )
                    )
                 ) {
                throw new IOException(oauthResponse.getStatusLine().toString());
            }
            // consume the response
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = responseHandler.handleResponse(oauthResponse);
            HashMap<String,String> jsonTokens = parseJSonStream(responseBody);
            jsonTokens.put("authorization_code", token.getCode());
            jsonTokens.put("authorization_state", token.getState());
            this.writeOauthProperties(jsonTokens);
            consumeContent(oauthResponse.getEntity());
            bstate =  true;
        } catch (IOException ex) {
            log.error("Error while getting access token", ex);
        } 
        return bstate;
      }
    
    /**
     * Login to Bungeni via the OAuth route
     * @param oauthForwardURL
     * @param oauthCameFromURL
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    private String oauthAuthenticate(String oauthForwardURL, String oauthCameFromURL) throws UnsupportedEncodingException, IOException{
        
        final HttpPost post = new HttpPost(oauthForwardURL);
        final HashMap<String, ContentBody> nameValuePairs = new HashMap<String, ContentBody>();
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
    
    
    private MultipartEntity getMultiPartEntity(HashMap<String, ContentBody> nameValuePairs) {
        MultipartEntity entity = 
                new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        Set<String> fields = nameValuePairs.keySet();
        for (String fieldName : fields) {
            entity.addPart(fieldName, nameValuePairs.get(fieldName));
        }
        return entity;
    }


    private boolean oauthNewAccessToken(Properties props) throws FileNotFoundException, IOException {
       String sRefreshToken = (String) props.get("refresh_token");
       String sRefreshTokenUrl = this.urlBase + this.oauthCredentials.renewAccessTokenUri(sRefreshToken);
       WebResponse wr = this.getUrl(sRefreshTokenUrl, false);
       if (wr.statusCode == 200 && 
               "application/json".equals(
                        wr.response.getEntity().getContentType().getValue()
                        )
        ) {
          HashMap<String,String> refreshTokens = parseJSonStream(wr.getResponseBody());
          try {
            this.writeOauthProperties(refreshTokens);
            return true;
          } catch(Exception ex) {
             log.error("Error while writing oauth properties !", ex);
             return false;
          }
       }
       return false;
    }
    
    class OAuthToken {
        
        String code ; 
        String state ; 
        
        public OAuthToken(String code, String state) {
            this.code = code ; 
            this.state = state;
        }
        
        public String getCode(){
            return this.code;
        }
        
        public String getState(){
            return this.state;
        }
        
    }
    
    private OAuthToken getAuthToken(String urlLoc) throws MalformedURLException, URISyntaxException {
          URL url = new URL(urlLoc);
          List<NameValuePair> nvps = URLEncodedUtils.parse(new URI(urlLoc), "UTF-8");
          // add the refresh key and state to the config file 
          String refreshKey = "";
          String refreshState = "";
            for (NameValuePair nameValuePair : nvps) {
              if (nameValuePair.getName().equals("code")){
                  refreshKey = nameValuePair.getValue();
              }
              if (nameValuePair.getName().equals("state")){
                  refreshState = nameValuePair.getValue();
              }
            }
           OAuthToken otoken = new OAuthToken(refreshKey, refreshState);  
           return otoken;
    }
    
    private OAuthToken oauthAuthorize(String oauthAuthorizeURL) throws IOException, JDOMException{
         // get pag einfo 
        OAuthToken token = null;
        WebResponse wr = this.getUrl(oauthAuthorizeURL, false);
        if (wr.statusCode == 200) {
            HashMap<String, ContentBody> formfields = BungeniServiceAccess.getInstance().getAuthorizeFormFieldValues(wr.responseBody);
            if ( !formfields.isEmpty() ){
                HttpContext context = new BasicHttpContext();
                // we use the form authorize URL here 
                final HttpPost post = new HttpPost(this.oauthAuthFormUrl);
                // we disable the automatic redirect of the URL since we want to grab 
                // the refresh token and anyway the redirect is to a dummy url
                final HttpParams params = new BasicHttpParams();
                params.setParameter(ClientPNames.HANDLE_REDIRECTS, Boolean.FALSE);
                post.setParams(params);
                post.setEntity(getMultiPartEntity(formfields));
                HttpResponse authResponse = getClient().execute(post, context);
                String redirectLocation = "";
                Header locationHeader = authResponse.getFirstHeader("location");
                //consume response
                //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                //responseHandler.handleResponse(authResponse);
                
                if (locationHeader != null){
                    redirectLocation = locationHeader.getValue();
                    EntityUtils.consumeQuietly(authResponse.getEntity());
                    try {
                        token = getAuthToken(redirectLocation);
                        // do someting with the returned codes
                    } catch (MalformedURLException ex) {
                        log.error("Error while getting oauthtoken", ex);
                    } catch (URISyntaxException ex) {
                        log.error("Error while getting oauthtoken", ex);
                    }
                    
                } else {
                    EntityUtils.consumeQuietly(authResponse.getEntity());
                    throw new IOException(authResponse.getStatusLine().toString());
                }
            } else {
                throw new IOException("Authorization failed !");
            }
        }
        return token;
    }
    
    private File getOauthPropertiesFile(){
        File fOauthProps = new File(
                BaseConfigReader.getSettingsFolder() + File.separator + "oauth.properties"
                );
        return fOauthProps;
    }
    
    private void writeOauthProperties(HashMap<String,String> values) throws FileNotFoundException, IOException {
        File fOauthProps = getOauthPropertiesFile();
        Properties oauthProps = new Properties();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Set<String> keys = values.keySet();
        for (String key : keys) {
            oauthProps.setProperty(key, values.get(key));
        }
        oauthProps.store(new FileOutputStream(fOauthProps), "stored on " + df.format(new Date()));
       }
    
    private Properties  getOauthProperties() throws IOException{
        File f = getOauthPropertiesFile();
        Properties props = new Properties();
        props.load(new FileInputStream(f));
        return props;
    }
            
            
    /**
     * Negotiate the Oauth access URL , this will forward to a login page
     * @return
     * @throws IOException 
     */
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
        private HttpResponse response;
        
        public WebResponse(String body, HttpResponse response) {
            this.statusCode = response.getStatusLine().getStatusCode();
            this.response = response;
            this.responseBody = body;
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
            wr = new WebResponse(sBody, response);
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
            wr = new WebResponse(sBody, response);
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
            wr = new WebResponse(responseBody, response);
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

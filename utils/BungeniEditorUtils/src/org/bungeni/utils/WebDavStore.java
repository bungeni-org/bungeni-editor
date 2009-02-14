/*
 * WebDavStore.java
 *
 * Created on June 8, 2007, 1:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.utils;

/**
 *
 * @author ashok
 */



import java.io.IOException;
import java.io.InputStream;
import java.net.NoRouteToHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import org.apache.webdav.lib.WebdavResource;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.httpclient.HttpsURL;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.log4j.Logger;
import org.apache.webdav.lib.WebdavResources;



public class WebDavStore {
   private static org.apache.log4j.Logger log = Logger.getLogger(WebDavStore.class.getName());
    private String connectionUrl = "" ;
    private int connectionPort = 8080 ;
    private String connectionBaseDirectory = "" ;
    private String connectionPath = "";
    private String connectionUsername = "" ;
    private String connectionPassword = "" ;
    private HttpURL httpURL = null;

    /** The WebDAV resource. */
    private WebdavResource webdavResource = null;
   
    /** Debug level for all debug messages */
    final static int DEBUG_ON  = Integer.MAX_VALUE;

    /** Debug level for no debug messages */
    final static int DEBUG_OFF = 0;
    /** The debug level. */
    private int debugLevel = DEBUG_OFF;


    public WebDavStore(){
    }

   public void connect(String filePath)
    {
        String path = connectionBaseDirectory;
        if (!path.endsWith("/") && !path.endsWith("\\") && !filePath.startsWith("/") && !filePath.startsWith("\\")) {
            // append / to the path

             path += "/";
        }

        if ((path.endsWith("/") || path.endsWith("\\")) && (filePath.startsWith("/") || filePath.startsWith("\\"))) {
            // remove / from the path

             path = path.substring(0, path.length() - 1);
        }

        path = path + filePath;
        log.debug("connecting "+ connectionUrl + ":" + connectionPort + ","+ path+ ","+ connectionUsername+ ","+ connectionPassword );
        connect(connectionUrl + ":" + connectionPort, path, connectionUsername, connectionPassword);
    }

    private void connect(String uri, String filePath, String login, String password)
    {

        if (!uri.endsWith("/") && !uri.endsWith("\\") && !filePath.startsWith("/") && !filePath.startsWith("\\")) {
            // append / to the path

             uri+="/";
        }

        if ((uri.endsWith("/") || uri.endsWith("\\")) && (filePath.startsWith("/") || filePath.startsWith("\\"))) {
            // remove / from the path

             uri = uri.substring(0, uri.length() - 1);
        }

        uri = uri + filePath;

        

        log.info("connect filePath : " + filePath);
        this.connectionPath = filePath;
       // log.info("allocResource = "+ uri);
        webdavResource = allocResource(webdavResource, uri, login, password, filePath);
    }

    public WebdavResource getResourceHandle(){
        return webdavResource;
    }
    private WebdavResource allocResource(WebdavResource resource, String uri, String login, String password, String filePath) {
        try {
            // Set up for processing WebDAV resources
            httpURL = uriToHttpURL(uri, login, password);
            //log.debug("In allocResource = "+ httpURL.getEscapedURI());
  
            if (resource == null) {
                resource = new WebdavResource(httpURL, filePath);
                /*    
                if (Settings.getProxy()) {
                    resource.setProxy(Settings.getProxyHost(), Settings.getProxyPort());
                    resource.setProxyCredentials(
                            new UsernamePasswordCredentials(
                                    Settings.getProxyUser(),
                                    Settings.getProxyPassword()));
                }
                 */
                resource.setDebug(debugLevel);
            } else {
                resource.close();
                resource.setHttpURL(httpURL);
            }

        }
        catch (Exception ex) {
            handleException(ex);
            resource = null;
            httpURL = null;
        }
        return resource;
    }
    
    public InputStream getContentAsStream() throws HttpException, IOException
    {
    	return webdavResource.getMethodData();
    }
    
    public boolean putMethod(String path, InputStream s) throws HttpException, IOException
    {
    	return webdavResource.putMethod(path, s);
    }
    
    /*
    public boolean readContentAsStream(OutputStream ostream) throws Exception {
        if (webdavResource.getIsCollection())
            throw new Exception("this is not a file");
              
        if (webdavResource.getMethod(ostream)) 
            return true;
        else
            throw new Exception("Error "+ webdavResource.getStatusCode() + " during retriving the file");
    }
*/
    public boolean isExist()
    {
        return webdavResource.getExistence();
    }


    public String getDisplayName()
    {
        return webdavResource.getDisplayName();
    }

    public long getContentLength()
    {
        return webdavResource.getGetContentLength();
    }

    public boolean isCollection()
    {
    	return webdavResource.isCollection();
    }

    /**
     * Get an array of pathnames and basic information denoting the WebDAV resources in the denoted by this pathname. array 0: displayname array 1: getcontentlength array 2: iscollection or getcontentype array 3: getlastmodifieddate array 4: name
     * @throws java.io.IOException 
     * @return java.util.Vector
     */
    public Vector listBasic() throws IOException {
        
        return webdavResource.listBasic();
    }

    private void handleException(Exception ex)
    {
        if (ex instanceof HttpException) {
            if (((HttpException) ex).getReasonCode() == HttpStatus.SC_METHOD_NOT_ALLOWED) {
                //log.debug("Not WebDAV-enabled?");
            }
            else if (((HttpException) ex).getReasonCode() == HttpStatus.SC_UNAUTHORIZED) {
                //log.debug("Unauthorized");
            }
            else {
                //log.error(ex.getMessage(), ex);
                //log.error(ex.getMessage(), ex);
            }
        }
        else if (ex instanceof NoRouteToHostException ){
            log.error(ex.getMessage(), ex);
        }
        else if (ex instanceof IOException) {
            log.error(ex.getMessage(), ex);
            
        }
        else {
            //log.error(ex.getMessage(), ex);
            
        }
    }

    void disconnect()
    {
        //log.info("disconnect");
        
        try {
            webdavResource.close();
        } catch (IOException e) {
        } finally {
            // Make sure the connection closed.
            httpURL = null;
            webdavResource = null;
        }
    }

    private static HttpURL uriToHttpURL(String uri, String login, String password) throws URIException {
        System.out.println("URItoHttpURL, URI = "+ uri);
        HttpURL httpURL =  uri.startsWith("https") ? new HttpsURL(uri)
                                                    : new HttpURL(uri);
        if (login != null && login.length() > 0)
            httpURL.setUserinfo(login, password);
        return httpURL;

    }

    public boolean upload(InputStream stream) throws IOException {
        boolean resourceExists = isExist();
        if (resourceExists)
            webdavResource.checkoutMethod();
        boolean putStatus = webdavResource.putMethod(stream);
        if (resourceExists)
            webdavResource.checkinMethod();
        return putStatus;
    }

    public Vector getMyVersions() {
    	Vector<String[]> version = new Vector<String[]>();
        try {
        	Enumeration versions =  webdavResource.reportMethod(webdavResource.getHttpURL(), 1);
        	while (versions.hasMoreElements()) {
                String filename = (String) versions.nextElement();
                filename = URIUtil.decode(filename);
                WebdavResource res = allocResource(null, filename, connectionUrl + connectionBaseDirectory, connectionPassword, filename.substring(connectionUrl.length() + 1 + String.valueOf(connectionPort).length()));
                String[] t = new String[4];
                t[0] = URIUtil.decode(res.getName());
                t[1] = res.getOwner();
                t[2] = Long.toString(res.getGetLastModified());
                t[3] = res.getPath();
                version.add(t);
        	}
        	return version;
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
    
    public Vector getVersions() throws HttpException {
        Vector<Vector<String>> versionVector = new Vector<Vector<String>>();
        try {
            Enumeration versions = webdavResource.reportMethod(webdavResource.getHttpURL(), 0);
            while (versions.hasMoreElements()) {
                String filename = (String) versions.nextElement();
                filename = URIUtil.decode(filename);
                WebdavResource res;
                res = allocResource(null, filename, connectionUrl + connectionBaseDirectory, connectionPassword, filename.substring(connectionUrl.length() + 1 + String.valueOf(connectionPort).length()));
                Vector<String> versionInfo = new Vector<String>();
                versionInfo.add(res.getDisplayName());
                versionInfo.add(filename);
                //SimpleDateFormat dateFormat = new SimpleDateFormat(configuration.getConfigurationValue("Application","dateformat"));
                SimpleDateFormat dateFormat = new SimpleDateFormat();
                Date lastModified = new Date(res.getGetLastModified());
                versionInfo.add(dateFormat.format(lastModified));
                versionInfo.add(res.getOwner());
                versionVector.add(versionInfo);
            }
        } catch (Exception e) {
            return null;
        }
        return versionVector;
    }
    
	public void setConnectionPassword(String connectionPassword) {
		this.connectionPassword = connectionPassword;
	}
        
        public String getConnectionPassword(){
            return this.connectionPassword;
        }

	public void setConnectionPort(int connectionPort) {
		this.connectionPort = connectionPort;
	}
        
        public int getConnectionPort(){
            return this.connectionPort;
        }
        
        public String getConnectionPath(){
            return this.connectionPath;
        }
                
	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}

        public String getConnectionUrl(){
            return this.connectionUrl;
        }
	public void setConnectionUsername(String connectionUsername) {
		this.connectionUsername = connectionUsername;
	}

        public String getConnectionUsername(){
            return this.connectionUsername;
        }
        
        
	public void setConnectionBaseDirectory(String connectionBaseDirectory) {
		this.connectionBaseDirectory = connectionBaseDirectory;
	}

     public void setPath(String path){
        try {
            this.webdavResource.setPath(path);
            System.out.println("\n\n\n Setting Path \n\n\n ");
        } catch (HttpException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
     } 
     
     public WebdavResources getChildResources(){
        WebdavResources wdrs = null;
         try {
            wdrs= this.webdavResource.getChildResources();
        } catch (HttpException ex) {
            log.debug(ex.getLocalizedMessage(), ex);
        } catch (IOException ex) {
            log.debug(ex.getLocalizedMessage(), ex);
        } finally{
            return wdrs;
        }
           
     }

     public String getPath(){
         return this.webdavResource.getPath();
     }
     
     public boolean exists(){
         return this.webdavResource.exists();
     }
     
}

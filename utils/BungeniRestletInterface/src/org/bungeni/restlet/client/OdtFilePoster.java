/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bungeni.restlet.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.OutputStream;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.Client;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;

/**
 * Helper class to post an ODT file to the transformer.
 * Response is returned as an Xml Message.
 * <TransformerRespose>
 * <sourceFile></sourceFile>
 * <transformResult>
 *  <state></state>
 *  <!-- for state; 0 = success / -1 = output with errors / -2 = failure -->
 *  <errors>
 *      <![CDATA[
 *
 *      ]]>s
 *  </errors>
 *  <output>
 *      <![CDATA[
 *
 *      ]]>
 * </output>
 * </transformResult>
 * </TransformerResponse>
 * @author Ashok Hariharan
 */
public class OdtFilePoster {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OdtFilePoster.class.getName());
    String clientReferer = "http://localhost:8180";
    private static String SERVER_NAME = "localhost";
    private static String SERVER_PORT = "8182";
    private static HashMap<String, String> POST_URI_MAP = new HashMap<String, String>() {

        {
            put("convert_to_anxml", "http://" + SERVER_NAME + ":" + SERVER_PORT + "/convert_to_anxml");
            put("set_convert_params", "http://" + SERVER_NAME + ":" + SERVER_PORT + "/set_convert_params");
        }
    };
    String fileName = "";
    byte[] fileContents = null;
    String conversionMode = "";

    /**
     * Create a new request Object
     * @return
     */
    private final Request getRequest(String postMapKey) {
        final Request request = new Request();
        // Identify ourselves.
        request.setReferrerRef(this.clientReferer);
        // Target resource.
        request.setResourceRef(POST_URI_MAP.get(postMapKey));
        // Action: Update
        request.setMethod(Method.POST);
        return request;
    }

    /**
     * Initializae the poster by setting the server URI
     * @param serverUri
     */
    public OdtFilePoster() {
    }

    /**
     * Sets the URI map for the server posting parameters
     * the two URI maps supported are :
     * convert_to_anxml
     * set_convert_params
     *
     * @param postUriMap
     */
    public static void setPostUriMap(HashMap<String, String> postUriMap) {
        POST_URI_MAP = postUriMap;
    }

    /**
     * Set the parameters first
     * @param documentType
     * @return
     */
    public Status postParams(String documentType, String pluginMode) {
        Status status = null;
        try {
            Form postParams = new Form();
            postParams.add("DocumentType", documentType);
            postParams.add("PluginMode", pluginMode);
            Representation formRep = postParams.getWebRepresentation();
            Request request = getRequest("set_convert_params");
            Client client = new Client(Protocol.HTTP);
            request.setEntity(formRep);
            final Response response = client.handle(request);
            status = response.getStatus();
        } catch (Exception ex) {
            log.error("postParams : ", ex);
        } finally {
            return status;
        }
    }

    /***
     * APi that does the actual "POST"
     * @param filetoPost - path the to the file to be posted
     * @param responseAsStream - the returned response will be written to this output stream
     * @return
     */
    public Status postFile(String filetoPost, OutputStream responseAsStream) {
        Status status = null;
        try {
            //ge the file handle to the file to post
            this.fileName = filetoPost;
            File fileToPost = new File(this.fileName);
            final Request request = getRequest("convert_to_anxml");
            //create  a client connector
            Client client = new Client(Protocol.HTTP);
            //generate a file representation of the submission
            Representation rep = new FileRepresentation(fileToPost, MediaType.APPLICATION_ZIP, 0);
            request.setEntity(rep);
            //get the http headers
            Form requestHeader = (Form) request.getAttributes().get("org.restlet.http.headers");
            //if there is no http header, add one
            if (requestHeader == null) {
                requestHeader = new Form();
                request.getAttributes().put("org.restlet.http.headers", requestHeader);
            }
            //we set a custom header so the file name gets transported
            requestHeader.add("X-Odt-File", fileToPost.getName());
            //finally post the request
            final Response response = client.handle(request);
            status = response.getStatus();
            Representation retRep = response.getEntity();
            retRep.write(responseAsStream);
        } catch (IOException ex) {
            log.error("postFile :", ex);
        } finally {
            return status;
        }


    }

    public boolean isServerRunning() {
        boolean bResponse = false;
        Client checkClient = null;
        Response clientResponse = null;
        try {
            checkClient = new Client(Protocol.HTTP);
            clientResponse = checkClient.get("http://" + SERVER_NAME + ":" + SERVER_PORT + "/");
        } catch (Exception ex) {
            log.error("isServerRunning:", ex);
            System.out.println("THrowing Exceptipn");
        } finally {
            if (clientResponse != null) {
                if (clientResponse.isEntityAvailable()) {
                    Representation respRep = clientResponse.getEntity();
                    String returnResponse = "";
                    try {
                        returnResponse = respRep.getText();
                    } catch (IOException ex) {
                        log.error("isServerRunning:", ex);
                        bResponse = false;

                    }
                    if (bResponse) {
                        bResponse = returnResponse.equals("SERVER_RUNNING");
                    }
                }
            }
        }
        return bResponse;
    }

    public static void main(String[] args) {
        FileOutputStream fostream = null;
        try {
            OdtFilePoster poster = new OdtFilePoster();
            System.out.print(poster.isServerRunning());
        //  poster.postParams("debaterecord", "odt2akn");
        //  fostream = new FileOutputStream(new File("/Users/ashok/dump.xml"));
        //  poster.postFile("/Users/ashok/Desktop/debaterecord_ken_eng_2008_12_17_main.odt", fostream);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            //     try {
            //         fostream.close();
            //     } catch (IOException ex) {
            //         ex.printStackTrace();
            //     }
        }
    }
}

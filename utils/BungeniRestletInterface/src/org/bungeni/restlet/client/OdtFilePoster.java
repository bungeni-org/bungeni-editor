/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.restlet.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.restlet.Client;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;
/**
 *
 * @author ashok
 */
public class OdtFilePoster {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OdtFilePoster.class.getName());

    
    String clientReferer = "http://localhost:8180";
    String serverURI = "http://localhost:8182/convert_to_anxml";

    String fileName = "";
    byte[] fileContents = null;
    String conversionMode = "";


    private byte[] fileToByteArray(String fFile) {
        byte[] bitesFile = null;
        try {
            File ffFile = new File(fFile);
            FileInputStream fistream = new FileInputStream(ffFile);
            Long fileSize = ffFile.length();
            bitesFile = new byte[fileSize.intValue()];
            fistream.read(bitesFile);
        } catch (IOException ex) {
            log.error("fileToyteArray :" , ex);
        } finally {
            return bitesFile;
        }

    }

    private  final Request getRequest(){
        final Request request = new Request();
        // Identify ourselves.
        request.setReferrerRef(this.clientReferer);
        // Target resource.
        request.setResourceRef(this.serverURI);
        // Action: Update
        request.setMethod(Method.POST);
        return request;
    }



    public OdtFilePoster (String serverUri) {
        this.serverURI = serverUri;
    }

    public Status postFile(String filetoPost){
        Status status = null;
        try {
            this.fileName = filetoPost;
            File fileToPost = new File(this.fileName);
            final Request request= getRequest();
            Client client = new Client(Protocol.HTTP);
            Representation rep = new FileRepresentation(fileToPost, MediaType.APPLICATION_PDF, 0);
            request.setEntity(rep);
            final Response response = client.handle(request);

            //byte[] fileBytes = this.fileToByteArray(this.fileName);
            // ByteArrayInputStream fileByteStream = new ByteArrayInputStream(fileBytes);
            // Representation representation = new org.restlet.resource.InputRepresentation(fileByteStream, MediaType.APPLICATION_OCTET_STREAM);
            // request.setEntity(representation);
            // Prepare HTTP client connector.
            //final Client client = new Client(Protocol.HTTP);
            // Make the call.
            //final Response response = client.handle(request);
            status = response.getStatus();
            Representation retRep = response.getEntity();
            retRep.write(new FileOutputStream("/Users/ashok/out.xml"));
        } catch (IOException ex) {
           log.error("fileToyteArray :" , ex);
        } finally {
            return status;
        }


    }

    public static void main(String[] args) {
        OdtFilePoster fc = new OdtFilePoster("http://localhost:8182/convert_to_anxml");
        System.out.println(fc.postFile("/Users/ashok/Desktop/book.pdf"));

    }

}

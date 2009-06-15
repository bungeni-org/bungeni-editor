/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.restlet.resources;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.bungeni.restlet.server.TransformerServer;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.Variant;

/**
 *Accepts ODT submissions - processes the ODT file and returns a response
 * @author Ashok Hariharan
 */
public class OdtResource  extends org.restlet.resource.Resource  {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(OdtResource.class.getName());

    /**
     * Add return Application xml as a variant
     * @param context
     * @param request
     * @param response
     */
    public OdtResource(Context context, Request request,
			Response response) {
		super(context, request, response);
        getVariants().add(new Variant(MediaType.APPLICATION_XML));
        
	}

    /**
     * Allow post for this resource
     * @return
     */
	@Override
	public boolean allowPost() {
		return true;
	}

	@Override
	public void acceptRepresentation(Representation entity) {
		log.debug("acceptRepresentation :  media type = " + entity.getMediaType());
        {
            FileOutputStream os = null;
            try {
                //get the submission headers, for the name of the input file
                Form requestHeaders = (Form) getRequest().getAttributes().get("org.restlet.http.headers");
                log.debug("output form headers = " + requestHeaders);
                String fileName = requestHeaders.getFirstValue("X-Odt-File");
                //write the file to a folder path on the server
                String folderPath = TransformerServer.getTempFileFolder();
                File file = new File(folderPath + File.separator + fileName);
                //overwrite the existing file
                os = new FileOutputStream(file, false);
                entity.write(os);
                //for now return a dummy response
                File outputXml = new File ("/home/undesa/Desktop/out.xml");
                Representation returnResponse = new FileRepresentation(outputXml,
				MediaType.APPLICATION_XML, 0);
                getResponse().setEntity(returnResponse);

            } catch (FileNotFoundException ex) {
                log.error("acceptRepresentation : ", ex);
            } catch (IOException e) {
                log.error("acceptRepresentation : ", e);
            } finally {
                try {
                    os.close();
                } catch (IOException ex) {
                    log.error("acceptRepresentation : ", ex);
                }
            }
        }
	}
}

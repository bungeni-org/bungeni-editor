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

/**
 *
 * @author ashok
 */
public class OdtResource  extends org.restlet.resource.Resource  {

    public OdtResource(Context context, Request request,
			Response response) {
		super(context, request, response);
	}

	@Override
	public boolean allowPost() {
		return true;
	}

	@Override
	public void post(Representation entity) {
		System.out.println(entity.getMediaType());
        {
            FileOutputStream os = null;
            try {
                String fileName = entity.getDownloadName();
                System.out.println("entity = "+ this.getRequest().getResourceRef().getLastSegment());
                Form requestHeaders = (Form) getRequest().getAttributes().get("org.restlet.http.headers");
                System.out.println("output form = " + requestHeaders);
                System.out.println("download name = "+ fileName);

                String folderPath = TransformerServer.getTempFileFolder();
                File file = new File(folderPath + File.separator + fileName);
                //overwrite the existing file
                os = new FileOutputStream(file, false);
                entity.write(os);
                File outputXml = new File ("/home/undesa/Desktop/out.xml");

                Representation returnResponse = new FileRepresentation(outputXml,
				MediaType.APPLICATION_XML, 0);
                getResponse().setEntity(returnResponse);

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    os.close();
                } catch (IOException ex) {
                   ex.printStackTrace();
                }
            }
        }
	}
}

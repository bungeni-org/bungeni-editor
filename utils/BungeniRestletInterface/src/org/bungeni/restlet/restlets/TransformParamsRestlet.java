/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.restlet.restlets;

import org.restlet.Restlet;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

/**
 *
 * @author Ashok Hariharan
 */
public class TransformParamsRestlet extends Restlet {
    private String documentType ;
private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TransformParamsRestlet.class.getName());

   @Override
    public void handle(Request request, Response response) {
       log.debug("handling method : "+ request.getMethod().getName());
       try {
        if(request.getMethod().equals(Method.POST)) {
            Form postedForm = request.getEntityAsForm();
            this.documentType = (String) postedForm.getFirstValue("DocumentType");
            System.out.println("doc type = "+ this.documentType);
            response.setStatus(Status.SUCCESS_NO_CONTENT);
        } else {
            response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }
       } catch (Exception ex) {
           log.error("handle : " , ex);
       }
   }

}

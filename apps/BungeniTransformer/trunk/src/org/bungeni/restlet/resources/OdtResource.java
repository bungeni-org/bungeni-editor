package org.bungeni.restlet.resources;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.bungeni.plugins.translator.OdtTranslate;

import org.bungeni.restlet.docs.Documentation;
import org.bungeni.restlet.server.TransformerServer;
import org.bungeni.restlet.utils.CommonUtils;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.FileRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
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
	
	/**
	 * Allow a get to enable the documentaiton display
	 */
	public boolean allowGet(){
		return true;
	}
	
	private static final String __DOC__ = "/org/bungeni/restlet/docs/convert_to_anxml.html";
	
	/**
	 * A get simply displays the documentation page
	 */
	public void handleGet(){
		String sDoc = Documentation.getDocumentation(__DOC__);
		StringRepresentation srep = new StringRepresentation(sDoc, MediaType.TEXT_HTML);
		getResponse().setEntity(srep);
	}
	
	private String getOdtFolderPath(){
		
		String folderPath = TransformerServer.getTempFileFolder() + File.separator + "odt";
	     File fFolder = new File(folderPath);
	     if (!fFolder.exists()) fFolder.mkdirs();
        
	     return folderPath;
        
	}
	
	private String getXmlFolderPath() {
		String folderPath = TransformerServer.getTempFileFolder() + File.separator + "xml";
	    File fFolder = new File(folderPath);
	    if (!fFolder.exists()) fFolder.mkdirs();
	    return folderPath;
	}
	
	private String getOutputXmlFile(String fileName){
	     int nIndex = fileName.indexOf(".");
         String xmlFile = fileName.substring(0, nIndex) + ".xml";
         String outputFile = getXmlFolderPath() + File.separator + xmlFile;
         return outputFile;
	}
	
	private String getOutputMetalexFile(String fileName){
	     int nIndex = fileName.indexOf(".");
        String xmlFile = fileName.substring(0, nIndex) + "_metalex.xml";
        String outputFile = getXmlFolderPath() + File.separator + xmlFile;
        return outputFile;
	}
	
	
	
	
	/**
	 * Accept the posted odt file
	 * @param fileName
	 * @param entity
	 */
	private String recieveOdtFile(String fileName, Representation entity) {
		FileOutputStream odtFile = null;
		
		String folderPath = getOdtFolderPath();
		
		File file = new File(folderPath + File.separator + fileName);
         try {
             //overwrite the existing file
        	odtFile = new FileOutputStream(file, false);
        	System.out.println("File size = " + entity.getSize());
        	
			entity.write(odtFile);
			odtFile.close();
		} catch (FileNotFoundException e) {
			log.error("recieveOdtFile:" , e);
		} catch (IOException e) {
			log.error("recieveOdtFile:" , e);
		} catch (Exception e) {
			log.error("(Exception) recieveOdtFile:" , e);
		}
		String fullPath = file.getAbsolutePath();
		System.out.println("Full path = " + fullPath);
		return file.getPath();
 	}

	/**
	 * Recieve an ODT file to transform
	 * -- transform the ODT to xml
	 * -- return the result xml back in an Xml envelop
	 * 
	 */
	@Override
	public void acceptRepresentation(Representation entity) {
		log.debug("acceptRepresentation :  media type = " + entity.getMediaType());
            try {
                //get the submission headers, for the name of the input file
                Form requestHeaders = (Form) getRequest().getAttributes().get("org.restlet.http.headers");
                log.debug("output form headers = " + requestHeaders);
                String fileName = requestHeaders.getFirstValue("X-Odt-File");
                //write the file to a folder path on the server
                final String odfFile = recieveOdtFile(fileName, entity);
                //get the path and name of the output xml file
                final String outputXmlFile = getOutputXmlFile(fileName);
                
                final String outputMetalexFile = getOutputMetalexFile(fileName);
                //Now start the transform
                OdtTranslate transInstance = OdtTranslate.getInstance();
                HashMap paramMap = new HashMap(){{
                	put("OdfFileURL", odfFile);
                	put("OutputMetalexFilePath", outputMetalexFile);
                    put("OutputFilePath",outputXmlFile);
                }};
                transInstance.updateParams(paramMap);
                //call the transform here
                System.out.println("trans map = " + transInstance.getParams());
                String validationErrors = transInstance.exec();
                //transform end
              //Now return a response
               //get the output AnXMl
                HashMap<String,String> outputXmlMap = new HashMap<String,String>();
                String transXml = getXmlContent(outputXmlFile);
                outputXmlMap.put("xml", transXml);
                String transMetalex = getXmlContent(outputMetalexFile);
                outputXmlMap.put("metalex", transMetalex);
                
               String responseMessage = generateResponseMessage("0", fileName, validationErrors, outputXmlMap);
               Representation returnResponse = new StringRepresentation(responseMessage,
            		   MediaType.APPLICATION_XML);
               getResponse().setEntity(returnResponse);

            } catch (Exception e) {
                log.error("acceptRepresentation : ", e);
                
            } finally {
             
            }
    }
	

	private String getXmlContent(String soutputFile) {
        File outputXml = new File (soutputFile);
        String transXml = "";
        if (outputXml.exists()) {
     	   try {
			transXml = CommonUtils.readTextFile(outputXml);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("getXmlFile", e);
		}
        }
        return transXml;
	}
	

private String generateOutputElements(HashMap<String,String> xmlout) {
	StringBuffer sbf = new StringBuffer();
	for (Iterator<String> itrXml = xmlout.keySet().iterator(); itrXml.hasNext(); ) {
		String outName = itrXml.next();
		String outXml = xmlout.get(outName);
		sbf.append("\t\t<output name=\""+ outName + "\">");
		sbf.append("\t\t\t<![CDATA[\n");
		sbf.append(outXml);
		sbf.append("\n]]>\n");
		sbf.append("\t\t</output>\n");
	}
	return sbf.toString();
}
	
	
	
	private String generateResponseMessage(String state, String sourceFile, String errors, HashMap<String,String> xmlout) {
		
		 /* <TransformerRespose>
		 * <sourceFile></sourceFile>
		 * <transformResult>
		 *  <state></state>
		 *  <!-- for state; 0 = success / -1 = output with errors / -2 = failure -->
		 *  <errors>
		 *      <![CDATA[
		 *
		 *      ]]>
		 *  </errors>
		 *  <output>
		 *      <![CDATA[
		 *
		 *      ]]>
		 * </output>
		 * </transformResult>
		 * </TransformerResponse>
		 */
		
		 return "<TransformerResponse>\n" + 
		 "\t<sourceFile>" +
		 sourceFile + 
		 "</sourceFile>\n" +
		 "\t<transformResult>\n" +
		  "\t\t<state>"  +  state + "</state>\n" +
		 	"\t\t<errors>\n" +
		  		"\t\t\t<![CDATA[" + 
		  		errors +
		     "]]>\n" +
		 "\t\t</errors>\n" +
		 generateOutputElements(xmlout) + 
		 "\t</transformResult>\n" +
		 "</TransformerResponse>\n";
		 
		
		
	}
}

/*
 * HTMLTransform.java
 *
 * Created on June 3, 2008, 4:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.ooo.transforms.loadable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.transforms.impl.BungeniDocTransform;
import org.bungeni.ooo.utils.CommonExceptionUtils;
import org.bungeni.extutils.MessageBox;
import org.bungeni.restlet.client.TransformerClient;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
/**
 *
 * @author Administrator
 */
public class AnXmlTransform extends BungeniDocTransform {
        private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(AnXmlTransform.class.getName());
        TransformerClient transformerClient ;
        private static final String TRANSFORM_OUTPUT_FILE = "transform_output.xml";

        /** Creates a new instance of HTMLTransform */
    public AnXmlTransform() {
        super();
        transformerClient = new TransformerClient();
    }

    
    
    private File convertUrlToFile(String sUrl) {
        File f = null;
        URL url = null;
        try {
            url = new URL(sUrl);
        } catch (MalformedURLException ex) {
            log.error("convertUrlToFile: "+ ex.getMessage());
            return null;
        }
        try {
            f = new File(url.toURI());
        } catch(URISyntaxException e) {
            f = new File(url.getPath());
        } finally {
            return f;
        }
    }
    
    private boolean writeOutputFile(File outputTrans)  {
        boolean bState = false;
		try 
		{
                FileInputStream fTrans = new FileInputStream(outputTrans);
                //String outputFilename = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + ooDocument.
               // MessageBox.OK(EXPORT_OUTPUT_FILE);
                FileOutputStream fOutTrans = new FileOutputStream(EXPORT_OUTPUT_FILE);
                    byte[] buf = new byte[1024];
		    int i = 0;
		    while ((i = fTrans.read(buf)) != -1) 
		    {
		            fOutTrans.write(buf, 0, i);
		    }
                     if (fTrans != null) fTrans.close();
		     if (fOutTrans != null) fOutTrans.close();
                    bState = true;
		}	
		catch (Exception ex) 
		{
                    bState = false;
                    log.error("writeOutputFile : " + ex.getMessage());
		} finally {
                    return bState;
                }
    }

    String EXPORT_OUTPUT_FILE  = "";
    public boolean transform(OOComponentHelper ooDocument) {
        boolean bState = false;
        try {
            //1 - query interface the document for the storable url
            
            File fopenDocumentFile  = null ;
            if (ooDocument.isDocumentOnDisk())  {
                String sDocUrl = ooDocument.getDocumentURL();
                //set the path to the output xml file
                
                fopenDocumentFile = convertUrlToFile(sDocUrl);
                String fullFileName = fopenDocumentFile.getName();
                String ext = fullFileName.substring(fullFileName.lastIndexOf(".")+1, fullFileName.length());
                String pref = fullFileName.substring(0, fullFileName.lastIndexOf(".")  );
                
                String defaultSavePath = BungeniEditorProperties.getEditorProperty("defaultSavePath");
                defaultSavePath = defaultSavePath.replace('/', File.separatorChar);
         
                EXPORT_OUTPUT_FILE = fopenDocumentFile.getParentFile().getPath()+File.separator + pref+ ".xml";
                //get temporary output file
                File fOut = new File(EXPORT_OUTPUT_FILE);
                File ftmpOutput = new File(fOut.getParent() + File.separator + TRANSFORM_OUTPUT_FILE );

                String pathPrefix = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + "transformer" + File.separator;
                //call the transformer server
                callTransform(fopenDocumentFile.getPath(), ftmpOutput.getPath());
                if (ftmpOutput.exists()) {
                    processOutputFile(ftmpOutput, fOut);
                    bState = true;
                }

            } else {
                MessageBox.OK("Please save the document before trying to transform it!");
                bState = false;
            }
        } catch (Exception ex) {
            log.error("transform : " + ex.getMessage());
            log.error("transform : " + CommonExceptionUtils.getStackTrace(ex));
        }
        return bState;
    }


    /**
     * API that calls the REST server ... this is run in a context class loader 
     * @param inputDocPath
     * @param tmpoutputFile
     */
    private void callTransform(String inputDocPath, String tmpoutputFile) {
        final ClassLoader savedClassLoader = Thread.currentThread().getContextClassLoader();
        FileOutputStream fostream  = null;
        try {
             Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
             if (transformerClient.isServerRunning()) {
                 transformerClient.postParams(BungeniEditorPropertiesHelper.getCurrentDocType(), "odt2akn");
                  fostream = new FileOutputStream(new File(tmpoutputFile));
                 transformerClient.postFile(inputDocPath, fostream);
             }
        } catch (Exception ex) {
            log.error("callTransform ", ex);
        } finally {
             Thread.currentThread().setContextClassLoader(savedClassLoader);
             try {
                  if (fostream != null)
                     fostream.close();
              } catch (IOException ex) {
                      log.error("callTransform : ", ex);
              }
        }
    }

    private SAXBuilder outputBuilder ;

    private String TRANSFORM_ERRORS = "/TransformerResponse/transformResult/errors";

    private String TRANSFORM_OUTPUT = "/TransformerResponse/transformResult/output";

    private String getErrors(Document xmlDoc) {
        String foundErrors = "";
        try {
            XPath errorPath = XPath.newInstance(TRANSFORM_ERRORS);
            Element errorElement = (Element) errorPath.selectSingleNode(xmlDoc);
            foundErrors =  errorElement.getValue();
            foundErrors = foundErrors.trim();
        } catch (JDOMException ex) {
            log.error("getErrors :", ex);
        }
        return foundErrors;
    }

     private String getOutput(Document xmlDoc) {
        String foundOut = "";
        try {
            XPath errorPath = XPath.newInstance(TRANSFORM_OUTPUT);
            Element outputElem = (Element) errorPath.selectSingleNode(xmlDoc);
            foundOut =  outputElem.getValue();
            foundOut = foundOut.trim();
        } catch (JDOMException ex) {
            log.error("getOutput :", ex);
        }
        return foundOut;
    }

    private void processOutputFile(File tmpoutputFile, File fOutputFile) {
        try {
            if (outputBuilder == null ) 
                        outputBuilder = new SAXBuilder(BungeniEditorProperties.SAX_PARSER_DRIVER, false);
            Document transDoc = outputBuilder.build(tmpoutputFile);
            //first get errors
            String outputErrors = getErrors(transDoc);
            //get the output file
            String outputfile = getOutput(transDoc);
            //write the output file
            FileWriter fw = new FileWriter(fOutputFile);
            BufferedWriter bOut = new BufferedWriter(fw);
            bOut.write(outputfile);
            bOut.close();
            //write the error file
            File fErrors = new File(tmpoutputFile.getParent() + File.separator + "errors.xml");
            FileWriter fwErrors = new FileWriter(fErrors);
            BufferedWriter bErr = new BufferedWriter(fwErrors);
            bErr.write(outputErrors);
            bErr.close();
        } catch (JDOMException ex) {
           log.error("processOutputFile", ex);
        } catch (IOException ex) {
           log.error("processOutputFile", ex);
        }

    }

}

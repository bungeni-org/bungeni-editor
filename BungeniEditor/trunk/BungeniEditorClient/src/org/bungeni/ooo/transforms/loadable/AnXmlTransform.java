package org.bungeni.ooo.transforms.loadable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.extutils.CommonANUtils;
import org.bungeni.extutils.CommonFileFunctions;
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
 * @author Ashok
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


   class OutputFileType {
        String fileType ;
     public OutputFileType (String fType) {
            this.fileType = fType;
        }

        public String generateFileName(String baseFileName) {
             String filePrefix =   CommonANUtils.getFilePrefix(baseFileName);
             String fileExt = CommonANUtils.getFileExt(baseFileName);
             if (fileExt.equals(fileType)) {
                 return baseFileName;
             } else
                 return filePrefix + "_" + fileType + ((fileExt.length() > 0 ) ? "." + fileExt: "");
        }
    }

    class OutputFile {
        OutputFileType outputFileType;
        String fileName;
        String fullPathToFile ;

        public OutputFile(String fullPath, String type) {
            outputFileType = new OutputFileType(type);
            fullPathToFile = fullPath;
            fileName = (new File(fullPathToFile)).getName();
        }

        public String getFullFileName(){
           return  outputFileType.generateFileName(fullPathToFile);
        }

        public File getFullFile(){
            return new File(getFullFileName());
        }
    }



    public boolean transform(OOComponentHelper ooDocument) {
        boolean bState = false;
        try {
            //1 - query interface the document for the storable url
            
            File fopenDocumentFile  = null ;
            if (ooDocument.isDocumentOnDisk())  {
                String sDocUrl = ooDocument.getDocumentURL();
                //set the path to the output xml file
                fopenDocumentFile = CommonFileFunctions.convertUrlToFile(sDocUrl);
                String fullFileName = fopenDocumentFile.getName();
                String ext = fullFileName.substring(fullFileName.lastIndexOf(".")+1, fullFileName.length());
                String pref = fullFileName.substring(0, fullFileName.lastIndexOf(".")  );
                
                EXPORT_OUTPUT_FILE = fopenDocumentFile.getParentFile().getPath()+File.separator + pref+ ".xml";
                //get temporary output file
                OutputFile outANxmlFile = new OutputFile(EXPORT_OUTPUT_FILE, "xml");
                OutputFile outMetalexFile = new OutputFile(EXPORT_OUTPUT_FILE, "metalex");
                ArrayList<OutputFile> outputFiles = new ArrayList<OutputFile>();
                outputFiles.add(outANxmlFile);
                outputFiles.add(outMetalexFile);

                // File fMetalexout = new File(outFile.getFullFileName());
                File ftmpOutput = new File(outANxmlFile.getFullFile().getParent() + File.separator + TRANSFORM_OUTPUT_FILE );

                String pathPrefix = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + File.separator + "transformer" + File.separator;
                //call the transformer server
                callTransform(fopenDocumentFile.getPath(), ftmpOutput.getPath());
                if (ftmpOutput.exists()) {
                    processOutputFile(ftmpOutput, outputFiles);
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

    private String TRANSFORM_OUTPUT = "/TransformerResponse/transformResult/output[@name='OUT_XML_TYPE']";
   
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

     private String getOutput(Document xmlDoc, String typeName) {
        String foundOut = "";
        try {
            String sTransType = TRANSFORM_OUTPUT.replace("OUT_XML_TYPE", typeName);
            XPath errorPath = XPath.newInstance(sTransType);
            Element outputElem = (Element) errorPath.selectSingleNode(xmlDoc);
            foundOut =  outputElem.getValue();
            foundOut = foundOut.trim();
        } catch (JDOMException ex) {
            log.error("getOutput :", ex);
        }
        return foundOut;
    }

    private void processOutputFile(File tmpoutputFile, ArrayList<OutputFile> outFiles) {
        try {
            if (outputBuilder == null ) 
                        outputBuilder = new SAXBuilder(BungeniEditorProperties.SAX_PARSER_DRIVER, false);
            Document transDoc = outputBuilder.build(tmpoutputFile);
            //first get errors
            String outputErrors = getErrors(transDoc);
            //get the output file
            writeOutputFiles(transDoc, outFiles);
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

    private void writeOutputFiles (Document xDoc, ArrayList<OutputFile> outDocs) {
       for (OutputFile outputFile : outDocs) {
                FileWriter fw = null;
                try {
                    fw = new FileWriter(outputFile.getFullFile());
                } catch (IOException ex) {
                    log.error("writeOutputFiles", ex);
                }
                if (fw != null) {
                    String sXmlOut = getOutput(xDoc, outputFile.outputFileType.fileType);
                    BufferedWriter bOut = new BufferedWriter(fw);
                    try {
                        bOut.write(sXmlOut);
                        bOut.close();
                    } catch (IOException ex) {
                        log.error("writeOutputFiles:write", ex);
                    }
                    }

        }

    }

}

package org.bungeni.ooo.transforms.loadable;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.db.DefaultInstanceFactory;
import org.bungeni.extutils.BungeniEditorProperties;
import org.bungeni.extutils.BungeniEditorPropertiesHelper;
import org.bungeni.extutils.CommonFileFunctions;
import org.bungeni.extutils.MessageBox;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.transforms.impl.BungeniDocTransform;
import org.bungeni.ooo.utils.CommonExceptionUtils;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

//~--- JDK imports ------------------------------------------------------------

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import org.bungeni.editor.BungeniEditorClient;
import org.bungeni.plugins.translator.OdtTranslate;

/**
 *
 * @author Ashok
 */
public class AnXmlTransform extends BungeniDocTransform {
    private static final String            TRANSFORM_OUTPUT_FILE = "transform_output.xml";
    private static org.apache.log4j.Logger log                   =
        org.apache.log4j.Logger.getLogger(AnXmlTransform.class.getName());
    String                                 EXPORT_OUTPUT_FILE    = "";
    private String                         TRANSFORM_ERRORS      = "/TransformerResponse/transformResult/errors";
    private String                         TRANSFORM_OUTPUT      =
        "/TransformerResponse/transformResult/output[@name='OUT_XML_TYPE']";
    private SAXBuilder                     outputBuilder;

    /** Creates a new instance of HTMLTransform */
    public AnXmlTransform() {
        super();
    }

    private boolean writeOutputFile(File outputTrans) {
        boolean bState = false;

        try {
            FileInputStream fTrans = new FileInputStream(outputTrans);

            // String outputFilename = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH() + ooDocument.
            // MessageBox.OK(EXPORT_OUTPUT_FILE);
            FileOutputStream fOutTrans = new FileOutputStream(EXPORT_OUTPUT_FILE);
            byte[]           buf       = new byte[1024];
            int              i         = 0;

            while ((i = fTrans.read(buf)) != -1) {
                fOutTrans.write(buf, 0, i);
            }

            if (fTrans != null) {
                fTrans.close();
            }

            if (fOutTrans != null) {
                fOutTrans.close();
            }

            bState = true;
        } catch (Exception ex) {
            bState = false;
            log.error("writeOutputFile : " + ex.getMessage());
        } finally {
            return bState;
        }
    }

    private String toOSPath(String path) {
        return path.replace("/", File.separator);
    }

    public boolean transform(OOComponentHelper ooDocument) {
        boolean bState = false;

        try {

            // 1 - query interface the document for the storable url
            File fopenDocumentFile = null;

            if (ooDocument.isDocumentOnDisk()) {
                String sDocUrl = ooDocument.getDocumentURL();

                // set the path to the output xml file
                fopenDocumentFile = CommonFileFunctions.convertUrlToFile(sDocUrl);

                String fullFileName = fopenDocumentFile.getName();
                String ext          = fullFileName.substring(fullFileName.lastIndexOf(".") + 1, fullFileName.length());
                String pref         = fullFileName.substring(0, fullFileName.lastIndexOf("."));

                final String outputDirectory = fopenDocumentFile.getParentFile().getPath() + File.separator;
                
                EXPORT_OUTPUT_FILE = outputDirectory + pref + ".xml";

                // get temporary output file
                OutputFile            outANxmlFile   = new OutputFile(EXPORT_OUTPUT_FILE, "xml");
                OutputFile            outMetalexFile = new OutputFile(EXPORT_OUTPUT_FILE, "metalex");
                ArrayList<OutputFile> outputFiles    = new ArrayList<OutputFile>();

                outputFiles.add(outANxmlFile);
                outputFiles.add(outMetalexFile);

                // File fMetalexout = new File(outFile.getFullFileName());
                
                String translatorRootFolder = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH()
                                            + File.separator + "transformer"
                                            + File.separator;

                OdtTranslate testObject = new OdtTranslate();

                HashMap paramMap = new HashMap();
		
                String currentDirectory = DefaultInstanceFactory.DEFAULT_INSTALLATION_PATH();

		String currentDocType = BungeniEditorPropertiesHelper.getCurrentDocType();

		paramMap.put("OdfFileURL", fopenDocumentFile.getPath());
		paramMap.put("OutputFilePath", outANxmlFile.getFullFile().getPath());
		paramMap.put("OutputMetalexFilePath", outMetalexFile.getFullFile().getPath());
		paramMap.put("TranslatorRootFolder", translatorRootFolder);
                //!+PIPELINE_SETTING (AH, 2011-09-20) Setting the pipeline manually is no longer required
                // This needs to be removed.
		paramMap.put("TranslatorConfigFile", this.toOSPath("configfiles/configs/config_debaterecord.xml"));
		//paramMap.put("TranslatorPipeline", this.toOSPath("metalex2akn/minixslt/debaterecord/pipeline.xsl") );
		paramMap.put("CurrentDocType", currentDocType);
		paramMap.put("CallerPanel", null);
		paramMap.put("PluginMode", "odt2akn");

                File foutput  = new File((String) paramMap.get("OutputFilePath"));
		if (foutput.exists()) {
			foutput.delete();
		}
		testObject.setParams(paramMap);
                //!+TRANSFORMER_INIT(AH, 2011-09-20) --
                // We set the context class loader for the current thread to the main 
                // class used by the editor -- otherwise Saxon attempts to use its own context
                // class-loader to initializae and discover itself which leads to exceptions because
                // there are 2 instances of Saxon loaded into memory, by setting the class loader
                // here Saxon uses the same class loader as the bungeni editor.
                ClassLoader tmp = Thread.currentThread().getContextClassLoader();
                Thread.currentThread().setContextClassLoader(BungeniEditorClient.class.getClassLoader());
                String sErrors = testObject.exec();
                 //!+TRANSFORMER_INIT(AH, 2011-09-20) -- See above
                Thread.currentThread().setContextClassLoader(tmp);
            	System.out.println("Translation Errors = \n\n" + sErrors);
                bState = true;
                /**
                File fnewout =  new File((String) paramMap.get("OutputFilePath"));

                if (ftmpOutput.exists()) {
                    processOutputFile(ftmpOutput, outputFiles);
                    bState = true;
                }**/
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


    private String getErrors(Document xmlDoc) {
        String foundErrors = "";

        try {
            XPath   errorPath    = XPath.newInstance(TRANSFORM_ERRORS);
            Element errorElement = (Element) errorPath.selectSingleNode(xmlDoc);

            foundErrors = errorElement.getValue();
            foundErrors = foundErrors.trim();
        } catch (JDOMException ex) {
            log.error("getErrors :", ex);
        }

        return foundErrors;
    }

    private String getOutput(Document xmlDoc, String typeName) {
        String foundOut = "";

        try {
            String  sTransType = TRANSFORM_OUTPUT.replace("OUT_XML_TYPE", typeName);
            XPath   errorPath  = XPath.newInstance(sTransType);
            Element outputElem = (Element) errorPath.selectSingleNode(xmlDoc);

            foundOut = outputElem.getValue();
            foundOut = foundOut.trim();
        } catch (JDOMException ex) {
            log.error("getOutput :", ex);
        }

        return foundOut;
    }

    private void processOutputFile(File tmpoutputFile, ArrayList<OutputFile> outFiles) {
        try {
            if (outputBuilder == null) {
                outputBuilder = new SAXBuilder(BungeniEditorProperties.SAX_PARSER_DRIVER, false);
            }

            FileReader     outFilereader = new FileReader(tmpoutputFile);
            BufferedReader bufReader     = new BufferedReader(outFilereader);
            Document       transDoc      = outputBuilder.build(bufReader);

            // first get errors
            String outputErrors = getErrors(transDoc);

            // get the output file
            writeOutputFiles(transDoc, outFiles);

            // write the error file
            File           fErrors  = new File(tmpoutputFile.getParent() + File.separator + "errors.xml");
            FileWriter     fwErrors = new FileWriter(fErrors);
            BufferedWriter bErr     = new BufferedWriter(fwErrors);

            bErr.write(outputErrors);
            bErr.close();
        } catch (JDOMException ex) {
            log.error("processOutputFile", ex);
        } catch (IOException ex) {
            log.error("processOutputFile", ex);
        }
    }

    private void writeOutputFiles(Document xDoc, ArrayList<OutputFile> outDocs) {
        for (OutputFile outputFile : outDocs) {
            FileWriter fw = null;

            try {
                fw = new FileWriter(outputFile.getFullFile());
            } catch (IOException ex) {
                log.error("writeOutputFiles", ex);
            }

            if (fw != null) {
                String         sXmlOut = getOutput(xDoc, outputFile.outputFileType.fileType);
                BufferedWriter bOut    = new BufferedWriter(fw);

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

package org.bungeni.translators.translator;

//~--- non-JDK imports --------------------------------------------------------

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import org.bungeni.translators.exceptions.DocumentNotFoundException;
import org.bungeni.translators.exceptions.TranslationFailedException;
import org.bungeni.translators.exceptions.TranslationToMetalexFailedException;
import org.bungeni.translators.exceptions.ValidationFailedException;
import org.bungeni.translators.exceptions.XSLTBuildingException;
import org.bungeni.translators.globalconfigurations.GlobalConfigurations;
import org.bungeni.translators.configurations.OAConfiguration;
import org.bungeni.translators.utility.dom.DOMUtility;
import org.bungeni.translators.utility.exceptionmanager.ValidationError;
import org.bungeni.translators.utility.files.FileUtility;
import org.bungeni.translators.utility.schemavalidator.SchemaValidator;
import org.bungeni.translators.utility.streams.StreamSourceUtility;
import org.bungeni.translators.utility.xslttransformer.XSLTTransformer;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;
import org.bungeni.translators.configurations.steps.OAPipelineStep;
import org.bungeni.translators.utility.runtime.Outputs;

/***

1 Odf is merged

2 Metalex translation
    1) get metalex config from TranslatorConfig_xxx.xml
    2) process the odt xml stream by applying input, replacement and output steps
    3) finally output the metalex xml

3 Build translation xslt

4 apply translation xslt on metalex output

 * @author Ashok Hariharan
 */
public class OATranslator implements org.bungeni.translators.interfaces.Translator {

    /* The instance of this Translator */
    private static OATranslator instance = null;

    /* This is the logger */
     private static org.apache.log4j.Logger logger              =
        org.apache.log4j.Logger.getLogger(OATranslator.class.getName());

    /* The configuration for the metalex translation */
    private String translatorConfigPath;

    /* The resource bundle for the messages */
    private ResourceBundle resourceBundle;

    //AH-23-06-2010 moved to translator_config
    //private String defaultPipelinePath ;

    private Boolean cachePipelineXSLT = false;

    //The source type is by default ODF
    //!+XML_SOURCE_TYPE(ah, 27-09-2011) 
    private XMLSourceFactory.XMLSourceType sourceType = XMLSourceFactory.XMLSourceType.ODF;

    /**
     * Private constructor used to create the Translator instance
     * @throws IOException
     * @throws InvalidPropertiesFormatException
     */
    private OATranslator() throws InvalidPropertiesFormatException, IOException, TranslationFailedException {

        // create the Properties object
        Properties properties           = new Properties();
        //this is the TranslatorConfig_<type>.xml file
        String     pathToPropertiesFile = GlobalConfigurations.getApplicationPathPrefix()
                                          + GlobalConfigurations.getConfigurationFilePath();

        // read the properties file
        InputStream propertiesInputStream = new FileInputStream(pathToPropertiesFile);

        // load the properties
        properties.loadFromXML(propertiesInputStream);

        // get the metalex configuration path
        this.translatorConfigPath = GlobalConfigurations.getApplicationPathPrefix()
                                 + properties.getProperty("translatorConfigPath");

        // get the path of the AKOMA NTOSO schema
        //this.akomantosoSchemaPath = GlobalConfigurations.getApplicationPathPrefix()
        //                            + properties.getProperty("akomantosoSchemaPath");

        // create the resource bundle
        this.resourceBundle = ResourceBundle.getBundle(properties.getProperty("resourceBundlePath"));

        //AH-23-06-2010 - Moved to translator_config 
        //this.defaultPipelinePath = GlobalConfigurations.getApplicationPathPrefix() + properties.getProperty("defaultPipeline");

        // check if pipeline xslt needs to be cached
        this.cachePipelineXSLT = Boolean.parseBoolean(properties.getProperty("cachePipelineXSLT"));

        logger.info("OATRANSLATOR ; translatorConfigPath :" + this.translatorConfigPath + " ;resourceBundle :" + this.resourceBundle + " ;cachePipelineXSLT : " + this.cachePipelineXSLT);
    }

    /**
     * Get the current instance of the Translator
     * @return the translator instance
     */
    public static synchronized OATranslator getInstance() {

        // if the instance is null create a new instance
        if (instance == null) {

            // create the instance
            try {
                instance = new OATranslator();
            } catch (Exception e) {
                logger.error("getInstance", e);
            }
        }

        // otherwise return the instance
        return instance;
    }

    /**
     * Prevent cloning of singleton instance
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Transforms the document at the given path using the pipeline at the given path
     * @param aDocumentPath the path of the document to translate
     * @param aPipelinePath the path of the pipeline to use for the translation
     * @return a hashmap containing handles to both the AN xml and the Metalex file ("anxml", "metalex")
     * @throws Exception
     * @throws TransformerFactoryConfigurationError
     */
    public HashMap<String, File> translate(String aDocumentPath)
            throws TransformerFactoryConfigurationError, Exception {
        HashMap<String, File> translatedFiles = new HashMap<String, File>();

        try {

            
            /***
             * First detect the input source type
             */
            if (aDocumentPath.endsWith(".odt")) {
                this.sourceType = XMLSourceFactory.XMLSourceType.ODF;
            } else {
                //if it does not end with odt set the source type to XML
                this.sourceType = XMLSourceFactory.XMLSourceType.XML;
            }

            /**
             * Get the source instance
             */
            IXMLSource sourceInstance = this.sourceType.getObjectInstance();


            /***
             * Get the appropriate input source stream
             */

            StreamSource ODFDocument = sourceInstance.getSource(aDocumentPath);

            /***
             * Get the translator configuration
             */
            OAConfiguration configuration  = this.getTranslatorConfiguration(this.translatorConfigPath);
            MetalexOutput metalexOutput = null;

            if (!configuration.verify()) {
                throw new TranslationFailedException( 
                        resourceBundle.getString("TRANSLATION_CONFIGURATION_FAIL"));
            }

            //we use a nested exception handler here to specifically catch intermediary
            //exceptions

            try {
                /**
                 * Apply input steps
                 */

                StreamSource inputStepsProcessedDoc = this.applyInputSteps(ODFDocument, configuration);

                StreamSource replaceStepsProcessedDoc = inputStepsProcessedDoc;
                if (configuration.hasReplaceSteps()) {
                    /**
                     * Apply the replace steps
                     */
                  replaceStepsProcessedDoc = this.applyReplaceSteps(inputStepsProcessedDoc, configuration);
                }
                /**
                 * Finally apply the output steps
                 */
                StreamSource outputStepsProcessedDoc = this.applyOutputSteps(replaceStepsProcessedDoc, configuration);
                /**
                 * At the end of the output steps we should have a metalex document, write it out
                 */
                metalexOutput = this.writeMetalexOutput(outputStepsProcessedDoc);
             } catch (Exception e) {
                    //(DEBUG_USEFUL)+ This is a useful catch-all point to put a break point if the
                    // translation is failing !!!
                    // get the message to print
                    String message = resourceBundle.getString("TRANSLATION_TO_METALEX_FAILED_TEXT");
                    System.out.println(message);
                    // print the message and the exception into the logger
                    logger.fatal((new TranslationToMetalexFailedException(message)).getStackTrace());
                    // RETURN null
                    return null;
                }
            
            translatedFiles.put("metalex", metalexOutput.metalexFile);

            /***
             * Build the XSLT pipeline
             */
            List<File> xsltPipes = this.buildXSLTPipeline(configuration);

            //AH-23-06-2010
            //File xslt = this.getXSLTPipeline(aPipelinePath);

            /**
             * Transform the Metalex using the built XSLT
             */
            StreamSource inputXmlStream = metalexOutput.metalexStream;
            for (File xslt : xsltPipes) {
                inputXmlStream = this.translateToAkomantoso(xslt, inputXmlStream);
             }
            StreamSource anXmlStream = inputXmlStream;

            //AH-23-06-2010
            //StreamSource anXmlStream = this.translateToAkomantoso(xslt, metalexOutput.metalexStream);

            StreamSource anXmlFinalStream = anXmlStream;
            if (configuration.hasPostXmlSteps()) {
                /***
                 * Finally call the Add namespace XSLT
                 */
                anXmlFinalStream = this.applyPostXmlSteps(anXmlStream,
                                                            configuration);
            }
            /**
             * Final Output
             */
            File fileToReturn = StreamSourceUtility.getInstance().writeToFile(anXmlFinalStream);
            translatedFiles.put("anxml", fileToReturn);

            // validate the produced document
            //AH-8-03-11 COMMENTED OUT FOR NOW UNTIL TESTED
            //SchemaValidator.getInstance().validate(fileToReturn, aDocumentPath, this.akomantosoSchemaPath);

            // write the stream to a File and return it
            // return fileToReturn;
        } catch (TransformerException e) {

            // get the message to print
            String message = resourceBundle.getString("TRANSLATION_FAILED_TEXT");

            System.out.println(message);

            // print the message and the exception into the logger
            logger.fatal((new TranslationFailedException(message)).getStackTrace());

            // RETURN null
            return null;
        } catch (SAXException e) {

            // get the message to print
            String message = resourceBundle.getString("VALIDATION_FAILED_TEXT");

            System.out.println(message);

            // print the message and the exception into the logger
            logger.fatal((new ValidationFailedException(message)).getStackTrace());

            // RETURN null
            return null;
        } catch (ParserConfigurationException e) {

            // get the message to print
            String message = resourceBundle.getString("VALIDATION_FAILED_TEXT");

            System.out.println(message);

            // print the message and the exception into the logger
            logger.fatal((new ValidationFailedException(message)).getStackTrace());

            // RETURN null
            return null;
        } catch (IOException e) {

            // get the message to print
            String message = resourceBundle.getString("IOEXCEPTION_TEXT");

            System.out.println(e.getMessage());

            // print the message and the exception into the logger
            logger.fatal((new DocumentNotFoundException(message)).getStackTrace());

            // RETURN null
            return null;
        }

        return translatedFiles;
    }


    /**
     * Provides access to the TranslatorConfig
     * @param aConfigurationPath
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws XPathExpressionException
     */
    private OAConfiguration getTranslatorConfiguration(String aConfigurationPath) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
            // get the File of the configuration
            Document configurationDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                                            FileUtility.getInstance().FileAsInputSource(aConfigurationPath));

            // create the configuration
            OAConfiguration configuration = new OAConfiguration(configurationDoc);
            return configuration;
    }


     private List<File> buildXSLTPipeline(OAConfiguration configuration) throws XPathExpressionException, SAXException, IOException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException{
            List<File> pipelines = new ArrayList<File>(0);
            List<OAPipelineStep> pipelineSteps  = configuration.getXsltPipeline();
            for (OAPipelineStep oAPipelineStep : pipelineSteps) {
                String pipeName = oAPipelineStep.getPipelineName();
                String pipeHref = oAPipelineStep.getPipelineHref();
                File xsltPipe = getXSLTPipeline(pipeName, pipeHref);
                pipelines.add(xsltPipe);
            }
            return pipelines;
     }
    /**
     * Builds the XSLT pipeline - once built , returns the cached copy
     * @param aPipelinePath
     * @return
     * @throws XPathExpressionException
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerException
     */
    private File getXSLTPipeline(String pipeName, String aPipelinePath) throws
            XPathExpressionException,
            SAXException, IOException,
            ParserConfigurationException,
            TransformerFactoryConfigurationError,
            TransformerException{
        /**
         * We cache the XSLT pipeline after building it
         */
        File xslt = null;
        String fullPipeName = pipeName + "_xslt_pipeline.xsl";
        File outputXSLT = Outputs.getInstance().File(fullPipeName);
        /**
         * Check if the cache parameter is enabled
         */
        if (this.cachePipelineXSLT && outputXSLT.exists()) {
            /*
             * Use the cached XSLT if it exists
             */
            xslt = outputXSLT;
        } else {
            //otherwise build the pipeline and return it
            xslt = this.buildXSLT(aPipelinePath);
            FileUtility.getInstance().copyFile(xslt, Outputs.getInstance().File(fullPipeName));
        }
        return xslt;
    }

    /***
     * Applys the input steps in the TranslatorConfig on the merged ODF
     * @param ODFDocument
     * @param configuration
     * @return
     * @throws TransformerFactoryConfigurationError
     * @throws Exception
     */
    public StreamSource applyInputSteps(StreamSource ODFDocument, OAConfiguration configuration)
            throws TransformerFactoryConfigurationError, Exception {
           // applies the input steps to the StreamSource of the ODF document
            StreamSource iteratedDocument = OAXSLTStepsResolver.resolve(ODFDocument,
                                                    configuration.getInputSteps());
            return iteratedDocument;
    }

    /***
     * Applies the replacement steps in the TranslatorConfig on the output of
     * the input steps
     * @param ODFDocument
     * @param configuration
     * @return
     * @throws TransformerFactoryConfigurationError
     * @throws Exception
     */
    public StreamSource applyReplaceSteps(StreamSource ODFDocument, OAConfiguration configuration)
                 throws TransformerFactoryConfigurationError, Exception {
            // applies the map steps to the StreamSource of the ODF document
            StreamSource iteratedDocument = OAReplaceStepsResolver.resolve(ODFDocument,
                                                    configuration);
            return iteratedDocument;
    }

    /***
     * Appies the output steps in the TranslatorConfig on the output of the
     * replacement steps
     * @param ODFDocument
     * @param configuration
     * @return
     * @throws TransformerFactoryConfigurationError
     * @throws Exception
     */
    public StreamSource applyOutputSteps(StreamSource ODFDocument, OAConfiguration configuration)
                 throws TransformerFactoryConfigurationError, Exception {
         // apply the OUTPUT XSLT to the StreamSource
         StreamSource resultStream = OAXSLTStepsResolver.resolve(ODFDocument,
                                                    configuration.getOutputSteps());
         return resultStream;
    }


    /***
     * 
     * @param anXmlStream
     * @param configuration
     * @return
     * @throws TransformerFactoryConfigurationError
     * @throws Exception
     */
    public StreamSource applyPostXmlSteps(StreamSource anXmlStream, OAConfiguration configuration)
             throws TransformerFactoryConfigurationError, Exception {
         // apply the OUTPUT XSLT to the StreamSource
         StreamSource resultStream = OAXSLTStepsResolver.resolve(anXmlStream,
                                                    configuration.getPostXmlSteps());
         return resultStream;
    }

    class MetalexOutput {
        StreamSource metalexStream;
        File metalexFile;

        public MetalexOutput(StreamSource ss, File mf) {
            this.metalexStream = ss;
            this.metalexFile = mf;
        }
    }


    private MetalexOutput writeMetalexOutput(StreamSource metalexDocument) throws TransformerException, IOException {
            File metalextmpFile = StreamSourceUtility.getInstance().writeToFile(metalexDocument);
            File metalexFile = FileUtility.getInstance().copyFile(metalextmpFile,
                    Outputs.getInstance().File("metalex.xml"));
            // Stream for metalex file
            StreamSource ssMetalex =
                    FileUtility.getInstance().FileAsStreamSource(metalexFile);
            return new MetalexOutput(ssMetalex, metalexFile);
    }

    public StreamSource translateToAkomantoso(File xsltFile, StreamSource metalexStream)
            throws FileNotFoundException, TransformerException, UnsupportedEncodingException{
            StreamSource    ssXslt  = FileUtility.getInstance().FileAsStreamSource(xsltFile);
            XSLTTransformer xsltTransformer = XSLTTransformer.getInstance();
            StreamSource result = xsltTransformer.transform(metalexStream, ssXslt);
            return result;
    }


    /**
     * Create and return an XSLT builded upon the instructions of the given pipeline.
     * @param aPipelinePath the pipeline upon which the XSLT will be created
     * @return a File containing the created XSLT
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws XPathExpressionException
     * @throws TransformerException
     * @throws TransformerFactoryConfigurationError
     */
    public File buildXSLT(String aPipelinePath)
            throws XPathExpressionException, SAXException, IOException, ParserConfigurationException,
                   TransformerFactoryConfigurationError, TransformerException {
        try {

            // create the XSLT document starting from the pipeline
            Document pipeline = OAPipelineResolver.getInstance().resolve(aPipelinePath);

            // write the document to a File
            File resultFile = DOMUtility.getInstance().writeToFile(pipeline);

            // return the file
            return resultFile;
        } catch (Exception e) {

            // get the message to print
            String message = resourceBundle.getString("XSLT_BUILDING_FAILED_TEXT");

            System.out.println(message);

            // print the message and the exception into the logger
            logger.fatal((new XSLTBuildingException(message)).getStackTrace());

            // RETURN null
            return null;
        }
    }

    public String getValidationErrors() {
        ArrayList<ValidationError> validationErrors = SchemaValidator.getInstance().getValidationErrors();
        StringBuilder               errorBuffer      = new StringBuilder();

        errorBuffer.append("<validationErrors>\n");

        if (validationErrors != null) {
            for (ValidationError error : validationErrors) {
                errorBuffer.append(error.getXmlString());
            }
        }

        errorBuffer.append("</validationErrors>");

        return errorBuffer.toString();
    }
}

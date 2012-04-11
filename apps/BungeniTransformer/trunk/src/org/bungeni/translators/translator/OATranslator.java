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
import org.bungeni.translators.utility.transformer.XSLTTransformer;

import org.w3c.dom.Document;

import org.xml.sax.SAXException;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;
import org.bungeni.translators.configurations.steps.OAPipelineStep;
import org.bungeni.translators.translator.XMLSourceFactory.XMLSourceType;
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
     private static org.apache.log4j.Logger log              =
        org.apache.log4j.Logger.getLogger(OATranslator.class.getName());

    /* The resource bundle for the messages */
    private ResourceBundle resourceBundle;

    //AH-23-06-2010 moved to translator_config
    //private String defaultPipelinePath ;

    private Boolean cachePipelineXSLT = false;

    //The source type is by default ODF
    //!+XML_SOURCE_TYPE(ah, 27-09-2011)
    private XMLSourceFactory.XMLSourceType sourceType = XMLSourceFactory.XMLSourceType.ODF;
    //!+INPUT_PARAMETERS (ah, nov-2011) added to support input parameters to the translator
    /**
     * This is the input parameter passed from the caller to the pipeline
     */
    private HashMap<String, String> pipelineInputParameters;

    /**
     * Private constructor used to create the Translator instance
     * @throws IOException
     * @throws InvalidPropertiesFormatException
     */
    private OATranslator() throws InvalidPropertiesFormatException, IOException, TranslationFailedException, XPathExpressionException {

        //!+TRANSLATOR_PROPERTIES(ah, oct-2011) Translator properties were moved into the main configuration
        //file -- translators are now configured via a single configurationfile
        //use OAConfiguration.getProperties() to get the propeties object of the translation
        this.pipelineInputParameters = new HashMap<String,String>();
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
                log.error("getInstance", e);
            }
        }

        // otherwise return the instance
        return instance;
    }

    private void setupConfiguration(String configurationFilePath) throws IOException, XPathExpressionException, TranslationFailedException {
        // create the Properties object
        Properties properties           = new Properties();
        //!+FIX_THIS_LATER (ah, oct-2011) The pipeline caching logic will also need
        // to take into account different input pipelines !!!
        //this is the config_<type>.xml
        //FIXED -- nothing to change here -- the default cache checking behavior is fine, its
        //checked on the specific pipeline i.e. if the output xslt exists
        String translatorConfigurationPath = GlobalConfigurations.getApplicationPathPrefix()   + configurationFilePath;
        //
        // get the translator configuration file
        //
        try {
            setupTranslatorConfiguration(translatorConfigurationPath);
        } catch (ParserConfigurationException ex) {
            log.error("Error while getting transltor configuration", ex);
        } catch (SAXException ex) {
            log.error("Error while getting transltor configuration", ex);
        } catch (XPathExpressionException ex) {
            log.error("Error while getting transltor configuration", ex);
        }

        //
        // verify the translator configurtion file
        //

        if (!OAConfiguration.getInstance().verify()) {
                throw new TranslationFailedException(
                        resourceBundle.getString("TRANSLATION_CONFIGURATION_FAIL"));
            }

        //
        // get the translation properties
        //
        properties = OAConfiguration.getInstance().getProperties();

        if (properties == null ) {
            throw new InvalidPropertiesFormatException("Invalid format for translator configuration properties");
        }

        // create the resource bundle
        this.resourceBundle = ResourceBundle.getBundle(properties.getProperty("resourceBundlePath"));

        // check if pipeline xslt needs to be cached
        this.cachePipelineXSLT = Boolean.parseBoolean(properties.getProperty("cachePipelineXSLT"));

        String strSourceType = properties.getProperty("inputXmlSource");

        this.sourceType = XMLSourceType.valueOf(strSourceType);

        log.info("OATRANSLATOR ; translatorConfigPath :" + translatorConfigurationPath + " ;resourceBundle :" + this.resourceBundle + " ;cachePipelineXSLT : " + this.cachePipelineXSLT);

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
     *
     * Translation type is autom
     *
     * @param aDocumentPath the path of the document to translate
     * @param aPipelinePath the path of the pipeline to use for the translation
     * @param inputParameters HashMap/ Dictionary of input parameters for the translator. These
     * need to be declared in the config of the pipeline in both input steps.
     * @return a hashmap containing handles to both the AN xml and the Metalex file ("anxml", "metalex")
     * @throws Exception
     * @throws TransformerFactoryConfigurationError
     */
    public HashMap<String, File> translate(
            String aDocumentPath,
            String configFilePath
            )
            throws TransformerFactoryConfigurationError, Exception {
        HashMap<String, File> translatedFiles = new HashMap<String, File>();

        try {

            /***
             * !+FIX_THIS_LATER -- the translator configuration is loaded
             * here instead of in the constructor.
             * TO BE FIXED in the EDITOR
             */
            this.setupConfiguration(configFilePath);

            /**
             * Source type is detected from configuration during object setup, not
             * done here anymore
             */

            /**
             * Get the source instance
             */
            IXMLSource sourceInstance = this.sourceType.getObjectInstance();


            /***
             * Get the appropriate input source stream
             */

            StreamSource xmlDocument = sourceInstance.getSource(aDocumentPath);

            /***
             * Get the translator configuration
             */
            OutputXML outputXML = null;


            //we use a nested exception handler here to specifically catch intermediary
            //exceptions

            try {
                /**
                 * Apply input steps
                 */

                StreamSource inputStepsProcessedDoc = this.applyInputSteps(xmlDocument);

                StreamSource replaceStepsProcessedDoc = inputStepsProcessedDoc;
                if (OAConfiguration.getInstance().hasReplaceSteps()) {
                    /**
                     * Apply the replace steps
                     */
                  replaceStepsProcessedDoc = this.applyReplaceSteps(inputStepsProcessedDoc);
                }
                /**
                 * Finally apply the output steps
                 */
                StreamSource outputStepsProcessedDoc = replaceStepsProcessedDoc;
                if (OAConfiguration.getInstance().hasOutputSteps()) {
                   outputStepsProcessedDoc = this.applyOutputSteps(replaceStepsProcessedDoc);
                }

                /**
                 * At the end of the output steps we should have a metalex document, write it out
                 */
                outputXML = this.writeOutputXML(outputStepsProcessedDoc);
             } catch (Exception e) {
                    //(DEBUG_USEFUL)+ This is a useful catch-all point to put a break point if the
                    // translation is failing !!!
                    // get the message to print
                    String message = resourceBundle.getString("TRANSLATION_TO_METALEX_FAILED_TEXT");
                    System.out.println(message);
                    // print the message and the exception into the logger
                    log.fatal((new TranslationToMetalexFailedException(message)), e);
                    // RETURN null
                    return null;
                }
            //!+FIX_THIS_LATER -- see other note on metalex.xml
            translatedFiles.put("metalex", outputXML.outputxmlFile);

            //!+PIPELINE (ah, oct-2011) -- the pipeline is non-mandatory now
            StreamSource anXmlStream  = null;
            if (OAConfiguration.getInstance().hasPipelineXML()) {
                /***
                 * Build the XSLT pipeline
                 */
                List<File> xsltPipes = this.buildXSLTPipeline();

                //AH-23-06-2010
                //File xslt = this.getXSLTPipeline(aPipelinePath);

                /**
                 * Transform the Metalex using the built XSLT
                 */
                StreamSource inputXmlStream = outputXML.outputxmlStream;
                for (File xslt : xsltPipes) {
                    inputXmlStream = this.translateToAkomantoso(xslt, inputXmlStream);
                 }
                anXmlStream = inputXmlStream;
            } else {
                anXmlStream = outputXML.outputxmlStream;
            }

            //AH-23-06-2010
            //StreamSource anXmlStream = this.translateToAkomantoso(xslt, metalexOutput.metalexStream);

            StreamSource anXmlFinalStream = anXmlStream;
            if (OAConfiguration.getInstance().hasPostXmlSteps()) {
                /***
                 * Finally call the Add namespace XSLT
                 */
                anXmlFinalStream = this.applyPostXmlSteps(anXmlStream);
            }
            /**
             * Final Output
             */
            //!+PIPELINE (ah, oct-2011) -- if there is no pipeline and no output steps then the 2 outputs
            //will be exactly the same
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
            log.fatal((new TranslationFailedException(message)).getStackTrace());

            // RETURN null
            return null;
        } catch (SAXException e) {

            // get the message to print
            String message = resourceBundle.getString("VALIDATION_FAILED_TEXT");

            System.out.println(message);

            // print the message and the exception into the logger
            log.fatal((new ValidationFailedException(message)).getStackTrace());

            // RETURN null
            return null;
        } catch (ParserConfigurationException e) {

            // get the message to print
            String message = resourceBundle.getString("VALIDATION_FAILED_TEXT");

            System.out.println(message);

            // print the message and the exception into the logger
            log.fatal((new ValidationFailedException(message)).getStackTrace());

            // RETURN null
            return null;
        } catch (IOException e) {

            // get the message to print
            String message = resourceBundle.getString("IOEXCEPTION_TEXT");

            System.out.println(e.getMessage());

            // print the message and the exception into the logger
            log.fatal((new DocumentNotFoundException(message)).getStackTrace());

            // RETURN null
            return null;
        }

        return translatedFiles;
    }


   /**
    * Alternate way of calling translate() with input XSLT parameters,
    * pass all the input parameters in the HashMap
    * @param aDocumentPath
    * @param configFilePath
    * @param inputParameters
    * @return
    * @throws TransformerFactoryConfigurationError
    * @throws Exception
    */
   public HashMap<String, File> translate(
            String aDocumentPath,
            String configFilePath,
            HashMap inputParameters
            )
            throws TransformerFactoryConfigurationError, Exception {
            this.pipelineInputParameters = inputParameters;
            return this.translate(aDocumentPath, configFilePath);
            
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
    private void setupTranslatorConfiguration(String aConfigurationPath) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
            // get the File of the configuration
            Document configurationDoc = OADocumentBuilderFactory.getInstance().getDBF().newDocumentBuilder().parse(
                                            FileUtility.getInstance().FileAsInputSource(aConfigurationPath));
            // create the configuration
            OAConfiguration configuration = OAConfiguration.getInstance();
            configuration.setConfiguration(configurationDoc);
    }


     private List<File> buildXSLTPipeline() throws XPathExpressionException, SAXException, IOException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException{
            List<File> pipelines = new ArrayList<File>(0);
            List<OAPipelineStep> pipelineSteps  = OAConfiguration.getInstance().getXsltPipeline();
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
            log.info("!!!!!!!! USING CACHED TEMPLATE !!!!!!!");
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
    public StreamSource applyInputSteps(StreamSource ODFDocument)
            throws TransformerFactoryConfigurationError, Exception {
           // applies the input steps to the StreamSource of the ODF document
            HashMap<String,Object> resolvedParameterMap = this.resolveParameterMap("input");
            StreamSource iteratedDocument = OAXSLTStepsResolver.getInstance().resolve(ODFDocument,
                                                    resolvedParameterMap,
                                                    OAConfiguration.getInstance().getInputSteps());
            return iteratedDocument;
    }

    private HashMap<String,Object> resolveParameterMap(String forStep) throws XPathExpressionException {
        //We merge the input parameter map with the parameters specified in Configuration
        HashMap<String,Object> resolvedMap = new HashMap<String,Object>();

        //first get the parameters from Config
        HashMap<String,Object> configParameters = OAConfiguration.getInstance().getParameters(forStep);

        //Validate the pipeline parameters - we cannot input parameters which are
        //not declared in the config

        //Now iterate through the input parameter map and identify parameters to merge

        for (String key : this.pipelineInputParameters.keySet()){
            //if the input parameter exists in configuration
            if (configParameters.containsKey(key)) {
               resolvedMap.put(key,this.pipelineInputParameters.get(key));
            } else {
                log.warn("WARNING !!!!: an undeclared paramter: "+ key +"  was passed as an input parameter "
                        + "This parameter will be ignored until declared in the configuraiton xml ");
            }
         }

        //Now iterate through the config parameters and identify missing ones to default
        for (String key : configParameters.keySet()) {
            if (!resolvedMap.containsKey(key)) {
                //config parameters support String values and XML nodes
                Object defaultConfigValue = configParameters.get(key);
                if (defaultConfigValue.getClass().equals(String.class)) {
                    String defaultConfigValueString = ((String)defaultConfigValue).trim();
                    if (defaultConfigValueString.isEmpty()) {
                       log.warn("WARNING !!!!: One of the default parameters : " + key + " has a empty default value in configuration ");
                    }
                resolvedMap.put(key, defaultConfigValueString);
                } else {
                    //the only other type allowed is XML 
                }
              

            }
        }

        return resolvedMap;

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
    public StreamSource applyReplaceSteps(StreamSource ODFDocument)
                 throws TransformerFactoryConfigurationError, Exception {
            // applies the map steps to the StreamSource of the ODF document
            StreamSource iteratedDocument = OAReplaceStepsResolver.resolve(ODFDocument,
                                                    OAConfiguration.getInstance());
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
    public StreamSource applyOutputSteps(StreamSource ODFDocument)
                 throws TransformerFactoryConfigurationError, Exception {
         // apply the OUTPUT XSLT to the StreamSource
        //!+FIX_THIS output steps dont process parameters -
         StreamSource resultStream = OAXSLTStepsResolver.getInstance().resolve(ODFDocument,
                                        OAConfiguration.getInstance().getOutputSteps()
                                        );
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
    public StreamSource applyPostXmlSteps(StreamSource anXmlStream)
             throws TransformerFactoryConfigurationError, Exception {
         // apply the OUTPUT XSLT to the StreamSource
         StreamSource resultStream = OAXSLTStepsResolver.getInstance().resolve(anXmlStream,
                                        OAConfiguration.getInstance().getPostXmlSteps()
                                        );
         return resultStream;
    }

    class OutputXML {
        StreamSource outputxmlStream;
        File outputxmlFile;

        public OutputXML(StreamSource ss, File mf) {
            this.outputxmlStream = ss;
            this.outputxmlFile = mf;
        }
    }


    private OutputXML writeOutputXML(StreamSource metalexDocument) throws TransformerException, IOException {
            File metalextmpFile = StreamSourceUtility.getInstance().writeToFile(metalexDocument);
            //!+FIX_THIS_LATER(ah,oct-2011) the cached intermediate outputfile has been
            //left as metalex.xml so it doesnt break the editor which expects that intermediate
            //output -- fix editor and then change this appropriately
            //File metalexFile = FileUtility.getInstance().copyFile(metalextmpFile,
            //        Outputs.getInstance().File("metalex.xml"));
            // Stream for metalex file
            //FIXED - this has been changed to the dynamic input parameter
            StreamSource ssMetalex =
                    FileUtility.getInstance().FileAsStreamSource(metalextmpFile);
            return new OutputXML(ssMetalex, metalextmpFile);
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
            log.fatal((new XSLTBuildingException(message)).getStackTrace());

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
package org.un.bungeni.translators.odttoakn.translator;

//~--- non-JDK imports --------------------------------------------------------

import org.un.bungeni.translators.exceptions.DocumentNotFoundException;
import org.un.bungeni.translators.exceptions.TranslationFailedException;
import org.un.bungeni.translators.exceptions.TranslationToMetalexFailedException;
import org.un.bungeni.translators.exceptions.ValidationFailedException;
import org.un.bungeni.translators.exceptions.XSLTBuildingException;
import org.un.bungeni.translators.globalconfigurations.GlobalConfigurations;
import org.un.bungeni.translators.odttoakn.configurations.OAConfiguration;
import org.un.bungeni.translators.utility.dom.DOMUtility;
import org.un.bungeni.translators.utility.exceptionmanager.ValidationError;
import org.un.bungeni.translators.utility.files.FileUtility;
import org.un.bungeni.translators.utility.odf.ODFUtility;
import org.un.bungeni.translators.utility.schemavalidator.SchemaValidator;
import org.un.bungeni.translators.utility.streams.StreamSourceUtility;
import org.un.bungeni.translators.utility.xslttransformer.XSLTTransformer;

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
import java.util.Properties;
import java.util.ResourceBundle;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;

public class OATranslator implements org.un.bungeni.translators.interfaces.Translator {

    /* The instance of this Translator */
    private static OATranslator instance = null;

    /* This is the logger */
    private static org.apache.log4j.Logger logger =
        org.apache.log4j.Logger.getLogger("org.un.bungeni.translators.odttoakn.translator.OATranslator");

    /* The path of the AKOMA NTOSO schema */
    private String akomantosoAddNamespaceXSLTPath;

    /* The path of the AKOMA NTOSO schema */
    private String akomantosoSchemaPath;

    /* The configuration for the metalex translation */
    private String metalexConfigPath;

    /* The resource bundle for the messages */
    private ResourceBundle resourceBundle;

    /**
     * Private constructor used to create the Translator instance
     * @throws IOException
     * @throws InvalidPropertiesFormatException
     */
    private OATranslator() throws InvalidPropertiesFormatException, IOException, TranslationFailedException {

        // create the Properties object
        Properties properties           = new Properties();
        String     pathToPropertiesFile = GlobalConfigurations.getApplicationPathPrefix()
                                          + GlobalConfigurations.getConfigurationFilePath();

        // read the properties file
        InputStream propertiesInputStream = new FileInputStream(pathToPropertiesFile);

        // load the properties
        properties.loadFromXML(propertiesInputStream);

        // get the metalex configuration path
        this.metalexConfigPath = GlobalConfigurations.getApplicationPathPrefix()
                                 + properties.getProperty("metalexConfigPath");

        // get the path of the AKOMA NTOSO schema
        this.akomantosoSchemaPath = GlobalConfigurations.getApplicationPathPrefix()
                                    + properties.getProperty("akomantosoSchemaPath");

        // get the path of the XSLT that add the namespace to AKOMA NTOSO produced files
        this.akomantosoAddNamespaceXSLTPath = GlobalConfigurations.getApplicationPathPrefix()
                + properties.getProperty("akomantosoAddNamespaceXSLTPath");

        // create the resource bundle
        this.resourceBundle = ResourceBundle.getBundle(properties.getProperty("resourceBundlePath"));
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
    public HashMap<String, File> translate(String aDocumentPath, String aPipelinePath)
            throws TransformerFactoryConfigurationError, Exception {
        HashMap<String, File> translatedFiles = new HashMap<String, File>();

        try {

            // get the document stream obtained after the merge of all the ODF XML contained in the given ODF pack
            ODFUtility odfUtil       = ODFUtility.getInstance();
            File       mergedOdfFile = odfUtil.mergeODF(aDocumentPath);

            // /// ---ASHOK--- //////
            FileUtility.getInstance().copyFile(mergedOdfFile, new File("./test4235406.xml"));

            // /// ---ASHOK--- //////

            StreamSource ODFDocument = FileUtility.getInstance().FileAsStreamSource(mergedOdfFile);

            // translate the document to METALEX
            File metalexFile = translateToMetalex(ODFDocument, this.metalexConfigPath);

            // FileUtility.getInstance().copyFile(metalexFile, new File("/Users/ashok/out.txt"));
            translatedFiles.put("metalex", metalexFile);

            // create the XSLT that transforms the metalex
            File xslt = this.buildXSLT(aPipelinePath);

            // Stream for metalex file
            StreamSource ssMetalex = FileUtility.getInstance().FileAsStreamSource(metalexFile);

            // streamsource to xslt that transforms the metalex
            StreamSource    ssXslt          = FileUtility.getInstance().FileAsStreamSource(xslt);
            XSLTTransformer xsltTransformer = XSLTTransformer.getInstance();

            // apply the XSLT to the document
            StreamSource result = xsltTransformer.transform(ssMetalex, ssXslt);

            // stream the AN xslt file
            StreamSource ssAnXsltpath =
                FileUtility.getInstance().FileAsStreamSource(this.akomantosoAddNamespaceXSLTPath);

            // apply to the result the XSLT that insert the namespace
            StreamSource resultWithNamespace = xsltTransformer.transform(result, ssAnXsltpath);

            // create the file that will be returned in case the validation do not fail
            File fileToReturn = StreamSourceUtility.getInstance().writeToFile(resultWithNamespace);

            translatedFiles.put("anxml", fileToReturn);

            // validate the produced document
            // SchemaValidator.getInstance().validate(new StreamSource(fileToReturn), this.akomantosoSchemaPath);
            SchemaValidator.getInstance().validate(fileToReturn, aDocumentPath, this.akomantosoSchemaPath);

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
     * Translate an ODF stream source to the METALEX format
     * @param ODFDocument the ODFStreamSource to translate
     * @param aConfigurationPath the path of the configuration file used for the translation
     * @return a File containing the document into the METALEX format
     * @throws TransformerFactoryConfigurationError
     * @throws Exception
     */
    public File translateToMetalex(StreamSource ODFDocument, String aConfigurationPath)
            throws TransformerFactoryConfigurationError, Exception {
        try {

            // get the File of the configuration
            Document configurationDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                                            FileUtility.getInstance().FileAsInputSource(aConfigurationPath));

            // create the configuration
            OAConfiguration configuration = new OAConfiguration(configurationDoc);

            // applies the input steps to the StreamSource of the ODF document
            StreamSource iteratedDocument = OAInputStepsResolver.resolve(ODFDocument, configuration);

            // applies the map steps to the StreamSource of the ODF document
            iteratedDocument = OAReplaceStepsResolver.resolve(iteratedDocument, configuration);

            // apply the OUTPUT XSLT to the StreamSource
            StreamSource resultStream = OAOutputStepsResolver.resolve(iteratedDocument, configuration);

            // write the source to a File
            File resultFile = StreamSourceUtility.getInstance().writeToFile(resultStream);

            // return the Source of the new document
            return resultFile;
        } catch (Exception e) {

            // get the message to print
            String message = resourceBundle.getString("TRANSLATION_TO_METALEX_FAILED_TEXT");

            System.out.println(message);

            // print the message and the exception into the logger
            logger.fatal((new TranslationToMetalexFailedException(message)).getStackTrace());

            // RETURN null
            return null;
        }
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
        StringBuffer               errorBuffer      = new StringBuffer();

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

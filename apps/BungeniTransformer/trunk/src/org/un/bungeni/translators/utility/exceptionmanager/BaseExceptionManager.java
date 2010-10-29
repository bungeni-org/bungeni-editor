package org.un.bungeni.translators.utility.exceptionmanager;

/* JDK imports */
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.ResourceBundle;
import java.util.Properties;
import java.util.regex.Pattern;

/* Non JDK imports */
import org.apache.xerces.parsers.DOMParser;
import org.un.bungeni.translators.globalconfigurations.GlobalConfigurations;
import org.un.bungeni.translators.utility.exceptionmanager.handlers.IExceptionHandler;
import org.xml.sax.SAXParseException;
/**
 *
 * @author murithi
 */
public class BaseExceptionManager implements IExceptionManager{

    /* This is the logger */
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ExceptionManager.class.getName());

    /* This is the string that will contain the path to the original ODF document */
    public String ODFDocument;

    /* This is the ODF string that contains all the section names and infos of the original document */
    public String ODFSectionString;

    /* instance of the exception manager */
    private static BaseExceptionManager instance = null;

    /* This is the DOM parser that will contain the references to the elements in witch errors occur */
    public DOMParser parser;

    /* Exception configuration file path */
    public String configurationFilePath = "";

    /* The resource bundle for the messages */
    private ResourceBundle resourceBundle;

    /**
     * Array of validation error messages
     */
    private ArrayList<ValidationError> validationErrors;

    /**
     * Private constructor used to create the BaseExceptionManager instance
     * @throws IOException
     * @throws InvalidPropertiesFormatException
     */
    private BaseExceptionManager() throws InvalidPropertiesFormatException, IOException {

        // create the Properties object
        Properties properties = new Properties();

        // read the properties file
        InputStream propertiesInputStream = new FileInputStream(GlobalConfigurations.getApplicationPathPrefix()
                                                + GlobalConfigurations.getConfigurationFilePath());

        // load the properties
        properties.loadFromXML(propertiesInputStream);
        this.validationErrors = new ArrayList<ValidationError>(0);

        // create the resource bundle
        this.resourceBundle = ResourceBundle.getBundle(properties.getProperty("resourceBundlePath"));
    }


    /**
     * Get the current instance of the BaseExceptionManager class
     * @return the ExceptionManager instance
     */
    public static synchronized BaseExceptionManager getInstance() {

        // if the instance is null create a new instance
        if (instance == null) {

            // create the instance
            try {
                instance = new BaseExceptionManager();
            } catch (Exception e) {
                logger.error("getInstance", e);
            }
        }

        // otherwise return the instance
        return instance;
    }

    /**
     * Prevent cloning of singleton
     */
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void init(){
        this.validationErrors.clear();
    }

    /*
     * Returns the configuration file path initialized during set up.
     */
    public String getConfigurationFilePath(){
        return this.configurationFilePath;
    }
    
    public void setConfigurationFilePath(String confFilePath){
        this.configurationFilePath = confFilePath;
    }

    public ArrayList<ValidationError> getValidationErrors(){
        return this.validationErrors;
    }

    public String getStartingWords(String anId){
        return null;
    }

    public void setODFDocument(String anODFDocument){
        this.ODFDocument = anODFDocument;
    }

    public void setDOMParser(DOMParser parser){
        this.parser = parser;
    }

    public void fatalError(SAXParseException ex){

    }

    public void warning(SAXParseException ex){
        this.processExceptions(ex);
    }

    public void error(SAXParseException ex){
        this.processExceptions(ex);
    }

    /*
     * This method processes exceptions by matching exceptions messages to
     * particular classes. Configuration is stored in the resources in a
     * properties file.
     */
    public void processExceptions(SAXParseException ex)
    {
        //get exception message
        String exceptionMessage = ex.getMessage();
        // property names
        Enumeration allPropertyNames = null;
        //load configuration properties
        String matchedExceptionClass = null;
        Pattern matchedPattern = null;
        ValidationError validationError = new ValidationError();
        Properties errorConfigProperties = new Properties();
        GlobalConfigurations.setApplicationPathPrefix("resources/");
        GlobalConfigurations.setConfigurationFilePath(this.getConfigurationFilePath());

        String finalConfigurationFilePath =
                GlobalConfigurations.getApplicationPathPrefix()+
                GlobalConfigurations.getConfigurationFilePath();
        try {
            InputStream configStream =
                    new FileInputStream(finalConfigurationFilePath);
            errorConfigProperties.loadFromXML(configStream);
            allPropertyNames = errorConfigProperties.propertyNames();

        } catch (IOException e) {
            //TODO log execption message
        }

        // Configuration contains keys as class names and values as regular
        // expressions matching exception messages
        // We only need to match one exception
        while(allPropertyNames.hasMoreElements()){
            String className = (String) allPropertyNames.nextElement();
            String regexValue = (String) errorConfigProperties.get(className);


            if (exceptionMessage.matches(regexValue)){
                matchedExceptionClass = className;
                matchedPattern = Pattern.compile(regexValue);
                break;
            }

        }

        if (matchedExceptionClass != null){
            try {
                Class handlerClass = Class.forName(matchedExceptionClass);            
                IExceptionHandler exceptionHandler =
                        (IExceptionHandler) handlerClass.newInstance();
                exceptionHandler.initialize(ex,
                        resourceBundle,
                        parser,
                        ODFSectionString);
                exceptionHandler.processException(validationError, matchedPattern);

            } catch (ClassNotFoundException e) {
                //TODO log exception message
            } catch (InstantiationException e){
                //TODO log exception message here
            } catch (IllegalAccessException e){
                //TODO log exception message here
            }

            if (validationError != null){
                this.validationErrors.add(validationError);
            }
        }

    }
}

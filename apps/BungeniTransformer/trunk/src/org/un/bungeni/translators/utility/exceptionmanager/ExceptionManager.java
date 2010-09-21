package org.un.bungeni.translators.utility.exceptionmanager;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.xerces.parsers.DOMParser;

import org.un.bungeni.translators.globalconfigurations.GlobalConfigurations;
import org.un.bungeni.translators.utility.odf.ODFUtility;

import org.w3c.dom.Node;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

//~--- JDK imports ------------------------------------------------------------

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This object parse a SAXEception understand what type of exception is and throws an
 * user readable error
 */
public class ExceptionManager implements ErrorHandler {

    /* The instance of this FileUtility object */
    private static ExceptionManager instance = null;

    /* This is the logger */
    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ExceptionManager.class.getName());

    /* This is the string that will contain the path to the original ODF document */
    public String ODFDocument;

    /* This is the ODF string that contains all the section names and infos of the original document */
    public String ODFSectionString;

    /* This is the DOM parser that will contain the references to the elements in witch errors occur */
    public DOMParser parser;

    /* The resource bundle for the messages */
    private ResourceBundle resourceBundle;

    /**
     * Array of validation error messages
     */
    private ArrayList<ValidationError> validationErrors;

    /**
     * Private constructor used to create the ExceptionManager instance
     * @throws IOException
     * @throws InvalidPropertiesFormatException
     */
    private ExceptionManager() throws InvalidPropertiesFormatException, IOException {

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
     * Get the current instance of the ExceptionManager class
     * @return the ExceptionManager instance
     */
    public static synchronized ExceptionManager getInstance() {

        // if the instance is null create a new instance
        if (instance == null) {

            // create the instance
            try {
                instance = new ExceptionManager();
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

    /**
     * Report a non-fatal error
     * @param ex the error condition
     */
    public void error(SAXParseException ex) {

        // the message of the exception
        String          exceptionMessage = ex.getMessage();
        ValidationError validationError  = new ValidationError();

        // check what type of text the exception launch
        if (exceptionMessage.matches("(.*)Attribute '(.*)' must appear on element '(.*)'.")) {

            // compile the regex
            Pattern p = Pattern.compile("(.*)Attribute '(.*)' must appear on element '(.*)'");

            // set the input
            Matcher m = p.matcher(exceptionMessage);

            // the attribute name
            String attribute = "";

            // the elemet name
            String element = "";

            while (m.find()) {

                // get the attribute name
                attribute = m.group(2).toString();
                validationError.setMissingAttribute(attribute);

                // get element name
                element = m.group(3).toString();
                validationError.setMissingAtributeFor(element);
            }

            // create the text of the exception
            String message = resourceBundle.getString("PROBLEM_DESCRIPTION_LEFT")
                             + resourceBundle.getString("MISSING_ATTRIBUTE_LEFT_TEXT") + attribute
                             + resourceBundle.getString("MISSING_ATTRIBUTE_CENTER_TEXT") + element
                             + resourceBundle.getString("MISSING_ATTRIBUTE_RIGHT_TEXT");

            validationError.getFullErrorMessage().add(0, message);

            try {

                /* Create the message for the section */
                String sectionId = "";

                // this will store the section name
                String sectionName = "";

                // this is the message that shows the id the name and the starting words of the found problem
                String messageId = resourceBundle.getString("VALIDATION_FAILED_TEXT") + "\n";

                // get the line and column number
                String errorLocation = ex.getLineNumber() + "-" + ex.getColumnNumber();

                validationError.setColNo(ex.getColumnNumber());
                validationError.setLineNo(ex.getLineNumber());

                // get the elements location string
                String elementsLocationString = LocationHandler.getInstance().getElementsLocation();

                // split the elements location string in lines
                String[] elementsLocationsLines = elementsLocationString.split("\n");

                // iterate the lines and find the one regarding the current element
                for (int i = 0; i < elementsLocationsLines.length; i++) {

                    // get the location of the element
                    String elementLocation = elementsLocationsLines[i].split(",")[0];

                    // if it is not the line regarding the current element continue
                    if (elementLocation.compareTo(errorLocation) != 0) {

                        // continue
                        continue;
                    }

                    // else get the section type and id
                    else {

                        // get the section type
                        sectionName = elementsLocationsLines[i].split(",")[1].split(":")[0];
                        validationError.setSectionType(sectionName);

                        // get the section id
                        sectionId = elementsLocationsLines[i].split(",")[1].split(":")[1];
                        validationError.setSectionId(sectionId);
                    }
                }

                String startingWords = getStartingWords(sectionId);

                // complete the message to show
                messageId = messageId + resourceBundle.getString("SECTION_TYPE_LEFT") + sectionName + ", "
                            + resourceBundle.getString("SECTION_ID_LEFT") + sectionId + ", "
                            + resourceBundle.getString("STARTING_WORD_TEXT_LEFT") + startingWords;
                validationError.setStartingWords(startingWords);
                validationError.getFullErrorMessage().add(0, messageId);

                /* Create the message for the parent section */

                // this will store the section id
                String sectionIdParent = "";

                // this will store the section name
                String sectionNameParent = "";

                // this is the message that shows the id the name and the starting words of the found problem
                String messageIdParent = "";

                // get the current visited node
                Node node = (Node) this.parser.getProperty("http://apache.org/xml/properties/dom/current-element-node");

                // get the name of the node
                sectionNameParent = node.getLocalName();

                if (node.getAttributes().getNamedItem("id") != null) {

                    // get the section id
                    sectionIdParent = node.getAttributes().getNamedItem("id").getNodeValue();
                }

                if (node.getAttributes().getNamedItem("name") != null) {

                    // get the section name
                    sectionNameParent = node.getAttributes().getNamedItem("name").getNodeValue();
                }

                validationError.setParentSectionType(sectionNameParent);
                validationError.setParentSection(sectionIdParent);

                // complete the message to show
                messageIdParent = messageIdParent + resourceBundle.getString("SECTION_PARENT_TYPE_LEFT")
                                  + sectionNameParent + ", " + resourceBundle.getString("SECTION_ID_LEFT")
                                  + sectionIdParent;

                // "\n" +
                // resourceBundle.getString("STARTING_WORD_TEXT_LEFT") +
                // getStartingWords(node.getAttributes().getNamedItem("id").getNodeValue());
                validationError.getFullErrorMessage().add(0, messageIdParent);

                // print the messages
                System.err.println(messageId);
                System.err.println(messageIdParent);
                System.err.println(message);
                this.validationErrors.add(validationError);
            } catch (SAXException e) {

                // print the stack trace
                e.printStackTrace();
            }
        } else if (exceptionMessage.matches("(.*)The content of element '(.*)' is not complete(.*)")) {

            // System.out.println("ALLLLOOOOOOORRRRRAAAAAAA");
            // compile the regex
            Pattern p = Pattern.compile("(.*)The content of element '(.*)' is not complete(.*)");

            // set the input
            Matcher m = p.matcher(exceptionMessage);

            // the attribute name
            String attribute = "";

            // the elemet name
            String element = "";

            while (m.find()) {

                // get the attribute name
                attribute = m.group(2).toString();
                validationError.setMissingAttribute(attribute);

                // get element name
                element = m.group(3).toString();
                validationError.setMissingAtributeFor(element);
            }

            // create the text of the exception
            String message = resourceBundle.getString("PROBLEM_DESCRIPTION_LEFT")
                             + resourceBundle.getString("MISSING_ATTRIBUTE_LEFT_TEXT") + attribute
                             + resourceBundle.getString("MISSING_ATTRIBUTE_CENTER_TEXT") + element
                             + resourceBundle.getString("MISSING_ATTRIBUTE_RIGHT_TEXT");

            validationError.getFullErrorMessage().add(0, message);

            try {

                /* Create the message for the section */
                String sectionId = "";

                // this will store the section name
                String sectionName = "";

                // this is the message that shows the id the name and the starting words of the found problem
                String messageId = resourceBundle.getString("VALIDATION_FAILED_TEXT") + "\n";

                // get the line and column number
                String errorLocation = ex.getLineNumber() + "-" + ex.getColumnNumber();

                validationError.setColNo(ex.getColumnNumber());
                validationError.setLineNo(ex.getLineNumber());

                // get the elements location string
                String elementsLocationString = LocationHandler.getInstance().getElementsLocation();

                // split the elements location string in lines
                String[] elementsLocationsLines = elementsLocationString.split("\n");

                // iterate the lines and find the one regarding the current element
                for (int i = 0; i < elementsLocationsLines.length; i++) {

                    // get the location of the element
                    String elementLocation = elementsLocationsLines[i].split(",")[0];

                    // if it is not the line regarding the current element continue
                    if (elementLocation.compareTo(errorLocation) != 0) {

                        // continue
                        continue;
                    }

                    // else get the section type and id
                    else {

                        // get the section type
                        sectionName = elementsLocationsLines[i].split(",")[1].split(":")[0];
                        validationError.setSectionType(sectionName);

                        // get the section id
                        sectionId = elementsLocationsLines[i].split(",")[1].split(":")[1];
                        validationError.setSectionId(sectionId);
                    }
                }

                String startingWords = getStartingWords(sectionId);

                // complete the message to show
                messageId = messageId + resourceBundle.getString("SECTION_TYPE_LEFT") + sectionName + ", "
                            + resourceBundle.getString("SECTION_ID_LEFT") + sectionId + ", "
                            + resourceBundle.getString("STARTING_WORD_TEXT_LEFT") + startingWords;
                validationError.setStartingWords(startingWords);
                validationError.getFullErrorMessage().add(0, messageId);

                /* Create the message for the parent section */

                // this will store the section id
                String sectionIdParent = "";

                // this will store the section name
                String sectionNameParent = "";

                // this is the message that shows the id the name and the starting words of the found problem
                String messageIdParent = "";

                // get the current visited node
                Node node = (Node) this.parser.getProperty("http://apache.org/xml/properties/dom/current-element-node");

                // get the name of the node
                sectionNameParent = node.getLocalName();

                if (node.getAttributes().getNamedItem("id") != null) {

                    // get the section id
                    sectionIdParent = node.getAttributes().getNamedItem("id").getNodeValue();
                }

                if (node.getAttributes().getNamedItem("name") != null) {

                    // get the section name
                    sectionNameParent = node.getAttributes().getNamedItem("name").getNodeValue();
                }

                validationError.setParentSectionType(sectionNameParent);
                validationError.setParentSection(sectionIdParent);

                // complete the message to show
                messageIdParent = messageIdParent + resourceBundle.getString("SECTION_PARENT_TYPE_LEFT")
                                  + sectionNameParent + ", " + resourceBundle.getString("SECTION_ID_LEFT")
                                  + sectionIdParent;

                // "\n" +
                // resourceBundle.getString("STARTING_WORD_TEXT_LEFT") +
                // getStartingWords(node.getAttributes().getNamedItem("id").getNodeValue());
                validationError.getFullErrorMessage().add(0, messageIdParent);

                // print the messages
                System.err.println(messageId);
                System.err.println(messageIdParent);
                System.err.println(message);
                this.validationErrors.add(validationError);
            } catch (SAXException e) {

                // print the stack trace
                e.printStackTrace();
            }
        } else {
            validationError.setColNo(ex.getColumnNumber());
            validationError.setLineNo(ex.getLineNumber());
            validationError.getFullErrorMessage().add(0, ex.getMessage());
            this.validationErrors.add(validationError);


        }
    }

    /**
     * Report a fatal error
     * @param ex the error condition
     */
    public void fatalError(SAXParseException ex) {

        // the message of the exception
        String exceptionMessage = ex.getMessage();

        // check what type of text the exception launch
        if (exceptionMessage.matches("(.*)Attribute '(.*)' must appear on element '(.*)'.")) {

            // compile the regex
            Pattern p = Pattern.compile("(.*)Attribute '(.*)' must appear on element '(.*)'");

            // set the input
            Matcher m = p.matcher(exceptionMessage);

            // the attribute name
            String attribute = "";

            // the elemet name
            String element = "";

            while (m.find()) {

                // get the attribute name
                attribute = m.group(2).toString();

                // get element name
                element = m.group(3).toString();
            }

            // create the text of the exception
            String message = resourceBundle.getString("MISSING_ATTRIBUTE_LEFT_TEXT") + attribute
                             + resourceBundle.getString("MISSING_ATTRIBUTE_CENTER_TEXT") + element
                             + resourceBundle.getString("MISSING_ATTRIBUTE_RIGHT_TEXT");

            System.err.println("At line " + ex.getLineNumber() + " of " + ex.getSystemId() + ':');
            System.err.println(message);

            // System.exit(0);
        } 
    }

    /**
     * Report a warning
     * @param ex the warning condition
     */
    public void warning(org.xml.sax.SAXParseException ex) {

        // the message of the exception
        String exceptionMessage = ex.getMessage();

        // check what type of text the exception launch
        if (exceptionMessage.matches("(.*)Attribute '(.*)' must appear on element '(.*)'.")) {

            // compile the regex
            Pattern p = Pattern.compile("(.*)Attribute '(.*)' must appear on element '(.*)'");

            // set the input
            Matcher m = p.matcher(exceptionMessage);

            // the attribute name
            String attribute = "";

            // the elemet name
            String element = "";

            while (m.find()) {

                // get the attribute name
                attribute = m.group(2).toString();

                // get element name
                element = m.group(3).toString();
            }

            // create the text of the exception
            String message = resourceBundle.getString("MISSING_ATTRIBUTE_LEFT_TEXT") + attribute
                             + resourceBundle.getString("MISSING_ATTRIBUTE_CENTER_TEXT") + element
                             + resourceBundle.getString("MISSING_ATTRIBUTE_RIGHT_TEXT");

            System.err.println("At line " + ex.getLineNumber() + " of " + ex.getSystemId() + ':');
            System.err.println(message);
        }
    }

    /**
     * Set the path of the original ODF document
     * @param aPathToODFDocument the path of the original ODF document
     */
    public void setODFDocument(String aPathToODFDocument) {

        // set the ODFDocument for the exception manager
        this.ODFDocument = aPathToODFDocument;

        // Create the Section Info String
        this.ODFSectionString = ODFUtility.getInstance().ExtractSection(this.ODFDocument);
    }

    /**
     * This element is used to set the DOMParser of this object
     * @param aDOMParser the DOMParser to set for this object
     */
    public void setDOMParser(DOMParser aDOMParser) {

        // set the DOM parser of the object to the given one
        this.parser = aDOMParser;
    }

    private String getStartingWords(String anId) {

        // the result that will contain the first words of the section
        String result = null;

        // get the the lines of the sections string into an array
        String[] sections = this.ODFSectionString.split("\n");

        // iterate all the line of the sections string
        for (int i = 0; i < sections.length; i++) {

            // split the line at the colons
            String[] idAndName = sections[i].split(":");

            // check if the id of the section is equal to the given one
            if (idAndName[0].compareTo(anId) == 0) {

                // assign the first words of the section to the result
                // ashok: sometimes the section is empty and does not have any content, in such cases idAndName[1] will
                // throw an outofbounds exception
                if (idAndName.length >= 2) {
                    result = idAndName[1];
                } else {
                    result = "";
                }
            }
        }

        return result;
    }

    /**
     * Clears the error message queue
     */
    public void init() {
        validationErrors.clear();
    }

    public ArrayList<ValidationError> getValidationErrors() {
        return this.validationErrors;
    }
}

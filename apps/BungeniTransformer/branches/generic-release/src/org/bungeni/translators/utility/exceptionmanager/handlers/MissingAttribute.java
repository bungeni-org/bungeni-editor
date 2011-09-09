package org.bungeni.translators.utility.exceptionmanager.handlers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bungeni.translators.utility.exceptionmanager.LocationHandler;
import org.bungeni.translators.utility.exceptionmanager.ValidationError;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author murithi
 */
public class MissingAttribute extends BaseExceptionHandler{

    public void processException(ValidationError validationError,
            Pattern pattern){
        // process missing attribute validation errors
        Matcher m = pattern.matcher(this.exception.getMessage());
        String attribute = "";
        String element = "";

        while(m.find()){
            //get attribute name name
            attribute = m.group(2).toString();
            validationError.setMissingAttribute(attribute);

            //get element with missing attribute
            element = m.group(1).toString();
            validationError.setMissingAtributeFor(element);

            // add localized messages
            validationError.getFullErrorMessage().add(0,
                    this.getLocalizedMessage(attribute, element));
            this.getSectionMessage(validationError);
        }
    }

    @Override
    public String getLocalizedMessage(String attribute, String element){
        //construct the string to return
        String localizedMessage = "";

        localizedMessage = resourceBundle.getString("PROBLEM_DESCRIPTION_LEFT")
                         + resourceBundle.getString("MISSING_ATTRIBUTE_LEFT_TEXT") + attribute
                         + resourceBundle.getString("MISSING_ATTRIBUTE_CENTER_TEXT") + element
                         + resourceBundle.getString("MISSING_ATTRIBUTE_RIGHT_TEXT");

        return localizedMessage;
    }

    public String getSectionMessage(ValidationError validationError){
        String sectionMessage = "";
        try {

            /* Create the message for the section */
            String sectionId = "";

            // this will store the section name
            String sectionName = "";

            // this is the message that shows the id the name and the starting words of the found problem
            String messageId = resourceBundle.getString("VALIDATION_FAILED_TEXT") + "\n";

            // get the line and column number
            String errorLocation = this.exception.getLineNumber() + "-" +
                    this.exception.getColumnNumber();

            validationError.setColNo(this.exception.getColumnNumber());
            validationError.setLineNo(this.exception.getLineNumber());

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
            messageId = messageId + this.resourceBundle.getString("SECTION_TYPE_LEFT") + sectionName + ", "
                        + this.resourceBundle.getString("SECTION_ID_LEFT") + sectionId + ", "
                        + this.resourceBundle.getString("STARTING_WORD_TEXT_LEFT") + startingWords;
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

               //TODO insert validation error into validationErrors array inside the manager
            //this.validationErrors.add(validationError);
        } catch (SAXException e) {

            // print the stack trace
            e.printStackTrace();
        }

        return sectionMessage;

    }

    private String getStartingWords(String anId) {

        // the result that will contain the first words of the section
        String result = null;
        String[] sections;
        
        // get the the lines of the sections string into an array
        if (this.ODFSectionString == null){
            return null;
        }else{
            sections = this.ODFSectionString.split("\n");
        }

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

}

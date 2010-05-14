package org.un.bungeni.translators.utility.exceptionmanager;

//~--- non-JDK imports --------------------------------------------------------

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;

import java.util.InvalidPropertiesFormatException;

/**
 * This object parse a SAXEception understand what type of exception is and throws an
 * user readable error
 */
public class LocationHandler extends DefaultHandler {

    /* The instance of this FileUtility object */
    private static LocationHandler instance = null;

    /* The string that will contains all the location of the elements into the document */
    private String elementsLocation = "";

    /* The locator of this element */
    private Locator locator;

    /**
     * Private constructor used to create the LocationHandler instance
     * @throws IOException
     * @throws InvalidPropertiesFormatException
     */
    private LocationHandler() throws InvalidPropertiesFormatException, IOException {}

    /**
     * Get the current instance of the LocationHandler class
     * @return the LocationHandler instance
     */
    public static LocationHandler getInstance() {

        // if the instance is null create a new instance
        if (instance == null) {

            // create the instance
            try {
                instance = new LocationHandler();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // otherwise return the instance
        return instance;
    }

    /**
     * Set the document locator
     * @param aLocator the locator that must be set
     */
    public void setDocumentLocator(Locator aLocator) {
        this.locator = aLocator;
    }

    /**
     * Get all the notification of a starting on an element and update
     * the string that will contain all the elements locations
     */
    public void startElement(String uri, String localName, String qName, Attributes at) throws SAXException {

        // this will contain the id of the element if it has one
        String elementId = "noid";

        // loop through all the element attributes
        for (int i = 0; i < at.getLength(); i++) {

            // if the element has an id set the elementId variable to its id
            if (at.getQName(i) == "id") {

                // set the elementId varialbe
                elementId = at.getValue(i);
            }
        }

        // get the location of all the elements and update the string that will contain them
        this.elementsLocation += locator.getLineNumber() + "-" + locator.getColumnNumber() + "," + qName + ":"
                                 + elementId + "\n";
    }

    /**
     * Return the string that contains all the locations of the elements into the produced
     * document.
     * @return the string that contains all the locations of the elements
     */
    public String getElementsLocation() {

        // return the string containing the location of all the element
        return this.elementsLocation;
    }
}

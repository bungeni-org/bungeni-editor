package org.bungeni.translators.utility.schemavalidator;

//~--- non-JDK imports --------------------------------------------------------

import org.bungeni.translators.exceptions.MissingAttributeException;

import org.xml.sax.SAXException;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * This is the interface for the Schema Validator object.
 * It is used to perform a validation of a document through a schema
 */
public interface SchemaValidatorInterface {

    /**
     * This method validate a document through a schema
     * @param aDocument the document to validate
     * @param aPathToODFDocument the path of the original ODF document
     * @param aSchemaPath the path of the schema that must be used for the validation
     * @throws SAXException
     * @throws IOException
     * @throws MissingAttributeException
     */
    public void validate(File aDocument, String aPathToODFDocument, String aSchemaPath)
            throws SAXException, IOException, ParserConfigurationException;
}

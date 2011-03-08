package org.bungeni.translators.utility.exceptionmanager.handlers;

/* JDK Imports */
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/* Non JDK imports */
import org.apache.xerces.parsers.DOMParser;
import org.bungeni.translators.utility.exceptionmanager.ValidationError;
import org.xml.sax.SAXParseException;

/**
 * Interface for classes implementing SAX exception handlers. Handlers
 * update a <code>ValidationError</code> instance with messages and details
 * useful for debugging. Instantiation is via an empty constructor - handlers
 * are known at runtime through configuration directives.
 *
 * @author murithi
 */

public interface IExceptionHandler {
    abstract void processException(ValidationError validationError,
            Pattern pattern);
    abstract String getLocalizedMessage(String attribute, String element);

    abstract String getSectionMessage(ValidationError validationError);

    abstract void initialize(SAXParseException ex,
            ResourceBundle resourceBundle,
            DOMParser parser,
            String ODFSectionString);
}

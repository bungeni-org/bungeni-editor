
package org.bungeni.translators.utility.exceptionmanager.handlers;

import java.util.regex.Pattern;
import org.bungeni.translators.utility.exceptionmanager.ValidationError;

/**
 * Default handler for SAX parse exceptions. Loads to exception message and
 * details of the exception into the validation error with no additional
 * messages. This is the last handler matched in the sequence.
 *
 * @author murithi
 */
public class DefaultHandler extends BaseExceptionHandler {

    public void processException(ValidationError validationError, Pattern pattern) {
        validationError.setColNo(this.exception.getColumnNumber());
        validationError.setLineNo(this.exception.getLineNumber());
        validationError.getFullErrorMessage().add(0, this.exception.getMessage());
    }

    public String getSectionMessage(ValidationError validationError) {
        throw new UnsupportedOperationException("Not implemented");
    }

}

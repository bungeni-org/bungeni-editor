package org.bungeni.extutils;

//~--- non-JDK imports --------------------------------------------------------

import org.jdom.input.SAXBuilder;

/**
 *
 * @author Ashok Hariharan
 */
public class CommonXmlUtils {
    private static SAXBuilder nonValidatingSaxBuilder = null;
    private static SAXBuilder validatingSaxBuilder    = null;

    public static SAXBuilder getNonValidatingSaxBuilder() {
        if (nonValidatingSaxBuilder == null) {
            nonValidatingSaxBuilder = new SAXBuilder(BungeniEditorProperties.SAX_PARSER_DRIVER, false);
        }

        return nonValidatingSaxBuilder;
    }

    public static SAXBuilder getValidatingSaxBuilder() {
        if (validatingSaxBuilder == null) {
            validatingSaxBuilder = new SAXBuilder(BungeniEditorProperties.SAX_PARSER_DRIVER, true);
        }

        return validatingSaxBuilder;
    }
}

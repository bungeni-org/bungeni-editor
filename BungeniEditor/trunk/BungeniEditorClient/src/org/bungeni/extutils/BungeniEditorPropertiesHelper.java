package org.bungeni.extutils;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;

/**
 *Short hand functions to access property values
 * @author Ashok Hariharan
 */
public class BungeniEditorPropertiesHelper {

    /** Creates a new instance of BungeniEditorPropertiesHelper */
    public BungeniEditorPropertiesHelper() {}

    public static String getDocumentRoot() {
        String activeDoc = BungeniEditorProperties.getEditorProperty("activeDocumentMode");
        String docRoot   = BungeniEditorProperties.getEditorProperty("root:" + activeDoc.trim());

        return docRoot;
    }

    public static String getCurrentDocType() {
        return BungeniEditorProperties.getEditorProperty("activeDocumentMode");
    }

    public static String getActiveProfile() {
        return BungeniEditorProperties.getEditorProperty("activeProfile");
    }

    public static Color getDialogBackColor() {
        String popColor  = BungeniEditorProperties.getEditorProperty("popupDialogBackColor");
        Color  backColor = Color.decode(popColor);

        return backColor;
    }
}

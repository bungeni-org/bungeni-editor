package org.bungeni.editor.config;

//~--- JDK imports ------------------------------------------------------------

import java.awt.Color;
import java.text.SimpleDateFormat;
import org.bungeni.editor.config.DocTypesReader;

/**
 *Short hand functions to access property values
 * @author Ashok Hariharan
 */
public class BungeniEditorPropertiesHelper {

    /** Creates a new instance of BungeniEditorPropertiesHelper */
    public BungeniEditorPropertiesHelper() {}

    /**
     * Returns the root section Type for a document type
     * @return
     */
    public static String getDocumentRoot() {
        String sDocRoot = DocTypesReader.getInstance().
                getRootForDocType(getCurrentDocType());
        return sDocRoot;
    }

    /**
     * Returns the name of the current doctype
     * @return
     */
    public static String getCurrentDocType() {
        return BungeniEditorProperties.getEditorProperty("activeDocumentMode");
    }

    private static SimpleDateFormat metadataDateFormat = null;

    public static SimpleDateFormat getMetadataDateFormat() {
        if (metadataDateFormat == null) {
            metadataDateFormat = new SimpleDateFormat (BungeniEditorProperties.getEditorProperty("metadataTimeFormat"));
        }
        return metadataDateFormat;
    }

    public static String getLangAlpha3Part2(){
        return BungeniEditorProperties.getEditorProperty("locale.Language.iso639-2");
    }
}

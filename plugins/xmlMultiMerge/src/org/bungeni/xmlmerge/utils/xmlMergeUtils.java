package org.bungeni.xmlmerge.utils;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 * Util functions used in the merge processing  
 * @author ashok
 */
public class xmlMergeUtils {
    public static String getFileNameFromPath(String fullPath) {
        File ffile = new File(fullPath);

        return ffile.getName();
    }

    public static String getFileNameWithoutExtension(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."));
    }

    public static String parentNodeFromAddress (String nodeAddress) {
        return nodeAddress.substring(0, nodeAddress.lastIndexOf("/"));
    }
}

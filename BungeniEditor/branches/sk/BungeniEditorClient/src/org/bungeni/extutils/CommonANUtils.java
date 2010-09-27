package org.bungeni.extutils;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 *
 * @author undesa
 */
public class CommonANUtils {
    public static File getNamedComponentFromFile(String sUrl, String namedComponent) {
        File   fFile = CommonFileFunctions.convertUrlToFile(sUrl);
        String sComp = fFile.getParent() + File.separator + namedComponent;
        File   fComp = new File(sComp);

        return fComp;
    }

    public static File getComponentFromFile(String sUrl, String sComponent) {
        File   fFile = CommonFileFunctions.convertUrlToFile(sUrl);
        String sComp = fFile.getParent() + File.separator + getFilePrefix(fFile.getName()) + "." + sComponent;
        File   fComp = new File(sComp);

        return fComp;
    }

    public static String getFilePrefix(String sFile) {
        int nIndex = sFile.lastIndexOf(".");

        if (nIndex != -1) {
            return sFile.substring(0, nIndex);
        } else {
            return sFile;
        }
    }

    public static String getFileExt(String sFile) {
        int    nIndex = sFile.lastIndexOf(".");
        String ssExt  = "";

        if (nIndex != -1) {
            ssExt = sFile.substring(nIndex + 1, sFile.length());
        }

        return ssExt;
    }
}

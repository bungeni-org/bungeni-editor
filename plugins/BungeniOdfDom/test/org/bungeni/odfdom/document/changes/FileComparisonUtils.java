package org.bungeni.odfdom.document.changes;

//~--- JDK imports ------------------------------------------------------------

import java.io.File;

/**
 *
 * @author Ashok Hariharan
 */
public class FileComparisonUtils {
    public static boolean fileEquals(String fn1, String fn2) {
        if ((new File(fn1)).length() == (new File(fn2)).length()) {
            return true;
        }

        return false;
    }
}

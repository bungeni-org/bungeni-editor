package org.bungeni.odfdom.utils;

//~--- non-JDK imports --------------------------------------------------------

import org.odftoolkit.odfdom.type.DateTime;

//~--- JDK imports ------------------------------------------------------------

import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * Provides helper functions to convert ODF date representation to a Java Date object
 * @author Ashok Hariharan
 */
public class BungeniOdfDateHelper {
    /**
     * This is the default presentation date format -- override / set as desired.
     */
    private static String PRESENTATION_DATE_FORMAT = "EEE, MMM d, yyyy, h:mm:ss a";

    /**
     * Takes an ODF date and returns it as a formatted date using the date format specified in the PRESENTATION_DATE_FORMAT static variable.
     * @param odfDate - String with ODF date
     * @return String with a formatted date
     */
    public static String odfDateToPresentationDate(String odfDate) {
        DateTime         dtdcDate = DateTime.valueOf(odfDate);
        Date             ddcDate  = dtdcDate.getXMLGregorianCalendar().toGregorianCalendar().getTime();
        SimpleDateFormat dfFormat = new SimpleDateFormat(PRESENTATION_DATE_FORMAT);

        return dfFormat.format(ddcDate);
    }
}

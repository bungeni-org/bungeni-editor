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
    public static String PRESENTATION_DATE_FORMAT = "EEE, MMM d, yyyy, h:mm:ss a";
    public static String DEFAULT_JAVA_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniOdfDateHelper.class.getName());

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

    /**
     * Converts an ODF Date to a Java Date type
     * @param odfDate - odfDate as a string
     * @return a Java Date
     */
    public static Date odfDateToJavaDate(String odfDate) {
        Date dtPresDate = null;
        DateTime         dtdcDate = DateTime.valueOf(odfDate);
        dtPresDate  = dtdcDate.getXMLGregorianCalendar().toGregorianCalendar().getTime();
        return dtPresDate;
   }

    public static String odfDateToFormattedJavaDate(String odfDate) {
       Date dtDate = odfDateToJavaDate(odfDate);
       SimpleDateFormat sdfformat = new SimpleDateFormat(DEFAULT_JAVA_DATE_FORMAT);
       return sdfformat.format(dtDate);
    }

}

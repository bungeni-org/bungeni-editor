package org.bungeni.odfdom.utils;

//~--- non-JDK imports --------------------------------------------------------

import java.text.ParseException;
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

    public static Date presentationDateToDate(String presDate) {
        Date dtPresDate = null;
        try {
            SimpleDateFormat dfFormat = new SimpleDateFormat(PRESENTATION_DATE_FORMAT);
            dtPresDate = dfFormat.parse(presDate);
        } catch (ParseException ex) {
           log.error(ex.getMessage());
        }
        return dtPresDate;

    }

}

package org.bungeni.odfdom.utils;

//~--- non-JDK imports --------------------------------------------------------

import org.odftoolkit.odfdom.type.DateTime;

//~--- JDK imports ------------------------------------------------------------

import java.text.SimpleDateFormat;

import java.util.Date;

/**
 *
 * @author Ashok
 */
public class BungeniOdfDateHelper {
    private static String PRESENTATION_DATE_FORMAT = "EEE, MMM d, yyyy, h:mm:ss a";

    public static String odfDateToPresentationDate(String odfDate) {
        DateTime         dtdcDate = DateTime.valueOf(odfDate);
        Date             ddcDate  = dtdcDate.getXMLGregorianCalendar().toGregorianCalendar().getTime();
        SimpleDateFormat dfFormat = new SimpleDateFormat(PRESENTATION_DATE_FORMAT);

        return dfFormat.format(ddcDate);
    }
}

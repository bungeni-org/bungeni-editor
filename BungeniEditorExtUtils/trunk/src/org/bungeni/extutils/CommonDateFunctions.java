
package org.bungeni.extutils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;


/**
 * Common date util functions
 * @author Ashok Hariharan
 */
public class CommonDateFunctions {

    private static Logger log = Logger.getLogger(CommonDateFunctions.class.getName());
    public static Date parseDate(String dateString, String dateFormat) {
        Date output = null;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            output = sdf.parse(dateString);
        } catch (ParseException ex) {
            log.error("error parsing date : " + dateString + " format = " + dateFormat , ex);
        }
        return output;
    }

}

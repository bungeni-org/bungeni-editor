package org.bungeni.extutils;

import java.awt.Component;
import java.util.Date;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXDatePicker;

/**
 *
 * @author Ashok Hariharan
 */
public class CommonFormFunctions {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CommonFormFunctions.class.getName());


    public static boolean validateField (Component field, String fieldName) {
        if (field.getClass().getName().equals(JTextField.class.getName())) {
            return validateField((JTextField) field, fieldName);
        }
        if (field.getClass().getName().equals(JXDatePicker.class.getName())) {
            return validateField((JXDatePicker) field, fieldName);
        }
        if (field.getClass().getName().equals(JSpinner.class.getName())) {
            return validateField((JSpinner) field, fieldName);
        }

        return false;
    }

    public static boolean validateField (JTextField field, String fieldName) {
        String fieldText = field.getText();
        if (fieldText.trim().length() == 0 ) {
            return false;
        }
        return true;
    }

    public static boolean validateField (JSpinner field, String fieldName) {
        boolean bState  =false;
        try {
               Object timeValue = field.getValue();
               Date hansardTime = (Date) timeValue;
               bState = true;
        } catch (Exception ex) {
            log.error("validateField (JSpinner) : " + ex.getMessage());
        } finally {
            return bState;
        }
    }



   public static boolean validateField (JXDatePicker field, String fieldName) {
        Date fieldText = null;
        boolean bState = false;
        try {
            Date foundDate = field.getDate();
            if (foundDate != null)
                bState = true;
        } catch (Exception ex) {
            log.error("validateField :JXDatePicker ::  " + ex.getMessage());
        } finally {
            return bState;
        }
   }


}

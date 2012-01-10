/*
 * sectionExists.java
 *
 * Created on January 26, 2008, 10:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar.conditions.runnable;

import com.sun.star.beans.XPropertySet;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.text.XTextField;
import org.bungeni.editor.toolbar.conditions.BungeniToolbarCondition;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;

/**
 * 
 * Contextual evaluator that checks if a field exists in a document.
 * e.g. fieldExists:field_name
 * will evaluate to true if the field_name exists in the document
 * will evaluate to false if the field_name does not exist in the document
 * @author Administrator
 */
public class fieldExists extends baseRunnableCondition {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(fieldExists.class.getName());
    /** Creates a new instance of sectionExists */
    public fieldExists() {
    }

    boolean check_fieldExists (OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        boolean bReturn = false;
        try {
        String fieldName  =  condition.getConditionValue();
        XEnumerationAccess fieldAccess = ooDocument.getTextFields();
        if (!fieldAccess.hasElements()) {
            //field does not exist
            bReturn = false;
        } else {
            //field may exist
            XEnumeration enumFields = fieldAccess.createEnumeration();
            while (enumFields.hasMoreElements()) {
                Object fieldObject = enumFields.nextElement();
                //XTextField xField = ooQueryInterface.XTextField(fieldObject);
                XServiceInfo xService = ooQueryInterface.XServiceInfo(fieldObject);
                if (xService.supportsService("com.sun.star.text.TextField.JumpEdit")){
                    XTextField xField = ooQueryInterface.XTextField(fieldObject);
                    XPropertySet xSet = ooQueryInterface.XPropertySet(xField );
                    if (xSet.getPropertySetInfo().hasPropertyByName("Hint")) {
                        String hintName = (String) xSet.getPropertyValue("Hint");
                        if (fieldName.equals(hintName))
                            bReturn = true;
                        else
                            bReturn = false;
                    }
                }
            }
        }
        } catch (NoSuchElementException ex) {
            log.error("check_fieldExists:" + ex.getMessage());
            bReturn = false;
        }  finally {
            return bReturn;
        }
    }
    

    @Override
    public boolean runCondition(OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
      return check_fieldExists(ooDocument, condition);
    }
        
  


 }

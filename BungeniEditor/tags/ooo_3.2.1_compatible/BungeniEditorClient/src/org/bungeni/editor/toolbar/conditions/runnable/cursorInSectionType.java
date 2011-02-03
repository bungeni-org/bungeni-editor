/*
 * cursorInSectionType.java
 *
 * Created on June 4, 2008, 1:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.toolbar.conditions.runnable;

import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNamed;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextSection;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.uno.Any;
import org.bungeni.editor.toolbar.conditions.BungeniToolbarCondition;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;

/**
 *
 * @author Administrator
 */
public class cursorInSectionType extends baseRunnableCondition {
      private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(cursorInSectionType.class.getName());
 
    /** Creates a new instance of cursorInSectionType */
    public cursorInSectionType() {
    }

    
   private boolean check_condition(String foundSectionType, String matchedSectionType) {
           // log.debug("check_condition : sectionCurrent, sectionCheck : " + sectionCurrent + ", " + sectionCheck);
             if (foundSectionType.matches(matchedSectionType)) 
                     return true;
             else
                     return false;
    }
        
   boolean check_cursorInSectionType (OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        boolean bReturn = true;
        try {
        String sectionTypeToActUpon =  condition.getConditionValue();
        XTextViewCursor viewCursor = ooDocument.getViewCursor();
        XPropertySet loXPropertySet = ooQueryInterface.XPropertySet(viewCursor);
        XTextSection matchedSection = (XTextSection)((Any)loXPropertySet.getPropertyValue("TextSection")).getObject();
        if (matchedSection != null){
                //cursor is indeed inside a section
                //check if cursor is collapsed
                if (viewCursor.isCollapsed()) {
                        String sectionType = ooDocument.getSectionType(matchedSection);
                        if (sectionType != null)
                            bReturn = check_condition(sectionType, sectionTypeToActUpon);
                        else
                            bReturn = false;
                    //evaluate condition immeediately
                } else {
                    //get start range
                    XTextRange rangeStart = viewCursor.getStart();
                    //get end range
                    XTextRange rangeEnd = viewCursor.getEnd();
                    String strSectRangeStart = "", strSectRangeEnd = "";
                    strSectRangeStart =  this.getSectionFromRange(rangeStart);
                    strSectRangeEnd = this.getSectionFromRange(rangeEnd);
                    if (strSectRangeStart.equals(strSectRangeEnd))  {
                       // log.debug("check_cursorInSection: start and end range sections are equal");
                        //evaluate condition here
                        String foundSectionType = ooDocument.getSectionType(strSectRangeStart);
                        if (foundSectionType != null)
                            bReturn = check_condition(foundSectionType, sectionTypeToActUpon);
                        else
                            bReturn = false;
                    } else {
                        //log.debug("check_cursorInSection: start and end range sections are NOT equal");
                        //fail condition if the starting span section is not equal to the ending span section
                        bReturn = false;
                    }
                }
         } else  {
             //log.debug("check_cursorInSection: matchedSection was null");
             bReturn = false;
         }
            
        } catch (UnknownPropertyException ex) {
            log.error("check_cursorInSection: " + ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error("check_cursorInSection: " + ex.getMessage());
        } finally {
            return bReturn;
        }
    }
    
   
     private String getSectionFromRange(XTextRange theRange ) {
        String theSectionName = ""; 
        try {
            log.debug("cursorInSection:getSectionFromRange()");
            XPropertySet rangeProps= ooQueryInterface.XPropertySet(theRange);
            XTextSection sectionInRange;
            sectionInRange = (XTextSection) ((Any) rangeProps.getPropertyValue("TextSection")).getObject();
            if ( sectionInRange != null) {
                 XNamed nameOfSection = ooQueryInterface.XNamed(sectionInRange);
                 theSectionName = nameOfSection.getName();       
                 log.debug("cursorInSection:getSectionFromRange(), sectionRange is not null =  theSectionName = "+ theSectionName);
            } else {
                 log.debug("cursorInSection:getSectionFromRange(), sectionRange is null ");
            }
        } catch (UnknownPropertyException ex) {
            log.error("getSectionFromRange: " + ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error("getSectionFromRange: " + ex.getMessage());
        } finally {
            return theSectionName;
        }
    }
     

    @Override
    public boolean runCondition(OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
         return check_cursorInSectionType(ooDocument, condition);
    }
    
}

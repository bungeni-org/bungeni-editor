package org.bungeni.editor.toolbar.conditions.runnable;

import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNamed;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextSection;
import com.sun.star.text.XTextViewCursor;
import com.sun.star.uno.Any;
import org.bungeni.editor.config.BungeniEditorProperties;
import org.bungeni.editor.config.BungeniEditorPropertiesHelper;
import org.bungeni.editor.config.DocTypesReader;
import org.bungeni.editor.toolbar.conditions.BungeniToolbarCondition;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;

/**
 * 
 * Contextual evaluator that checks if the cursor is in a particular section in the document.
 * e.g. cursorInSection:section_name
 * will evaluate to true if the cursor is placed in section called section_name
 * will evaluate to false if the cursor is placed in section not called section_name
 * will evaluate to false if the cursor is not placed in a section.
 * @author Ashok Hariharan
 */
public class cursorInSection extends baseRunnableCondition {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(cursorInSection.class.getName());

    /** Creates a new instance of sectionExists */
    public cursorInSection() {
    }


    private String getSectionFromRange(XTextRange theRange) {
        String theSectionName = "";
        try {
            //log.debug("cursorInSection:getSectionFromRange()");
            XPropertySet rangeProps = ooQueryInterface.XPropertySet(theRange);
            XTextSection sectionInRange;
            sectionInRange = (XTextSection) ((Any) rangeProps.getPropertyValue("TextSection")).getObject();
            if (sectionInRange != null) {
                XNamed nameOfSection = ooQueryInterface.XNamed(sectionInRange);
                theSectionName = nameOfSection.getName();
                //log.debug("cursorInSection:getSectionFromRange(), sectionRange is not null =  theSectionName = " + theSectionName);
            } else {
                //log.debug("cursorInSection:getSectionFromRange(), sectionRange is null ");
            }
        } catch (UnknownPropertyException ex) {
            log.error("getSectionFromRange: " + ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error("getSectionFromRange: " + ex.getMessage());
        } finally {
            return theSectionName;
        }
    }

    private boolean check_condition(String sectionCurrent, String sectionCheck) {
        log.debug("check_condition : sectionCurrent, sectionCheck : " + sectionCurrent + ", " + sectionCheck);
        if (sectionCurrent.matches(sectionCheck)) {
            //System.out.println("true:condition_check : c:" + sectionCurrent + ", check:" + sectionCheck);
            return true;
        } else {
            //System.out.println("false:condition_check : c:" + sectionCurrent + ", check:" + sectionCheck);
            return false;
        }
    }

    boolean check_cursorInSection(OOComponentHelper ooDocument, String condition) throws ConditionFailureException {
        boolean bReturn = true;
        ConditionFailureException cex = null;
        try {
            String sectionToActUpon = condition;
            if (sectionToActUpon.equals(BungeniEditorPropertiesHelper.getDocumentRoot())) {
                String activeDoc = BungeniEditorProperties.getEditorProperty("activeDocumentMode");
                sectionToActUpon = DocTypesReader.getInstance().getRootForDocType(activeDoc);
            }
            XTextViewCursor viewCursor = ooDocument.getViewCursor();
            XPropertySet loXPropertySet = ooQueryInterface.XPropertySet(viewCursor);
            XTextSection matchedSection = (XTextSection) ((Any) loXPropertySet.getPropertyValue("TextSection")).getObject();
            if (matchedSection != null) {
                //log.debug("check_cursorInSection: matchedSection was not null");
                //cursor is indeed inside a section
                //check if cursor is collapsed
                if (viewCursor.isCollapsed()) {
                    //log.debug("check_cursorInSection: viewCursor is collapsed");
                    String matchedSectionName = ooQueryInterface.XNamed(matchedSection).getName();
                    bReturn = check_condition(matchedSectionName, sectionToActUpon);
                    //evaluate condition immeediately
                } else {
                    //get start range
                    XTextRange rangeStart = viewCursor.getStart();
                    //get end range
                    XTextRange rangeEnd = viewCursor.getEnd();
                    String strSectRangeStart = "", strSectRangeEnd = "";
                    strSectRangeStart = this.getSectionFromRange(rangeStart);
                    strSectRangeEnd = this.getSectionFromRange(rangeEnd);
                    if (strSectRangeStart.equals(strSectRangeEnd)) {
                        //log.debug("check_cursorInSection: start and end range sections are equal");
                        //evaluate condition here
                        bReturn = check_condition(strSectRangeStart, sectionToActUpon);
                    } else {
                        //log.debug("check_cursorInSection: start and end range sections are NOT equal");
                        //fail condition if the starting span section is not equal to the ending span section
                        bReturn = false;
                        //System.out.println("Throwing conditionfailureexception !!!!");
                        //we throw a conditional failure exception to terminate the evaluation
                        throw new ConditionFailureException("check_cursorInSection: start and end range sections are NOT equal");
                    }
                }
            } else {
                //log.debug("check_cursorInSection: matchedSection was null");
                bReturn = false;
            }
        } catch (ConditionFailureException cx) {
            //System.out.println("Catching condition failure exception");
            //cache the exception object
            cex = cx;
        } catch (UnknownPropertyException ex) {
            log.error("check_cursorInSection: " + ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error("check_cursorInSection: " + ex.getMessage());
        } finally {
            //if no error was thrown return safely
            if (cex == null) {
                return bReturn;
            } else {
                //otherwise throw the exception again
                //System.out.println("Throwing condition exception failure again");
                throw new ConditionFailureException("calling exception handler parent");
            }
        }
    }

    /**
     * We dont catch any exceptions here, simply throw it to the caller (ProcessCondition)
     * @param ooDocument
     * @param condition
     * @return
     * @throws org.bungeni.editor.toolbar.conditions.runnable.ConditionFailureException
     */
    public boolean runCondition(OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        boolean bState = false;
        try {
            String strValue = condition.getConditionValue();
            String[] conditions = strValue.split(",");
            Boolean[] arrBooleans = new Boolean[conditions.length];
            for (int i=0; i < conditions.length ; i++ ) {
                arrBooleans[i] = check_cursorInSection(ooDocument, conditions[i]);
                if (arrBooleans[i] == true) {
                    bState = true;
                    break;
                }
            }
            //bState = check_cursorInSection(ooDocument, condition);
        } catch (ConditionFailureException ex) {
            log.error("cursorInSection failed ", ex);
        }
        return bState;
    }
}

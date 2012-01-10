package org.bungeni.editor.toolbar.conditions.runnable;

import com.sun.star.container.XNamed;
import com.sun.star.text.XTextSection;
import org.bungeni.editor.toolbar.conditions.BungeniToolbarCondition;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;

/**
 * 
 *Check if section has a child section of a particular names
 * @author Ashok Hariharan
 */
public class sectionHasChild extends baseRunnableCondition {
    /** Creates a new instance of sectionExists */
    public sectionHasChild() {
    }

    synchronized boolean check_sectionHasChild (OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        boolean hasChild = false;
        String childSection =  condition.getConditionValue();
        XTextSection currentSection = ooDocument.currentSection();
        if (currentSection != null ) {
            XTextSection[] childSections = currentSection.getChildSections();
            for (XTextSection xTextSection : childSections) {
                XNamed xsecName = ooQueryInterface.XNamed(xTextSection);
                String secName = xsecName.getName();
                if (secName.matches(childSection)) {
                    hasChild = true;
                }
            }
        }
        return hasChild ;
    }

    @Override
    public boolean runCondition(OOComponentHelper ooDocument, BungeniToolbarCondition condition) {
        return check_sectionHasChild (ooDocument, condition);
    }
 }

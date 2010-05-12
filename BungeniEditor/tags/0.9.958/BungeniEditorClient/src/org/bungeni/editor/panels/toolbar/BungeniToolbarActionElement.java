package org.bungeni.editor.panels.toolbar;

import org.bungeni.editor.selectors.SelectorDialogModes;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * Wrapper class for abstracting a toolbar <action> or <subaction> element
 * e.g. "
 * <action name="Conclusion.create" title="Mark as Conclusion" mode="TEXT_SELECTED_INSERT" target="toolbarSubAction.makeConclusionSection.section_creation" visible="true" condition="cursorInSection:debaterecord :and: textSelected:true" />
 * @author Ashok Hariharan
 */
public class BungeniToolbarActionElement {
    /**
     * The element being abstracted
     */
    private Element actionElement = null;

    public BungeniToolbarActionElement(Element actionElem) {
        actionElement = actionElem;
    }

    private String getNullableAttributeValue (String attrName) {
        Attribute attr = actionElement.getAttribute(attrName);
        if (attr == null) {
            return new String("");
        } else
            return attr.getValue().trim();
    }
    
    public String getName() {
        return getNullableAttributeValue("name");
    }

    public String getTitle() {
        return getNullableAttributeValue("title");
    }

    public String getTarget() {
        //used by BungeniToolbarTargetProcessor
         return getNullableAttributeValue("target");
    }

    public SelectorDialogModes getMode() {
         String smode =  getNullableAttributeValue("mode");
         return SelectorDialogModes.valueOf(smode);
    }

    public String getCondition() {
          String scondition = getNullableAttributeValue("condition");
          return scondition;
    }

    public boolean getVisible() {
        Attribute visiAttrib = actionElement.getAttribute("visible");
        if (visiAttrib == null)
            return true;
        else  {
            String sValue = visiAttrib.getValue().trim();
            Boolean bValue = Boolean.parseBoolean(sValue);
            return bValue;
        }
    }

}

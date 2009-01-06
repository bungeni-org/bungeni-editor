/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.utils;

import com.sun.star.text.XTextSection;
import java.util.HashMap;
import org.bungeni.editor.BungeniEditorPropertiesHelper;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public class NodeDisplayTextSetter implements INodeSetterCallback {
    private OOComponentHelper ooDocument;
    private String setterName = "NodeDisplayTextSetter";
    
    public NodeDisplayTextSetter (OOComponentHelper ooDoc) {
        //setterName = name;
        ooDocument = ooDoc;
    }
    public void nodeSetter(BungeniBNode aNode) {
        aNode.setDisplayText(getText(aNode.getName(), ooDocument));
    }

      private String getText(String sectionName, OOComponentHelper ooDoc){
        
                boolean numberedHeading = false;
                XTextSection aSection = ooDoc.getSection(sectionName);
                HashMap<String,String> sectionMeta  = ooDoc.getSectionMetadataAttributes(aSection);
                String sectionType = "";
                if (sectionMeta.containsKey("BungeniSectionType")){
                    sectionType = sectionMeta.get("BungeniSectionType");
                    if (sectionType.equals("NumberedContainer")) {
                        numberedHeading = true;
                        //nodeName = aSection.getAnchor().getString();
                    } /* else {
                        nodeName = aNode.getName();
                    }*/
                    
                } /*else {
                  nodeName = aNode.getName();  
                }*/
                String dispText = "";
                if (sectionName.equals(BungeniEditorPropertiesHelper.getDocumentRoot())) {
                    dispText = BungeniEditorPropertiesHelper.getDocumentRoot();
                } else {
                    sectionType = CommonResourceBundleHelperFunctions.getSectionTypeMetaString(sectionType);
                    dispText = aSection.getAnchor().getString();
                    dispText = (dispText == null ) ? "" : dispText;
                    dispText =  (dispText.length() > 15) ? dispText.substring(0,14) : dispText;
                    dispText = (dispText.length() == 0) ? sectionName : dispText;
                    dispText = "<font color=green>"+dispText.replaceAll("\\>|\\<|~", "")+"</font>";
                    dispText = (sectionType.length() != 0) ? "<i>"+sectionType+"</i><span> - </span>"+dispText: dispText;
                   // dispText = (sectionType.length() != 0) ? sectionType+"-"+dispText: dispText;
                   // dispText = (sectionType.length() != 0) ? sectionType : dispText;
                    
                    //dispText = dispText + "..";
                }    
                    return "<html>"+dispText+"</html>";
        }
        

    public void setName(String name) {
        setterName = name;
    }

    public String getName() {
        return setterName;
    }
}

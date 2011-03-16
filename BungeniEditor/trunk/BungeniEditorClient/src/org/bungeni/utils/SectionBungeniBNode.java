package org.bungeni.utils;

import com.sun.star.text.XTextSection;
import java.util.HashMap;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public class SectionBungeniBNode extends BungeniBNode {
    
    public SectionBungeniBNode (String name, Object nodeObj, OOComponentHelper ooDocument) {
        super(name, nodeObj);
        setDisplayText(getText(name, ooDocument));
    }
    
    public SectionBungeniBNode (String name, OOComponentHelper ooDocument) {
        super(name);
        setDisplayText(getText(name, ooDocument));
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
                    String dispText = aSection.getAnchor().getString();
                    dispText = (dispText == null ) ? "" : dispText;
                    dispText =  (dispText.length() > 15) ? dispText.substring(0,14) : dispText;
                    dispText = (dispText.length() == 0) ? sectionName : dispText;
                    dispText = (sectionType.length() != 0) ? sectionType+"-"+dispText: dispText;
                    
                    dispText = dispText + "..";
                    
                    return dispText;
        }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.actions.routers;

import com.sun.star.beans.UnknownPropertyException;
import com.sun.star.beans.XPropertySet;
import com.sun.star.beans.XPropertySetInfo;
import com.sun.star.container.NoSuchElementException;
import com.sun.star.container.XEnumeration;
import com.sun.star.container.XEnumerationAccess;
import com.sun.star.container.XNameAccess;
import com.sun.star.container.XNamed;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.WrappedTargetException;
import com.sun.star.lang.XServiceInfo;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextSection;
import com.sun.star.uno.AnyConverter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;
import org.bungeni.extutils.CommonDocumentUtilFunctions;

/**
 *
 * @author undesa
 */
public class routerCreateHeadingLock  extends defaultRouter {
  
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(routerCreateHeadingLock.class.getName());
 
    XTextRange beginRange = null, endRange = null;
    String fromRangeName = "", toRangeName = "", foundSectionType = "";
    /** Creates a new instance of routerCreateSection */
    public routerCreateHeadingLock() {
        super();
        
    }
    
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction, javax.swing.JFrame pFrame,OOComponentHelper ooDocument) {
      //the heading lock is always from a source reference to a target referece
      // the source and target references are availabel in the subAction's value parameter
      String referenceRange = subAction.action_value();
      parseActionValue(referenceRange);
      //String[] fromToRanges = referenceRange.split(",");
      markSectionBetweenRanges(ooDocument);
      return new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
     /* boolean bState = ooDocument.setSelectedTextStyle(styleName);
      if (bState)
          return new BungeniValidatorState(true, new BungeniMsg("SUCCESS")); 
      else    
          return new BungeniValidatorState(true, new BungeniMsg("APPLY_STYLE_FAILURE")); */

    }

    private void parseActionValue (String actionValue) {
        //e.g from(A,B);with(C)
        //e.g. from(A);with(C)
        String[] separateFromWith = actionValue.split(";");
        String fromToRange = separateFromWith[0];
        String withSecType = separateFromWith[1];
        parseFromToRanges(fromToRange);
        parseSecType(withSecType);
    }
    
    private void parseSecType(String withSecType) {
        Pattern p = Pattern.compile("with\\(([a-zA-Z]+)\\)");
        Matcher m = p.matcher(withSecType);
        if (m.find()) {
            foundSectionType = m.group(1);
        }
    }
    
    private void parseFromToRanges(String strFromToRanges) {
        Pattern p = Pattern.compile("from\\(([a-zA-Z]+),([a-zA-Z]+)\\)");
        Matcher m = p.matcher(strFromToRanges);
        if (m.find()) {
            //group (0) is always the full match 
            fromRangeName = m.group(1);
            toRangeName = m.group(2);
        } 
    }

    private void browsePortion (XEnumeration xPortionEnum, String fromRef, String toRef) {
       try {
        while (xPortionEnum.hasMoreElements()) {
            Object objPortion  = xPortionEnum.nextElement();
            XPropertySet xPortionProps = ooQueryInterface.XPropertySet(objPortion);
            XPropertySetInfo xPortionPropsInfo = xPortionProps.getPropertySetInfo();
             //look for the textportiontype = referencemark
            if (xPortionPropsInfo.hasPropertyByName("TextPortionType")) {
                  String strPortionType = AnyConverter.toString(xPortionProps.getPropertyValue("TextPortionType"));
                  if (strPortionType.equals("ReferenceMark")) {
                        //get the actual reference mark object
                        Object refmark = xPortionProps.getPropertyValue("ReferenceMark");
                        //get the properties of the reference mark
                        // XPropertySet refmarkProps = ooQueryInterface.XPropertySet(refmark);
                        XNamed xrefMarkName = ooQueryInterface.XNamed(refmark);
                        String refmarkName = xrefMarkName.getName(); // AnyConverter.toString(refmarkProps.getPropertyValue("Name"));
                        if (refmarkName.startsWith(fromRef+":")) {
                            //get the anchor of the refeence mark from the xtextcontent interface
                            if (beginRange == null ) {
                                XTextContent xRefContent = ooQueryInterface.XTextContent(refmark);
                                beginRange = xRefContent.getAnchor();
                            }
                        }
                        if (refmarkName.startsWith(toRef+":")) {
                            if (endRange == null) {
                                XTextContent xRefContent = ooQueryInterface.XTextContent(refmark);
                                endRange = xRefContent.getAnchor();
                            }
                        }
                  }
           }
        }
       } catch (NoSuchElementException ex) {
           
       } catch (WrappedTargetException ex) {
           
       } catch (IllegalArgumentException ex) {
           
       } catch (UnknownPropertyException ex) {
           
       }
    }
    
    private void markSectionBetweenRanges(OOComponentHelper ooDocument) {
        //names of from and to ranges to match
        String fromRef = this.fromRangeName, toRef = this.toRangeName;
        //get the from range
        //begin and end ranges for references
        //XTextRange beginRange = null, endRange = null;
        XNameAccess xRefmarksAccess = ooDocument.getReferenceMarks();
          //get the current section
        XTextSection xCurrent = ooDocument.currentSection();
          //get the cureent seciton ranges
        XTextRange sectionAnchor = xCurrent.getAnchor();
          //enumerate the currentsection
        XEnumerationAccess xAccess = ooQueryInterface.XEnumerationAccess(sectionAnchor);
        XEnumeration elementEnum = xAccess.createEnumeration();
        //run the enumerator
        try {
        while (elementEnum.hasMoreElements()) {
            Object aElement = elementEnum.nextElement();
            XServiceInfo xinfo = ooQueryInterface.XServiceInfo(aElement);
            //check if enumerated object is a paragrpah
            if (xinfo.supportsService("com.sun.star.text.Paragraph")) {
                //if its a paragraph...enumerate the paragraph portions
                XEnumerationAccess xPortionEnumAccess = ooQueryInterface.XEnumerationAccess(aElement);
                XEnumeration xPortionEnum = xPortionEnumAccess.createEnumeration();
                //oPortionEnum = oTextElement.createEnumeration()
                //run the portion enumerator
                browsePortion(xPortionEnum, fromRef, toRef);
                /*
                while (xPortionEnum.hasMoreElements()) {
                    //get the portion
                    Object objPortion = xPortionEnum.nextElement();
                  //  browsePortion (objPortion);
                    //get the portion properties
                    XPropertySet xPortionProps = ooQueryInterface.XPropertySet(objPortion);
                    XPropertySetInfo xPortionPropsInfo = xPortionProps.getPropertySetInfo();
                    //look for the textportiontype = referencemark
                    if (xPortionPropsInfo.hasPropertyByName("TextPortionType")) {
                        String strPortionType = AnyConverter.toString(xPortionProps.getPropertyValue("TextPortionType"));
                        if (strPortionType.equals("ReferenceMark")) {
                            //get the actual reference mark object
                            Object refmark = xPortionProps.getPropertyValue("ReferenceMark");
                            //get the properties of the reference mark
                            XPropertySet refmarkProps = ooQueryInterface.XPropertySet(refmark);
                            String refmarkName = AnyConverter.toString(refmarkProps.getPropertyValue("Name"));
                            if (refmarkName.startsWith(fromRef+":")) {
                                //get the anchor of the refeence mark from the xtextcontent interface
                                if (beginRange == null ) {
                                    XTextContent xRefContent = ooQueryInterface.XTextContent(refmark);
                                    beginRange = xRefContent.getAnchor();
                                }
                            }
                            if (refmarkName.startsWith(toRef+":")) {
                                if (endRange == null) {
                                    XTextContent xRefContent = ooQueryInterface.XTextContent(refmark);
                                    endRange = xRefContent.getAnchor();
                                }
                            }
                        }
                    }
                }*/
            }
          }
        } catch (NoSuchElementException ex) {
            log.error("markSectionBetweenRanges : " + ex.getMessage());
        } catch (WrappedTargetException ex) {
            log.error("markSectionBetweenRanges : " + ex.getMessage());
        }
        createLock(ooDocument);
        /*
        XTextRange spanRangeStart = null, spanRangeEnd = null;
        if (beginRange != null ) {
            spanRangeStart = beginRange.getStart();                
            if (endRange == null) {
                //get the span from & to range
                spanRangeEnd = beginRange.getEnd();
            } else {
                //endRange wasnt null...so get the end o the endRange
                spanRangeEnd = endRange.getEnd();
            }
            XTextCursor xLockCursor = ooDocument.getTextDocument().getText().createTextCursor();
            xLockCursor.gotoRange(spanRangeStart, false);
            xLockCursor.gotoRange(spanRangeEnd, true);
            //we create the section now... then inherit the metadata... 
            String newSection = CommonDocumentUtilFunctions.getUniqueSectionName("heading", ooDocument);
            XTextContent newSectionContent = ooDocument.createTextSection(newSection, (short)1);
            try {
                ooDocument.getTextDocument().getText().insertTextContent(xLockCursor, newSectionContent, true);
            } catch (IllegalArgumentException ex) {
                log.error("markSectionBetweenRanges :" + ex.getMessage() );
            }
        } */
    }
    
    private void createLock(OOComponentHelper ooDocument) {
        XTextRange spanRangeStart = null, spanRangeEnd = null;
        try {

            if (beginRange != null ) {
                spanRangeStart = beginRange.getStart();                
                if (endRange == null) {
                    //get the span from & to range
                    spanRangeEnd = beginRange.getEnd();
                } else {
                    //endRange wasnt null...so get the end o the endRange
                    spanRangeEnd = endRange.getEnd();
                }
                XTextCursor xLockCursor = ooDocument.getTextDocument().getText().createTextCursor();
                xLockCursor.gotoRange(spanRangeStart, false);
                xLockCursor.gotoRange(spanRangeEnd, true);
                String lockCursorString = xLockCursor.getString();
                log.debug("routerCreateHeadingLock : span string : " +lockCursorString);
                //we create the section now... then inherit the metadata... 
                String newSection = CommonDocumentUtilFunctions.getUniqueSectionName("heading", ooDocument);
                XTextContent newSectionContent = ooDocument.createTextSection(newSection, (short)1);
                    ooDocument.getTextDocument().getText().insertTextContent(xLockCursor, newSectionContent, true);
                HashMap<String,String> sectionMeta = new HashMap<String,String>();
                sectionMeta.put("BungeniSectionType",this.foundSectionType);
                ooDocument.setSectionMetadataAttributes(newSection, sectionMeta);    
            }

        } catch (IllegalArgumentException ex) {
                log.error("markSectionBetweenRanges :" + ex.getMessage() );
        }
    }
}
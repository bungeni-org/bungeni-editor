/*
 * toolbarButtonEventHandler.java
 *
 * Created on July 24, 2007, 4:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.panels.toolbarevents;

import com.sun.star.beans.PropertyValue;
import com.sun.star.container.XNamed;
import com.sun.star.text.XText;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextViewCursor;
import javax.swing.JOptionPane;
import org.bungeni.editor.panels.ItoolbarButtonEvent;
import org.bungeni.editor.panels.toolbarButtonCommandFactory;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.extutils.MessageBox;

/**
 *
 * @author Administrator
 */
public class toolbarButtonEventHandler extends Object implements ItoolbarButtonEvent {
    private OOComponentHelper ooDocument = null;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(toolbarButtonEventHandler.class.getName());
    
    /** Creates a new instance of toolbarButtonEventHandler */
    public toolbarButtonEventHandler() {
    }

    public void doCommand(OOComponentHelper ooDocument, String cmd ) {
        if (this.ooDocument == null ) this.ooDocument = ooDocument;
        log.debug("in doCommand "+cmd);
    
        if (cmd.equals("makePrayerSection"))
            doMakeSection(cmd);
        else if (cmd.equals("makeQASection"))
            doMakeSection(cmd);
        else if (cmd.equals("makePaperSection"))
            doMakeSection(cmd);
        else if (cmd.equals("makeNoticeOfMotionSection"))
            doMakeSection(cmd);
        else if (cmd.equals("makeQuestionBlockSection"))
            doMakeSection(cmd);
        else if (cmd.equals("makePrayerMarkup"))
            doMarkup(cmd);
        else if (cmd.equals("makePaperMarkup"))
            doMarkup(cmd);
        else if (cmd.equals("makePaperDetailsMarkup"))
            doMarkup(cmd);
        else if (cmd.equals("makeNoticeOfMotionMarkup"))
            doMarkup(cmd);
        else if (cmd.equals("makeNoticeMarkup"))
            doMarkup(cmd);
        else if (cmd.equals("makeNoticeDetailsMarkup"))
            doMarkup(cmd);
        else
            MessageBox.OK("the command action: "+cmd+" has not been implemented!");
    }

    
    public void doCommand(OOComponentHelper ooDocument) {
    }
    
    private void doMakeSection(String cmd){
           //get the section name and numbering type for the command
           String namingConvention, numberingType;
           String newName = "";
           namingConvention = toolbarButtonCommandFactory.getCommandNamingConvention(cmd);
           if (namingConvention.equals("")) {
                log.debug("unable to name section, section mame was blank");
                MessageBox.OK("The command:" + cmd+" does not have a naming convention associated with it");
                return;
           }
           numberingType = toolbarButtonCommandFactory.getCommandNumberingType(cmd);
           if (numberingType.equals("single")) {
                newName = namingConvention;
           } else if (numberingType.equals("serial")) {
                //do sequential naming thing....or whatever.....
                newName = namingConvention;
           } else if (numberingType.equals("")) {
               MessageBox.OK("The command: "+ cmd+ " does not have a numbering type associated with it");
               return;
           }
           if (this.ooDocument.getTextSections().hasByName(newName)){
                   log.debug("in doc command: section  already exists");
                   MessageBox.OK("The section:  prayers already exists");
            }
          else {
               log.debug("in doCommand : adding text section prayers");
               addTextSection(newName);
               MessageBox.OK(newName + " section was added !");
          }
    }
    private void addTextSection(String sectionName){
 
        String sectionClass = "com.sun.star.text.TextSection";
        XTextViewCursor xCursor = ooDocument.getViewCursor();
        XText xText = xCursor.getText();
        XTextContent xSectionContent = ooDocument.createTextSection(sectionName, (short)1);
        try {
            xText.insertTextContent(xCursor, xSectionContent , true);        
        } catch (com.sun.star.lang.IllegalArgumentException ex) {
            log.error("in addTextSection : "+ex.getLocalizedMessage(), ex);
        }        
        
        
    }

    private void doMarkup(String cmd) {
        log.debug("in doMarkup for command: "+cmd);
        
           String namingConvention = "", numberingType = "", newName = "";
           namingConvention = toolbarButtonCommandFactory.getCommandNamingConvention(cmd);
           numberingType = toolbarButtonCommandFactory.getCommandNumberingType(cmd);
           log.debug("naming convention = "+ namingConvention);
            if (namingConvention.equals("")) {
                log.debug("unable to name section, section mame was blank");
                MessageBox.OK("The command:" + cmd+" does not have a naming convention associated with it");
                return;
            }
           numberingType = toolbarButtonCommandFactory.getCommandNumberingType(cmd);
           log.debug("numbering type = " + numberingType);
           if (numberingType.equals("markup")) {    
              
               PropertyValue[] loadProps = new com.sun.star.beans.PropertyValue[2];
               loadProps[0] = new PropertyValue();
               loadProps[0].Name = new String( "Template");
               loadProps[0].Value = namingConvention;
               loadProps[1] = new PropertyValue();
               loadProps[1].Name = new String( "Family");
               loadProps[1].Value = new Integer(2);
               log.debug("invoking execute dispatch");
               ooDocument.executeDispatch(".uno:StyleApply", loadProps);
           }
    }
}

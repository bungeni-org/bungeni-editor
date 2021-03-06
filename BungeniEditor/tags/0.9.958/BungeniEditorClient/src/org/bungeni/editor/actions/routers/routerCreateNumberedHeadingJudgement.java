package org.bungeni.editor.actions.routers;

//~--- non-JDK imports --------------------------------------------------------

import com.sun.star.text.XParagraphCursor;
import org.bungeni.editor.actions.toolbarAction;
import org.bungeni.editor.actions.toolbarSubAction;
import org.bungeni.error.BungeniMsg;
import org.bungeni.error.BungeniValidatorState;
import org.bungeni.ooo.OOComponentHelper;

//~--- JDK imports ------------------------------------------------------------

import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextViewCursor;

import org.bungeni.ooo.ooQueryInterface;

/**
 *
 * @author Ashok Hariharan
 */
public class routerCreateNumberedHeadingJudgement extends defaultRouter {
    private static org.apache.log4j.Logger log                   =
        org.apache.log4j.Logger.getLogger(routerCreateSection.class.getName());
    private static final String __referencePrefix__ = "numRef";
     private  static final String __referenceNameSeparator__ = ":";
    /** Creates a new instance of routerCreateNumberedHeading */
    public routerCreateNumberedHeadingJudgement() {}

    /*
     * This router action marks a heading as a numbered heading and write protects it after applying the associated style
     * apply_numbered_heading:style_name
     */
    @Override
    public BungeniValidatorState route_TextSelectedInsert(toolbarAction action, toolbarSubAction subAction,
            javax.swing.JFrame pFrame, OOComponentHelper ooDocument) {
            //first get heading span
            String styleName = subAction.action_value();
            XTextViewCursor xviewCursor =  ooDocument.getViewCursor();
            XTextCursor xheadCursor = xviewCursor.getText().createTextCursorByRange(xviewCursor);
            XParagraphCursor xparaCursor = ooQueryInterface.XParagraphCursor(xheadCursor);
            xparaCursor.gotoNextParagraph(false);
            xparaCursor.gotoPreviousParagraph(true);
            // xparaCursor
            //set the heading style over the cursor
            ooDocument.setStyleOverCursor(styleName, xheadCursor);
            //add a number reference over the view cursor selection.
            int i = 1;
            String newRefNo  = __referencePrefix__ + __referenceNameSeparator__ + i;
            while (ooDocument.getReferenceMarks().hasByName(newRefNo) ) {
                newRefNo = __referencePrefix__ + __referenceNameSeparator__ +  (++i) ;
            }
            routerCreateReference rcf = new routerCreateReference();
            //update the action value with the reference name
            subAction.setActionValue(newRefNo);
            rcf.route_TextSelectedInsert(action, subAction, pFrame, ooDocument);
            

        return new BungeniValidatorState(true, new BungeniMsg("SUCCESS"));
    }

  

   

 
}

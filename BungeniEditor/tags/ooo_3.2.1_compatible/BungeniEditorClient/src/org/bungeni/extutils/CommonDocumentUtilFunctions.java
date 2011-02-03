/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.extutils;

import com.sun.star.text.XRelativeTextContentInsert;
import com.sun.star.text.XTextContent;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextSection;
import com.sun.star.text.XTextViewCursor;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Random;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooQueryInterface;

/**
 *
 * @author undesa
 */
public class CommonDocumentUtilFunctions {

         private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CommonDocumentUtilFunctions.class.getName());

    
    public static String getUniqueSectionName(String prefixName, OOComponentHelper ooDocument) {
        String sName = "";
        for (;;) {
            Random sRandom = new Random();
            int nRandom = sRandom.nextInt(10000);
            if (!ooDocument.hasSection(prefixName+nRandom)) {
                sName = prefixName + nRandom ;
                break;
            }
        }
      return sName; 
    } 

    public static void selectSection (OOComponentHelper ooDocument, String sectionName) {
        if (ooDocument.hasSection(sectionName)) {
            XTextViewCursor xCursor = ooDocument.getViewCursor();
            XTextSection xSection = ooDocument.getSection(sectionName);
            XTextRange sectionRange = xSection.getAnchor();
            //clear any existing selections
            xCursor.gotoRange(sectionRange.getStart(), false);
            xCursor.gotoRange(sectionRange, true);
        }
    }

    public static String getUniqueReferenceName(String prefixName, OOComponentHelper ooDocument) {
        String sName = "";
        for (;;) {
            Random sRand = new Random();
            int nRandom = sRand.nextInt(10000);
            if (ooDocument.getReferenceMarks().hasByName(prefixName+nRandom)) {
                continue;
            } else 
                return prefixName + nRandom;
        }
    }
    
    public static void writeToClipboard(String writeMe) {
		// get the system clipboard
		Clipboard systemClipboard =
			Toolkit
				.getDefaultToolkit()
				.getSystemClipboard();
		// set the textual content on the clipboard to our 
		// Transferable object
		// we use the 
		Transferable transferableText =
			new StringSelection(writeMe);
		systemClipboard.setContents(
			transferableText,
			null);
    }

    /**
     * Adds a new line before a named section
     * @param ooDoc
     * @param sectionName
     */
    public static void addNewLineBeforeSection(OOComponentHelper ooDoc, String sectionName) {
          XTextSection xSelectSection = ooDoc.getSection(sectionName);
          XTextContent oPar = ooQueryInterface.XTextContent(ooDoc.createInstance("com.sun.star.text.Paragraph"));
          XRelativeTextContentInsert xRelativeText = ooQueryInterface.XRelativeTextContentInsert(ooDoc.getTextDocument().getText());
               try {
                   xRelativeText.insertTextContentBefore(oPar, ooQueryInterface.XTextContent(xSelectSection));
                  } catch (com.sun.star.lang.IllegalArgumentException ex) {
                    log.error("insertTextContentbefore :" + ex.getMessage());
                 }
                    //move visible cursor to the point where the new para was added
              ooDoc.getViewCursor().gotoRange(xSelectSection.getAnchor().getStart(), false);
    }

    /**
     * Add new line after section
     * @param ooDoc
     * @param sectionName
     */
    public static void addNewLineAfterSection(OOComponentHelper ooDoc, String sectionName) {
         XTextSection xSelectSection = ooDoc.getSection(sectionName);

         XTextContent oPar = ooQueryInterface.XTextContent(ooDoc.createInstance("com.sun.star.text.Paragraph"));
         XRelativeTextContentInsert xRelativeText = ooQueryInterface.XRelativeTextContentInsert(ooDoc.getTextDocument().getText());
               try {
               xRelativeText.insertTextContentAfter(oPar, ooQueryInterface.XTextContent(xSelectSection));
               } catch (com.sun.star.lang.IllegalArgumentException ex) {
                 log.error("insertTextContentbefore :" + ex.getMessage());
               }
         //move visible cursor to point where para was added
         ooDoc.getViewCursor().gotoRange(xSelectSection.getAnchor().getEnd(), false);
    }


}

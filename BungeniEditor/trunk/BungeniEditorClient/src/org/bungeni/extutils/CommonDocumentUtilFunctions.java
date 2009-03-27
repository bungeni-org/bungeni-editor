/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.extutils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Random;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public class CommonDocumentUtilFunctions {
    
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

    
}

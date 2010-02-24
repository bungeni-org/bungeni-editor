package org.bungeni.trackchanges.uno;

import com.sun.star.lang.XComponent;
import java.util.ArrayList;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.ooo.OOComponentHelper;

/**
 * Keeps track of open ODF files 
 * @author Ashok Hariharan
 */
public class UnoOdfOpenFiles {

    static ArrayList<UnoOdfFile> openFiles = new ArrayList<UnoOdfFile>(0);

    public static UnoOdfFile getFile (BungeniOdfDocumentHelper docFile) {
        for (UnoOdfFile unoOdfFile : openFiles) {
            if (unoOdfFile.isFileOpen(docFile)) {
                return unoOdfFile;
            }
        }
        //no open files were find -- we create a handle and return it to theuser
       XComponent xComponent = OOComponentHelper.openExistingDocument(docFile.getDocumentPath());
       UnoOdfFile uoFile = new UnoOdfFile(docFile, xComponent);
       openFiles.add(uoFile);
       return uoFile;
      }
    
}

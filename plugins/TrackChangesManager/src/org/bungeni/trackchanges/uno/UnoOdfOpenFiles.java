package org.bungeni.trackchanges.uno;

import com.sun.star.io.IOException;
import com.sun.star.lang.IllegalArgumentException;
import com.sun.star.lang.XComponent;
import com.sun.star.uno.Exception;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.ooo.BungenioOoHelper;
import org.bungeni.ooo.OOComponentHelper;

/**
 * Keeps track of open ODF files 
 * @author Ashok Hariharan
 */
public class UnoOdfOpenFiles {

    static ArrayList<UnoOdfFile> openFiles = new ArrayList<UnoOdfFile>(0);
    private static org.apache.log4j.Logger log =
        org.apache.log4j.Logger.getLogger(UnoOdfOpenFiles.class.getName());


    public static UnoOdfFile getFile (BungeniOdfDocumentHelper docFile) {
        boolean handleUpdated = false;
        // if the desktop is terminated ... we clear the array and start afresh
        if (BungenioOoHelper.isDesktopTerminated()) {
            log.info("getFile : desktop was terminated - clearing openfiles");
            openFiles.clear();
        } else {
            log.info("getFile : desktop is active - look for the file in open files");
                    for (UnoOdfFile unoOdfFile : openFiles) {
                            // first check if it exists in the UNO odf file array
                            if (unoOdfFile.isFileOpen(docFile)) {
                                log.info("getFile : isFileOpen = true for : " + docFile.getDocumentPath());
                                // now check if the xComponent handle has been disposed
                                if (!unoOdfFile.isFileDisposed()) {
                                   /* if (BungenioOoHelper.isDesktopTerminated()) {
                                        // if the desktop is terminated
                                        // we simply recreate the handle
                                           // if dispoaed recreate the xcomponent handle and send it back
                                           XComponent xComponent = null;
                                            try {
                                                xComponent = OOComponentHelper.openExistingDocument(docFile.getDocumentPath());
                                            } catch (Exception ex) {
                                                log.error("Generic Exception : " + ex.getMessage());
                                            }
                                           unoOdfFile.setComponent(xComponent);
                                          return unoOdfFile;
                                    } */
                                    //not disposed return it
                                    log.info("getFile : file is not disposed, returing ");
                                    return unoOdfFile;
                                } else {
                                    // if dispoaed recreate the xcomponent handle and send it back
                                    log.info("getFile : file was disposed, creating new handle");
                                       XComponent xComponent = null;
                                    try {
                                        xComponent = OOComponentHelper.openExistingDocument(docFile.getDocumentPath());
                                    } catch (Exception ex) {
                                        log.error("Generic Exception : " + ex.getMessage());
                                    }
                                       unoOdfFile.setComponent(xComponent);
                                       return unoOdfFile;
                                }
                            }
                        }
        }

        log.info("getFile : desktop was terminated, adding blank handle");
        //no open files were find, or it  -- we create a handle and return it to theuser
       XComponent xComponent = null;
        try {
            xComponent = OOComponentHelper.openExistingDocument(docFile.getDocumentPath());
        } catch (Exception ex) {
            log.error("Generic Exception : " + ex.getMessage());
         } 
       UnoOdfFile uoFile = new UnoOdfFile(docFile, xComponent);
       openFiles.add(uoFile);
       return uoFile;
    }
    
}

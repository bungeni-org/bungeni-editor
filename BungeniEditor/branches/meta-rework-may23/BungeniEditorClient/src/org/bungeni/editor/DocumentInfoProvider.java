/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor;

import java.util.Timer;
import org.bungeni.ooo.OOComponentHelper;

/**
 *
 * @author undesa
 */
public class DocumentInfoProvider {
    private static OOComponentHelper ooDocument;
    private static Timer componentTimer ;
    public synchronized void setOOComponentHelper(OOComponentHelper ooDoc) {
            componentTimer.cancel();
            DocumentInfoProvider.ooDocument = ooDoc;
           // componentTimer.
        }


    }

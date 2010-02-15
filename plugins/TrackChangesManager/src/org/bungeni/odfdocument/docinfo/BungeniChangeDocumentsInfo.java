/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.odfdocument.docinfo;

import java.util.ArrayList;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

/**
 *
 * @author ashok
 */
public class BungeniChangeDocumentsInfo {
    private ArrayList<BungeniOdfDocumentHelper> changesInfo = new ArrayList<BungeniOdfDocumentHelper>(0);

    public BungeniChangeDocumentsInfo() {

    }

    public boolean addDocument(BungeniOdfDocumentHelper docHelper) {
        if (!changesInfo.contains(docHelper)) {
            changesInfo.add(docHelper);
            return true;
        }
        return false;
    }

    public ArrayList<BungeniOdfDocumentHelper> getDocuments() {
        return changesInfo;
    }

    public int getSize() {
        return changesInfo.size();
    }


}

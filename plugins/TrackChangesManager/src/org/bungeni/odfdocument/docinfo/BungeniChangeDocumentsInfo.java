package org.bungeni.odfdocument.docinfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.odftoolkit.odfdom.doc.OdfDocument;

/**
 *
 * @author ashok
 */
public class BungeniChangeDocumentsInfo {
    private ArrayList<BungeniOdfDocumentHelper> changesInfo = new ArrayList<BungeniOdfDocumentHelper>(0);
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(BungeniChangeDocumentsInfo.class.getName());

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

    public void clear(){
        changesInfo.clear();
    }

    public void sortDocuments() {
        Collections.sort(changesInfo, new Comparator(){

            public int compare(Object t1, Object t2) {
                BungeniOdfDocumentHelper doc1 = (BungeniOdfDocumentHelper) t1;
                BungeniOdfDocumentHelper doc2 = (BungeniOdfDocumentHelper) t2;
                String author1 = doc1.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocAuthor");
                String author2 = doc2.getPropertiesHelper().getUserDefinedPropertyValue("BungeniDocAuthor");
                return author1.compareToIgnoreCase(author2);
            }

        });
    }

    public synchronized void reload(File[] files) {
                this.clear();
                for (int i = 0; i < files.length ; i++ ) {
                    OdfDocument oDoc = null;
                    try {

                        BungeniOdfDocumentHelper docHelper = new BungeniOdfDocumentHelper(files[i]);
                        this.addDocument(docHelper);

                    } catch (Exception ex) {
                        log.error("reload = " + ex.getMessage(), ex);
                    }

                }
                this.sortDocuments();
    }

}

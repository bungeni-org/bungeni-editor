package org.bungeni.odfdocument.docinfo;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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

    public void reload(String aFile) {
        try {
            File afFile = new File(aFile);
            BungeniOdfDocumentHelper odfFile = new BungeniOdfDocumentHelper(afFile);
            reload(odfFile);
        } catch (Exception ex) {
            log.error("reload(String file) :" + ex.getMessage());
        }
    }

    public void reload(String[] files) {
        List<File> odfdocs = new ArrayList<File>(0);
        for (String aFile : files) {
            try {
                File ffFile = new File(aFile);
                odfdocs.add(ffFile);
            } catch (Exception ex) {
                log.error("reload(List<String> files) " + ex.getMessage());
            }
        }
        reload(odfdocs.toArray(new File[odfdocs.size()]));
    }

    public void reload(List<BungeniOdfDocumentHelper> files) {
        this.changesInfo.clear();
        for (BungeniOdfDocumentHelper bungeniOdfDocumentHelper : files) {
            this.changesInfo.add(bungeniOdfDocumentHelper);
        }
        this.sortDocuments();
    }

    public void reload (BungeniOdfDocumentHelper file) {
        int i = 0;
        boolean bExists = false;
        for (int j = 0; j < changesInfo.size(); j++) {
            BungeniOdfDocumentHelper curfile = changesInfo.get(j);
            if (file == null ) System.out.println("file WAS NULL");
            if (curfile.getDocumentPath().equals(file.getDocumentPath())) {
                changesInfo.remove(j);
                changesInfo.add(file);
                bExists = true;
                break;
            }
        }
        if (!bExists) {
            changesInfo.add(file);
        }
        this.sortDocuments();
    }

    public void reload(File[] files) {
                System.out.println("Clearing changes");
                this.clear();
                for (int i = 0; i < files.length ; i++ ) {
                    System.out.println("Adding file - " + files[i].getName());
                    OdfDocument oDoc = null;
                    try {
                        System.out.println("Creating odf helper object");
                        BungeniOdfDocumentHelper docHelper = new BungeniOdfDocumentHelper(files[i]);
                        System.out.println("Created odf helper object");
                        this.addDocument(docHelper);
                        System.out.println("Added odfhelper object");

                    } catch (Exception ex) {
                        System.out.println("reload exception = " + ex.getMessage() + " " + files[i].getName());
                        log.error("reload = " + ex.getMessage(), ex);
                    }

                }
                System.out.println("Sorting documents");
                this.sortDocuments();
                 System.out.println("Finishing sort..");
    }


}

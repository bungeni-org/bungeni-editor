package org.bungeni.odfdocument.docinfo;

import java.util.HashMap;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;

/**
 *
 * @author ashok
 */
public class BungeniDocumentInfo {
     public BungeniOdfDocumentHelper docHelper;
     public String initialCreator ;
     public String creationDate ;
     public String docCreator;
     public String editingDuration;
     public int editingCycles;

     public BungeniDocumentInfo (BungeniOdfDocumentHelper docHelper) {
         this.docHelper = docHelper;

     }

     public class DocumentStatistic {
         int tableCount = 0  ;
         int imageCount = 0;
         int objectCount = 0;
         int pageCount = 0;
         int paragraphCount = 0;
         int wordCount = 0;
         int characterCount = 0;
     } ;
     
     public DocumentStatistic docStats;
     
     /*
     <meta:initial-creator>Ashok Hariharan</meta:initial-creator>
        <meta:creation-date>2010-02-09T12:00:01</meta:creation-date>
        <dc:date>2010-02-09T12:09:12</dc:date>
        <dc:creator>Ashok Hariharan</dc:creator>
        <meta:editing-duration>PT00H07M48S</meta:editing-duration>
        <meta:editing-cycles>4</meta:editing-cycles>
        <meta:generator>OpenOffice.org/3.1$Unix
            OpenOffice.org_project/310m19$Build-9420</meta:generator>
        <meta:document-statistic meta:table-count="0" meta:image-count="0" meta:object-count="0"
            meta:page-count="2" meta:paragraph-count="7" meta:word-count="500"
            meta:character-count="2746"/>
    */


   public  HashMap<String,String> metaUserDefined = new HashMap<String,String>();

}

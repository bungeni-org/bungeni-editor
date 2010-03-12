
package org.bungeni.trackchanges.utils;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.utils.BungeniOdfFileCopy;

/**
 *
 * @author Ashok Hariharan
 */
public class ReviewDocuments {

    public static final String DEFAULT_DOC_PATTERN = "(u[0-9][0-9][0-9][0-9][a-z0-9_-]*?.odt)";
    public static final String PREFIX_DOC_PATTERN = "([a-z]*)_";

    class ReviewStage {
        private String stageName;
        private String stageDocumentPrefix;

        public ReviewStage(String stageN, String stagePref) {
            this.stageName = stageN;
            this.stageDocumentPrefix = stagePref;
        }

        @Override
        public String toString(){
            return stageName;
        }

        public String getStageDocumentPrefix(){
            return stageDocumentPrefix;
        }
    }


    private BungeniOdfDocumentHelper originalDocument;
    private String originalDocumentName ;

    private BungeniOdfDocumentHelper reviewDocument;
    private String reviewDocumentName;

    private HashMap<String,ReviewStage> stagePrefix = new HashMap<String,ReviewStage>(){
        {
            put ("ClerkReview", new ReviewStage("ClerkReview", "clerk"));
            put ("ClerkConsolidationReview", new ReviewStage("ClerkConsolidationReview", "cons"));
        }
    };

    private String REVIEW_PREFIX = "clerk_";


    public ReviewDocuments() {

    }

    public ReviewDocuments(BungeniOdfDocumentHelper reviewDoc, String stageName) throws Exception {
        this.originalDocument = reviewDoc;

        String baseURI = this.originalDocument.getOdfDocument().getBaseURI();
        File fodfDoc  = new File (new URI(baseURI));
        String fileName = fodfDoc.getName();
        this.originalDocumentName = fileName;

        String dirPath = fodfDoc.getParentFile().toURI().toString();
        String newBaseURI = dirPath + (dirPath.endsWith("/")?"":"/")  + REVIEW_PREFIX + fileName;
        File fodfCopyDoc = new File (new URI(newBaseURI));
        if (!fodfCopyDoc.exists()) {
            BungeniOdfFileCopy fcp = new BungeniOdfFileCopy(originalDocument.getOdfDocument().getPackage());
            fodfCopyDoc.createNewFile();
            fcp.copyFile(fodfCopyDoc);
        }
        this.reviewDocument = new BungeniOdfDocumentHelper(fodfCopyDoc);

    }


    public String getDocumentNameWithoutPrefix(){
        Pattern pDoc = Pattern.compile(ReviewDocuments.DEFAULT_DOC_PATTERN);
        Matcher pDocMatcher  = pDoc.matcher(this.originalDocumentName);
        boolean bDocMatcher = pDocMatcher.find();
        if (bDocMatcher) {
            //doc pattern was matched .. but it could still have a prefix
            //so we check by regexing for the prefix too.
            Pattern pDocWithPrefix = Pattern.compile(ReviewDocuments.PREFIX_DOC_PATTERN + ReviewDocuments.DEFAULT_DOC_PATTERN);
            Matcher pPrefixMatcher = pDocWithPrefix.matcher(this.originalDocumentName);
            boolean bPrefixMatch = pPrefixMatcher.find();
            if (!bPrefixMatch) {
                //just the file name without the prefix
            } else {
                //file name with prefix
            }
        }
        return new String();

    }



    public BungeniOdfDocumentHelper getReviewCopy() {
        return reviewDocument;
    }

    public BungeniOdfDocumentHelper getOriginal() {
        return originalDocument;
    }

    public static void main(String[] args) {
        String s = "cons_u2840_ke_bill_2010-2-25_eng.odt";
        Pattern pat = Pattern.compile(ReviewDocuments.DEFAULT_DOC_PATTERN);
        Matcher mm = pat.matcher(s);
        boolean bFind = mm.find();
        System.out.println(bFind);
         for (int i=0; i<=mm.groupCount(); i++) {
        System.out.println(i + " " + mm.group(i));
    }


    }

}

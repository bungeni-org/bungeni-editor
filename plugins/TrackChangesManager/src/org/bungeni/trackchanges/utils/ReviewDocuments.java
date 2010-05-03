
package org.bungeni.trackchanges.utils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.utils.BungeniOdfFileCopy;

/**
 *
 * @author Ashok Hariharan
 */
public class ReviewDocuments {

    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ReviewDocuments.class.getName());

    public static final String DEFAULT_DOC_PATTERN = "(u[0-9][0-9][0-9][0-9][a-z0-9_-]*?.odt)";
    public static final String PREFIX_DOC_PATTERN = "([a-z]*)";
    public static final String PREFIX_SEPARATOR = "_";

    /** These are the stage definitions **/
    private static final HashMap<String,ReviewStage> stagePrefix = new HashMap<String,ReviewStage>(){
        {
            put ("Default", new ReviewStage("Default", ""));
            put ("ClerkReview", new ReviewStage("ClerkReview", "clerk"));
            put ("ClerkConsolidationReview", new ReviewStage("ClerkConsolidationReview", "cons"));
            put ("ReportsView", new ReviewStage("ReportsView", "[a-z_]*?", "(report_[a-z_]*?.odt)"));
            put ("ClerkApproveRejectView", new ReviewStage("ClerkApproveRejectView", "cons"));
        }
    };

    /**
     *
     * @param stage
     * @return
     */
    public static final ReviewStage getReviewStage (String stage) {
        return stagePrefix.get(stage);
    }




    public static class ReviewStage {
        private String stageName;
        private String stageDocumentPrefix;
        private String stageDocPattern ;

        public ReviewStage(String stageN, String stagePref) {
            this(stageN, stagePref, DEFAULT_DOC_PATTERN);
        }

        public ReviewStage (String stageN, String stagePref, String docPattern) {
            this.stageName = stageN;
            this.stageDocumentPrefix = stagePref;
            this.stageDocPattern = docPattern;
        }
        @Override
        public String toString(){
            return stageName;
        }

        public String getStageDocumentPrefix(){
            return stageDocumentPrefix;
        }

        public String getDocumentFilterPattern(){
            if (stageDocumentPrefix.length() > 0 )
                return stageDocumentPrefix + PREFIX_SEPARATOR + stageDocPattern;
            else
                return DEFAULT_DOC_PATTERN;
        }
    }

    private String stageName ;

    private BungeniOdfDocumentHelper originalDocument = null;
    private String originalDocumentName ;
    private File fOriginalFile = null;

    private BungeniOdfDocumentHelper reviewDocument = null;
    private String reviewDocumentName;



    public ReviewDocuments() {

    }

    public ReviewDocuments(BungeniOdfDocumentHelper reviewDoc, String stageName) throws Exception {
        this.originalDocument = reviewDoc;
        this.stageName = stageName;
        String baseURI = this.originalDocument.getOdfDocument().getBaseURI();
        this.fOriginalFile  = new File (new URI(baseURI));
        String fileName =  this.fOriginalFile.getName();
        this.originalDocumentName = fileName;
    }

    public String getStagePrefix(){
        String sPrefix = null;
        if (ReviewDocuments.stagePrefix.containsKey(this.stageName)) {
            ReviewStage rStage = ReviewDocuments.stagePrefix.get(this.stageName);
            sPrefix = rStage.getStageDocumentPrefix();
        }
        return sPrefix;
    }

    public   String getDocumentNameWithoutPrefix(){
        Pattern pDoc = Pattern.compile(ReviewDocuments.DEFAULT_DOC_PATTERN);
        Matcher pDocMatcher  = pDoc.matcher(this.originalDocumentName);
        boolean bDocMatcher = pDocMatcher.find();
        if (bDocMatcher) {
            //doc pattern was matched .. but it could still have a prefix
            //so we check by regexing for the prefix too.
            Pattern pDocWithPrefix = Pattern.compile(ReviewDocuments.PREFIX_DOC_PATTERN + ReviewDocuments.PREFIX_SEPARATOR + ReviewDocuments.DEFAULT_DOC_PATTERN);
            Matcher pPrefixMatcher = pDocWithPrefix.matcher(this.originalDocumentName);
            boolean bPrefixMatch = pPrefixMatcher.find();
            if (!bPrefixMatch) {
                //just the file name without the prefix
                return this.originalDocumentName;
            } else {
                //file name with prefix
                int nGrp = pPrefixMatcher.groupCount();
                if (nGrp == 2) {
                    return pPrefixMatcher.group(2);
                }
            }
        }
        return new String();
    }


    private String getNewFileName() {
       return this.getStagePrefix() + ReviewDocuments.PREFIX_SEPARATOR + this.getDocumentNameWithoutPrefix() ;

    }

    private String getFolderURI(){
        String dirPath = fOriginalFile.getParentFile().toURI().toString();
        String newBaseURI = dirPath + (dirPath.endsWith("/")?"":"/") ;
        return newBaseURI;
    }

    public boolean reviewCopyExists(){
        boolean bState = true;
        bState =  reviewCopyFile().exists();
        return bState;
    }

    public boolean deleteReviewCopyFile(){
        boolean bState = false;
        File freviewCopy = reviewCopyFile();
        if (freviewCopy != null) {
            if (freviewCopy.exists()){
                bState = freviewCopy.delete();
            }
        }
        return bState;
    }

    public File reviewCopyFile(){
        File fcopy = null;
        try {
            String newBaseURI = getFolderURI() + getNewFileName();
            fcopy = new File(new URI(newBaseURI));
        } catch (URISyntaxException ex) {
           log.error("reviewCopyFile : " + ex.getMessage());
        }
        return fcopy;

    }

    public BungeniOdfDocumentHelper getReviewCopy() {
        if (reviewDocument == null) {
            try {
                String newBaseURI = getFolderURI() + getNewFileName();
                File fodfCopy = new File(new URI(newBaseURI));
                if (!fodfCopy.exists()) {
                    BungeniOdfFileCopy fcp = new BungeniOdfFileCopy(originalDocument.getOdfDocument().getPackage());
                    fodfCopy.createNewFile();
                    fcp.copyFile(fodfCopy);
                }
                this.reviewDocument = new BungeniOdfDocumentHelper(fodfCopy);
            } catch (Exception ex) {
               log.error("getReviewCopy :" + ex.getMessage(), ex);
            }

        }
        return reviewDocument;
    }

    public BungeniOdfDocumentHelper getOriginal() {
        return originalDocument;
    }

  

}

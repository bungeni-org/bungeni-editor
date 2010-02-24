
package org.bungeni.trackchanges.utils;

import java.io.File;
import java.net.URI;
import org.bungeni.odfdom.document.BungeniOdfDocumentHelper;
import org.bungeni.odfdom.utils.BungeniOdfFileCopy;

/**
 *
 * @author Ashok Hariharan
 */
public class ReviewDocuments {

    private BungeniOdfDocumentHelper originalDocument;
    private BungeniOdfDocumentHelper reviewDocument;
    private String REVIEW_PREFIX = "clerk_";


    public ReviewDocuments() {

    }

    public ReviewDocuments(BungeniOdfDocumentHelper reviewDoc) throws Exception {
        this.originalDocument = reviewDoc;
        String baseURI = this.originalDocument.getOdfDocument().getBaseURI();
        File fodfDoc  = new File (new URI(baseURI));
        String fileName = fodfDoc.getName();
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


    public BungeniOdfDocumentHelper getReviewCopy() {
        return reviewDocument;
    }

    public BungeniOdfDocumentHelper getOriginal() {
        return originalDocument;
    }


}

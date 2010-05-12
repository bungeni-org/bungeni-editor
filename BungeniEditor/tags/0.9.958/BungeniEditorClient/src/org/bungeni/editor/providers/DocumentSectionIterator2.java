/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.providers;

import com.sun.star.text.XTextSection;
import org.bungeni.ooo.OOComponentHelper;

/**
 * Second implementation of section iterator; this section iterator uses the
 * getChildSections() API to iterate child sections - and supports iterating within
 * sections as opposed to the whole document.
 * @author Ashok
 */
public class DocumentSectionIterator2 {
    IBungeniSectionIteratorListener2 callback;
    private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DocumentSectionIterator2.class.getName());
    protected XTextSection originSection  = null;
    protected OOComponentHelper ooDocument = null;


    /** Creates a new instance of DocumentSectionIterator */
    public DocumentSectionIterator2(OOComponentHelper ooDocument, String startingSection, IBungeniSectionIteratorListener2 callback) {
        this.originSection = ooDocument.getSection(startingSection);
        this.callback = callback;
        this.ooDocument = ooDocument;
    }

    public DocumentSectionIterator2(OOComponentHelper ooDocument, XTextSection startingSection, IBungeniSectionIteratorListener2 callback) {
        this.originSection = startingSection;
        this.callback = callback;
        this.ooDocument = ooDocument;
    }


    public void startIterator() {
        if (!callback.iteratorCallback(originSection))
            return;
        recurseAllNodes(originSection);
    }



    private void recurseAllNodes(XTextSection xoriginSection) {
       // BungeniBNode theBNode = (BungeniBNode) theNode.getUserObject();
        try {
            XTextSection[] childSections = xoriginSection.getChildSections();
            if (childSections != null) {
                if (childSections.length > 0 ) {
                    for (XTextSection childSection : childSections) {
                        if (callback.iteratorCallback(childSection) == false) {
                            return;
                        }
                        recurseAllNodes(childSection);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("recurseAllNodes : " + ex.getMessage());
        }
        }
}

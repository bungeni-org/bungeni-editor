package org.bungeni.odfdom.section;

//~--- non-JDK imports --------------------------------------------------------

import org.openoffice.odf.doc.element.text.OdfSection;

/**
 *
 * @author Ashok
 */
public interface IBungeniOdfSectionIterator {
    public boolean nextSection(BungeniOdfSectionHelper helper, OdfSection nSection);
}

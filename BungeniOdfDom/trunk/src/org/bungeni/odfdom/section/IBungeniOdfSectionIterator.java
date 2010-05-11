package org.bungeni.odfdom.section;

//~--- non-JDK imports --------------------------------------------------------

import org.odftoolkit.odfdom.doc.text.OdfTextSection;

/**
 *
 * @author Ashok
 */
public interface IBungeniOdfSectionIterator {
    public boolean nextSection(BungeniOdfSectionHelper helper, OdfTextSection nSection);
}

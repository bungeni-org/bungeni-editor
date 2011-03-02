package org.bungeni.odfdom.section;

//~--- non-JDK imports --------------------------------------------------------

import org.odftoolkit.odfdom.dom.element.text.TextSectionElement;

/**
 *
 * @author Ashok
 */
public interface IBungeniOdfSectionIterator {
    public boolean nextSection(BungeniOdfSectionHelper helper, TextSectionElement nSection);
}

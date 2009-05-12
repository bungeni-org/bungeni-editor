/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.odfdom.section;

import org.openoffice.odf.doc.element.text.OdfSection;

/**
 *
 * @author undesa
 */
public interface IBungeniOdfSectionIterator  {
    public boolean nextSection(BungeniOdfSectionHelper helper, OdfSection nSection);
}

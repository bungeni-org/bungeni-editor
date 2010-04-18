
package org.bungeni.trackchanges.utils;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Ashok Hariharna
 */
public abstract class GenericListSelectionListener implements ListSelectionListener {

            public void valueChanged(ListSelectionEvent lse) {
                ListSelectionModel lsm = (ListSelectionModel) lse.getSource();

                if (lse.getValueIsAdjusting()) {
                    return;
                }

                int firstIndex = lse.getFirstIndex();
                int lastIndex  = lse.getLastIndex();

                if (lsm.isSelectionEmpty()) {
                    return;
                } else {

                    // Find out which indexes are selected.
                    int nIndex = lsm.getMinSelectionIndex();

                    // do struff here
                    onSelectIndex(nIndex);
                }
            }

            abstract public void onSelectIndex(int nIndex) ;

}

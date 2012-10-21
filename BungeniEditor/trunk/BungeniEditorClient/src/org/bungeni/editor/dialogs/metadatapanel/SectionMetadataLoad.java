package org.bungeni.editor.dialogs.metadatapanel;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.log4j.Logger;

import org.bungeni.utils.CommonResourceBundleHelperFunctions;
import org.bungeni.ooo.OOComponentHelper;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Ashok Hariharan
 */
public class SectionMetadataLoad extends AbstractTableModel {
    private static String[]                column_names       = { "Metadata", "Value" };
    private static org.apache.log4j.Logger log                = Logger.getLogger(SectionMetadataLoad.class.getName());
    HashMap<String, String>                sectionMetadataMap = new HashMap<String, String>();
    OOComponentHelper                      ooDocument;
    Object[][]                             sectionMetadata;
    String                                 sectionName;

    /** Creates a new instance of SectionMetadataLoad */
    public SectionMetadataLoad(OOComponentHelper ooDoc, String sectName) {
        this.ooDocument  = ooDoc;
        this.sectionName = sectName;
        getSectionMetadata(sectName);
    }

    public int getRowCount() {
        return sectionMetadata.length;
    }

    public int getColumnCount() {
        return column_names.length;
    }

    @Override
    public String getColumnName(int column) {
        return column_names[column];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return CommonResourceBundleHelperFunctions.getSectionMetaString(
                sectionMetadata[rowIndex][columnIndex].toString());
        } else {
            if (sectionMetadata[rowIndex][0].equals("BungeniSectionType")) {
                return CommonResourceBundleHelperFunctions.getSectionTypeMetaString(
                    (String) sectionMetadata[rowIndex][columnIndex]);
            } else {
                return sectionMetadata[rowIndex][columnIndex];
            }
        }
    }

    public void getSectionMetadata(String sectionName) {
        try {
            sectionMetadataMap = ooDocument.getSectionMetadataAttributes(sectionName);
            log.debug("SectionMetadataLoad SectionName: " + sectionName);

            if (sectionMetadataMap.size() > 0) {
                sectionMetadata = new Object[sectionMetadataMap.size()][column_names.length];

                Iterator metaIterator = sectionMetadataMap.keySet().iterator();

                while (metaIterator.hasNext()) {
                    for (int i = 0; i < sectionMetadataMap.size(); i++) {
                        String metaName = (String) metaIterator.next();

                        sectionMetadata[i][0] = metaName;
                        sectionMetadata[i][1] = sectionMetadataMap.get(metaName);
                    }
                }
            } else {
                sectionMetadata = new Object[0][column_names.length];
                log.debug("sectionMetadataMap size=0");
            }
        } catch (NoSuchElementException ex) {
            log.error(ex.getMessage());
        }
    }
}

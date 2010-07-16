
package org.bungeni.editor.metadata;

import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocMetadata;

/**
 * Extended metadata model for the debaterecord document type
 * @author undesa
 */
public class BillMetaModel extends BaseEditorDocMetaModel {

    public static final String[] GROUPED_METADATA = { };
    public static final String[] THIS_METAMODEL   = { "BungeniBillNo", "BungeniBillName", "BungeniDateOfAssent", "BungeniDateOfCommencement" };

    public BillMetaModel(){
        super();
    }
    
    @Override
    public void setup(){
        super.setup();

        for (String sMeta : THIS_METAMODEL) {
            this.docMeta.put(sMeta, "");
        }
    }

    @Override
    public void saveModel(OOComponentHelper ooDocument) {
        ooDocMetadata docM = new ooDocMetadata(ooDocument);

        for (String sMeta : THIS_METAMODEL) {
            docM.AddProperty(sMeta, docMeta.get(sMeta));
        }

        // now process grouped metadata
        // we process all metadata starting with the grouped metadata filter :
        if (GROUPED_METADATA.length > 0 )
            for (String sGmeta : docMeta.keySet()) {
                for (String sGfilter : GROUPED_METADATA) {
                    if (sGmeta.startsWith(sGfilter)) {
                        docM.AddProperty(sGmeta, docMeta.get(sGmeta));
                    }
                }
            }
    }
}

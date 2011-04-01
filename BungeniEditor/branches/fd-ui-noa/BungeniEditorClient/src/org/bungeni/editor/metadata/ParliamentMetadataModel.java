/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocMetadata;

/**
 * Extended metadata model for the debaterecord document type
 * @author undesa
 */
public class ParliamentMetadataModel extends BaseEditorDocMetaModel {
    public ParliamentMetadataModel(){
        super();
    }
    
       public static final String[] THIS_METAMODEL = {
        "BungeniParliamentID",
        "BungeniParliamentSitting",
        "BungeniParliamentSession"
    };

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
    }
}

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
public class JudgementMetadataModel extends BaseEditorDocMetaModel {
    public JudgementMetadataModel(){
        super();
    }

    public static final String[] THIS_METAMODEL = {
        "BungeniJudgementNo",
        "BungeniCaseNo",
        "BungeniJudgementDate"
    };

    public static final String[] GROUPED_METADATA = {
        "BungeniJudgeName:",
        "BungeniPartyName:"
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
         //now process grouped metadata
         //we process all metadata starting with the grouped metadata filter :
         for (String sGmeta : docMeta.keySet()) {
             for (String sGfilter : GROUPED_METADATA) {

             }
         }
    }
}

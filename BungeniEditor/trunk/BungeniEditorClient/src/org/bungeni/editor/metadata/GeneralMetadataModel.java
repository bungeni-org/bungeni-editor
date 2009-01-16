/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

/**
 * Extended metadata model for the debaterecord document type
 * @author undesa
 */
public class GeneralMetadataModel extends BaseEditorDocMetaModel {
    public GeneralMetadataModel(){
        super();
    }
    
    @Override
    public void setup(){
        super.setup();
        this.docMeta.put("BungeniParliamentID", "");
        this.docMeta.put("BungeniParliamentSitting", "");
        this.docMeta.put("BungeniParliamentSession", "");
        this.docMeta.put("BungeniOfficialDate", "");
        this.docMeta.put("BungeniOfficialTime", "");
        this.docMeta.put("__BungeniDocMeta", "false");
    }
}

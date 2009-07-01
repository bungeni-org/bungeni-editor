/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

/**
 * Extended metadata model for the debaterecord document type
 * @author undesa
 */
public class BillMetaModel extends BaseEditorDocMetaModel {
    public BillMetaModel(){
        super();
    }
    
    @Override
    public void setup(){
        super.setup();
        this.docMeta.put("BungeniParliamentID", "");
        this.docMeta.put("BungeniParliamentSitting", "");
        this.docMeta.put("BungeniParliamentSession", "");
        this.docMeta.put("BungeniBillOfficialDate", "");
        this.docMeta.put("BungeniBillOfficialTime", "");
        this.docMeta.put("BungeniBillNo", "");
        this.docMeta.put("BungeniDateOfAssent", "");
        this.docMeta.put("BungeniDateOfCommencement", "");
       // this.docMeta.put("__BungeniDocMeta", "false");
    }
}

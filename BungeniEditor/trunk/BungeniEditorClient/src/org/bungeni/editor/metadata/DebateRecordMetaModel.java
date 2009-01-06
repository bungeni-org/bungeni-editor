/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

/**
 *
 * @author undesa
 */
public class DebateRecordMetaModel extends BaseEditorDocMetaModel {
    public DebateRecordMetaModel(){
        super();
    }
    
    @Override
    public void setup(){
        super.setup();
        this.docMeta.put("BungeniParliamentID", "");
        this.docMeta.put("BungeniParliamentSitting", "");
        this.docMeta.put("BungeniParliamentSession", "");
        this.docMeta.put("BungeniDebateOfficialDate", "");
        this.docMeta.put("BungeniDebateOfficialTime", "");
        this.docMeta.put("__BungeniDocMeta", "false");
    }
}

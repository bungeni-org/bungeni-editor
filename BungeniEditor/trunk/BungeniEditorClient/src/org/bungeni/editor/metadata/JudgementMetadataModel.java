/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

/**
 * Extended metadata model for the debaterecord document type
 * @author undesa
 */
public class JudgementMetadataModel extends BaseEditorDocMetaModel {
    public JudgementMetadataModel(){
        super();
    }
    
    @Override
    public void setup(){
        super.setup();
        this.docMeta.put("BungeniJudgementNo", "");
        this.docMeta.put("BungeniCaseNo", "");
        this.docMeta.put("BungeniJudgementDate", "");
        this.docMeta.put("__BungeniDocMeta", "false");
    }
}

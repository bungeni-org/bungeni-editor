/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

/**
 * Extended metadata model for the debaterecord document type
 * @author undesa
 */
public class ParliamentMetadataModel extends BaseEditorDocMetaModel {
    public ParliamentMetadataModel(){
        super();
    }
    
    @Override
    public void setup(){
        super.setup();
        this.docMeta.put("BungeniParliamentID", "");
        this.docMeta.put("BungeniParliamentSitting", "");
        this.docMeta.put("BungeniParliamentSession", "");
    }
}

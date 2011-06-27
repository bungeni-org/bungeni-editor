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

    public ParliamentMetadataModel() {
        super();
    }
    private String bungeniParliamentID = "";
    private String bungeniParliamentSitting = "";
    private String bungeniParliamentSession = "";
    public static final String[] THIS_METAMODEL = {
        "BungeniParliamentID",
        "BungeniParliamentSitting",
        "BungeniParliamentSession"
    };

    @Override
    public void setup() {
        super.setup();
        this.docMeta.put("BungeniParliamentID", bungeniParliamentID);
        this.docMeta.put("BungeniParliamentSitting", bungeniParliamentSitting);
        this.docMeta.put("BungeniParliamentSession", bungeniParliamentSession);
//         for (String sMeta : THIS_METAMODEL) {
//            this.docMeta.put(sMeta, "");
//        }
    }

    @Override
    public void saveModel(OOComponentHelper ooDocument) {
        ooDocMetadata docM = new ooDocMetadata(ooDocument);
        for (String sMeta : THIS_METAMODEL) {
            docM.AddProperty(sMeta, docMeta.get(sMeta));
        }
    }
    public String getBungeniParliamentID() {
        return bungeniParliamentID;
    }

    public void setBungeniParliamentID(String BungeniParliamentID) {
        this.bungeniParliamentID = BungeniParliamentID;
    }

    public String getBungeniParliamentSession() {
        return bungeniParliamentSession;
    }

    public void setBungeniParliamentSession(String BungeniParliamentSession) {
        this.bungeniParliamentSession = BungeniParliamentSession;
    }

    public String getBungeniParliamentSitting() {
        return bungeniParliamentSitting;
    }

    public void setBungeniParliamentSitting(String BungeniParliamentSitting) {
        this.bungeniParliamentSitting = BungeniParliamentSitting;
    }
}

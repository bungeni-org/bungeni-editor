/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.editor.metadata;

import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocMetadata;

/**
 * Extended metadata model for the act document type
 * @author bzuAdmin
 */
public class ActSourceModel extends BaseEditorDocMetaModel {
    
    
    private String BungeniSrcName = "";
    private String BungeniPublicationArea = "";
    private String BungeniSrcPublicationDate = "";
    private String BungeniSourceType = "";
    private String BungeniSourceNo = "";
    
    public static final String[] THIS_METAMODEL = {
        "BungeniSrcName",
        "BungeniSrcPublicationDate",
        "BungeniPublicationArea",
        "BungeniSourceType",
        "BungeniSourceNo"
    };
    
    public ActSourceModel(){
        super();
    }
    
    @Override
    public void setup(){
        super.setup();
        this.docMeta.put("BungeniPublicationArea", BungeniPublicationArea);
        this.docMeta.put("BungeniSrcName", BungeniSrcName);
        this.docMeta.put("BungeniSrcPublicationDate", BungeniSrcPublicationDate);
        this.docMeta.put("BungeniSourceType", BungeniSourceType);
        this.docMeta.put("BungeniSourceNo", BungeniSourceNo);
    }

    public String getBungeniPublicationArea() {
        return BungeniPublicationArea;
    }

    public void setBungeniPublicationArea(String BungeniPublicationArea) {
        this.BungeniPublicationArea = BungeniPublicationArea;
    }
    
    
    public String getBungeniSrcName() {
        return BungeniSrcName;
    }

    public void setBungeniSrcName(String BungeniSrcName) {
        this.BungeniSrcName = BungeniSrcName;
    }
    
    
    public String getBungeniSrcPublicationDate() {
        return BungeniSrcPublicationDate;
    }

    public void setBungeniSrcPublicationDate(String BungeniSrcPublicationDate) {
        this.BungeniSrcPublicationDate = BungeniSrcPublicationDate;
    }
    
    public String getBungeniSourceType() {
        return BungeniSourceType;
    }
    
    public void setBungeniSourceType(String BungeniSourceType) {
        this.BungeniSourceType = BungeniSourceType;
    }
    
    public String getBungeniSourceNo() {
        return BungeniSourceNo;
    }
    
    public void setBungeniSourceNo(String BungeniSourceNo) {
        this.BungeniSourceNo = BungeniSourceNo;
    }
    

}

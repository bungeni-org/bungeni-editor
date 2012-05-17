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
public class ActGeneralMetadataModel extends BaseEditorDocMetaModel {
    
    private String BungeniLanguageCode = "";
    private String BungeniPublicationName = "";
    private String BungeniPublicationArea = "";
    private String BungeniPublicationDate = "";
    private String BungeniSourceName = "";
    private String BungeniSourceSide = "";
    private String BungeniSourceType = "";
    private String BungeniSourceNo = "";
    
    public static final String[] THIS_METAMODEL = {
        "BungeniLanguageCode",
        "BungeniEffectiveDate",
        "BungeniPublicationName",
        "BungeniPublicationDate",
        "BungeniPublicationArea",
        "BungeniSourceName",
        "BungeniSourceSide",
        "BungeniSourceType",
        "BungeniSourceNo"
    };
    
    public ActGeneralMetadataModel(){
        super();
    }
    
    @Override
    public void setup(){
        super.setup();
        this.docMeta.put("BungeniLanguageCode", BungeniLanguageCode);
        this.docMeta.put("BungeniPublicationArea", BungeniPublicationArea);
        this.docMeta.put("BungeniPublicationName", BungeniPublicationName);
        this.docMeta.put("BungeniPublicationDate", BungeniPublicationDate);
        this.docMeta.put("BungeniSourceName", BungeniSourceName);
        this.docMeta.put("BungeniSourceSide", BungeniSourceSide);
        this.docMeta.put("BungeniSourceType", BungeniSourceType);
        this.docMeta.put("BungeniSourceNo", BungeniSourceNo);
    }

    
    public String getBungeniLanguageCode() {
        return BungeniLanguageCode;
    }

    public void setBungeniLanguageCode(String BungeniLanguageCode) {
        this.BungeniLanguageCode = BungeniLanguageCode;
    }

    
    public String getBungeniPublicationArea() {
        return BungeniPublicationArea;
    }

    public void setBungeniPublicationArea(String BungeniPublicationArea) {
        this.BungeniPublicationArea = BungeniPublicationArea;
    }
    
    
    public String getBungeniPublicationName() {
        return BungeniPublicationName;
    }

    public void setBungeniPublicationName(String BungeniPublicationName) {
        this.BungeniPublicationName = BungeniPublicationName;
    }
    
    
    public String getBungeniPublicationDate() {
        return BungeniPublicationDate;
    }

    public void setBungeniPublicationDate(String BungeniPublicationDate) {
        this.BungeniPublicationDate = BungeniPublicationDate;
    }
    
    
    public String getBungeniSourceName() {
        return BungeniSourceName;
    }

    public void setBungeniSourceName(String BungeniSourceName) {
        this.BungeniSourceName = BungeniSourceName;
    }
    
    
    public String getBungeniSourceSide() {
        return BungeniSourceSide;
    }
    
    public void setBungeniSourceSide(String BungeniSourceSide) {
        this.BungeniSourceSide = BungeniSourceSide;
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

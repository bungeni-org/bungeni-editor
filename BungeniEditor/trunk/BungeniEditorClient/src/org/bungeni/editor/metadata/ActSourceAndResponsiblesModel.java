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
public class ActSourceAndResponsiblesModel extends BaseEditorDocMetaModel {
    
    
    private String BungeniPublicationName = "";
    private String BungeniPublicationArea = "";
    private String BungeniPublicationDate = "";
    private String BungeniSourceName = "";
    private String BungeniSourceSide = "";
    private String BungeniSourceType = "";
    private String BungeniSourceNo = "";
    private String BungeniPageNo = "";
    
    public static final String[] THIS_METAMODEL = {
        
        "BungeniEffectiveDate",
        "BungeniPublicationName",
        "BungeniPublicationDate",
        "BungeniPublicationArea",
        "BungeniSourceName",
        "BungeniSourceSide",
        "BungeniSourceType",
        "BungeniSourceNo",
        "BungeniPageNo"
    };
    
    public ActSourceAndResponsiblesModel(){
        super();
    }
    
    @Override
    public void setup(){
        super.setup();
        this.docMeta.put("BungeniPublicationArea", BungeniPublicationArea);
        this.docMeta.put("BungeniPublicationName", BungeniPublicationName);
        this.docMeta.put("BungeniPublicationDate", BungeniPublicationDate);
        this.docMeta.put("BungeniSourceName", BungeniSourceName);
        this.docMeta.put("BungeniSourceSide", BungeniSourceSide);
        this.docMeta.put("BungeniSourceType", BungeniSourceType);
        this.docMeta.put("BungeniSourceNo", BungeniSourceNo);
        this.docMeta.put("BungeniPageNo", BungeniPageNo);
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
    
    public void setBungeniPageNo(String BungeniPageNo) {
        this.BungeniPageNo = BungeniPageNo;
    }
   
      public String getBungeniPageNo() {
        return BungeniPageNo;
    }
    
    public void setBungeniSourceNo(String BungeniSourceNo) {
        this.BungeniSourceNo = BungeniSourceNo;
    }
    

}

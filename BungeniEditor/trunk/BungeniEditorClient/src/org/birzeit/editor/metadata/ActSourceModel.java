/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.birzeit.editor.metadata;

import org.bungeni.editor.metadata.BaseEditorDocMetaModel;
import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocMetadata;

/**
 * Extended metadata model for the act document type
 *
 * @author bzuAdmin
 */
public class ActSourceModel extends BaseEditorDocMetaModel {

    private String BungeniPublicationSrcName = "";
    private String BungeniPublicationSrcNameID = "";
    private String BungeniPublicationArea = "";
    private String BungeniSourceType = "";
    private String BungeniSourceNo = "";
    private String BungeniPublicationDate = "";
    private String BungeniPublicationDateHijri = "";
    private String BungeniPublicationSrcName_AN = "";
    
    public static final String[] THIS_METAMODEL = {
        "BungeniPublicationSrcName",
        "BungeniPublicationSrcNameID",
        "BungeniPublicationArea",
        "BungeniSourceType",
        "BungeniSourceNo",
        "BungeniPublicationDate",
        "BungeniPublicationDateHijri",
        "BungeniPublicationSrcName_AN"
    };

    public ActSourceModel() {
        super();
    }

    @Override
    public void setup() {
        super.setup();
        this.docMeta.put("BungeniPublicationDate", BungeniPublicationDate);
        this.docMeta.put("BungeniPublicationDateHijri", BungeniPublicationDateHijri);
        this.docMeta.put("BungeniPublicationSrcName", BungeniPublicationSrcName);
        this.docMeta.put("BungeniPublicationSrcNameID", BungeniPublicationSrcNameID);
        this.docMeta.put("BungeniPublicationArea", BungeniPublicationArea);
        this.docMeta.put("BungeniSourceType", BungeniSourceType);
        this.docMeta.put("BungeniSourceNo", BungeniSourceNo);
        this.docMeta.put("BungeniPublicationSrcName_AN", BungeniPublicationSrcName_AN);
    }

    public String getBungeniPublicationArea() {
        return BungeniPublicationArea;
    }

    public void setBungeniPublicationArea(String BungeniPublicationArea) {
        this.BungeniPublicationArea = BungeniPublicationArea;
    }

    public String getBungeniPublicationSrcName() {
        return BungeniPublicationSrcName;
    }

    public void setBungeniPublicationSrcName(String BungeniPublicationSrcName) {
        this.BungeniPublicationSrcName = BungeniPublicationSrcName;
    }

    public String getBungeniPublication_date() {
        return BungeniPublicationDate;
    }

    public void setBungeniPublication_date(String BungeniPublication_date) {
        this.BungeniPublicationDate = BungeniPublication_date;
    }

    public String getBungeniPublication_dateHijri() {
        return BungeniPublicationDateHijri;
    }

    public void setBungeniPublication_dateHijri(String BungeniPublication_dateHijri) {
        this.BungeniPublicationDateHijri = BungeniPublication_dateHijri;
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

    public String getBungeniPublicationSrcName_AN() {
        return BungeniPublicationSrcName_AN;
    }

    public void setBungeniPublicationSrcName_AN(String BungeniPublicationSrcName_AN) {
        this.BungeniPublicationSrcName_AN = BungeniPublicationSrcName_AN;
    }
}

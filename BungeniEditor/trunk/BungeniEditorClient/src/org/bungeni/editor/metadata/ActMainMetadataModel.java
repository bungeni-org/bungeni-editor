/*
 * Copyright (C) 2012 bzuadmin
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.bungeni.editor.metadata;

import org.bungeni.ooo.OOComponentHelper;
import org.bungeni.ooo.ooDocMetadata;
/**
 *
 * @author bzuadmin
 */
public class ActMainMetadataModel extends BaseEditorDocMetaModel{
    public static final String[] GROUPED_METADATA = { };
    
    private String BungeniLanguageCode = "";
    private String BungeniActName = "";
    private String BungeniActNo = "";
    private String BungeniActYear = "";
    private String BungeniActEffectiveDate = "";
    private String BungeniActType = "";
    private String BungeniActScope = "";
    private String BungeniActCategory = "";
    private String BungeniActState="";
    private String BungeniActFamily="";
    private String BungeniActFamilyPossible="";
    private String BungeniActHistoricalPeriod="";
    private String BungeniPageNo = "";
    private String BungeniPageCount = "";
    
    
    
    public static final String[] THIS_METAMODEL = {
        "BungeniLanguageCode",
        "BungeniActName",
        "BungeniActNo",
        "BungeniActYear",
        "BungeniActType",
        "BungeniActScope",
        "BungeniActState",
        "BungeniActFamily",
        "BungeniActFamilyPossible",
        "BungeniActEffectiveDate",
        "BungeniActCategory",
        "BungeniPageNo",
        "BungeniPageCount"
    };
  
    public ActMainMetadataModel(){
        super();
    }
    
    @Override
    public void setup(){
       super.setup();
        this.docMeta.put("BungeniLanguageCode", BungeniLanguageCode);
        this.docMeta.put("BungeniActName", BungeniActName);
        this.docMeta.put("BungeniActNo", BungeniActNo);
        this.docMeta.put("BungeniActYear", BungeniActYear);
        this.docMeta.put("BungeniActType", BungeniActType);
        this.docMeta.put("BungeniActScope", BungeniActScope);
        this.docMeta.put("BungeniActEffectiveDate", BungeniActEffectiveDate);
        this.docMeta.put("BungeniActCategory", BungeniActCategory);
        this.docMeta.put("BungeniActState", BungeniActState);
        this.docMeta.put("BungeniActFamily", BungeniActFamily);
        this.docMeta.put("BungeniActFamilyPossible", BungeniActFamilyPossible);
        this.docMeta.put("BungeniActHistoricalPeriod", BungeniActHistoricalPeriod);
        this.docMeta.put("BungeniPageNo", BungeniPageNo);
        this.docMeta.put("BungeniPageCount", BungeniPageCount);  
        this.docMeta.put("ActType", "");
    }

//    @Override
//    public void saveModel(OOComponentHelper ooDocument) {
//        ooDocMetadata docM = new ooDocMetadata(ooDocument);
//
//        for (String sMeta : THIS_METAMODEL) {
//            docM.AddProperty(sMeta, docMeta.get(sMeta));
//        }
//    }
    
     public String getBungeniLanguageCode() {
        return BungeniLanguageCode;
    }

    public void setBungeniLanguageCode(String BungeniLanguageCode) {
        this.BungeniLanguageCode = BungeniLanguageCode;
    }

    public String getBungeniActName(){
        return BungeniActName;
    }
    public void setBungeniActName(String BungeniActName){
        this.BungeniActName = BungeniActName;
    }
    
    public String getBungeniActNo(){
        return BungeniActNo;
    }
    public void setBungeniActNo(String BungeniActNo){
        this.BungeniActNo = BungeniActNo;
    }
    
     public String getBungeniActYear(){
        return BungeniActYear;
    }
    public void setBungeniActYear(String BungeniActYear){
        this.BungeniActYear = BungeniActYear;
    }
    
    public String getBungeniActType() {
        return BungeniActType;
    }
    public void setBungeniActType(String BungeniActType) {
        this.BungeniActType = BungeniActType;
    }
    
    public String getBungeniActFamily() {
        return BungeniActFamily;
    }
    public void setBungeniActFamily(String BungeniActFamily) {
        this.BungeniActFamily = BungeniActFamily;
    }
    
    
    public String getBungeniActFamilyPossible() {
        return BungeniActFamilyPossible;
    }
    public void setBungeniActFamilyPossible(String BungeniActFamilyPossible) {
        this.BungeniActFamilyPossible = BungeniActFamilyPossible;
    }
    
    public String getBungeniActHistoricalPeriod() {
        return BungeniActHistoricalPeriod;
    }
    public void setBungeniActHistoricalPeriod(String BungeniActHistoricalPeriod) {
        this.BungeniActHistoricalPeriod = BungeniActHistoricalPeriod;
    }
    
    public String getBungeniActScope() {
        return BungeniActType;
    }
    public void setBungeniActScope(String BungeniActScope) {
        this.BungeniActScope = BungeniActScope;
    }
    
    public String getBungeniActState() {
        return BungeniActState;
    }
    public void setBungeniActState(String BungeniActState) {
        this.BungeniActState = BungeniActState;
    }
    
    
    public String getBungeniActEffectiveDate() {
        return BungeniActEffectiveDate;
    }
    public void setBungeniActEffectiveDate(String BungeniActEffectiveDate) {
        this.BungeniActEffectiveDate = BungeniActEffectiveDate;
    }
    
    public String getBungeniActCategory() {
        return BungeniActCategory;
    }
    public void setBungeniActCategory(String BungeniActCategory) {
        this.BungeniActCategory = BungeniActCategory;
    }

     public void setBungeniPageNo(String BungeniPageNo) {
        this.BungeniPageNo = BungeniPageNo;
    }
   
      public String getBungeniPageNo() {
        return BungeniPageNo;
    }
      
    public void setBungeniPageCount(String BungeniPageCount) {
        this.BungeniPageCount = BungeniPageCount;
    }
   
      public String getBungeniPageCount() {
        return BungeniPageCount;
    }
}

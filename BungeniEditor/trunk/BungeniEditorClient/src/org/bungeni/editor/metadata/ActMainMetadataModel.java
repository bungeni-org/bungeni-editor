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
    private String BungeniActEffectiveDate = "";
    private String BungeniActType = "";
    private String BungeniActScope = "";
    private String BungeniActRelease = "";
    private String BungeniActReleaseDate = "";
    private String BungeniActDeveloped = "";
    private String BungeniActDevelopedDate = "";
    private String BungeniActApproved = "";
    private String BungeniActAprovedDate = "";
    private String BungeniActImplementation = "";
    private String BungeniSecondaryActPromulgating = "";
    private String BungeniActImplementationDate = "";
    private String BungeniSecondaryActPromulgatingDate = "";
    private String BungeniActState="";
    private String BungeniActFamily="";
    private String BungeniActHistoricalPeriod="";
    
    
    public static final String[] THIS_METAMODEL = {
        "BungeniLanguageCode",
        "BungeniActType",
        "BungeniActScope",
        "BungeniActState",
        "BungeniActFamily",
        "BungeniActEffectiveDate",
        "BungeniActRelease",
        "BungeniActReleaseDate",
        "BungeniActDeveloped",
        "BungeniActDevelopedDate",
        "BungeniActApproved",
        "BungeniActAprovedDate",
        "BungeniActPromulgated",
        "BungeniActPromulgatedDate",
        "BungeniActImplementation",
        "BungeniActImplementationDate",
        "BungeniSecondaryActPromulgating",
        "BungeniActHistoricalPeriod",
        "BungeniSecondaryActPromulgatingDate"
    };
  
    public ActMainMetadataModel(){
        super();
    }
    
    @Override
    public void setup(){
       super.setup();
        this.docMeta.put("BungeniLanguageCode", BungeniLanguageCode);
        this.docMeta.put("BungeniActType", BungeniActType);
        this.docMeta.put("BungeniActScope", BungeniActScope);
        this.docMeta.put("BungeniActEffectiveDate", BungeniActEffectiveDate);
        this.docMeta.put("BungeniActSourceSide", BungeniActRelease);
        this.docMeta.put("BungeniActReleaseDate", BungeniActReleaseDate);
        this.docMeta.put("BungeniActDeveloped", BungeniActDeveloped);
        this.docMeta.put("BungeniActDevelopedDate", BungeniActDevelopedDate);
        this.docMeta.put("BungeniActApproved", BungeniActApproved);
        this.docMeta.put("BungeniActAprovedDate", BungeniActAprovedDate);
        this.docMeta.put("BungeniActImplementation", BungeniActImplementation);
        this.docMeta.put("BungeniActImplementationDate", BungeniActImplementationDate);
        this.docMeta.put("BungeniSecondaryActPromulgating", BungeniSecondaryActPromulgating);
        this.docMeta.put("BungeniSecondaryActPromulgatingDate", BungeniSecondaryActPromulgatingDate);
        this.docMeta.put("BungeniActState", BungeniActState);
        this.docMeta.put("BungeniActFamily", BungeniActFamily);
        this.docMeta.put("BungeniActHistoricalPeriod", BungeniActHistoricalPeriod);
    }

    @Override
    public void saveModel(OOComponentHelper ooDocument) {
        ooDocMetadata docM = new ooDocMetadata(ooDocument);

        for (String sMeta : THIS_METAMODEL) {
            docM.AddProperty(sMeta, docMeta.get(sMeta));
        }
    }
    
     public String getBungeniLanguageCode() {
        return BungeniLanguageCode;
    }

    public void setBungeniLanguageCode(String BungeniLanguageCode) {
        this.BungeniLanguageCode = BungeniLanguageCode;
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
    
    public String getBungeniActRelease() {
        return BungeniActRelease;
    }
    public void setBungeniActRelease(String BungeniActRelease) {
        this.BungeniActRelease = BungeniActRelease;
    }

     public String getBungeniActReleaseDate() {
        return BungeniActReleaseDate;
    }
    public void setBungeniActReleaseDate(String BungeniActReleaseDate) {
        this.BungeniActReleaseDate = BungeniActReleaseDate;
    }
    
    public String getBungeniActDeveloped() {
        return BungeniActDeveloped;
    }
    public void setBungeniActDeveloped(String BungeniActDeveloped) {
        this.BungeniActDeveloped = BungeniActDeveloped;
    }
    
    public String getBungeniActDevelopedDate() {
        return BungeniActDevelopedDate;
    }
    public void setBungeniActDevelopedDate(String BungeniActDevelopedDate) {
        this.BungeniActDevelopedDate = BungeniActDevelopedDate;
    }
    
    public String getBungeniActApproved() {
        return BungeniActApproved;
    }
    public void setBungeniActApproved(String BungeniActApproved) {
        this.BungeniActApproved = BungeniActApproved;
    }
    
    public String getBungeniActAprovedDate() {
        return BungeniActAprovedDate;
    }
    public void setBungeniActAprovedDate(String BungeniActAprovedDate) {
        this.BungeniActAprovedDate = BungeniActAprovedDate;
    }
    
    public String getBungeniActImplementation() {
        return BungeniActImplementation;
    }
    public void setBungeniActImplementation(String BungeniActImplementation) {
        this.BungeniActImplementation = BungeniActImplementation;
    }
    
    public String getBungeniActImplementationDate() {
        return BungeniActImplementationDate;
    }
    public void setBungeniActImplementationDate(String BungeniActImplementationDate) {
        this.BungeniActImplementationDate = BungeniActImplementationDate;
    }
    
    public String getBungeniSecondaryActPromulgating() {
        return BungeniSecondaryActPromulgating;
    }
    public void setBungeniSecondaryActPromulgating(String BungeniSecondaryActPromulgating) {
        this.BungeniSecondaryActPromulgating = BungeniSecondaryActPromulgating;
    }
    
    public String getBungeniSecondaryActPromulgatingDate() {
        return BungeniSecondaryActPromulgatingDate;
    }
    public void setBungeniSecondaryActPromulgatingDate(String BungeniSecondaryActPromulgatingDate) {
        this.BungeniSecondaryActPromulgatingDate = BungeniSecondaryActPromulgatingDate;
    }

    public void setBungeniActName(String value) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void setBungeniActNo(String value) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void setBungeniActYear(String value) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}

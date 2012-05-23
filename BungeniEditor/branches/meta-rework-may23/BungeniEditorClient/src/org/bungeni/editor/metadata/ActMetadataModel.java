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
public class ActMetadataModel extends BaseEditorDocMetaModel{
    public static final String[] GROUPED_METADATA = { };
    
    
    private String BungeniActEffectiveDate = "";
    private String BungeniActSourceSide = "";
    private String BungeniActType = "";
    private String BungeniActDeveloped = "";
    private String BungeniActDevelopedDate = "";
    private String BungeniActApproved = "";
    private String BungeniActAprovedDate = "";
    private String BungeniActPromulgated = "";
    private String BungeniActPromulgatedDate = "";
    private String BungeniActImplementation = "";
    private String BungeniSecondaryActPromulgating = "";
    
    
    public static final String[] THIS_METAMODEL = {
        "BungeniActType",
        "BungeniActEffectiveDate",
        "BungeniActSourceSide",
        "BungeniActDeveloped",
        "BungeniActDevelopedDate",
        "BungeniActApproved",
        "BungeniActAprovedDate",
        "BungeniActPromulgated",
        "BungeniActPromulgatedDate",
        "BungeniActImplementation",
        "BungeniSecondaryActPromulgating"
    };
  
    public ActMetadataModel(){
        super();
    }
    
    @Override
    public void setup(){
       super.setup();
        this.docMeta.put("BungeniActType", BungeniActType);
        this.docMeta.put("BungeniActEffectiveDate", BungeniActEffectiveDate);
        this.docMeta.put("BungeniActSourceSide", BungeniActSourceSide);
        this.docMeta.put("BungeniActDeveloped", BungeniActDeveloped);
        this.docMeta.put("BungeniActDevelopedDate", BungeniActDevelopedDate);
        this.docMeta.put("BungeniActApproved", BungeniActApproved);
        this.docMeta.put("BungeniActAprovedDate", BungeniActAprovedDate);
        this.docMeta.put("BungeniActPromulgated", BungeniActPromulgated);
        this.docMeta.put("BungeniActPromulgatedDate", BungeniActPromulgatedDate);
        this.docMeta.put("BungeniActImplementation", BungeniActImplementation);
        this.docMeta.put("BungeniSecondaryActPromulgating", BungeniSecondaryActPromulgating);
    }

    @Override
    public void saveModel(OOComponentHelper ooDocument) {
        ooDocMetadata docM = new ooDocMetadata(ooDocument);

        for (String sMeta : THIS_METAMODEL) {
            docM.AddProperty(sMeta, docMeta.get(sMeta));
        }
    }
    
    
    public String getBungeniActType() {
        return BungeniActType;
    }
    public void setBungeniActType(String BungeniActType) {
        this.BungeniActType = BungeniActType;
    }
    
    public String getBungeniActEffectiveDate() {
        return BungeniActEffectiveDate;
    }
    public void setBungeniActEffectiveDate(String BungeniActEffectiveDate) {
        this.BungeniActEffectiveDate = BungeniActEffectiveDate;
    }
    
    public String getBungeniActSourceSide() {
        return BungeniActSourceSide;
    }
    public void setBungeniActSourceSide(String BungeniActSourceSide) {
        this.BungeniActSourceSide = BungeniActSourceSide;
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
    
    public String getBungeniActPromulgated() {
        return BungeniActPromulgated;
    }
    public void setBungeniActPromulgated(String BungeniActPromulgated) {
        this.BungeniActPromulgated = BungeniActPromulgated;
    }
    
    public String getBungeniActPromulgatedDate() {
        return BungeniActPromulgatedDate;
    }
    public void setBungeniActPromulgatedDate(String BungeniActPromulgatedDate) {
        this.BungeniActPromulgatedDate = BungeniActPromulgatedDate;
    }
    
    public String getBungeniActImplementation() {
        return BungeniActImplementation;
    }
    public void setBungeniActImplementation(String BungeniActImplementation) {
        this.BungeniActImplementation = BungeniActImplementation;
    }
    
    public String getBungeniSecondaryActPromulgating() {
        return BungeniSecondaryActPromulgating;
    }
    public void setBungeniSecondaryActPromulgating(String BungeniSecondaryActPromulgating) {
        this.BungeniSecondaryActPromulgating = BungeniSecondaryActPromulgating;
    }
    
}

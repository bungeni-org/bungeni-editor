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

/**
 *
 * @author bzuadmin
 */
public class ActResponsibleAuthoritiesModel extends BaseEditorDocMetaModel {

    private String BungeniActRelease = "";
    private String BungeniActReleaseDate = "";
    private String BungeniActDeveloped = "";
    private String BungeniActDevelopedDate = "";
    private String BungeniActApproved = "";
    private String BungeniActApprovedDate = "";
    private String BungeniActImplementation = "";
    private String BungeniActImplementationDate = "";
    private String BungeniSecondaryActPromulgating = "";
    private String BungeniSecondaryActPromulgatingDate = "";
    
    public static final String[] THIS_METAMODEL = {
        
        "BungeniActRelease",
        "BungeniActReleaseDate",
        "BungeniActDeveloped",
        "BungeniActDevelopedDate",
        "BungeniActApproved",
        "BungeniActAprovedDate",
        "BungeniActImplementation",
        "BungeniActImplementationDate",
        "BungeniSecondaryActPromulgating",
        "BungeniSecondaryActPromulgatingDate"        
    };
    
    public ActResponsibleAuthoritiesModel(){
        super();
    }
    
    @Override
    public void setup(){
        super.setup();
        this.docMeta.put("BungeniActRelease", BungeniActRelease);
        this.docMeta.put("BungeniActReleaseDate", BungeniActReleaseDate);
        this.docMeta.put("BungeniActDeveloped", BungeniActDeveloped);
        this.docMeta.put("BungeniActDevelopedDate", BungeniActDevelopedDate); 
        this.docMeta.put("BungeniActApproved", BungeniActApproved);
        this.docMeta.put("BungeniActApprovedDate", BungeniActApprovedDate);
        this.docMeta.put("BungeniActImplementation", BungeniActImplementation);
        this.docMeta.put("BungeniActImplementationDate", BungeniActImplementationDate); 
        this.docMeta.put("BungeniSecondaryActPromulgating", BungeniSecondaryActPromulgating);
        this.docMeta.put("BungeniSecondaryActPromulgatingDate", BungeniSecondaryActPromulgatingDate);
    }

    public String getBungeniActRelease() {
        return BungeniActRelease;
    }

    public void setBungeniActRelease(String BungeniActRelease) {
        this.BungeniActRelease = BungeniActRelease;
    }
    
    
    public String getBungeniBungeniActReleaseDate() {
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
    
    public String getBungeniActApprovedDate() {
        return BungeniActApprovedDate;
    }

    public void setBungeniActApprovedDate(String BungeniActApprovedDate) {
        this.BungeniActApprovedDate = BungeniActApprovedDate;
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
    
    
}

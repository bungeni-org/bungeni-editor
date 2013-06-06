/*
 * Copyright (C) 2012 bzuadmin
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
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
    private String BungeniActReleaseID = "";
    private String BungeniActReleaseDate = "";
    private String BungeniActReleaseDateHijri = "";
    private String BungeniActDeveloped = "";
    private String BungeniActDevelopedDate = "";
    private String BungeniActApproved = "";
    private String BungeniActApprovedDate = "";
    public static final String[] THIS_METAMODEL = {
        "BungeniActRelease",
        "BungeniActReleaseID",
        "BungeniActReleaseDate",
        "BungeniActReleaseDateHijri",
        "BungeniActDeveloped",
        "BungeniActDevelopedDate",
        "BungeniActApproved",
        "BungeniActAprovedDate"
    };

    public ActResponsibleAuthoritiesModel() {
        super();
    }

    @Override
    public void setup() {
        super.setup();
        this.docMeta.put("BungeniActRelease", BungeniActRelease);
        this.docMeta.put("BungeniActReleaseID", BungeniActReleaseID);
        this.docMeta.put("BungeniActReleaseDate", BungeniActReleaseDate);
        this.docMeta.put("BungeniActReleaseDateHijri", BungeniActReleaseDateHijri);
        this.docMeta.put("BungeniActDeveloped", BungeniActDeveloped);
        this.docMeta.put("BungeniActDevelopedDate", BungeniActDevelopedDate);
        this.docMeta.put("BungeniActApproved", BungeniActApproved);
        this.docMeta.put("BungeniActApprovedDate", BungeniActApprovedDate);
        
        
        // FRBR Items
        // work
        docMeta.put("BungeniWorkAuthor", "");
        docMeta.put("BungeniWorkAuthorAs", "");
        docMeta.put("BungeniWorkAuthorURI", "");
        docMeta.put("BungeniWorkDate", "");
        docMeta.put("BungeniWorkDateName", "");
        docMeta.put("BungeniWorkURI", "");

        // expression
        docMeta.put("BungeniExpAuthor", "");
         docMeta.put("BungeniExpAuthorAs", "");
        docMeta.put("BungeniExpAuthorURI", "");
        docMeta.put("BungeniExpDate", "");
        docMeta.put("BungeniExpDateName", "");
        docMeta.put("BungeniExpURI", "");

        // manifestation
        docMeta.put("BungeniManAuthor", "");
        docMeta.put("BungeniManAuthorAs", "");
        docMeta.put("BungeniManAuthorURI", "");
        docMeta.put("BungeniManDate", "");
        docMeta.put("BungeniManDateName", "");
        docMeta.put("BungeniManURI", "");
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

    public String getBungeniBungeniActReleaseDateHijri() {
        return BungeniActReleaseDateHijri;
    }

    public void setBungeniActReleaseDateHijri(String BungeniActReleaseDateHijri) {
        this.BungeniActReleaseDateHijri = BungeniActReleaseDateHijri;
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
}

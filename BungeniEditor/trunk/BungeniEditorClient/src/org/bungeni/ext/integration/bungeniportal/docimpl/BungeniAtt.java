/*
 * Copyright (C) 2013 UN/DESA Africa i-Parliaments
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
package org.bungeni.ext.integration.bungeniportal.docimpl;

import java.util.List;

/**
 * GSON mapper class for Bungeni Attachments
 * @author UN/DESA Africa i-Parliaments
 */
public class BungeniAtt {
    private Integer attachment_id;
    private String description;
    private BungeniVocabType language;
    private String mimetype;
    private String name;
    private String title;
    private List<BungeniPermission> permissions;
    private BungeniVocabType status;
    private BungeniVocabType type;
    private String status_date;
    private boolean selected = false;
    private String documentURL;


    /**
     * @return the attachment_id
     */
    public Integer getAttachment_id() {
        return attachment_id;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the language
     */
    public BungeniVocabType getLanguage() {
        return language;
    }

    /**
     * @return the mimetype
     */
    public String getMimetype() {
        return mimetype;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the permissions
     */
    public List<BungeniPermission> getPermissions() {
        return permissions;
    }

    /**
     * @return the status
     */
    public BungeniVocabType getStatus() {
        return status;
    }

    /**
     * @return the status_date
     */
    public String getStatus_date() {
        return status_date;
    }
    
    @Override
    public String toString(){
        return getTitle();
    }
    
    public void setSelected(boolean bState){
        this.selected = bState;
    }
    
    public boolean isSelected(){
        return this.selected;
    }
    
    public void setURL(String docURL){
        this.documentURL = docURL;
    }
    
    public String getDocumentURL(){
        return this.documentURL;
    }
    
    public BungeniVocabType getType(){
        return this.type;
    }

}

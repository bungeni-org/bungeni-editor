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
 *
 * @author Ashok
 */
public class BungeniBaseDoc {
    
    private BungeniVocabType status;
    private String status_date;
    //private BungeniVocabType doc_type ; 
    private String doc_type;
    private String language ; 
    private String tags;
    private List<BungeniPermission> permissions;
    private String title;
    private String type;
    private String acronym; 
    private String body;
    private String description;
    private String timestamp;
    private Integer owner_id;
    private Integer doc_id;
    private String documentURL;
    private List<BungeniAtt> attachments; 
    
    
    
    /**
     * @return the attachments
     */
    public List<BungeniAtt> getAttachments() {
        return attachments;
    }

      public BungeniAtt getSelectedAttachment(){
        if (attachments != null) {
            for (BungeniAtt att : attachments) {
                if (att.isSelected()) {
                    return att;
                }
            }
        }
        return null;
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

    /**
     * @return the doc_type
     */
    public String getDoc_type() {
        return doc_type;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return this.language;
    }

    /**
     * @return the permissions
     */
   public List<BungeniPermission> getPermissions() {
        return permissions;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the acronym
     */
    public String getAcronym() {
        return acronym;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * @return the owner_id
     */
    public Integer getOwner_id() {
        return owner_id;
    }

    /**
     * @return the doc_id
     */
    public Integer getDoc_id() {
        return doc_id;
    }
    
    public void setURL(String docURL){
        this.documentURL = docURL;
    }
    
    public String getDocumentURL(){
        return this.documentURL;
    }


 
}

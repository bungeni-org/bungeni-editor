/*
 * Copyright (C) 2013 UN/DESA Africa i-Parliaments Action Plan
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
package org.bungeni.ext.integration.bungeniportal.docimpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

/**
 * GSON Mapping class for Bungeni Doc types
 * @author Ashok Hariharan
 */
public class BungeniDoc extends BungeniBaseDoc{

    private String admissible_date;
    
    private List<BungeniEvent> sa_events;
    private List<BungeniAtt> attachments;

    private String notice_date;
    private String submission_date;

    /**
     * @return the admissible_date
     */
    public String getAdmissibleDate() {
        return admissible_date;
    }

    /**
     * @return the doc_id
     */
    public Integer getDocId() {
        return getDoc_id();
    }

    /**
     * @return the doc_type
     */
    public BungeniVocabType getDocType() {
        return getDoc_type();
    }

    /**
     * @return the notice_date
     */
    public String getNoticeDate() {
        return notice_date;
    }

    /**
     * @return the owner_id
     */
    public Integer getOwnerId() {
        return getOwner_id();
    }

    /**
     * @return the status_date
     */
    public String getStatusDate() {
        return getStatus_date();
    }

    /**
     * @return the submission_date
     */
    public String getSubmissionDate() {
        return submission_date;
    }

    public List<BungeniEvent> getSaEvents(){
        return this.sa_events;
    }
    
    /**
    public static void main(String[] args){
        Reader reader = new InputStreamReader(BungeniDoc.class.getResourceAsStream("test.json"));
        Gson gson = new GsonBuilder().create();
        BungeniDoc p = gson.fromJson(reader, BungeniDoc.class);
        System.out.println(p);
   }**/
}

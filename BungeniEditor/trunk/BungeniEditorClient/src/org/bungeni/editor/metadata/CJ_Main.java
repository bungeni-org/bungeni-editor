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

import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author bzuadmin
 */
public class CJ_Main {

    private String CJ_Main_ID;
    private String CJ_Main_CJ_Main_ID;
    private String CJ_Main_CJ_CaseTypes_ID;
    private String CJ_Main_NumNo;
    private String CJ_Main_NumChar;
    private String CJ_Main_NumYr;
    private String CJ_Main_RegDate;
    private String CJ_Main_DateField;
    private String CJ_Main_DecNumN0;
    private String CJ_Main_DecNumYr;
    private Date CJ_Main_DecDate;
    private String CJ_Main_Subject;
    private String CJ_Main_Subject_E;
    private String CJ_Main_IsDissent;
    private String CJ_Main_Notes;
    private String CJ_Main_CJ_Files_ID;
    private String CJ_Main_SerialNum;
    private String CJ_Main_CJ_Regions_ID;
    private String CJ_Main_CJ_HistPeriods_ID;
    private String CJ_Main_CJ_CourtTypes_ID;
    private String CJ_Main_CJ_Cities_ID;
    private String CJ_Judgments_CourtName;
    private String CJ_Judgments_CourtType;
    private String CJ_Main_CJ_Domains_ID;
    private String CJ_Judgments_IsApproved;
    private String CJ_Judgments_CheckedBy;
    private String CJ_Main_EnteredBy;
    private String CJ_Main_DateEntered;
    private String CJ_Main_UpdatedBy;
    private String CJ_Main_DateUpdated;
    private String CJ_Main_IsImage;
    private String CJ_Main_IsImage_E;
    private String CJ_Main_IsPublished;
    private String CJ_Main_DateCoverted;
    private String CJ_Main_IsMainCourtType;
    private String CJ_Main_IsStamp;
    private String CJ_Main_IsStamp_E;
    private String CJ_Main_IsMarkedforPLComments;
    private String CJ_Main_CJ_CaseStatus_ID;
    private String CJ_Main_IsFullText;
    private String CJ_Main_IsFullText_E;
    private String CJ_Main_IsComment;
    private String CJ_Main_IsComment_E;
    private String CJ_Main_Judg1;
    private String CJ_Main_Judg2;
    private String CJ_Main_Judg3;
    private String CJ_Main_Judg4;
    private String CJ_Main_Judg5;
    private String CJ_Main_Source;
    private String CJ_Main_HCJId;
    private String CJ_Main_Part1;
    private String CJ_Main_Part2;
    private String CJ_Main_DUpdated;
    private String CJ_Main_Importance;

    public CJ_Main() {
        CJ_Main_ID = "";
    }

    public CJ_Main(String cjID) {
        CJ_Main_ID = cjID;
    }
    
     public CJ_Main(String cjID, String cjMain_NumNo, String cjMain_NumYr, Date cjMain_DecDate, String cjMain_CJ_CourtTypes_ID, String cjMain_CJ_Cities_ID) {
        CJ_Main_ID = cjID;
        CJ_Main_NumNo = cjMain_NumNo;
        CJ_Main_NumYr = cjMain_NumYr;
        CJ_Main_DecDate = cjMain_DecDate;
        CJ_Main_CJ_CourtTypes_ID = cjMain_CJ_CourtTypes_ID;
        CJ_Main_CJ_Cities_ID = cjMain_CJ_Cities_ID;
    }

    public String getCJ_Main_ID() {
        return CJ_Main_ID;
    }

    public String getCJ_Main_CJ_CaseTypes_ID() {
        return CJ_Main_CJ_CaseTypes_ID;
    }

    public String getCJ_Main_NumNo() {
        return CJ_Main_NumNo;
    }

    public String getCJ_Main_NumYr() {
        return CJ_Main_NumYr;
    }

    public Date getCJ_Main_DecDate() {
        return CJ_Main_DecDate;
    }

    public String getCJ_Main_CJ_Files_ID() {
        return CJ_Main_CJ_Files_ID;
    }

    public String getCJ_Main_SerialNum() {
        return CJ_Main_SerialNum;
    }

    public String getCJ_Main_CJ_Regions_ID() {
        return CJ_Main_CJ_Regions_ID;
    }

    public String getCJ_Main_CJ_HistPeriods_ID() {
        return CJ_Main_CJ_HistPeriods_ID;
    }

    public String getCJ_Main_CJ_CourtTypes_ID() {
        return CJ_Main_CJ_CourtTypes_ID;
    }

    public String getCJ_Main_CJ_Cities_ID() {
        return CJ_Main_CJ_Cities_ID;
    }

    public String getCJ_Judgments_CourtName() {
        return CJ_Judgments_CourtName;
    }

    public String getCJ_Main_CJ_Domains_ID() {
        return CJ_Main_CJ_Domains_ID;
    }

    public String getCJ_Main_Importance() {
        return CJ_Main_Importance;
    }
}

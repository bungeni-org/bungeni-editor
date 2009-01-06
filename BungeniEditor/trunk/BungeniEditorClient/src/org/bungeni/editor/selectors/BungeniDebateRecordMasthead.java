/*
 * BungeniDebateRecordMasthead.java
 *
 * Created on December 20, 2007, 10:43 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bungeni.editor.selectors;

import java.util.Date;

/**
 *
 * @author Administrator
 */
public class BungeniDebateRecordMasthead {
    
    public static final String NAME="BungeniDebateRecordMasthead";
    
    /** Creates a new instance of BungeniDebateRecordMasthead */
    public BungeniDebateRecordMasthead() {
    }
    
    private Date debateRecordDate;
    private Date debateRecordTime;
    private String debateRecordLogo;

    public Date getDebateRecordDate() {
        return debateRecordDate;
    }

    public void setDebateRecordDate(Date debateRecordDate) {
        this.debateRecordDate =  debateRecordDate;
    }

    public Date getDebateRecordTime() {
        return debateRecordTime;
    }

    public void setDebateRecordTime(Date debateRecordTime) {
        Date dtToTime;
        this.debateRecordTime = debateRecordTime;
    }

    public String getDebateRecordLogo() {
        return debateRecordLogo;
    }

    public void setDebateRecordLogo(String debateRecordLogo) {
        this.debateRecordLogo = debateRecordLogo;
    }
    
  
}

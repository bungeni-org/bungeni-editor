/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Dave
 */
@Embeddable
public class JudgementJudgePK implements Serializable {
    @Basic(optional = false)
    @Column(name = "JUDGE_ID")
    private int judgeId;
    @Basic(optional = false)
    @Column(name = "JUDGEMENT_ID")
    private String judgementId;

    public JudgementJudgePK() {
    }

    public JudgementJudgePK(int judgeId, String judgementId) {
        this.judgeId = judgeId;
        this.judgementId = judgementId;
    }

    public int getJudgeId() {
        return judgeId;
    }

    public void setJudgeId(int judgeId) {
        this.judgeId = judgeId;
    }

    public String getJudgementId() {
        return judgementId;
    }

    public void setJudgementId(String judgementId) {
        this.judgementId = judgementId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) judgeId;
        hash += (judgementId != null ? judgementId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof JudgementJudgePK)) {
            return false;
        }
        JudgementJudgePK other = (JudgementJudgePK) object;
        if (this.judgeId != other.judgeId) {
            return false;
        }
        if ((this.judgementId == null && other.judgementId != null) || (this.judgementId != null && !this.judgementId.equals(other.judgementId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bungeni.connector.jpa.entity.JudgementJudgePK[judgeId=" + judgeId + ", judgementId=" + judgementId + "]";
    }

}

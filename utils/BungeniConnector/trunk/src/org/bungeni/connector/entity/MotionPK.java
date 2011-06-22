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
public class MotionPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "MOTION_NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "MOTION_ID")
    private String id;

    public MotionPK() {
    }

    public MotionPK(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getMotionName() {
        return name;
    }

    public void setMotionName(String name) {
        this.name = name;
    }

    public String getMotionId() {
        return id;
    }

    public void setMotionId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MotionPK)) {
            return false;
        }
        MotionPK other = (MotionPK) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bungeni.connector.jpa.entity.MotionPK[name=" + name + ", id=" + id + "]";
    }

}

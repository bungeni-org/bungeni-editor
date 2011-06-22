/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Dave
 */
@Entity
@Table(name = "MOTIONS")
@NamedQueries({
    @NamedQuery(name = "Motion.findAll", query = "SELECT m FROM Motion m"),
    @NamedQuery(name = "Motion.findByMotionName", query = "SELECT m FROM Motion m WHERE m.pk.name = :name"),
    @NamedQuery(name = "Motion.findByMotionTitle", query = "SELECT m FROM Motion m WHERE m.title = :title"),
    @NamedQuery(name = "Motion.findByMotionBy", query = "SELECT m FROM Motion m WHERE m.by = :by"),
    @NamedQuery(name = "Motion.findByMotionText", query = "SELECT m FROM Motion m WHERE m.text = :text"),
    @NamedQuery(name = "Motion.findByMotionId", query = "SELECT m FROM Motion m WHERE m.pk.id = :id"),
    @NamedQuery(name = "Motion.findByMotionUri", query = "SELECT m FROM Motion m WHERE m.uri = :uri")})
public class Motion implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected MotionPK pk;
    @Basic(optional = false)
    @Column(name = "MOTION_TITLE")
    private String title;
    @Basic(optional = false)
    @Column(name = "MOTION_BY")
    private String by;
    @Basic(optional = false)
    @Column(name = "MOTION_TEXT")
    private String text;
    @Column(name = "MOTION_URI")
    private String uri;
    public static final String MOTION_ALIAS = "motion";
    public Motion() {
    }

    public Motion(MotionPK pk) {
        this.pk = pk;
    }

    public Motion(MotionPK pk, String title, String by, String text) {
        this.pk = pk;
        this.title = title;
        this.by = by;
        this.text = text;
    }

    public Motion(String name, String id) {
        this.pk = new MotionPK(name, id);
    }

    public MotionPK getMotionPK() {
        return pk;
    }

    public void setMotionPK(MotionPK pk) {
        this.pk = pk;
    }

    public String getMotionTitle() {
        return title;
    }

    public void setMotionTitle(String title) {
        this.title = title;
    }

    public String getMotionBy() {
        return by;
    }

    public void setMotionBy(String by) {
        this.by = by;
    }

    public String getMotionText() {
        return text;
    }

    public void setMotionText(String text) {
        this.text = text;
    }

    public String getMotionUri() {
        return uri;
    }

    public void setMotionUri(String uri) {
        this.uri = uri;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pk != null ? pk.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Motion)) {
            return false;
        }
        Motion other = (Motion) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bungeni.connector.jpa.entity.Motion[pk=" + pk + "]";
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Dave
 */
@Entity
@Table(name = "METADATA_INFO")
@NamedQueries({
    @NamedQuery(name = "MetadataInfo.findAll", query = "SELECT m FROM MetadataInfo m"),
    @NamedQuery(name = "MetadataInfo.findByKeyId", query = "SELECT m FROM MetadataInfo m WHERE m.id = :id"),
    @NamedQuery(name = "MetadataInfo.findByKeyType", query = "SELECT m FROM MetadataInfo m WHERE m.type = :type"),
    @NamedQuery(name = "MetadataInfo.findByKeyName", query = "SELECT m FROM MetadataInfo m WHERE m.name = :name"),
    @NamedQuery(name = "MetadataInfo.findByKeyValue", query = "SELECT m FROM MetadataInfo m WHERE m.value = :value")})
public class MetadataInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "KEY_ID")
    private Long id;
    @Column(name = "KEY_TYPE")
    private String type;
    @Column(name = "KEY_NAME")
    private String name;
    @Column(name = "KEY_VALUE")
    private String value;
    public static final String METADATA_ALIAS = "metadata";
    public MetadataInfo() {
    }

    public MetadataInfo(Long id) {
        this.id = id;
    }

    public Long getKeyId() {
        return id;
    }

    public void setKeyId(Long id) {
        this.id = id;
    }

    public String getKeyType() {
        return type;
    }

    public void setKeyType(String type) {
        this.type = type;
    }

    public String getKeyName() {
        return name;
    }

    public void setKeyName(String name) {
        this.name = name;
    }

    public String getKeyValue() {
        return value;
    }

    public void setKeyValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetadataInfo)) {
            return false;
        }
        MetadataInfo other = (MetadataInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bungeni.connector.entity.MetadataInfo[id=" + id + "]";
    }

}

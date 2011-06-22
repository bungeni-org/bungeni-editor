/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector.entity;

import java.io.Serializable;
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
@Table(name = "PERSONS")
@NamedQueries({
    @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p"),
    @NamedQuery(name = "Person.findById", query = "SELECT p FROM Person p WHERE p.pk.id = :id"),
    @NamedQuery(name = "Person.findByFirstName", query = "SELECT p FROM Person p WHERE p.first = :first"),
    @NamedQuery(name = "Person.findByLastName", query = "SELECT p FROM Person p WHERE p.last = :last"),
    @NamedQuery(name = "Person.findByUri", query = "SELECT p FROM Person p WHERE p.pk.uri = :uri"),
    @NamedQuery(name = "Person.findByRole", query = "SELECT p FROM Person p WHERE p.role = :role")})
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PersonPK pk;
    @Column(name = "FIRST_NAME")
    private String first;
    @Column(name = "LAST_NAME")
    private String last;
    @Column(name = "ROLE")
    private String role;

    public static final String PERSON_ALIAS = "member";
    public Person() {
    }

    public Person(PersonPK pk) {
        this.pk = pk;
    }

    public Person(int id, String uri) {
        this.pk = new PersonPK(id, uri);
    }

    public PersonPK getPersonPK() {
        return pk;
    }

    public void setPersonPK(PersonPK pk) {
        this.pk = pk;
    }

    public String getFirstName() {
        return first;
    }

    public void setFirstName(String first) {
        this.first = first;
    }

    public String getLastName() {
        return last;
    }

    public void setLastName(String last) {
        this.last = last;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.pk == null && other.pk != null) || (this.pk != null && !this.pk.equals(other.pk))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "First: "+getFirstName()+" Last: "+getLastName();
    }

}

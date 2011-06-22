/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bungeni.connector.entity.controller;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import org.bungeni.connector.entity.controller.exceptions.NonexistentEntityException;
import org.bungeni.connector.entity.controller.exceptions.PreexistingEntityException;
import org.bungeni.connector.entity.Party;
import org.bungeni.connector.entity.PartyPK;

/**
 *
 * @author Dave
 */
public class PartyJpaController {

    public PartyJpaController() {
        emf = Persistence.createEntityManagerFactory("BungeniConnectorPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Party party) throws PreexistingEntityException, Exception {
        if (party.getPartyPK() == null) {
            party.setPartyPK(new PartyPK());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(party);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findParty(party.getPartyPK()) != null) {
                throw new PreexistingEntityException("Party " + party + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Party party) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            party = em.merge(party);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                PartyPK id = party.getPartyPK();
                if (findParty(id) == null) {
                    throw new NonexistentEntityException("The party with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PartyPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Party party;
            try {
                party = em.getReference(Party.class, id);
                party.getPartyPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The party with id " + id + " no longer exists.", enfe);
            }
            em.remove(party);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Party> findPartyEntities() {
        return findPartyEntities(true, -1, -1);
    }

    public List<Party> findPartyEntities(int maxResults, int firstResult) {
        return findPartyEntities(false, maxResults, firstResult);
    }

    private List<Party> findPartyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Party as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Party findParty(PartyPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Party.class, id);
        } finally {
            em.close();
        }
    }

    public int getPartyCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Party as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

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
import org.bungeni.connector.entity.MetadataInfo;
import org.bungeni.connector.entity.controller.exceptions.NonexistentEntityException;

/**
 *
 * @author Dave
 */
public class MetadataInfoJpaController {

    public MetadataInfoJpaController() {
        emf = Persistence.createEntityManagerFactory("BungeniConnectorPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MetadataInfo metadataInfo) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(metadataInfo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MetadataInfo metadataInfo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            metadataInfo = em.merge(metadataInfo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = metadataInfo.getKeyId();
                if (findMetadataInfo(id) == null) {
                    throw new NonexistentEntityException("The metadataInfo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MetadataInfo metadataInfo;
            try {
                metadataInfo = em.getReference(MetadataInfo.class, id);
                metadataInfo.getKeyId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The metadataInfo with id " + id + " no longer exists.", enfe);
            }
            em.remove(metadataInfo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MetadataInfo> findMetadataInfoEntities() {
        return findMetadataInfoEntities(true, -1, -1);
    }

    public List<MetadataInfo> findMetadataInfoEntities(int maxResults, int firstResult) {
        return findMetadataInfoEntities(false, maxResults, firstResult);
    }

    private List<MetadataInfo> findMetadataInfoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from MetadataInfo as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public MetadataInfo findMetadataInfo(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MetadataInfo.class, id);
        } finally {
            em.close();
        }
    }

    public int getMetadataInfoCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from MetadataInfo as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

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
import org.bungeni.connector.entity.TabledDocument;

/**
 *
 * @author Dave
 */
public class TabledDocumentJpaController {

    public TabledDocumentJpaController() {
        emf = Persistence.createEntityManagerFactory("BungeniConnectorPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TabledDocument tabledDocument) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(tabledDocument);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTabledDocument(tabledDocument.getId()) != null) {
                throw new PreexistingEntityException("TabledDocument " + tabledDocument + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TabledDocument tabledDocument) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            tabledDocument = em.merge(tabledDocument);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tabledDocument.getId();
                if (findTabledDocument(id) == null) {
                    throw new NonexistentEntityException("The tabledDocument with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TabledDocument tabledDocument;
            try {
                tabledDocument = em.getReference(TabledDocument.class, id);
                tabledDocument.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tabledDocument with id " + id + " no longer exists.", enfe);
            }
            em.remove(tabledDocument);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TabledDocument> findTabledDocumentEntities() {
        return findTabledDocumentEntities(true, -1, -1);
    }

    public List<TabledDocument> findTabledDocumentEntities(int maxResults, int firstResult) {
        return findTabledDocumentEntities(false, maxResults, firstResult);
    }

    private List<TabledDocument> findTabledDocumentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from TabledDocument as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TabledDocument findTabledDocument(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TabledDocument.class, id);
        } finally {
            em.close();
        }
    }

    public int getTabledDocumentCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from TabledDocument as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

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
import org.bungeni.connector.entity.Bill;

/**
 *
 * @author Dave
 */
public class BillJpaController {

    public BillJpaController() {
        emf = Persistence.createEntityManagerFactory("BungeniConnectorPU");
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Bill bill) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(bill);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBill(bill.getBillUri()) != null) {
                throw new PreexistingEntityException("Bill " + bill + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Bill bill) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            bill = em.merge(bill);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = bill.getBillUri();
                if (findBill(id) == null) {
                    throw new NonexistentEntityException("The bill with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Bill bill;
            try {
                bill = em.getReference(Bill.class, id);
                bill.getBillUri();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bill with id " + id + " no longer exists.", enfe);
            }
            em.remove(bill);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Bill> findBillEntities() {
        return findBillEntities(true, -1, -1);
    }

    public List<Bill> findBillEntities(int maxResults, int firstResult) {
        return findBillEntities(false, maxResults, firstResult);
    }

    private List<Bill> findBillEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Bill as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Bill findBill(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Bill.class, id);
        } finally {
            em.close();
        }
    }

    public int getBillCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Bill as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}

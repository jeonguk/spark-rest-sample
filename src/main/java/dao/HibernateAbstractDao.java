package dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class HibernateAbstractDao<T, PK extends Serializable> implements HibernateDao<T, PK> {

    private EntityManagerFactory entityManagerFactory;

    public HibernateAbstractDao() {
        entityManagerFactory = Persistence.createEntityManagerFactory("jpa");
    }

    protected EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    @Override
    public T create(T t) {
        final EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }
        return t;
    }

    @Override
    public T update(T t) {
        final EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(t);
            em.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }
        return t;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T findById(PK id) {
        final EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            final T obj = (T) em.find(getClazz(), id);
            em.getTransaction().commit();
            return obj;
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(T t) {
        final EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.contains(t) ? t : em.merge(t));
            em.getTransaction().commit();
        } catch (Exception e) {
            throw e;
        } finally {
            em.close();
        }
    }

    public abstract Class<?> getClazz();

}

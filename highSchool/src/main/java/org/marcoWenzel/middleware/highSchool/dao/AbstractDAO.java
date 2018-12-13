package org.marcoWenzel.middleware.highSchool.dao;


import java.util.List;
import org.hibernate.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.marcoWenzel.middleware.highSchool.model.Student;
import org.marcoWenzel.middleware.highSchool.util.HibernateUtil;

public abstract class AbstractDAO<T> {
    
    protected Class<T> clazz;
    protected final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    
    public AbstractDAO(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Class<T> getClazz(){
        return this.clazz;
    }
    
    public void setClazz(Class<T> clazz){
        this.clazz = clazz;
    }
    
    public List<T> findAll() {
        List<T> entities = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from " + this.clazz.getSimpleName());
            entities = query.list();
            session.getTransaction().commit();
        }
        catch (Exception exception) {
           if (session != null) {
               session.getTransaction().rollback();
           }
        }
        finally {
            if (session != null) {
               session.close();
            }
        }

        return entities;
    }
   public int maxNotifid() {
	   Session session = null;
	   List<Integer> maxList=null;
	   int maxid = 0;
       try {
           session = sessionFactory.openSession();
           session.beginTransaction();
           Query query = session.createQuery("select e.primaryKey.notificationNumber from " + this.clazz.getSimpleName()+ " e order by e.primaryKey.notificationNumber DESC");
           maxList = query.list();
           session.getTransaction().commit();
           maxid=maxList.get(0)+1;
       }
       catch (Exception exception) {
          if (session != null) {
        	  System.out.println(exception);
              session.getTransaction().rollback();
          }
       }
       finally {
           if (session != null) {
              session.close();
           }
       }   
	return maxid;  
   }
   public int maxid(String field) {
	   Session session = null;
	   List <Student>entities = null;
	   List<Integer> maxList=null;
	   int maxid = 0;
       try {
           session = sessionFactory.openSession();
           session.beginTransaction();
           Query query = session.createQuery("select e."+field+" from " + this.clazz.getSimpleName()+ " e order by e."+field+" DESC");
           maxList = query.list();
           session.getTransaction().commit();
           if (!maxList.isEmpty())
        	   maxid=maxList.get(0)+1;
           else
        	   maxid=0;
       }
       catch (Exception exception) {
          if (session != null) {
        	  System.out.println(exception);
              session.getTransaction().rollback();
          }
       }
       finally {
           if (session != null) {
              session.close();
           }
       }   
	return maxid;  
   }
   
    public T get(String id){
        T entity = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from " + this.clazz.getSimpleName() + " e where e.id = :ID");
            query.setParameter("ID", id);
            entity = (T) query.uniqueResult();
            Hibernate.initialize(entity);
            session.getTransaction().commit();
        }
        catch (Exception exception) {
           if (session != null) {
               session.getTransaction().rollback();
           }
        }
        finally {
            if (session != null) {
               session.close();
            }
        }

        return entity;
    }
    public T get(int id){
        T entity = null;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            Query query = session.createQuery("from " + this.clazz.getSimpleName() + " e where e.id = :ID");
            query.setParameter("ID", id);
            entity = (T) query.uniqueResult();
            Hibernate.initialize(entity);
            session.getTransaction().commit();
        }
        catch (Exception exception) {
           if (session != null) {
               session.getTransaction().rollback();
           }
        }
        finally {
            if (session != null) {
               session.close();
            }
        }

        return entity;
    }
    
    public boolean create(T entity) {
        Boolean success = false;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
            success = true;
        }
        catch (Exception exception) {
        	System.err.println(exception);
           if (session != null) {
               session.getTransaction().rollback();
           }
        }
        finally {
            if (session != null) {
            	//session.flush();
               session.close();
            }
        }
        
        return success;
    }
    
    public boolean update(T entity) {
        Boolean success = false;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.merge(entity);
            session.getTransaction().commit();
            success = true;
        }
        catch (Exception exception) {
           if (session != null) {
               session.getTransaction().rollback();
           }
        }
        finally {
            if (session != null) {
               session.close();
            }
        }

        return success;
    }
    
    public boolean delete(T entity) {
        Boolean success = false;
        Session session = null;

        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(entity);
            session.getTransaction().commit();
            success = true;
        }
        catch (Exception exception) {
           if (session != null) {
               session.getTransaction().rollback();
           }
        }
        finally {
            if (session != null) {
               session.close();
            }
        }

        return success;
    }

}

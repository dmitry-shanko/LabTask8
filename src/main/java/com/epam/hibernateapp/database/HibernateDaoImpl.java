package com.epam.hibernateapp.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.hibernateapp.database.exception.DaoException;

public class HibernateDaoImpl<T, ID extends Serializable> implements GeneralDao<T, ID>
{
	static
	{
		Locale.setDefault(Locale.US);
	}

	private static final Logger log = LoggerFactory.getLogger(HibernateDaoImpl.class);

	private SessionFactory sessionFactory;
	private Class<T> persistanceType;
	private Class<ID> countType;
	private String persistanceTypeName;
	private String countTypeName;
	private String totalCountQuery;

	private HibernateDaoImpl(Class<T> persistanceType, Class<ID> countType)
	{
		this.setPersistanceType(persistanceType);
		this.setCountType(countType);
		this.persistanceTypeName = persistanceType.getSimpleName();
		this.countTypeName = countType.getSimpleName();
	}

	@SuppressWarnings("unused")
	private void init()
	{
		log.debug("Debug message for HibernateDaoImpl.class:");
		log.debug("Params: sessionFactory={}, persistanceType={}, persistanceTypeName={}, countType={}, countTypeName={}", new Object[]{sessionFactory, persistanceType, persistanceTypeName, countType, countTypeName});
		totalCountQuery = "select count(ID) from ".concat(persistanceTypeName);
	}

	public void setSessionFactory(SessionFactory sessionFactory)
	{
		log.debug("com.epam.hibernateapp.database.HibernateDaoImpl public void setSessionFactory(SessionFactory sessionFactory): sessionFactory={}", sessionFactory);
		if (null != sessionFactory)
		{
			this.sessionFactory = sessionFactory;
		}
	}
	/**
	 * @return the persistanceType
	 */
	public Class<T> getPersistanceType() 
	{
		return persistanceType;
	}
	/**
	 * @param persistanceType the persistanceType to set
	 */
	public void setPersistanceType(Class<T> persistanceType) 
	{
		this.persistanceType = persistanceType;
		this.persistanceTypeName = persistanceType.getSimpleName();
	}
	/**
	 * @return the countType
	 */
	public Class<ID> getCountType() 
	{
		return countType;
	}
	/**
	 * @param countType the countType to set
	 */
	public void setCountType(Class<ID> countType) 
	{
		this.countType = countType;
		this.countTypeName = countType.getSimpleName();
	}
	/**
	 * @return the persistanceTypeName
	 */
	public String getPersistanceTypeName() 
	{
		return persistanceTypeName;
	}
	/**
	 * @return the countTypeName
	 */
	public String getCountTypeName() 
	{
		return countTypeName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(ID id) throws DaoException 
	{
		log.debug("Attempt to get(id): persistanceTypeName={}, id={}", persistanceTypeName, id);
		if (null != id)
		{
			try 
			{				
				T o = (T) getSession().get(persistanceType, id);
				log.debug("Got by id: persistanceTypeName={}, Object={}", persistanceTypeName, o);
				return o;
			} 
			catch (HibernateException e) 
			{
				throw new DaoException("Can't get by ID instance of " + persistanceTypeName, e);
			}
		}
		else
		{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public T add(T o) throws DaoException 
	{
		if (null != o)
		{
			try 
			{
				log.info("Attempt to save(o): persistanceTypeName={}, Object={}", persistanceTypeName, o);
				ID id = (ID) getSession().save(o);
				log.debug("Object has been saved: persistanceTypeName={}, ID={}", persistanceTypeName, id);
				return get(id);
			} 
			catch (HibernateException e) 
			{
				throw new DaoException("Can't save such instance of " + persistanceTypeName, e);
			}
		}
		else
		{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAll(int firstResult, int lastResult) throws DaoException 
	{
		try 
		{
			log.info("Attempt to getAll(): persistanceTypeName={}", persistanceTypeName);
			log.debug("Get All for: firstResult={}, lastResult={}", firstResult, lastResult);
			if (this.persistanceType.equals(com.epam.hibernateapp.model.Employee.class))
			{
				Criteria crit = getSession().createCriteria(getPersistanceType());
				crit.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
				return crit.setFirstResult(firstResult).setMaxResults(lastResult - firstResult).list();
			}
			List<T> entities = findByCriteria();
			log.debug("Collection has been taken from database: persistanceTypeName={}, entities.size={}", persistanceTypeName, entities.size());
			log.trace("entities={}", entities);
			return entities;
		} 
		catch (HibernateException e) 
		{
			throw new DaoException("Can't get such instance of " + persistanceTypeName, e);
		}
	} 

	@Override
	public void quickAdd(List<T> list) throws DaoException 
	{
		if (list != null)
		{
			Session session = this.getSession();
			try
			{
				for (T entity : list)
				{
					session.save(entity);
				}
				session.flush();
			}
			catch (HibernateException e) 
			{
				throw new DaoException("Can't save such instance of " + persistanceTypeName, e);
			}
		}
		else
		{
			throw new DaoException("List is null, can't add anything from null collection");
		}
	}

	@SuppressWarnings("unchecked")
	protected List<T> findByCriteria(Criterion... criterion) throws DaoException
	{		
		if (null != criterion)
		{
			try
			{
				Session session = getSession();
				Criteria crit = session.createCriteria(getPersistanceType());
				for (Criterion c : criterion) 
				{
					crit.add(c);
				}
				return crit.list();
			}
			catch (HibernateException e)
			{
				throw new DaoException("Can't find " + getPersistanceType() + " in " + getClass() + " because of " + e.getClass() + ".\n", e);
			}
		}
		else
		{
			return new ArrayList<T>(0);
		}
	}
	
	@Override
	public long getTotalCount() throws DaoException 
	{
		long totalCount = 0;
		Session session = this.getSession();
		try
		{
			Query query = session.createQuery(totalCountQuery);
			totalCount = (Long) query.uniqueResult();
		}
		catch (HibernateException e) 
		{
			throw new DaoException("Can't get count of " + persistanceTypeName, e);
		}
		return totalCount;
	}

	protected Session getSession() throws DaoException
	{
		Session session = null;
		if (null == sessionFactory)
		{
			throw new DaoException("SessionFactory is null, can't proceed");
		}
		else
		{
			try 
			{
				
				session = sessionFactory.getCurrentSession();
			} 
			catch (HibernateException e) 
			{
				try
				{
					log.warn("Can't getCurrentSession from sessionFactory=" + sessionFactory, e);
					session = sessionFactory.openSession();
				}
				catch (HibernateException e1)
				{
					log.debug("HibernateException catched: ", e1.getMessage());
					throw new DaoException("Can't get current session from " + sessionFactory + " or open new session in " + getClass(), e1);
				}
			}
		}		
		return session;
	}
}
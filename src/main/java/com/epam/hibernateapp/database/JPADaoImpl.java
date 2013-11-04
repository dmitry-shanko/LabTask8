package com.epam.hibernateapp.database;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.hibernateapp.database.exception.DaoException;

public class JPADaoImpl<T, ID extends Serializable> implements GeneralDao<T, ID>
{
	static
	{
		Locale.setDefault(Locale.US);
	}

	private static final Logger log = LoggerFactory.getLogger(JPADaoImpl.class);	

	private Class<T> persistanceType;
	private Class<ID> countType;
	private String persistanceTypeName;
	private String countTypeName;
	private String totalCountQuery ;

	@PersistenceContext
	private EntityManager entityManager;

	private JPADaoImpl(Class<T> persistanceType, Class<ID> countType)
	{
		this.setPersistanceType(persistanceType);
		this.setCountType(countType);
		this.persistanceTypeName = persistanceType.getSimpleName();
		this.countTypeName = countType.getSimpleName();
	}

	@SuppressWarnings("unused")
	private void init()
	{
		log.debug("Debug message for JPADaoImpl.class:");
		log.debug("Params: entityManager={}, persistanceType={}, persistanceTypeName={}, countType={}, countTypeName={}", new Object[]{entityManager, persistanceType, persistanceTypeName, countType, countTypeName});
		totalCountQuery = "select count(ID) from ".concat(persistanceTypeName);
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

	@Override
	public T get(ID id) throws DaoException 
	{
		log.debug("Attempt to get(id): persistanceTypeName={}, id={}", persistanceTypeName, id);
		if (null == id)
		{
			return null;
		}
		try
		{
			T o = entityManager.find(persistanceType, id);
			log.debug("Got by id: persistanceTypeName={}, Object={}", persistanceTypeName, o);
			return o;
		}
		catch (Exception e)
		{
			log.debug("Some Exception catched: ", e.getMessage());
			throw new DaoException("Can't get by ID instance of " + persistanceTypeName, e);
		}
	}

	@Override
	public T add(T o) throws DaoException 
	{
		log.info("Attempt to save(o): persistanceTypeName={}, Object={}", persistanceTypeName, o);
		if (null != o)
		{
			try 
			{			
				T mergedEntity = (T) entityManager.merge(o);
				entityManager.flush();
				log.debug("Object has been merged: persistanceTypeName={}, mergedEntity={}", persistanceTypeName, mergedEntity);
				return mergedEntity;
			} 
			catch (PersistenceException e) 
			{
				throw new DaoException("Can't merge such instance of " + persistanceTypeName, e);
			}
		}
		else
		{
			return null;
		}
	}

	@Override
	public List<T> getAll(int firstResult, int lastResult) throws DaoException 
	{
		try
		{
			CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(persistanceType);
			criteriaQuery.from(persistanceType);
			TypedQuery<T> query = entityManager.createQuery(criteriaQuery);
			if (this.persistanceType.equals(com.epam.hibernateapp.model.Employee.class))
			{
				log.debug("Get All for: firstResult={}, lastResult={}", firstResult, lastResult);
				query.setFirstResult(firstResult).setMaxResults(lastResult - firstResult);
			}
			return query.getResultList();
		}
		catch (PersistenceException e)
		{
			throw new DaoException("Can't get all instances of " + persistanceTypeName, e);
		}
	}

	@Override
	public void quickAdd(List<T> list) throws DaoException
	{
		if (list != null)
		{
			try
			{
				for (T entity : list)
				{
					entityManager.persist(entity);
				}
				entityManager.flush();
			}
			catch (PersistenceException e) 
			{
				throw new DaoException("Can't save such instance of " + persistanceTypeName, e);
			}
		}
		else
		{
			throw new DaoException("List is null, can't add anything from null collection");
		}		
	}

	@Override
	public long getTotalCount() throws DaoException 
	{
		long totalCount = 0;
		try
		{
			Query query = entityManager.createQuery(totalCountQuery);
			totalCount = (Long) query.getSingleResult();
		}
		catch (PersistenceException e)
		{
			throw new DaoException("Can't get count of " + persistanceTypeName, e);
		}
		return totalCount;
	}
}
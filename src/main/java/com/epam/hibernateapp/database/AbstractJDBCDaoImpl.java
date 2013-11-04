package com.epam.hibernateapp.database;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.cfg.NamingStrategy;
import org.slf4j.Logger;

import com.epam.hibernateapp.database.exception.DaoException;

public abstract class AbstractJDBCDaoImpl<T, ID extends Serializable> implements GeneralDao<T, ID>
{
	static
	{
		Locale.setDefault(Locale.US);
	}

	private BasicDataSource dataSource;
	private NamingStrategy namingStrategy;

	private Class<T> persistanceType;
	private Class<ID> countType;
	private String persistanceTypeName;
	private String countTypeName;

	private boolean showSql = false;

	protected AbstractJDBCDaoImpl(Class<T> persistanceType, Class<ID> countType)
	{
		this.setPersistanceType(persistanceType);
		this.setCountType(countType);
		this.persistanceTypeName = persistanceType.getSimpleName();
		this.countTypeName = countType.getSimpleName();
	}

	/**
	 * @return the dataSource
	 */
	public BasicDataSource getDataSource() 
	{
		return dataSource;
	}
	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(BasicDataSource dataSource) 
	{
		this.dataSource = dataSource;
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
	/**
	 * @return the namingStrategy
	 */
	public NamingStrategy getNamingStrategy() 
	{
		return namingStrategy;
	}
	/**
	 * @param namingStrategy the namingStrategy to set
	 */
	public void setNamingStrategy(NamingStrategy namingStrategy) 
	{
		this.namingStrategy = namingStrategy;
	}

	/**
	 * @return the showSql
	 */
	public boolean isShowSql() {
		return showSql;
	}

	/**
	 * @param showSql the showSql to set
	 */
	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}

	@Override
	public void quickAdd(List<T> list) throws DaoException 
	{
		if (list != null)
		{
			for (T entity : list)
			{
				this.add(entity);
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
		throw new DaoException("Not supported operation.");
	}

	@Override
	public List<T> getAll(int firstResult, int lastResult) throws DaoException 
	{
		throw new DaoException("Not supported operation.");
	}

	@Override
	public T get(ID id) throws DaoException 
	{
		throw new DaoException("Not supported operation.");
	}

	protected PreparedStatement createPreparedStatement(Logger log, String sql, String...columnNames) throws DaoException
	{
		if (showSql)
		{
			log.debug("Attempt to create PreparedStatement for sql query: sql={}, columnNames={}", sql, columnNames);
		}
		if (null == sql)
		{
			throw new DaoException("Can't use null SQL statement");
		}
		try 
		{
			Connection connection = getDataSource().getConnection();
			try
			{
				if ((null == columnNames) || (columnNames.length < 1))
				{
					return connection.prepareStatement(sql);
				}
				else
				{
					return connection.prepareStatement(sql, columnNames);
				}
			}
			catch (SQLException e)
			{
				throw new DaoException("Can't create such preparedStatement for sql=" + sql, e);
			}
		}
		catch (SQLException e) 
		{
			throw new DaoException("Can't get connection to database", e);
		}
	}
	//
	//	@Override
	//	public T get(ID id) throws DaoException 
	//	{
	//		/*		try 
	//		{
	//			Connection con = dataSource.getConnection();
	//			PreparedStatement preparedStatement = null;
	//			String sql = "select "
	//		}
	//		catch (SQLException e) 
	//		{
	//			throw new DaoException("Can't get connection to database", e);
	//		}
	//		// TODO Auto-generated method stub
	//		 */		return null;
	//	}
	//
	//	@Override
	//	public T add(T o) throws DaoException 
	//	{
	//		try 
	//		{
	//			Connection con = dataSource.getConnection();
	//			PreparedStatement preparedStatement = null;
	//			log.debug("{} creating query for insert.", getClass());
	//			String sql = createQuery(INSERT_TYPE);			
	//			log.debug("Created query for insert: sql={}", sql);
	//			try
	//			{
	//				preparedStatement = con.prepareStatement(sql, new String[]{"ID"});
	//				int result = preparedStatement.executeUpdate();
	//				if (result > 0) 
	//				{
	//					ResultSet rs = preparedStatement.getGeneratedKeys();
	//					if (rs.next()) 
	//					{
	//						int id = rs.getInt(1);
	//						log.debug("Generated id: id={}", id);
	//						this.setParameters(o, new Object[]{id}, "ID");
	//						return o;
	//					}
	//					else 
	//					{
	//						throw new DaoException("Id was not generated");
	//					}
	//				}
	//				else 
	//				{
	//					throw new DaoException("Entity of " + persistanceTypeName + " was not added. Entity=" + o.toString());
	//				}
	//			}
	//			catch (SQLException e)
	//			{
	//				throw new DaoException("Can't get add instance of " + persistanceTypeName + " to database", e);
	//			}
	//		}
	//		catch (SQLException e) 
	//		{
	//			throw new DaoException("Can't get connection to database", e);
	//		}
	//	}
	//
	//	@Override
	//	public List<T> getAll() throws DaoException 
	//	{
	//		List<T> resultList = new ArrayList<T>();
	//		return resultList;
	//	}
	//
	//	@Override
	//	public void quickAdd(List<T> list) throws DaoException 
	//	{
	//		// TODO Auto-generated method stub
	//
	//	}
	//
	//	@Override
	//	public long getTotalCount() throws DaoException {
	//		// TODO Auto-generated method stub
	//		return 0;
	//	}
	//
	//	private String createQuery(int type)
	//	{
	//		StringBuilder sql = new StringBuilder();
	//		switch(type)
	//		{
	//		case SELECT_TYPE:
	//			sql.append("select");
	//			break;
	//		case INSERT_TYPE:
	//			sql.append("insert into");
	//			break;
	//		case UPDATE_TYPE:
	//			sql.append("update");
	//			break;
	//		default: break;
	//		}
	//		return sql.toString();
	//	}
	//
	//	private void setParameters(T o, Object[] args, String...names) throws DaoException
	//	{
	//		log.debug("setParameters method: entity={}, args={}, names={}", new Object[]{o, args, names});
	//		if ((null == o) || (null == args) || (names == null) || (names.length != args.length))
	//		{
	//			throw new DaoException("Error in filling fields in " + persistanceType);
	//		}
	//		else
	//		{
	//			try
	//			{
	//				for (int i = 0; i < args.length; i++)
	//				{
	//					String methodName = "set".concat(names[i]);
	//					Method method = persistanceType.getDeclaredMethod(methodName, Object.class);
	//					if (method == null)
	//					{
	//						throw new DaoException("There is no such method \"public void " + methodName + "(Object arg)\" in " + persistanceType);
	//					}
	//					try 
	//					{
	//						method.invoke(o, args[i]);
	//					} 
	//					catch (IllegalAccessException	| IllegalArgumentException | InvocationTargetException e) 
	//					{
	//						throw new DaoException("Can't invoke " + method.toString() + " in " + persistanceType, e);
	//					}
	//				}
	//			} 
	//			catch (NoSuchMethodException | SecurityException e) 
	//			{
	//				throw new DaoException("Can't add such entity of " + persistanceTypeName + ". Error in declaring bean. There is no method \"public void setId(" + persistanceTypeName + ")\"", e);
	//			}
	//		}
	//	}
}

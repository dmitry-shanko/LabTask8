package com.epam.hibernateapp.database.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.SequenceGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.hibernateapp.database.AbstractJDBCDaoImpl;
import com.epam.hibernateapp.database.exception.DaoException;
import com.epam.hibernateapp.model.Position;

public class PositionJDBCDaoImpl extends AbstractJDBCDaoImpl<Position, Integer>
{
	private static final Logger log = LoggerFactory.getLogger(PositionJDBCDaoImpl.class);
	
	private final int QUERY_TYPE_INSERT = 1;
	
	private String addQuery;
	
	protected PositionJDBCDaoImpl(Class<Position> persistanceType, Class<Integer> countType) 
	{
		super(persistanceType, countType);
	}

	@SuppressWarnings("unused")
	private void init()
	{
		log.debug("Debug message for PositionJDBCDaoImpl.class:");
		log.debug("Params: persistanceType={}, persistanceTypeName={}, countType={}, countTypeName={}", new Object[]{getPersistanceType(), getPersistanceTypeName(), getCountType(), getCountTypeName()});
		addQuery = generateSQLQuery(1);
		log.debug("addQuery={}", new Object[]{addQuery});
	}

	@Override
	public Position add(Position o) throws DaoException 
	{
		if (null != o)
		{
			PreparedStatement preparedStatement = this.createPreparedStatement(log, addQuery, new String[] {getNamingStrategy().columnName("id")});
			ResultSet rs = null;
			try
			{
				preparedStatement.setString(1, o.getPositionName());				
				int result = preparedStatement.executeUpdate();
				if (result > 0) 
				{
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) 
					{
						o.setId(rs.getInt(1));
						return o;
					} 
					else 
					{
						throw new DaoException("Id was not generated");
					}
				}
				else 
				{
					throw new DaoException("New instance of Position was not added");
				}
			}
			catch (SQLException e)
			{
				throw new DaoException("Can't add instance of Workstation", e);
			}
			finally
			{
				if (rs != null)
				{
					close(rs);
				}
				else
				{
					close(preparedStatement);
				}
			}
		}
		else
		{
			throw new DaoException("Can't add null entity of " + getPersistanceTypeName());
		}
	}
	
	private String generateSQLQuery(int queryType)
	{
		StringBuilder sb = new StringBuilder("");
		switch (queryType)
		{
		case QUERY_TYPE_INSERT:
			sb.append("INSERT INTO \"ROOT\".\"");
			sb.append(getNamingStrategy().classToTableName(getPersistanceTypeName()));
			sb.append("\" (");
			sb.append(getNamingStrategy().propertyToColumnName("id"));
			sb.append(", ");
			sb.append(getNamingStrategy().propertyToColumnName("positionName"));
			sb.append(") VALUES (");
			sb.append(getPersistanceType().getAnnotation(SequenceGenerator.class).sequenceName());
			sb.append(".NEXTVAL");
			sb.append(", ?)");
			break;
		default: 
			break;
		}
		return sb.toString();
	}

	private void close(Statement statement)
	{
		try
		{
			close(statement.getConnection());
			statement.close();			
		}
		catch (SQLException e)
		{
			log.error("Can't complete closing of statement=" + statement, e);
		}
	}

	private void close(ResultSet resultSet)
	{
		try
		{
			close(resultSet.getStatement());
			resultSet.close();						
		}
		catch (SQLException e)
		{
			log.error("Can't complete closing of resultSet=" + resultSet, e);
		}
	}

	private void close(Connection connection)
	{
		try
		{
			connection.close();			
		}
		catch (SQLException e)
		{
			log.error("Can't complete closing of connection=" + connection, e);
		}
	}
}

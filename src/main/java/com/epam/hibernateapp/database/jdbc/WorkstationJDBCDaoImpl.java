package com.epam.hibernateapp.database.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.SequenceGenerator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.hibernateapp.database.AbstractJDBCDaoImpl;
import com.epam.hibernateapp.database.exception.DaoException;
import com.epam.hibernateapp.model.Address;
import com.epam.hibernateapp.model.City;
import com.epam.hibernateapp.model.Company;
import com.epam.hibernateapp.model.Country;
import com.epam.hibernateapp.model.Employee;
import com.epam.hibernateapp.model.Office;
import com.epam.hibernateapp.model.Position;
import com.epam.hibernateapp.model.Workstation;

public class WorkstationJDBCDaoImpl extends AbstractJDBCDaoImpl<Workstation, Integer>
{
	private static final Logger log = LoggerFactory.getLogger(WorkstationJDBCDaoImpl.class);

	private final int QUERY_TYPE_SELECT = 1;
	private final int QUERY_TYPE_INSERT = 2;

	private String getAllForEmployeeQuery;
	private String addQuery;

	protected WorkstationJDBCDaoImpl(Class<Workstation> persistanceType, Class<Integer> countType) 
	{
		super(persistanceType, countType);
	}

	@SuppressWarnings("unused")
	private void init()
	{
		log.debug("Debug message for WorkstationJDBCDaoImpl.class:");
		log.debug("Params: persistanceType={}, persistanceTypeName={}, countType={}, countTypeName={}", new Object[]{getPersistanceType(), getPersistanceTypeName(), getCountType(), getCountTypeName()});
		getAllForEmployeeQuery = generateSQLQuery(1);
		addQuery = generateSQLQuery(2);
		log.debug("addQuery={}, getAllForEmployeeQuery={}", new Object[]{addQuery, getAllForEmployeeQuery});
	}

	@Override
	public Workstation add(Workstation o) throws DaoException 
	{
		if (null != o)
		{
			PreparedStatement preparedStatement = this.createPreparedStatement(log, addQuery, new String[] {getNamingStrategy().columnName("id")});
			ResultSet rs = null;
			try
			{
				preparedStatement.setInt(1, o.getOffice().getId());
				preparedStatement.setInt(2, o.getPosition().getId());
				preparedStatement.setInt(3, o.getEmployee().getId());				
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
					throw new DaoException("New instance of Workstation was not added");
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

	public Set<Workstation> getWorkstationsForEmployee(Employee employee) throws DaoException
	{
		Set<Workstation> workstations = new HashSet<Workstation>();
		if (null != employee)
		{
			PreparedStatement preparedStatement = createPreparedStatement(log, getAllForEmployeeQuery);
			ResultSet resultSet = null;
			try
			{
				preparedStatement.setInt(1, employee.getId());
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next())
				{
					workstations.add(createWorkstation(resultSet));
				} 
			}
			catch (SQLException e)
			{
				log.error("Can't collect workstations for employee with id={}", employee.getId());
				log.error("SQLException:", e);
				throw new DaoException("Can't collect workstations for employee with id=" + employee.getId(), e);
			}
			finally
			{
				if (resultSet != null)
				{
					close(resultSet);
				}
				else
				{
					close(preparedStatement);
				}
			}
		}
		return workstations;
	}

	private Workstation createWorkstation(ResultSet resultSet) throws DaoException
	{
		Workstation workstation = new Workstation();
		if (null != resultSet)
		{
			try 
			{
				Position position = new Position();			
				position.setPositionName(resultSet.getString(getNamingStrategy().propertyToColumnName("positionName")));
				workstation.setPosition(position);
				Office office = new Office();
				office.setEmployeesNumber(resultSet.getInt("employee_count"));
				Address address = new Address();
				address.setAddress(resultSet.getString(getNamingStrategy().propertyToColumnName("address")));
				City city = new City();
				Country country = new Country();
				city.setCityName(resultSet.getString(getNamingStrategy().propertyToColumnName("cityName")));
				country.setCountryName(resultSet.getString(getNamingStrategy().propertyToColumnName("countryName")));
				city.setCountry(country);
				Company company = new Company();
				company.setCompanyName(resultSet.getString(getNamingStrategy().propertyToColumnName("companyName")));
				address.setCity(city);
				office.setAddress(address);
				office.setCompany(company);
				workstation.setOffice(office);
			} 
			catch (SQLException e) 
			{
				throw new DaoException("Can't create workstation. SQLException", e);
			}
		}
		return workstation;
	}

	private String generateSQLQuery(int queryType)
	{
		StringBuilder sb = new StringBuilder("");
		switch (queryType)
		{
		case QUERY_TYPE_SELECT:
			sb.append("SELECT workstation.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));
			sb.append(", country.");
			sb.append(getNamingStrategy().propertyToColumnName("countryName"));
			sb.append(", city.");
			sb.append(getNamingStrategy().propertyToColumnName("cityName"));
			sb.append(", address.");
			sb.append(getNamingStrategy().propertyToColumnName("address"));
			sb.append(", company.");
			sb.append(getNamingStrategy().propertyToColumnName("companyName"));
			sb.append(", position.");
			sb.append(getNamingStrategy().propertyToColumnName("positionName"));
			sb.append(", (SELECT COUNT(works.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));
			sb.append(") FROM ");
			sb.append(getNamingStrategy().classToTableName("office"));
			sb.append(" offi, ");
			sb.append(getNamingStrategy().classToTableName("workstation"));
			sb.append(" works WHERE offi.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));
			sb.append("=office.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));
			sb.append(" AND works.");
			sb.append(getNamingStrategy().propertyToColumnName("OFFICE_ID"));
			sb.append("=offi.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));
			sb.append(") as employee_count FROM ");
			sb.append(getNamingStrategy().classToTableName("workstation"));
			sb.append(" workstation, ");
			sb.append(getNamingStrategy().classToTableName("office"));
			sb.append(" office, ");
			sb.append(getNamingStrategy().classToTableName("country"));
			sb.append(" country, ");
			sb.append(getNamingStrategy().classToTableName("city"));
			sb.append(" city, ");
			sb.append(getNamingStrategy().classToTableName("address"));
			sb.append(" address, ");
			sb.append(getNamingStrategy().classToTableName("company"));
			sb.append(" company, ");
			sb.append(getNamingStrategy().classToTableName("position"));
			sb.append(" position WHERE workstation.");
			sb.append(getNamingStrategy().propertyToColumnName("EMPLOYEE_ID"));
			sb.append("=? AND workstation.");
			sb.append(getNamingStrategy().propertyToColumnName("OFFICE_ID"));
			sb.append("=office.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));
			sb.append(" AND office.");
			sb.append(getNamingStrategy().propertyToColumnName("COMPANY_ID"));
			sb.append("=company.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));			
			sb.append(" AND office.");
			sb.append(getNamingStrategy().propertyToColumnName("ADDRESS_ID"));
			sb.append("=address.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));			
			sb.append(" AND address.");
			sb.append(getNamingStrategy().propertyToColumnName("CITY_ID"));
			sb.append("=city.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));			
			sb.append(" AND city.");
			sb.append(getNamingStrategy().propertyToColumnName("COUNTRY_ID"));
			sb.append("=country.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));
			sb.append(" AND workstation.");
			sb.append(getNamingStrategy().propertyToColumnName("POSITION_ID"));
			sb.append("=position.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));
			break;
		case QUERY_TYPE_INSERT:
			sb.append("INSERT INTO \"ROOT\".\"");
			sb.append(getNamingStrategy().classToTableName(getPersistanceTypeName()));
			sb.append("\" (");
			sb.append(getNamingStrategy().propertyToColumnName("id"));
			sb.append(", ");
			sb.append(getNamingStrategy().propertyToColumnName("OFFICE_ID"));
			sb.append(", ");
			sb.append(getNamingStrategy().propertyToColumnName("POSITION_ID"));
			sb.append(", ");
			sb.append(getNamingStrategy().propertyToColumnName("EMPLOYEE_ID"));
			sb.append(") VALUES (");
			sb.append(getPersistanceType().getAnnotation(SequenceGenerator.class).sequenceName());
			sb.append(".NEXTVAL");
			sb.append(", ?, ?, ?)");
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
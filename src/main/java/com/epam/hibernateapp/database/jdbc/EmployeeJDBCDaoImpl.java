package com.epam.hibernateapp.database.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

public class EmployeeJDBCDaoImpl extends AbstractJDBCDaoImpl<Employee, Integer>
{
	private static final Logger log = LoggerFactory.getLogger(EmployeeJDBCDaoImpl.class);

	private final int QUERY_TYPE_SELECT = 1;
	private final int QUERY_TYPE_INSERT = 2;
	private final int QUERY_TYPE_COUNT = 3;

	private String getAllQuery;
	private String addQuery;
	private String totalCount;

	private WorkstationJDBCDaoImpl workstationDao;

	private EmployeeJDBCDaoImpl(Class<Employee> persistanceType, Class<Integer> countType) 
	{
		super(persistanceType, countType);
	}

	@SuppressWarnings("unused")
	private void init() throws DaoException
	{
		log.debug("Debug message for EmployeeJDBCDaoImpl.class:");
		log.debug("Params: persistanceType={}, persistanceTypeName={}, countType={}, countTypeName={}", new Object[]{getPersistanceType(), getPersistanceTypeName(), getCountType(), getCountTypeName()});
		if (null == workstationDao)
		{
			throw new DaoException("Can't create " + getClass() + " with no WorkstationJDBCDaoImpl");
		}
		getAllQuery = generateSQLQuery(1);
		addQuery = generateSQLQuery(2);
		totalCount = generateSQLQuery(3);
		log.debug("addQuery={}, totalCount={}, getAllQuery={}", new Object[]{addQuery, totalCount, getAllQuery});
	}
	/**
	 * @return the workstationDao
	 */
	public WorkstationJDBCDaoImpl getWorkstationDao() 
	{
		return workstationDao;
	}
	/**
	 * @param workstationDao the workstationDao to set
	 */
	public void setWorkstationDao(WorkstationJDBCDaoImpl workstationDao) 
	{
		this.workstationDao = workstationDao;
	}

	@Override
	public Employee get(Integer id) throws DaoException 
	{
		throw new DaoException("Method is not supported");
	}

	@Override
	public Employee add(Employee employee) throws DaoException 
	{
		if (null != employee)
		{
			PreparedStatement preparedStatement = this.createPreparedStatement(log, addQuery, new String[] {getNamingStrategy().columnName("id")});
			ResultSet rs = null;
			try
			{
				preparedStatement.setString(1, employee.getFirstName());
				preparedStatement.setString(2, employee.getLastName());
				preparedStatement.setInt(3, (employee.getAddress() != null ? employee.getAddress().getId() : 0));				
				int result = preparedStatement.executeUpdate();
				if (result > 0) 
				{
					rs = preparedStatement.getGeneratedKeys();
					if (rs.next()) 
					{
						employee.setId(rs.getInt(1));
						return employee;
					} 
					else 
					{
						throw new DaoException("Id was not generated");
					}
				}
				else 
				{
					throw new DaoException("New instance of Employee was not added");
				}
			}
			catch (SQLException e)
			{
				throw new DaoException("Can't add instance of Employee", e);
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

	@Override
	public List<Employee> getAll(int firstResult, int lastResult) throws DaoException 
	{
		List<Employee> resultList = new ArrayList<Employee>();
		PreparedStatement preparedStatement = this.createPreparedStatement(log, getAllQuery);	
		ResultSet resultSet = null;
		try
		{
			log.debug("Get All for: firstResult={}, lastResult={}", firstResult, lastResult);
			preparedStatement.setInt(1, firstResult);
			preparedStatement.setInt(2, lastResult);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next())
			{
				Employee prevEmpl = createEmployee(resultSet);
				resultList.add(prevEmpl);
				while (resultSet.next())
				{
					Employee curEmpl = createEmployee(resultSet);
					if (!prevEmpl.getId().equals(curEmpl.getId()))
					{
						prevEmpl = curEmpl;
						resultList.add(prevEmpl);
					}
					else
					{
						prevEmpl.getWorkstations().addAll(curEmpl.getWorkstations());
					}
				}
			} 
		}
		catch (SQLException e)
		{
			throw new DaoException("Can't get instances of Employee", e);
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
		return resultList;
	}

	@Override
	public long getTotalCount() throws DaoException 
	{
		long totalCount = 0;
		PreparedStatement preparedStatement = this.createPreparedStatement(log, this.totalCount);
		ResultSet resultSet = null;
		try
		{
			resultSet = preparedStatement.executeQuery();
			if (resultSet != null) 
			{
				if (resultSet.next()) 
				{
					totalCount = resultSet.getLong(1);
				} 
				else 
				{
					throw new DaoException("Count was not generated");
				}
			}
			else 
			{
				throw new DaoException("No result for such query: sql=" + this.totalCount);
			}
		}
		catch (SQLException e)
		{
			throw new DaoException("Can't get count of Employee", e);
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
		return totalCount;
	}

	private Employee createEmployee(ResultSet resultSet) throws DaoException
	{
		Employee employee = new Employee();
		if (null != resultSet)
		{
			try 
			{
				employee.setId(resultSet.getInt("employee_id"));
				employee.setFirstName(resultSet.getString("employee_firstname"));
				employee.setLastName(resultSet.getString("employee_lastname"));
				Address address = new Address();
				address.setAddress(resultSet.getString("employee_address"));
				employee.setAddress(address);
				City city = new City();
				city.setCityName(resultSet.getString("employee_city"));
				address.setCity(city);
				Country country = new Country();
				country.setCountryName(resultSet.getString("employee_country"));
				city.setCountry(country);				

				Set<Workstation> workstations = new HashSet<Workstation>();
				employee.setWorkstations(workstations);

				String positionName = resultSet.getString("office_positionname");
				if (positionName != null && positionName.length() > 0)
				{
					Workstation workstation = new Workstation();
					workstation.setId(resultSet.getInt("office_work_id"));
					Position position = new Position();
					position.setPositionName(positionName);
					workstation.setPosition(position);
					Office office = new Office();
					office.setEmployeesNumber(resultSet.getInt("office_employee_count"));
					Company company = new Company();
					company.setCompanyName(resultSet.getString("office_companyname"));
					office.setCompany(company);
					address = new Address();
					address.setAddress(resultSet.getString("office_address"));
					city = new City();
					city.setCityName(resultSet.getString("office_cityname"));
					country = new Country();
					country.setCountryName(resultSet.getString("office_countryname"));
					city.setCountry(country);
					address.setCity(city);
					office.setAddress(address);
					workstation.setOffice(office);
					workstations.add(workstation);	
				}
				//				employee.setWorkstations(workstationDao.getWorkstationsForEmployee(employee));
			} 
			catch (SQLException e) 
			{
				throw new DaoException("Can't parse Employee from resultSet", e);
			}
		}
		return employee;
	}

	private String generateSQLQuery(int queryType)
	{
		StringBuilder sb = new StringBuilder("");
		switch (queryType)
		{
		case QUERY_TYPE_SELECT:
			//			sb.append("SELECT * FROM (select row_number() over(order by ");
			//			sb.append("employee.");
			//			sb.append(getNamingStrategy().propertyToColumnName("id"));
			//			sb.append(") rn, employee.");
			//			sb.append(getNamingStrategy().propertyToColumnName("id"));
			//			sb.append(", employee.");
			//			sb.append(getNamingStrategy().propertyToColumnName("firstName"));
			//			sb.append(", employee.");
			//			sb.append(getNamingStrategy().propertyToColumnName("lastName"));
			//			sb.append(", address.");
			//			sb.append(getNamingStrategy().propertyToColumnName("address"));
			//			sb.append(", city.");
			//			sb.append(getNamingStrategy().propertyToColumnName("cityName"));
			//			sb.append(", country.");
			//			sb.append(getNamingStrategy().propertyToColumnName("countryName"));
			//			sb.append(" FROM ");
			//			sb.append(getNamingStrategy().classToTableName("employee"));
			//			sb.append(" employee, ");
			//			sb.append(getNamingStrategy().classToTableName("address"));
			//			sb.append(" address, ");
			//			sb.append(getNamingStrategy().classToTableName("city"));
			//			sb.append(" city, ");
			//			sb.append(getNamingStrategy().classToTableName("country"));
			//			sb.append(" country WHERE address.");
			//			sb.append(getNamingStrategy().propertyToColumnName("id"));
			//			sb.append("=employee.");
			//			sb.append(getNamingStrategy().propertyToColumnName("ADDRESS_ID"));
			//			sb.append(" AND city.");
			//			sb.append(getNamingStrategy().propertyToColumnName("id"));
			//			sb.append("=address.");
			//			sb.append(getNamingStrategy().propertyToColumnName("CITY_ID"));
			//			sb.append(" AND country.");
			//			sb.append(getNamingStrategy().propertyToColumnName("id"));
			//			sb.append("=city.");
			//			sb.append(getNamingStrategy().propertyToColumnName("COUNTRY_ID"));
			//			sb.append(") result_table WHERE rn>? AND rn<=?");
			//			sb.append("");
			/*			sb.append("SELECT employee.ID as employee_id, employee.firstname as employee_firstname, employee.lastname as employee_lastname, empl_addr.address as employee_address, empl_addr.cityname as employee_city, empl_addr.countryname" +
					" as employee_country, works.companyname as office_companyname, works.positionname as office_positionname, works.countryname as office_countryname, works.cityname" +
					" as office_cityname, works.address as office_address, works.empl_count as office_employee_count FROM (SELECT addr.id, " +
					"addr.address, cit.cityname, countr.countryname FROM t_lab_task_8_address addr,  t_lab_task_8_city cit, t_lab_task_8_country countr" +
					" WHERE cit.id=addr.city_id AND countr.id=cit.country_id) empl_addr, (SELECT * FROM (select row_number() over(order by employee.ID) rn, " +
					"employee.address_id, employee.firstname, employee.id, employee.lastname FROM T_LAB_TASK_8_EMPLOYEE employee) WHERE rn >? AND rn<=?) employee left join " +
					"(SELECT company.companyname, workstation.employee_id, position.positionname, country.countryname, city.cityname, address.address, (SELECT COUNT(workst.ID) " +
					"FROM T_LAB_TASK_8_OFFICE offi, T_LAB_TASK_8_WORKSTATION workst WHERE offi.ID=office.ID AND workst.OFFICE_ID=offi.ID) as empl_count FROM " +
					"t_lab_task_8_workstation workstation, t_lab_task_8_office office, t_lab_task_8_position position, t_lab_task_8_company company, t_lab_task_8_country country, t_lab_task_8_city city, t_lab_task_8_address address WHERE workstation." +
					"OFFICE_ID=office.ID AND office.COMPANY_ID=company.ID AND office.ADDRESS_ID=address.ID AND address.CITY_ID=city.ID AND city.COUNTRY_ID=country.ID" +
					" AND workstation.POSITION_ID=position.ID) works on employee.ID=works.employee_id WHERE empl_addr.id=employee.address_id ORDER BY employee.ID");*/
			sb.append("SELECT employee.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));
			sb.append(" as employee_id, employee.");
			sb.append(getNamingStrategy().propertyToColumnName("firstName"));
			sb.append(" as employee_firstname, employee.");
			sb.append(getNamingStrategy().propertyToColumnName("lastName"));
			sb.append(" as employee_lastname, empl_addr.");
			sb.append(getNamingStrategy().propertyToColumnName("address"));
			sb.append(" as employee_address, empl_addr.");
			sb.append(getNamingStrategy().propertyToColumnName("cityName"));
			sb.append(" as employee_city, empl_addr.");
			sb.append(getNamingStrategy().propertyToColumnName("countryName"));
			sb.append(" as employee_country, works.");
			sb.append(getNamingStrategy().propertyToColumnName("companyName"));			
			sb.append(" as office_companyname, works.");
			sb.append(getNamingStrategy().propertyToColumnName("positionName"));			
			sb.append(" as office_positionname, works.");
			sb.append(getNamingStrategy().propertyToColumnName("countryName"));			
			sb.append(" as office_countryname, works.");
			sb.append(getNamingStrategy().propertyToColumnName("cityName"));			
			sb.append(" as office_cityname, works.");
			sb.append(getNamingStrategy().propertyToColumnName("address"));	
			sb.append(" as office_address, works.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));	
			sb.append(" as office_work_id, works.empl_count as office_employee_count FROM (SELECT addr.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));			
			sb.append(", addr.");
			sb.append(getNamingStrategy().propertyToColumnName("address"));			
			sb.append(", cit.");
			sb.append(getNamingStrategy().propertyToColumnName("cityName"));			
			sb.append(", countr.");
			sb.append(getNamingStrategy().propertyToColumnName("countryName"));
			sb.append(" FROM ");
			sb.append(getNamingStrategy().classToTableName("Address"));			
			sb.append(" addr, ");			
			sb.append(getNamingStrategy().classToTableName("City"));			
			sb.append(" cit, ");			
			sb.append(getNamingStrategy().classToTableName("Country"));			
			sb.append(" countr WHERE cit.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));		
			sb.append("=addr.");	
			sb.append(getNamingStrategy().propertyToColumnName("CITY_ID"));		
			sb.append(" AND countr.");	
			sb.append(getNamingStrategy().propertyToColumnName("id"));	
			sb.append("=cit.");	
			sb.append(getNamingStrategy().propertyToColumnName("COUNTRY_ID"));	
			sb.append(") empl_addr, (SELECT * FROM (select row_number() over(order by employee.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));	
			sb.append(") rn, employee.");
			sb.append(getNamingStrategy().propertyToColumnName("ADDRESS_ID"));			
			sb.append(", employee.");
			sb.append(getNamingStrategy().propertyToColumnName("firstName"));
			sb.append(", employee.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));
			sb.append(", employee.");
			sb.append(getNamingStrategy().propertyToColumnName("lastName"));
			sb.append(" FROM ");
			sb.append(getNamingStrategy().classToTableName("Employee"));
			sb.append(" employee) WHERE rn >? AND rn<=?) employee left join (SELECT company.");
			sb.append(getNamingStrategy().propertyToColumnName("companyName"));
			sb.append(", workstation.");			
			sb.append(getNamingStrategy().propertyToColumnName("employee_id"));
			sb.append(", workstation.");			
			sb.append(getNamingStrategy().propertyToColumnName("id"));
			sb.append(", position.");			
			sb.append(getNamingStrategy().propertyToColumnName("positionname"));
			sb.append(", country.");			
			sb.append(getNamingStrategy().propertyToColumnName("countryname"));
			sb.append(", city.");			
			sb.append(getNamingStrategy().propertyToColumnName("cityname"));
			sb.append(", address.");			
			sb.append(getNamingStrategy().propertyToColumnName("address"));
			sb.append(", (SELECT COUNT(workst.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));			
			sb.append(") FROM ");
			sb.append(getNamingStrategy().classToTableName("Office"));
			sb.append(" offi, ");			
			sb.append(getNamingStrategy().classToTableName("Workstation"));
			sb.append(" workst WHERE offi.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));		
			sb.append("=office.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));		
			sb.append(" AND workst.");
			sb.append(getNamingStrategy().propertyToColumnName("OFFICE_ID"));				
			sb.append("=offi.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));				
			sb.append(") as empl_count FROM ");
			sb.append(getNamingStrategy().classToTableName("Workstation"));			
			sb.append(" workstation, ");
			sb.append(getNamingStrategy().classToTableName("Office"));			
			sb.append(" office, ");					
			sb.append(getNamingStrategy().classToTableName("Position"));
			sb.append(" position, ");			
			sb.append(getNamingStrategy().classToTableName("Company"));
			sb.append(" company, ");			
			sb.append(getNamingStrategy().classToTableName("Country"));
			sb.append(" country, ");			
			sb.append(getNamingStrategy().classToTableName("City"));
			sb.append(" city, ");			
			sb.append(getNamingStrategy().classToTableName("Address"));
			sb.append(" address WHERE workstation.");			
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
			sb.append(") works on employee.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));			
			sb.append("=works.");
			sb.append(getNamingStrategy().propertyToColumnName("employee_id"));			
			sb.append(" WHERE empl_addr.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));			
			sb.append("=employee.");
			sb.append(getNamingStrategy().propertyToColumnName("address_id"));			
			sb.append(" ORDER BY employee.");
			sb.append(getNamingStrategy().propertyToColumnName("id"));
			log.info("Query in BIG SELECT: query={}", sb.toString());
			break;
		case QUERY_TYPE_INSERT:
			sb.append("INSERT INTO \"ROOT\".\"");
			sb.append(getNamingStrategy().classToTableName(getPersistanceTypeName()));
			sb.append("\" (");
			sb.append(getNamingStrategy().propertyToColumnName("id"));
			sb.append(", ");
			sb.append(getNamingStrategy().propertyToColumnName("firstName"));
			sb.append(", ");
			sb.append(getNamingStrategy().propertyToColumnName("lastName"));
			sb.append(", ");
			sb.append(getNamingStrategy().propertyToColumnName("ADDRESS_ID"));
			sb.append(") VALUES (");
			sb.append(getPersistanceType().getAnnotation(SequenceGenerator.class).sequenceName());
			sb.append(".NEXTVAL");
			sb.append(", ?, ?, ?)");
			break;
		case QUERY_TYPE_COUNT:
			sb.append("SELECT COUNT(");
			sb.append(getNamingStrategy().propertyToColumnName("id"));
			sb.append(") FROM ");
			sb.append(getNamingStrategy().classToTableName(getPersistanceTypeName()));
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
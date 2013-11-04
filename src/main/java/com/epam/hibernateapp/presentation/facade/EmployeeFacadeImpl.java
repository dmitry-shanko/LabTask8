package com.epam.hibernateapp.presentation.facade;

import java.util.List;

import com.epam.hibernateapp.database.GeneralDao;
import com.epam.hibernateapp.database.exception.DaoException;
import com.epam.hibernateapp.model.Employee;
import com.epam.hibernateapp.presentation.service.DatabaseService;

public class EmployeeFacadeImpl implements EmployeeFacade
{
	private GeneralDao<Employee, Integer> employeeDao;
	private DatabaseService databaseService;
	/**
	 * @param employeeDao the employeeDao to set
	 */
	public void setEmployeeDao(GeneralDao<Employee, Integer> employeeDao) 
	{
		this.employeeDao = employeeDao;
	}
	/**
	 * @param databaseService the databaseService to set
	 */
	public void setDatabaseService(DatabaseService databaseService) 
	{
		this.databaseService = databaseService;
	}
	
	@Override
	public void saveOrUpdate(Employee o) throws DaoException 
	{
		employeeDao.add(o);
	}
	
	@Override
	public Employee get(Integer id) throws DaoException
	{
		Employee employee = employeeDao.get(id);
		return employee;
	}

	@Override
	public List<Employee> getEmployeeList(int firstResult, int lastResult) throws DaoException 
	{
		List<Employee> employees = employeeDao.getAll(firstResult, lastResult);
		return employees;
	}
	
	@Override
	public boolean fillInDatabase()
	{
		return databaseService.fillInDatabase();
	}
	
	@Override
	public long getTotalCount() throws DaoException 
	{
		return employeeDao.getTotalCount();
	}
}
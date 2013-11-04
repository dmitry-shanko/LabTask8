package com.epam.hibernateapp.presentation.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.hibernateapp.database.GeneralDao;
import com.epam.hibernateapp.database.exception.DaoException;
import com.epam.hibernateapp.model.Address;
import com.epam.hibernateapp.model.City;
import com.epam.hibernateapp.model.Company;
import com.epam.hibernateapp.model.Country;
import com.epam.hibernateapp.model.Employee;
import com.epam.hibernateapp.model.Office;
import com.epam.hibernateapp.model.Position;
import com.epam.hibernateapp.model.Workstation;

public class DatabaseServiceImpl implements DatabaseService
{
	private static final Logger log = LoggerFactory.getLogger(DatabaseServiceImpl.class);
	private final int size = 10000;

	private GeneralDao<Country, Integer> countryDao;
	private GeneralDao<City, Integer> cityDao;
	private GeneralDao<Address, Integer> addressDao;
	private GeneralDao<Employee, Integer> employeeDao;
	private GeneralDao<Company, Integer> companyDao;
	private GeneralDao<Office, Integer> officeDao;
	private GeneralDao<Position, Integer> positionDao;
	private GeneralDao<Workstation, Integer> workstationDao;

	/**
	 * @param countryDao the countryDao to set
	 */
	public void setCountryDao(GeneralDao<Country, Integer> countryDao) 
	{
		this.countryDao = countryDao;
	}
	/**
	 * @param cityDao the cityDao to set
	 */
	public void setCityDao(GeneralDao<City, Integer> cityDao) 
	{
		this.cityDao = cityDao;
	}
	/**
	 * @param addressDao the addressDao to set
	 */
	public void setAddressDao(GeneralDao<Address, Integer> addressDao) 
	{
		this.addressDao = addressDao;
	}
	/**
	 * @param employeeDao the employeeDao to set
	 */
	public void setEmployeeDao(GeneralDao<Employee, Integer> employeeDao) 
	{
		this.employeeDao = employeeDao;
	}
	/**
	 * @param companyDao the companyDao to set
	 */
	public void setCompanyDao(GeneralDao<Company, Integer> companyDao) 
	{
		this.companyDao = companyDao;
	}
	/**
	 * @param officeDao the officeDao to set
	 */
	public void setOfficeDao(GeneralDao<Office, Integer> officeDao) 
	{
		this.officeDao = officeDao;
	}
	/**
	 * @param positionDao the positionDao to set
	 */
	public void setPositionDao(GeneralDao<Position, Integer> positionDao) 
	{
		this.positionDao = positionDao;
	}
	/**
	 * @param workstationDao the workstationDao to set
	 */
	public void setWorkstationDao(GeneralDao<Workstation, Integer> workstationDao) 
	{
		this.workstationDao = workstationDao;
	}

	@Override
	public boolean fillInDatabase() 
	{
		log.debug("Params in current implementation of DatabaseService interface: class={}, countryDao={}, cityDao={}, addressDao={}, employeeDao={}, companyDao={}, officeDao={}, positionDao={}, workstationDao={}", new Object[]{getClass(), countryDao, cityDao, addressDao, employeeDao, companyDao, officeDao, positionDao, workstationDao});
		try 
		{
			long startTime = System.currentTimeMillis();
			List<Country> countries = generateCountries(size);
			this.addCountries(countries);
			List<City> cities = generateCities(size);
			this.fillCountryAndCities(countries, cities);
			this.addCities(cities);	
			
			countries = null;
			
			List<Address> addresses = generateAddresses(size);
			this.fillCityAndAddresses(cities, addresses);
			this.addAddresses(addresses);
			
			cities = null;
			
			List<Company> companies = generateCompanies(size);
			this.addCompanies(companies);
			List<Office> officies = generateOffices(size);
			this.fillCompanyAddressAndOfficies(companies, addresses, officies);
			this.addOfficies(officies);
			
			companies = null;
			
			List<Employee> employees = generateEmployees(size);
			this.fillAddressAndEmployees(addresses, employees);
			this.addEmployees(employees);
			
			addresses = null;
			
			List<Position> positions = generatePositions(size);
			this.addPositions(positions);
			List<Workstation> workstations = generateWorkstations(size);
			this.fillEmployeePositionOfficeAndWorkstations(employees, positions, officies, workstations);
			this.addWorkstations(workstations);
			
			long endTime = System.currentTimeMillis();
			log.debug("Result time={}", endTime - startTime);
			employees = null;
			workstations = null;
			officies = null;
			positions = null;
		} 
		catch (DaoException e) 
		{
			log.error("DaoException catched trying to add City:", e);
			return false;
		}
		return true;
	}

	private List<Country> generateCountries(int number)
	{
		List<Country> countries = new ArrayList<Country>();
		String countryName = "Country";
		if (number >= 0)
		{
			for (int i = 0; i < number; i++)
			{
				Country country = new Country();
				country.setCountryName(countryName + (i+1));
				countries.add(country);
			}
		}
		return countries;
	}

	private List<City> generateCities(int number)
	{
		List<City> cities = new ArrayList<City>();
		String cityName = "City";
		if (number >= 0)
		{
			for (int i = 0; i < number; i++)
			{
				City city = new City();
				city.setCityName(cityName + (i+1));
				cities.add(city);
			}
		}
		return cities;
	}

	private void fillCountryAndCities(List<Country> countries, List<City> cities)
	{
		if ((null!= countries) && (null != cities) && (countries.size() > 0))
		{
			int countriesSize = countries.size();
			for (City city : cities)
			{
				int randCountryNumber = (int) (Math.random() * countriesSize);
				city.setCountry(countries.get(randCountryNumber));
			}
		}
	}
	
	private List<Address> generateAddresses(int number)
	{
		List<Address> addresses = new ArrayList<Address>();
		String addressName = "Address";
		if (number >= 0)
		{
			for (int i = 0; i < number; i++)
			{
				Address address = new Address();
				address.setAddress(addressName + (i+1));
				addresses.add(address);
			}
		}
		return addresses;
	}
	
	private void fillCityAndAddresses(List<City> cities, List<Address> addresses)
	{
		if ((null != addresses) && (null != cities) && (cities.size() > 0))
		{
			int citiesSize = cities.size();
			for (Address address : addresses)
			{
				int randCityNumber = (int) (Math.random() * citiesSize);
				address.setCity(cities.get(randCityNumber));
			}
		}
	}
	
	private List<Company> generateCompanies(int number)
	{
		List<Company> companies = new ArrayList<Company>();
		String companyName = "Company";
		if (number >= 0)
		{
			for (int i = 0; i < number; i++)
			{
				Company company = new Company();
				company.setCompanyName(companyName + (i+1));
				companies.add(company);
			}
		}
		return companies;
	}
	
	private List<Office> generateOffices(int number)
	{
		List<Office> officies = new ArrayList<Office>();
		if (number >= 0)
		{
			for (int i = 0; i < number; i++)
			{
				Office office = new Office();
				officies.add(office);
			}
		}
		return officies;
	}
	
	private void fillCompanyAddressAndOfficies(List<Company> companies, List<Address> addresses, List<Office> officies)
	{
		if ((null != addresses) && (null != companies) && (companies.size() > 0) && (addresses.size() > 0))
		{
			int addressesSize = addresses.size();
			int companiesSize = companies.size();
			for (Office office : officies)
			{
				int randAddressNumber = (int) (Math.random() * addressesSize);
				int randCompanyNumber = (int) (Math.random() * companiesSize);
				office.setAddress(addresses.get(randAddressNumber));
				office.setCompany(companies.get(randCompanyNumber));
			}
		}
	}
	
	private List<Employee> generateEmployees(int number)
	{
		List<Employee> employees = new ArrayList<Employee>();
		String firstName = "firstName";
		String lastName = "lastName";
		if (number >= 0)
		{
			for (int i = 0; i < number; i++)
			{
				Employee employee = new Employee();
				employee.setFirstName(firstName + (i + 1));
				employee.setLastName(lastName + (i + 1));
				employees.add(employee);
			}
		}
		return employees;
	}
	
	private void fillAddressAndEmployees(List<Address> addresses, List<Employee> employees)
	{
		if ((null != addresses) && (null != employees) && (addresses.size() > 0))
		{
			int addressesSize = addresses.size();
			for (Employee employee : employees)
			{
				int randAddressNumber = (int) (Math.random() * addressesSize);
				employee.setAddress(addresses.get(randAddressNumber));
			}
		}
	}
	
	private List<Position> generatePositions(int number)
	{
		List<Position> positions = new ArrayList<Position>();
		String positionName = "positionName";
		if (number >= 0)
		{
			for (int i = 0; i < number; i++)
			{
				Position position = new Position();
				position.setPositionName(positionName + (i+1));
				positions.add(position);
			}
		}
		return positions;
	}
	
	private List<Workstation> generateWorkstations(int number)
	{
		List<Workstation> workstations = new ArrayList<Workstation>();
		if (number >= 0)
		{
			for (int i = 0; i < number; i++)
			{
				Workstation workstation = new Workstation();
				workstations.add(workstation);
			}
		}
		return workstations;
	}
	
	private void fillEmployeePositionOfficeAndWorkstations(List<Employee> employees, List<Position> positions, List<Office> officies, List<Workstation> workstations)
	{
		if ((null != employees) && (null != positions) && (null != officies) && (null != workstations) && (employees.size() > 0) && (positions.size() > 0) && (officies.size() > 0))
		{
			int employeesSize = employees.size();
			int positionsSize = positions.size();
			int officiesSize = officies.size();
			for (Workstation workstation : workstations)
			{
				int randEmployeeNumber = (int) (Math.random() * employeesSize);
				int randPositionNumber = (int) (Math.random() * positionsSize);
				int randOfficeNumber = (int) (Math.random() * officiesSize);
				workstation.setEmployee(employees.get(randEmployeeNumber));
				workstation.setOffice(officies.get(randOfficeNumber));
				workstation.setPosition(positions.get(randPositionNumber));
			}
		}
	}

	private void addCities(List<City> cities) throws DaoException
	{
		cityDao.quickAdd(cities);
	}
	
	private void addCountries(List<Country> countries) throws DaoException
	{
		countryDao.quickAdd(countries);
	}
	
	private void addAddresses(List<Address> addresses) throws DaoException
	{
		addressDao.quickAdd(addresses);
	}
	
	private void addCompanies(List<Company> companies) throws DaoException
	{
		companyDao.quickAdd(companies);
	}
	
	private void addOfficies(List<Office> officies) throws DaoException
	{
		officeDao.quickAdd(officies);
	}
	
	private void addEmployees(List<Employee> employees) throws DaoException
	{
		employeeDao.quickAdd(employees);
	}
	
	private void addPositions(List<Position> positions) throws DaoException
	{
		positionDao.quickAdd(positions);
	}
	
	private void addWorkstations(List<Workstation> workstations) throws DaoException
	{
		workstationDao.quickAdd(workstations);
	}
}
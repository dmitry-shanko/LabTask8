package com.epam.hibernateapp.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
@SequenceGenerator(name = "PK", sequenceName = "F_LAB_TASK_8_ADDRESS_SEQ")
@Cacheable(true)
public class Address implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -527383388957926526L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PK")
	private Integer id;
	@Column
	private String address;
	@ManyToOne
	@JoinColumn(name = "CITY_ID")
	private City city;
	@OneToMany(mappedBy = "address")
	private Set<Employee> employees;
	@OneToMany(mappedBy = "address")
	private Set<Office> offices;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the city
	 */
	public City getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(City city) {
		this.city = city;
	}


	/**
	 * @return the employees
	 */
	public Set<Employee> getEmployees() {
		return employees;
	}

	/**
	 * @param employees the employees to set
	 */
	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}

	/**
	 * @return the offices
	 */
	public Set<Office> getOffices() {
		return offices;
	}

	/**
	 * @param offices the offices to set
	 */
	public void setOffices(Set<Office> offices) {
		this.offices = offices;
	}

	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder("Address={id=");
		sb.append(id);
		sb.append("; address=");
		sb.append(address);
		sb.append("; city=");
		sb.append(city);
		sb.append(".}.");
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj) 
		{
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) 
		{
			return false;
		}
		Address address = (Address) obj;
		if (id != null ? !id.equals(address.id) : address.id != null)
		{
			return false;
		}
		if (this.address != null ? !this.address.equals(address.address) : address.address != null) 
		{
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() 
	{
		final int hash = 45;
		int result = 44;
		result = hash * result + (id != null ? id : 0);
		result = hash * result + ((null != address) ? address.hashCode() : 0);
		result = hash * result +  ((null != city) ? (city.getId() != null ? city.getId() + 1 : 1) : 0);
		result = hash * result + (employees != null ? employees.hashCode() : 0);
		result = hash * result + (offices != null ? offices.hashCode() : 0);
		return result;
	}
}
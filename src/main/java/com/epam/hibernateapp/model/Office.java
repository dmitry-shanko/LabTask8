package com.epam.hibernateapp.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;

@Entity
@Table
@SequenceGenerator(name = "PK", sequenceName = "F_LAB_TASK_8_OFFICE_SEQ")
@Cacheable(true)
public class Office implements Serializable
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8069853538152577084L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PK")
	private Integer id;
	@ManyToOne
	@JoinColumn(name = "ADDRESS_ID")
	private Address address;
	@ManyToOne
	@JoinColumn(name = "COMPANY_ID")
	private Company company;
	@OneToMany(mappedBy = "office")
	private Set<Workstation> workstations;
	@Formula(value = "(SELECT COUNT(work.EMPLOYEE_ID) FROM T_LAB_TASK_8_WORKSTATION work WHERE work.OFFICE_ID =	ID)")
	private Integer employeesNumber;
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
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the company
	 */
	public Company getCompany() {
		return company;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompany(Company company) {
		this.company = company;
	}

	/**
	 * @return the workstations
	 */
	public Set<Workstation> getWorkstations() {
		return workstations;
	}

	/**
	 * @param workstations the workstations to set
	 */
	public void setWorkstations(Set<Workstation> workstations) {
		this.workstations = workstations;
	}

	/**
	 * @return the employeesNumber
	 */
	public Integer getEmployeesNumber() {
		return employeesNumber;
	}

	/**
	 * @param employeesNumber the employeesNumber to set
	 */
	public void setEmployeesNumber(Integer employeesNumber) {
		this.employeesNumber = employeesNumber;
	}

	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder("Office={id=");
		sb.append(id);
		sb.append("; company=");
		sb.append(company);
		sb.append("; employeesNumber=");
		sb.append(employeesNumber);
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
		Office office = (Office) obj;
		if (id != null ? !id.equals(office.id) : office.id != null)
		{
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() 
	{
		final int hash = 23;
		int result = 24;
		result = hash * result + (id != null ? id : 0);
		result = hash * result + ((null != employeesNumber) ? employeesNumber : 0);
		result = hash * result +  ((null != address) ? (address.getId() != null ? address.getId() + 1 : 1) : 0);
		result = hash * result +  ((null != company) ? (company.getId() != null ? company.getId() + 1 : 1) : 0);
		result = hash * result + (workstations != null ? workstations.hashCode() : 0);
		return result;
	}
}
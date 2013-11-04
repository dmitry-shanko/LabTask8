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
@SequenceGenerator(name = "PK", sequenceName = "F_LAB_TASK_8_EMPLOYEE_SEQ")
@Cacheable(true)
public class Employee implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3532034336749563724L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PK")
	private Integer id;
	@Column
	private String firstName;
	@Column
	private String lastName;
	@ManyToOne
	@JoinColumn(name = "ADDRESS_ID")
	private Address address;
	@OneToMany(mappedBy = "employee")
	private Set<Workstation> workstations;
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder("Employee={id=");
		sb.append(id);
		sb.append("; firstName=");
		sb.append(firstName);
		sb.append("; lastName=");
		sb.append(lastName);
		sb.append("; address=");
		sb.append(address);
		sb.append("; workstations=");
		sb.append(workstations);
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
		Employee employee = (Employee) obj;
		if (id != null ? !id.equals(employee.id) : employee.id != null)
		{
			return false;
		}
		if (firstName != null ? !firstName.equals(employee.firstName) : employee.firstName != null) 
		{
			return false;
		}
		if (lastName != null ? !lastName.equals(employee.lastName) : employee.lastName != null) 
		{
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() 
	{
		final int hash = 21;
		int result = 22;
		result = hash * result + (id != null ? id : 0);
		result = hash * result + ((null != firstName) ? firstName.hashCode() : 0);
		result = hash * result + ((null != lastName) ? lastName.hashCode() : 0);
		result = hash * result +  ((null != address) ? (address.getId() != null ? address.getId() + 1 : 1) : 0);
		result = hash * result + (workstations != null ? workstations.hashCode() : 0);
		return result;
	}
}
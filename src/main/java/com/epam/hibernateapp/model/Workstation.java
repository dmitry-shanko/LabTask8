package com.epam.hibernateapp.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
@SequenceGenerator(name = "PK", sequenceName = "F_LAB_TASK_8_WORKSTATION_SEQ")
@Cacheable(true)
public class Workstation implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4309420873446609241L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PK")
	private Integer id;
	@ManyToOne
	@JoinColumn(name = "OFFICE_ID")
	private Office office;
	@ManyToOne
	@JoinColumn(name = "POSITION_ID")
	private Position position;
	@ManyToOne
	@JoinColumn(name = "EMPLOYEE_ID")
	private Employee employee;
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
	 * @return the office
	 */
	public Office getOffice() {
		return office;
	}

	/**
	 * @param office the office to set
	 */
	public void setOffice(Office office) {
		this.office = office;
	}

	/**
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * @return the employee
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * @param employee the employee to set
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder("Workstation={id=");
		sb.append(id);
		sb.append("; office=");
		sb.append(office);
		sb.append("; position=");
		sb.append(position);
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
		Workstation workstation = (Workstation) obj;
		if (id != null ? !id.equals(workstation.id) : workstation.id != null)
		{
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() 
	{
		final int hash = 25;
		int result = 26;
		result = hash * result + (id != null ? id : 0);
		result = hash * result +  ((null != office) ? (office.getId() != null ? office.getId() + 1 : 1) : 0);
		result = hash * result +  ((null != position) ? (position.getId() != null ? position.getId() + 1 : 1) : 0);
		result = hash * result +  ((null != employee) ? (employee.getId() != null ? employee.getId() + 1 : 1) : 0);
		return result;
	}
}
package com.epam.hibernateapp.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table
@SequenceGenerator(name = "PK", sequenceName = "F_LAB_TASK_8_COMPANY_SEQ")
@Cacheable(true)
public class Company implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3317426145107108444L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PK")
	private Integer id;	
	@Column
	private String companyName;
	@OneToMany(mappedBy = "company")
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
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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
		StringBuilder sb = new StringBuilder("Company={id=");
		sb.append(id);
		sb.append("; companyName=");
		sb.append(companyName);
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
		Company company = (Company) obj;
		if (id != null ? !id.equals(company.id) : company.id != null)
		{
			return false;
		}
		if (companyName != null ? !companyName.equals(company.companyName) : company.companyName != null) 
		{
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() 
	{
		final int hash = 9;
		int result = 10;
		result = hash * result + (id != null ? id : 0);
		result = hash * result + ((null != companyName) ? companyName.hashCode() : 0);
		result = hash * result + (offices != null ? offices.hashCode() : 0);
		return result;
	}
}
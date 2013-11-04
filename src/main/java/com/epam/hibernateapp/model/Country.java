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
@SequenceGenerator(name = "PK", sequenceName = "F_LAB_TASK_8_COUNTRY_SEQ")
@Cacheable(true)
public class Country implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2826071916576642791L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PK")
	private Integer id;	
	@Column
	private String countryName;	
	@OneToMany(mappedBy = "country")
	private Set<City> cities;
	
	/**
	 * @return the id
	 */
	public Integer getId() 
	{
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) 
	{
		this.id = id;
	}
	/**
	 * @return the countryName
	 */
	public String getCountryName() 
	{
		return countryName;
	}
	/**
	 * @param countryName the countryName to set
	 */
	public void setCountryName(String countryName) 
	{
		this.countryName = countryName;
	}
	/**
	 * @return the cities
	 */
	public Set<City> getCities() 
	{
		return cities;
	}
	/**
	 * @param cities the cities to set
	 */
	public void setCities(Set<City> cities) 
	{
		this.cities = cities;
	}

	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder("Country={id=");
		sb.append(id);
		sb.append("; countryName=");
		sb.append(countryName);;
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
		Country country = (Country) obj;
		if (id != null ? !id.equals(country.id) : country.id != null)
		{
			return false;
		}
		if (countryName != null ? !countryName.equals(country.countryName) : country.countryName != null) 
		{
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() 
	{
		final int hash = 11;
		int result = 12;
		result = hash * result + (id != null ? id : 0);
		result = hash * result + ((null != countryName) ? countryName.hashCode() : 0);
		result = hash * result + (cities != null ? cities.hashCode() : 0);
		return result;
	}
}
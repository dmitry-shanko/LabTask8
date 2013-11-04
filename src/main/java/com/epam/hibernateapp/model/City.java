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
@SequenceGenerator(name = "PK", sequenceName = "F_LAB_TASK_8_CITY_SEQ")
@Cacheable(true)
public class City implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6314931522206213756L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PK")
	private Integer id;	
	@Column
	private String cityName;
	@ManyToOne
	@JoinColumn(name = "COUNTRY_ID")
	private Country country;
	@OneToMany(mappedBy = "city")
	private Set<Address> addresses;
	
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
	 * @return the cityName
	 */
	public String getCityName() 
	{
		return cityName;
	}
	/**
	 * @param cityName the cityName to set
	 */
	public void setCityName(String cityName) 
	{
		this.cityName = cityName;
	}	
	/**
	 * @return the country
	 */
	public Country getCountry() 
	{
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(Country country) 
	{
		this.country = country;
	}
	
	/**
	 * @return the addresses
	 */
	public Set<Address> getAddresses() {
		return addresses;
	}
	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder("City={id=");
		sb.append(id);
		sb.append("; cityName=");
		sb.append(cityName);
		sb.append("; country=");
		sb.append(country);
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
		City city = (City) obj;
		if (id != null ? !id.equals(city.id) : city.id != null)
		{
			return false;
		}
		if (cityName != null ? !cityName.equals(city.cityName) : city.cityName != null) 
		{
			return false;
		}
		return true;
	}
	
	@Override
	public int hashCode() 
	{
		final int hash = 13;
		int result = 14;
		result = hash * result + (id != null ? id : 0);
		result = hash * result + ((null != cityName) ? cityName.hashCode() : 0);
		result = hash * result +  ((null != country) ? (country.getId() != null ? country.getId() + 1 : 1) : 0);
		result = hash * result + (addresses != null ? addresses.hashCode() : 0);
		return result;
	}
}
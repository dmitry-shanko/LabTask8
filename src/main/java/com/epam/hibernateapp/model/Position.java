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
@SequenceGenerator(name = "PK", sequenceName = "F_LAB_TASK_8_POSITION_SEQ")
@Cacheable(true)
public class Position implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3669431135020371311L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "PK")
	private Integer id;
	@Column
	private String positionName;
	@OneToMany(mappedBy = "position")
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
	 * @return the positionName
	 */
	public String getPositionName() {
		return positionName;
	}

	/**
	 * @param positionName the positionName to set
	 */
	public void setPositionName(String positionName) {
		this.positionName = positionName;
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
		StringBuilder sb = new StringBuilder("Position={id=");
		sb.append(id);
		sb.append("; positionName=");
		sb.append(positionName);
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
		Position position = (Position) obj;
		if (id != null ? !id.equals(position.id) : position.id != null)
		{
			return false;
		}
		if (positionName != null ? !positionName.equals(position.positionName) : position.positionName != null) 
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
		result = hash * result + ((null != positionName) ? positionName.hashCode() : 0);
		result = hash * result + (workstations != null ? workstations.hashCode() : 0);
		return result;
	}
}
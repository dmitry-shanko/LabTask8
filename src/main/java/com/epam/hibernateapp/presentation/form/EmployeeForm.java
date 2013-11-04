package com.epam.hibernateapp.presentation.form;

import java.util.List;

import org.apache.struts.action.ActionForm;

import com.epam.hibernateapp.model.Employee;

public class EmployeeForm extends ActionForm
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6751478713881139548L;
	private List<Employee> employeeList;
	private long totalCount;
	private String employeePerPage;
	private String pageNumber;
	private int maxPageNumber;
	/**
	 * @return the employeeList
	 */
	public List<Employee> getEmployeeList() {
		return employeeList;
	}
	/**
	 * @param employeeList the employeeList to set
	 */
	public void setEmployeeList(List<Employee> employeeList) {
		this.employeeList = employeeList;
	}
	/**
	 * @return the totalCount
	 */
	public long getTotalCount() {
		return totalCount;
	}
	/**
	 * @param totalCount the totalCount to set
	 */
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	/**
	 * @return the employeePerPage
	 */
	public String getEmployeePerPage() {
		return employeePerPage;
	}
	/**
	 * @param employeePerPage the employeePerPage to set
	 */
	public void setEmployeePerPage(String employeePerPage) {
		this.employeePerPage = employeePerPage;
	}
	/**
	 * @return the pageNumber
	 */
	public String getPageNumber() {
		return pageNumber;
	}
	/**
	 * @param pageNumber the pageNumber to set
	 */
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	/**
	 * @return the maxPageNumber
	 */
	public int getMaxPageNumber() {
		return maxPageNumber;
	}
	/**
	 * @param maxPageNumber the maxPageNumber to set
	 */
	public void setMaxPageNumber(int maxPageNumber) {
		this.maxPageNumber = maxPageNumber;
	}	
}

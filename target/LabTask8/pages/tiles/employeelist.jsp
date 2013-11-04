<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="/WEB-INF/tags/pageTag.tld" prefix="pgt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<span class="contentNewsTitle"><html:link action="/employee">
		<bean:message key="label.employeelist.list" />
	</html:link></span>
&gt;&gt;
<bean:message key="label.employeelist.list" />
<br>
<br>
<c:set var="entityPerPageMessage" scope="page"><bean:message key="label.employeelist.employeeperpage" /></c:set>
<c:set var="pageNumberMessage" scope="page"><bean:message key="label.employeelist.pagenumber" /></c:set>
<c:set var="buttonMessage" scope="page"><bean:message key="label.employeelist.go" /></c:set>
<pgt:paging action="employee.do" 
entityPerPageAttrName="employeePerPage" 
pageNumberAttrName="pageNumber" 
currentEntityPerPage="${employeeForm.employeePerPage }"
currentPageNumber="${employeeForm.pageNumber }" 
entityPerPageMessage="${entityPerPageMessage}"
pageNumberMessage="${pageNumberMessage}"
buttonMessage="${buttonMessage}" 
maxPageNumber="${employeeForm.maxPageNumber }"/>
<table class="table table-condensed">
	<tbody>
		<tr class="info">
			<th>Id</th>
			<th>First Name</th>
			<th>Last Name</th>
			<th>Address</th>
			<th>Office</th>
		</tr>
	</tbody>
	<logic:iterate id="employee" name="employeeForm"
		property="employeeList">
		<tbody>
			<tr class="info">
				<td><bean:write name="employee" property="id" /></td>
				<td><bean:write name="employee" property="firstName" /></td>
				<td><bean:write name="employee" property="lastName" /></td>
				<td><bean:write name="employee"
						property="address.city.country.countryName" />, <bean:write
						name="employee" property="address.city.cityName" />, <bean:write
						name="employee" property="address.address" /></td>
				<td><logic:iterate id="workstation" property="workstations"
						name="employee">

						<table class="table table-condensed">
							<tbody>
								<tr class="info">
									<th>Company name:</th>
									<td class="table2"><bean:write name="workstation"
											property="office.company.companyName" /></td>
								</tr>
							</tbody>
							<tbody>
								<tr class="info">
									<th>Address:</th>
									<td class="table2"><bean:write name="workstation"
											property="office.address.city.country.countryName" />, <bean:write
											name="workstation" property="office.address.city.cityName" />,
										<bean:write name="workstation"
											property="office.address.address" /></td>
								</tr>
							</tbody>
							<tbody>
								<tr class="info">
									<th>Count of employees:</th>
									<td class="table2"><bean:write name="workstation"
											property="office.employeesNumber" /></td>
								</tr>
							</tbody>
							<tbody>
								<tr class="info">
									<th>Position:</th>
									<td class="table2"><bean:write name="workstation"
											property="position.positionName" /></td>
								</tr>
						</table>
						<br>
					</logic:iterate></td>
			</tr>
	</logic:iterate>
</table>
<br>
<!-- (((employeeForm.pageNumber - 1) * employeeForm.employeePerPage) + 1) : ((employeeForm.pageNumber - 1) * employeeForm.employeePerPage) -->
<bean:message key="label.employeelist.show" />&nbsp;<c:out value="${((((employeeForm.pageNumber - 1) * employeeForm.employeeList.size()) == employeeForm.employeeList.size()) ?  (employeeForm.employeeList.size() > 0 ? employeeForm.employeeList.size() + 1 : employeeForm.employeeList.size()) :   (((employeeForm.pageNumber - 1) * employeeForm.employeePerPage) + 1)  ) }"/> - <c:out value="${(employeeForm.pageNumber - 1) * employeeForm.employeePerPage + employeeForm.employeeList.size() }"/>&nbsp;<bean:message key="label.employeelist.from" />
<bean:write name="employeeForm" property="totalCount"/>
<pgt:paging action="employee.do" 
entityPerPageAttrName="employeePerPage" 
pageNumberAttrName="pageNumber" 
currentEntityPerPage="${employeeForm.employeePerPage }"
currentPageNumber="${employeeForm.pageNumber }" 
entityPerPageMessage="${entityPerPageMessage}"
pageNumberMessage="${pageNumberMessage}"
buttonMessage="${buttonMessage}" 
maxPageNumber="${employeeForm.maxPageNumber }"/>
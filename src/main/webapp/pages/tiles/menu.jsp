<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>


<p class="menu_title">
	<bean:message key="menu.head" />
</p>
<div class="menu_ul">
	<ul>
		<li><html:link action="/employee">
				<bean:message key="menu.employee" />
			</html:link></li>
	</ul>
</div>
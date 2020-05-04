<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h3><span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>  <fmt:message key="showCity.title" /></h3>

<div class="show-box">
	<br />
	
	<p>	
		<strong><fmt:message key="city.id"/>:</strong> ${city.ID}
	</p>
	
	<c:set var="id" value="${city.ID}" scope="request"/>
	
	<br />
	
	<p>
		<strong><fmt:message key="city.ibgeCode"/>:</strong> ${city.IBGE_CODE}
	</p>
	
	<br />
	
	<p>
		<strong><fmt:message key="city.name"/>:</strong> ${city.NAME}
	</p>
	
	<br />
	
	<p>
		<strong><fmt:message key="city.state"/>:</strong> ${city.STATE_ID.INITIALS}
	</p>
</div>
	
<div class="clear-both"></div>
<jsp:include page="/templates/panel/navBack.jsp" />
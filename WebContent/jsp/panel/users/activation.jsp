<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<div>

	<c:choose>
		<c:when test="${successAccountActivation == true }">
			<div class="wrapper-special">
				<div>
					<h2><fmt:message key="user.mailComfirmationHello"/> <strong>${userActivation}</strong>,</h2>
					<h2><fmt:message key="user.mailComfirmationEmail"/> ${emailActivation} <fmt:message key="user.mailComfirmationSuccess"/></h2>
					<h4><fmt:message key="success.registerStore" /></h4>
					<h4><fmt:message key="advertisement.noStoreTxt2" /></h4>
					<br />
					<a href="${linkStoresForm}" type="button" class="btn btn-primary btn-lg"><fmt:message key="success.clickHereStore" /></a>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<h3><fmt:message key="user.mailComfirmationFail"/></h3>
		</c:otherwise>
	</c:choose>
	
	<br />
	<br />
	<a href="${linkHome}"><fmt:message key="page.backHome" /></a>
	
</div>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="wrapper-special">
	<div>
		<h1><fmt:message key="user.accountIncomplete"/></h1>
		<br />
		<a href="${linkUsersForm}" type="button" class="btn btn-primary btn-lg"><fmt:message key="page.backFormUser" /></a>
		<br />
		<br />
		<a href="${linkHome}"><fmt:message key="page.backHome" /></a>
	</div>
</div>
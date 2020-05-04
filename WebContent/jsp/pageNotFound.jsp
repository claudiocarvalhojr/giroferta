<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="wrapper-pagenotfound">
	<div>
		<h1>Ooops!!</h1>
		<h2><fmt:message key="page.pageNotFound" /></h2>
		<br />
		<a href="${linkHome}"><fmt:message key="page.backHome" /></a>
	</div>
</div>
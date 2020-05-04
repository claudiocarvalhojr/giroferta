<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div>
	<a href="Routes?p=${pEdit}&id=${id}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}"><fmt:message key="nav.edit" /></a> | <jsp:include page="navBack.jsp" />
</div>
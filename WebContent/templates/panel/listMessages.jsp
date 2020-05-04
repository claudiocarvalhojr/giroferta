<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div>
	<ul class="list-group">
		<c:forEach items="${messages}" var="message">
			<li class="list-group-item ${typeMessage}"><fmt:message key="${message}"/></li>
			<br>
			
			<script type="text/javascript">
				console.log('MENSAGEM: ${message}');
			</script>
			
		</c:forEach>
	</ul>
</div>
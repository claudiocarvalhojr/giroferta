<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:choose>
	<c:when test="${successSendInstructionsResetPassword == null}">
	
		<h3><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>  <fmt:message key="formSendInstructionsResetPassword.title" /></h3>
		
		<jsp:include page="/templates/panel/listMessages.jsp"/>
		
		<br />
		<div class="clear-both"></div>

		<form action="${linkUsersSendInstructionsResetPassword}" method="POST" name="formResetPassword">
			
			<fmt:message key="sendInstructionsResetPassword.email"/>
			
			<br />
		
			<input type="text" id="email" name="email" value="${email}" class="form-control" placeholder="" aria-describedby="basic-addon1">
			
			<br>
				
			<div class="clear-both"></div>			
		
			<br />
			
			<button type="submit" class="btn btn-primary" name="btSendInstructions"><fmt:message key="form.btSendInstructions"/></button>
					
		</form>
		
	</c:when>
	<c:otherwise>
	
		<h2><fmt:message key="sendInstructionsResetPassword.success" /></h2>
		
		<br />
		<br />
		<a href="${linkHome}"><fmt:message key="page.backHome" /></a>
	
	</c:otherwise>
</c:choose>

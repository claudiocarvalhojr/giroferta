<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:choose>
	<c:when test="${successUpdatePassword == null}">
	
		<h3><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>  <fmt:message key="formUpdatePassword.title" /></h3>
		
		<jsp:include page="/templates/panel/listMessages.jsp"/>
		
		<br />
		<div class="clear-both"></div>
		
		<form action="${linkUsersConfirmUpdatePassword}" method="POST" name="formUpdatePassword">
			
			<fmt:message key="updatePassword.currentPassword"/>
			
			<br />
			
			<input type="password" id="currentPassword" name="currentPassword" value="${currentPassword}" class="form-control" placeholder="" aria-describedby="basic-addon1">
			
			<br>
				
			<fmt:message key="updatePassword.newPassword"/>
			
			<br />
			
			<input type="password" id="newPassword" name="newPassword" value="${newPassword}" class="form-control" placeholder="" aria-describedby="basic-addon1">
			
			<div class="password-tips">
				*<fmt:message key="updatePassword.passwordTip1"/>
				<br />
				*<fmt:message key="updatePassword.passwordTip2"/>
				<br />
				*<fmt:message key="updatePassword.passwordTip3"/>
			</div>
			
			<br />
			
			<fmt:message key="updatePassword.retypePassword"/>
			
			<br />
			
			<input type="password" id="retypePassword" name="retypePassword" value="${retypePassword}" class="form-control" placeholder="" aria-describedby="basic-addon1">
				
			<br />
					
			<div class="clear-both"></div>			
		
			<br />
			
			<button type="submit" class="btn btn-primary" name="btSave"><fmt:message key="form.btSave"/></button>
					
		</form>
		
	</c:when>
	<c:otherwise>
	
		<h2><fmt:message key="updatePassword.success" /></h2>
		
		<br />
		<br />
		<a href="${linkHome}"><fmt:message key="page.backHome" /></a>
	
	</c:otherwise>
</c:choose>

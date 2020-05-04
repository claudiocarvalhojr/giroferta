<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="box-left">
	
	<h3><span class="glyphicon glyphicon-user" aria-hidden="true"></span>  <fmt:message key="panel.loginTitle" /></h3>

	<form action="${linkCheckLogin}" method="POST" name="formLogin" class="form-login">
		<div class="input-group">
		  <span class="input-group-addon" id="basic-addon1">@</span>
		  <input type="text" class="form-control" placeholder="<fmt:message key="panel.loginMailField" />" aria-describedby="basic-addon1" name="emailLogin">
		</div>
		
		<br />
		
		<div class="input-group">
		  <span class="input-group-addon" id="basic-addon1">&nbsp;*&nbsp;</span>
		  <input type="password" class="form-control" placeholder="<fmt:message key="panel.loginPasswordField" />" aria-describedby="basic-addon1" name="passwordLogin">
		</div>
		
		<br />
		<button type="submit" class="btn btn-default"><fmt:message key="panel.loginSignIn" /></button>
	</form>
		
	<c:if test="${loginValidationMessage != null}">
		<ul class="list-group">
			<li class="list-group-item list-group-item-danger">${loginValidationMessage}</li>
		</ul>
	</c:if>
	<h4>
		<fmt:message key="panel.loginForgotYourPasswordMessage" />
		<a href="${linkUsersFormSendInstructionsResetPassword}">
			<span class="label label-default">
				<fmt:message key="panel.loginRedefine" />
			</span>
		</a>
	</h4>
</div>

<div class="box-right">	
	<h3><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>  <fmt:message key="panel.newUserTitle" /></h3>
	
	<a href="${linkUsersForm}" class="btn btn-primary" role="button" style="margin-top: 9px;">
		<fmt:message key="panel.registerButton" />
	</a>
	
	<div style="margin-top: 40px;">
		<h3><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span>  <fmt:message key="panel.loginSocialNetwork" /></h3>
		<div style="margin: 15px 0;">
			<img src="${giroUrl}/resources/images/bt-login-facebook.png" id="bt-login-facebook" style="cursor: pointer; width: 240px;">
		</div>
		<div style="margin: 15px 0;">
			<img src="${giroUrl}/resources/images/bt-login-google.png" id="bt-login-google" style="cursor: pointer; width: 240px;">
		</div>
	</div>

</div>

<div class="clear-both"></div>
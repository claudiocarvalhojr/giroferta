<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="footer-contact-form">

	<form action="${formAction}" method="POST" id="contact-form-footer" name="formContactFooter">
		
		<h3>Preencha seus dados</h3>
		<h5>Entraremos em contato o mais breve possível!</h5>
		
		<div class="form-group">
		  <label for="footer-contact-usr"><fmt:message key="footer.contactFormName"/></label>
		  <input type="text" class="form-control" id="footer-contact-usr">
		</div>
		
		<br />
		
		<div class="form-group">
		  <label for="footer-contact-mail"><fmt:message key="footer.contactFormMail"/></label>
		  <input type="text" class="form-control" id="footer-contact-mail">
		</div>
		
		<br />
		
		<div class="form-group">
		  <label for="footer-contact-subject"><fmt:message key="footer.contactFormSubject"/></label>
		  <input type="text" class="form-control" id="footer-contact-subject">
		</div>
		
		<br />
		
		<div class="form-group" style="margin-bottom: 27px;">
		  <label for="footer-contact-message"><fmt:message key="footer.contactFormMessage"/></label>
		  <textarea class="form-control" rows="5" id="footer-contact-message"></textarea>
		</div>
		
		<button type="button" class="btn btn-primary bt-send-contact-footer" name="btSave"><fmt:message key="form.btSave"/></button>
		<button type="button" class="btn btn-secondary bt-cancel-contact" name="btCancel"><fmt:message key="form.cancel"/></button>
	
	</form>
	
</div>

<div class="store-contact-form">
	<c:choose>
		<c:when test="${currentUser != null}">
			<form action="${formAction}" method="POST" id="store-contact-form" name="formContactStore">
				
				<h3>Mensagem para a loja</h3>
				-
				<div class="form-group" style="margin-bottom: 27px;">
				  <label for="message"><fmt:message key="footer.contactFormMessage"/></label>
				  <textarea class="form-control ta-message" rows="5" id="message"></textarea>
				</div>
				
				<button type="button" class="btn btn-primary bt-enviar-msg" name="btSave"><fmt:message key="form.btSend"/></button>
				<button type="button" class="btn btn-secondary bt-cancel-store-contact" name="btCancel"><fmt:message key="form.cancel"/></button>
			
			</form>
		</c:when>
		<c:otherwise>
			<div class="must-logged">
				Você precisa fazer <a href="${linkLogin}">login</a> para enviar uma mensagem.
				<br/><br/>
				<button type="button" class="btn btn-link bt-cancel-store-contact" name="btCancel"><fmt:message key="form.cancel"/></button>
			</div>
		</c:otherwise>
	</c:choose>
</div>
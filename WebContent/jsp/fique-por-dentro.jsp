<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="wrapper-panel">
	<div class="box-center">
		<form action="${linkNewsletterLists}" method="POST">
		<br />
		<br />
		
		<span class="span-newsletter-ttl">
			<fmt:message key="fiquepordentro.newsletterMessage" />
		</span>
		
		<br />
		<br />
		
		<span class="span-newsletter-iwant">EU QUERO!!! <span class="glyphicon glyphicon-heart-empty"></span></span>
		
		<br />
		<br />
		
		<span class="span-newsletter-sub">
			<fmt:message key="fiquepordentro.newsletterMessageSub" />
		</span>
		
		<br />
		<br />
		<br />
		
		<div class="newsletter-fields">
			<input type="text" class="name-newsletter form-control" value="<fmt:message key="footer.name" />" />
			<input type="text" class="mail-newsletter form-control" value="<fmt:message key="footer.mail" />" />
		</div>
		
		<br />
		<a class="btn btn-primary btn-signup-newsletter" href="javascript:void(0);" role="button">
			<fmt:message key="footer.register" />
		</a>
		</form>
		
		<hr >
		
		<div class="newsletter-social">
			<span class="span-newsletter-iwant">
				<fmt:message key="fiquepordentro.social1" />
			</span>
			
			<br />
			<br />
			
			<span class="span-newsletter-social-txt">
				<fmt:message key="fiquepordentro.social2" />
			</span>
			
			<br />
			
			<span class="glyphicon glyphicon-hand-down newsletter-hand-down"></span>
		</div>
		
	</div>
</div>
<div class="clear-both"></div>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="footer-site">
	<span style="color: white;">Giroferta LTDA - CNPJ 23.731.274/0001-56</span>
	<br />
	<br />
	<a href="http://www.facebook.com.br/girofertaoficial">
		<img alt="Facebook" src="${giroUrl }/resources/images/ico-facebook.png">
	</a>
	<a href="http://www.instagram.com/giroferta">
		<img alt="Instagram" src="${giroUrl }/resources/images/ico-instagram.png">
	</a>
	<a href="http://www.twitter.com/giroferta">
		<img alt="Twitter" src="${giroUrl }/resources/images/ico-twitter.png">
	</a>
	<a href="javascript:void(0);" class="footer-contact-button">
		<img alt="Mail" src="${giroUrl }/resources/images/ico-mail.png">
	</a>
	
	<br />
	
	<span class="span-newsletter">
		<fmt:message key="footer.newsletterMessage" />
	</span>
	
	<br />
	<br />
	
	<input type="text" class="name-newsletter" value="<fmt:message key="footer.name" />" />
	<input type="text" class="mail-newsletter" value="<fmt:message key="footer.mail" />" />
	<a class="btn btn-primary btn-signup-newsletter" href="javascript:void(0);" role="button">
		<fmt:message key="footer.register" />
	</a>
	
	<jsp:include page="/jsp/contactForm.jsp"/>
</div>

<div style="text-align: center; background-color: #2C3B42;">
	<span id="siteseal"><script async type="text/javascript" src="https://seal.godaddy.com/getSeal?sealID=oDMukn15SKcIy3sRn5KlcytbhN7cDLL60xapFUGncV57IaZGGFa2uwByf6mU"></script></span>
</div>
<div id="wait" class="loading">
	<div id="progressBar">
	  <div id="progress">
	    <div id="label"></div>
	  </div>
	</div>
</div>
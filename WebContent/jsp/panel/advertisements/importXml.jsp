<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/templates/panel/listMessages.jsp"/>

<br />
<div class="clear-both"></div>

<div>
	<form action="${linkAdvertisementsImportRunXml}" method="POST" name="formImportAdvertisementsXml">
		<div>
			<h3><fmt:message key="advertisement.importYourAdvertisements"/></h3>
			<br />
			<fmt:message key="advertisement.importUrlXml"/>
			<br />
			<input type="text" id="tfUrlXml" name="tfUrlXml" value="${currentUser.url_xml}" style="width: 400px;">
			<br />
			<br />
			<button type="submit" class="btn btn-primary" name="btImport"><fmt:message key="advertisement.linkImport"/></button>
			<br />
			<br />
			<br />
			<a href="${linkHome}"><fmt:message key="page.backHome" /></a>
		</div>
	</form>
</div>
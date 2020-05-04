<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h3><fmt:message key="admin.title.sales" /></h3>

<ul class="submenu">
  <li><a href="${linkVendorNewAccount}"><fmt:message key="vendor.menu.newAccount" /></a></li>
  <li><a href="${linkVendorCourtesyForm}"><fmt:message key="vendor.menu.courtesy" /></a></li>
  <li><a href="${linkVendorMyAccountsList}"><fmt:message key="vendor.menu.myAccounts" /></a></li>
  <li style="margin-top: 15px;"><a href="${linkHome}"><fmt:message key="page.backHome" /></a></li>
</ul>

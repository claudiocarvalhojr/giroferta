<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h3><fmt:message key="admin.title.manager" /></h3>

<ul class="submenu">
  <li><a href="${linkAdminManageAccountsForm}"><fmt:message key="admin.submenu.accounts" /></a></li>
  <li><a href="${linkAdminJobs}"><fmt:message key="admin.submenu.jobs" /></a></li>
  <li><a href="${linkAdminReports}"><fmt:message key="admin.submenu.reports" /></a></li>
  <li style="margin-top: 15px;"><a href="${linkHome}"><fmt:message key="page.backHome" /></a></li>
</ul>
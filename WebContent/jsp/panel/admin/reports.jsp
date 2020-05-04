<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h3><fmt:message key="admin.title.reports" /></h3>

<ul class="submenu">
  <li><a href="${linkAdminReportByAdv}"><fmt:message key="admin.reports.byAdv" /></a></li>
  <li><a href="${linkAdminReportByDate}"><fmt:message key="admin.reports.byDate" /></a></li>
  <li style="margin-top: 15px;"><a href="${linkAdminManager}"><fmt:message key="admin.back" /></a></li>
</ul>

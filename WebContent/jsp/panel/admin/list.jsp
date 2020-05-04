<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div style="width: 100%;">
	<div style="width: 100%;">
		<div style="float: left; width: 70%;">
			<a class="btn btn-default" href="${linkVendorNewAccount}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}" role="button">
				<span class="glyphicon glyphicon-plus"></span>
				<fmt:message key="vendor.list.link.newAccount"/>
			</a>
  		</div>
<%-- 		<div style="float: right; width: 30%; text-align: right">
			<jsp:include page="/templates/panel/searchList.jsp" />
		</div>
 --%>		<div style="clear: both;"><br></div>
	</div>
	<div>
		<jsp:include page="/templates/panel/listMessages.jsp"/>
	</div>
	<div style="width: 100%;">
		<div class="panel panel-default">
			<!-- Default panel contents -->
			<div class="panel-heading"><fmt:message key="vendor.list.title"/></div>
			<!-- Table -->
				<table class="table table-striped table-hover table-responsive">
				<thead>
					<tr align="left">
						<th style="width: 10%"><fmt:message key="vendor.list.id"/></th>
						<th style="width: 28%"><fmt:message key="vendor.list.name"/></th>
						<th style="width: 17%"><fmt:message key="vendor.list.cpfCnpj"/></th>
						<th style="width: 18%"><fmt:message key="vendor.list.created_at"/></th>
						<th style="width: 15%"><fmt:message key="vendor.list.url"/></th>
						<th style="width: 12%"><fmt:message key="vendor.list.balance"/></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="user">
					
						
						<tr title="${advertisement.id} - ${advertisement.title}">
							<td>${user.id}</td>
							<c:choose>
								<c:when test="${user.type_person == 'J'}">
									<c:choose>
										<c:when test="${fn:length(user.company_name) > 25}">
											<c:set var="stringAux" value="${fn:substring(user.company_name, 0, 24)}" />
											<td>${stringAux}...</td>
										</c:when>
										<c:otherwise>
											<td>${user.company_name}</td>
										</c:otherwise>
									</c:choose>
									<td align="left">${user.cnpj}</td>
								</c:when>
								<c:when test="${user.type_person == 'F'}">
									<c:choose>
										<c:when test="${fn:length(user.name) > 25}">
											<c:set var="stringAux" value="${fn:substring(user.name, 0, 24)}" />
											<td>${stringAux}...</td>
										</c:when>
										<c:otherwise>
											<td>${user.name }</td>
										</c:otherwise>
									</c:choose>
									<td align="left">${user.cpf}</td>
								</c:when>
							</c:choose>
							<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${user.created_at}"/></td>
							<td align="left">/${user.url}</td>					
							<td align="left">
								<fmt:formatNumber value="${user.balance}" type="currency"/>
							</td>					
						</tr>
					</c:forEach>
				</tbody>
	 		</table>
		</div>
	</div>
	<div style="width: 100%; text-align: center;">
		<br>
		<jsp:include page="/templates/panel/navList.jsp"/>
	</div>
</div>
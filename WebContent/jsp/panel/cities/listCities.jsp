<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div style="width: 100%;">
	<div style="width: 100%;">
		<div style="float: left; width: 70%;">
			<a class="btn btn-default" href="Routes?p=newCity&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}" role="button">
				<span class="glyphicon glyphicon-plus"></span>
				<fmt:message key="listCities.linkNewCity"/>
			</a>
		</div>
		<div style="float: right; width: 30%; text-align: right">
			<jsp:include page="/templates/panel/searchList.jsp" />
		</div>
		<div style="clear: both;"><br></div>
	</div>
	<div>
		<jsp:include page="/templates/panel/listMessages.jsp"/>
	</div>
	<div style="width: 100%;">
		<div class="panel panel-default">
			<!-- Default panel contents -->
			<div class="panel-heading"><fmt:message key="listCities.title"/></div>
			<!-- Table -->
				<table class="table">
				<thead>
					<tr align="left">
						<th style="width: 5%"><fmt:message key="city.id"/></th>
						<th style="width: 10%"><fmt:message key="city.ibgeCode"/></th>
						<th style="width: 70%"><fmt:message key="city.name"/></th>
						<th style="width: 5%"><fmt:message key="city.state"/></th>
						<th style="width: 10%" align="right"></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="city">
						<tr>
							<td>${city.ID}</td>
							<td>${city.IBGE_CODE}</td>
							<td>${city.NAME}</td>
							<td>${city.STATE_ID.INITIALS}</td>
							<td align="right">
								<c:set var="id" value="${city.ID}" scope="request"/>
								<jsp:include page="/templates/panel/navButtonsList.jsp"/>
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
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div style="width: 100%;">
	<div style="width: 100%;">
		<div style="float: left; width: 70%;">
			<a class="btn btn-default" href="${linkStoresForm}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}" role="button">
				<span class="glyphicon glyphicon-plus"></span>
				<fmt:message key="listStores.linkNewStore"/>
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
			<div class="panel-heading"><fmt:message key="listStores.title"/></div>
			<!-- Table -->
				<table class="table table-striped table-hover table-responsive">
				<thead>
					<tr align="left">
						<th style="width: 5%"><fmt:message key="store.id"/></th>
						<th style="width: 27%"><fmt:message key="store.name"/></th>
						<th style="width: 22%"><fmt:message key="store.filial"/></th>
						<th style="width: 31%"><fmt:message key="store.cityState"/></th>
						<th style="width: 5%"><fmt:message key="store.state"/></th>
						<th style="width: 10%" align="right"></th>
					</tr>	
				</thead>
				<tbody>
					<c:forEach items="${list}" var="store">
						<tr>
							<td>${store.id}</td>
							<c:choose>
								<c:when test="${fn:length(store.name) > 30}">
									<c:set var="stringAux" value="${fn:substring(store.name, 0, 29)}" />
									<td>${stringAux} ...</td>
								</c:when>
								<c:otherwise>
									<td>${store.name}</td>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${fn:length(store.filial) > 30}">
									<c:set var="stringAux" value="${fn:substring(store.filial, 0, 29)}" />
									<td>${stringAux} ...</td>
								</c:when>
								<c:otherwise>
									<td>${store.filial}</td>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${fn:length(store.city_id.name) > 30}">
									<c:set var="stringAux" value="${fn:substring(store.city_id.name, 0, 29)}" />
									<td>${stringAux} ...</td>
								</c:when>
								<c:otherwise>
									<td>${store.city_id.name}</td>
								</c:otherwise>
							</c:choose>
							<td>${store.city_id.state_id.initials}</td>
							<td align="right">
								<div class="nav-list">
									<a href="${linkStoresForm}&store.id=${store.id}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}">
										<span class="glyphicon glyphicon-pencil"></span>
									</a>
									<a href="javascript:void(0);" class="remove-item-buttom">
										<span class="glyphicon glyphicon-trash"></span>
									</a>
									<div class="remove-confirmation">
										<fmt:message key="nav.removeBox.text" />
										<br >
										<a class="btn btn-danger" href="${linkStoresDelete}&store.id=${store.id}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}" role="button">
											<fmt:message key="nav.removeBox.remove" />
										</a>
										<a class="btn btn-default bt-remove-cancel" href="javascript:void(0);" role="button">
											<fmt:message key="nav.removeBox.cancel" />
										</a>
									</div>
								</div>								
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
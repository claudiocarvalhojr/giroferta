<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:if test="${currentUser.balance < currentUser.click_values_id.value}">
	<div class="panel panel-warning">
		<div class="panel-heading"><fmt:message key="listAdvertisements.nocredit1" /></div>
	  	<div class="panel-body">
		    <fmt:message key="listAdvertisements.nocredit2" />
	  	</div>
	</div>
</c:if>

<div style="width: 100%;">
	<div style="width: 100%;">
		<div style="float: left; width: 70%;">
			<a class="btn btn-default" href="${linkAdvertisementsForm}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}" role="button">
				<span class="glyphicon glyphicon-plus"></span>
				<fmt:message key="listAdvertisements.linkNewAdvertisement"/>
			</a>
			<c:if test="${currentUser.import_xml == 0}">
				<a class="btn btn-default" href="${linkAdvertisementsImportCsvTxt}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}" role="button">
					<span class="glyphicon glyphicon-plus"></span>
					<fmt:message key="listAdvertisements.linkImportCsvTxt"/>
				</a>
			</c:if>
<!-- 
			<a class="btn btn-default" href="${linkAdvertisementsImportXml}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}" role="button">
				<span class="glyphicon glyphicon-plus"></span>
				<fmt:message key="listAdvertisements.linkImportXml"/>
			</a>
 -->
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
			<div class="panel-heading"><fmt:message key="listAdvertisements.title"/></div>
			<!-- Table -->
				<table class="table table-striped table-hover table-responsive">
				<thead>
					<tr align="left">
						<th style="width: 10%"><fmt:message key="advertisement.id"/></th>
						<th style="width: 40%"><fmt:message key="advertisement.title"/></th>
						<th style="width: 18%"><fmt:message key="advertisement.created_at"/></th>
						<th style="width: 14%"><fmt:message key="advertisement.availability"/></th>
						<th style="width: 8%"><fmt:message key="advertisement.publishUnpublish"/></th>
						<th style="width: 10%" align="right"></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="advertisement">
					
						
						<tr title="${advertisement.id} - ${advertisement.title}">
							<td onclick="location.href='${giroUrl}${advertisement.url}'" style="cursor: pointer;">${advertisement.id}</td>
							<c:choose>
								<c:when test="${fn:length(advertisement.title) > 30}">
									<c:set var="stringAux" value="${fn:substring(advertisement.title, 0, 29)}" />
									<td onclick="location.href='${giroUrl}${advertisement.url}'" style="cursor: pointer;">${stringAux} ...</td>
								</c:when>
								<c:otherwise>
									<td onclick="location.href='${giroUrl}${advertisement.url}'" style="cursor: pointer;">${advertisement.title}</td>
								</c:otherwise>
							</c:choose>
							<td onclick="location.href='${giroUrl}${advertisement.url}'" style="cursor: pointer;"><fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${advertisement.created_at}"/></td>
							<td align="center" onclick="location.href='${giroUrl}${advertisement.url}'" style="cursor: pointer;">${advertisement.availability}</td>
							<td align="center">
								<div>
									<c:choose>
										<c:when test="${advertisement.status == 1}">
											<a class="label label-success" href="${linkAdvertisementsPublish}&advertisement.id=${advertisement.id}">
												<fmt:message key="nav.publishBox.publish" />
											</a>
										</c:when>
										<c:when test="${advertisement.status == 2}">
											<a class="label label-danger unpublish-item-buttom" href="javascript:void(0);">
												<fmt:message key="nav.unpublishBox.unpublish" />
											</a>
											<div class="unpublish-confirmation">
												<fmt:message key="nav.unpublishBox.text" />
												<br >
												<a class="btn btn-danger" href="${linkAdvertisementsUnpublish}&advertisement.id=${advertisement.id}" role="button">
													<fmt:message key="nav.unpublishBox.unpublish" />
												</a>
												<a class="btn btn-default bt-remove-cancel" href="javascript:void(0);" role="button">
													<fmt:message key="nav.removeBox.cancel" />
												</a>
											</div>
										</c:when>
									</c:choose>
								</div>
							</td>					
							<td align="right">
								<div class="nav-list">
	<%--
									<a href="${linkAdvertisementsShow}&advertisement.id=${advertisement.id}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}">
										<span class="glyphicon glyphicon-eye-open"></span>
									</a>
	--%>
									<a href="${linkAdvertisementsForm}&advertisement.id=${advertisement.id}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}">
										<span class="glyphicon glyphicon-pencil"></span>
									</a>
									<a href="javascript:void(0);" class="remove-item-buttom">
										<span class="glyphicon glyphicon-trash"></span>
									</a>
									<div class="remove-confirmation">
										<fmt:message key="nav.removeBox.text" />
										<br >
										<a class="btn btn-danger" href="${linkAdvertisementsDelete}&advertisement.id=${advertisement.id}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}" role="button">
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
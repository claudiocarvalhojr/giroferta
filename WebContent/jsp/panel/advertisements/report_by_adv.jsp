<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
	google.charts.load('current', {'packages':['bar']});
	google.charts.setOnLoadCallback(drawChart);
	
	function drawChart() {
		
		type_report = '${type_report}';
		
		  var data;
		  var type_report = '${type_report}';
		  if (type_report == 'gross') {
			  data = google.visualization.arrayToDataTable([
			    ['Anúncios', 'Cliques'],
			    <c:forEach items="${list}" var="advertisement">
					['${advertisement.id}', '${advertisement.gross_click}'],
				</c:forEach>
			  ]);
		  }
		  else if (type_report == 'count') {
			  data = google.visualization.arrayToDataTable([
   			    ['Anúncios', 'Cliques'],
   			    <c:forEach items="${list}" var="advertisement">
   					['${advertisement.id}', '${advertisement.count_click}'],
   				</c:forEach>
   			  ]);
		  }
		  else if (type_report == 'purchase') {
			  data = google.visualization.arrayToDataTable([
   			    ['Anúncios', 'Cliques'],
   			    <c:forEach items="${list}" var="advertisement">
   					['${advertisement.id}', '${advertisement.listHistory_purchases.size()}'],
   				</c:forEach>
   			  ]);
		  }
		  else {
			  data = google.visualization.arrayToDataTable([
   			    ['Anúncios', 'Cliques'],
   			    <c:forEach items="${list}" var="advertisement">
   					['${advertisement.id}', '${advertisement.count_click}'],
   				</c:forEach>
   			  ]);
		  }

		  var options = {
		    chart: {
		      title: 'Performance de anúncios',
		      subtitle: '(${subtitleReport})',
              width: '100%'
		    }
//		    bars: 'horizontal',
		  };

		  var chart = new google.charts.Bar(document.getElementById('columnchart_material'));

		  chart.draw(data, options);
		}
	
		
//		window.setTimeout('location.reload()', 60000);
</script>

<div>
	<div class="well well-lg" style="padding-top: 20px !important;">
		<h4><fmt:message key="report.advertisement.title" /></h4>
		<h5><fmt:message key="report.advertisement.balance" /> <fmt:formatNumber value="${currentUser.balance}" type="currency"/></h5>
		<h5><fmt:message key="report.advertisement.value_click" /> <fmt:formatNumber value="${currentUser.click_values_id.value}" type="currency"/></h5>
		<h5><fmt:message key="report.advertisement.gross_click" /> ${currentUser.gross_click} (www.giroferta.com/${currentUser.url})</h5>
	</div>
	
	<br />

	<c:if test="${list.size() > 0}">
	
		<div id="columnchart_material" style="width: 100%; height: 400px;"></div>
	
		<br />
		<br />
			
		<table class="table table-striped table-hover table-responsive">
			<thead>
				<tr>
					<th style="width: 5%"><fmt:message key="report.advertisement.column.id" /></th>
					<th style="width: 30%"><fmt:message key="report.advertisement.column.title" /></th>
					<th style="width: 15%"><a href="${linkAdvertisementsReportByAdv}?type_report=gross"><fmt:message key="report.advertisement.column.gross_click" /></a></th>
					<th style="width: 10%"><a href="${linkAdvertisementsReportByAdv}?type_report=count"><fmt:message key="report.advertisement.column.count_click" /></a></th>
					<th style="width: 15%"><a href="${linkAdvertisementsReportByAdv}?type_report=purchase"><fmt:message key="report.advertisement.column.purchases" /></a></th>
					<th style="width: 10%"><fmt:message key="report.advertisement.column.qtda_images" /></th>
					<th style="width: 15%"><fmt:message key="report.advertisement.column.status" /></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${listCopy}" var="advertisement">
					<tr title="${advertisement.id} - ${advertisement.title}" onclick="location.href='${giroUrl}${advertisement.url}'" style="cursor: pointer;">
						<td>${advertisement.id}</td>
						<c:choose>
							<c:when test="${fn:length(advertisement.title) > 30}">
								<c:set var="stringAux" value="${fn:substring(advertisement.title, 0, 29)}" />
								<td>${stringAux} ...</td>
							</c:when>
							<c:otherwise>
								<td>${advertisement.title}</td>
							</c:otherwise>
						</c:choose>
						<td align="center">${advertisement.gross_click}</td>
						<td align="center">${advertisement.count_click}</td>
						<td align="center">
							<c:choose>
								<c:when test= "${not empty advertisement.listHistory_purchases}">
									${advertisement.listHistory_purchases.size()}
								</c:when>
								<c:otherwise>0</c:otherwise>
							</c:choose>
						</td>
						<td align="center">${advertisement.qtda_images}</td>
						<td>
							<c:choose>
								<c:when test="${advertisement.status == 1}"><fmt:message key="report.advertisement.status1" /></c:when>
								<c:when test="${advertisement.status == 2}"><fmt:message key="report.advertisement.status2" /></c:when>
								<c:when test="${advertisement.status == 3}"><fmt:message key="report.advertisement.status3" /></c:when>
							</c:choose>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<div style="width: 100%; text-align: center;">
			<jsp:include page="/templates/panel/navList.jsp"/>
		</div>

	
<%-- 		<c:forEach items="${listCopy}" var="ads">
			<c:if test= "${not empty ads.listHistory_purchases}">
				<c:forEach items="${ads.listHistory_purchases}" var="purchases">
					<c:if test="${purchases.created_at >= '2017-01-01 00:00:00' && purchases.created_at <= '2017-01-09 23:59:59'}">
						<br />
						${purchases.id} - <fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${purchases.created_at}"/>
					</c:if>
				</c:forEach>
			</c:if>
		</c:forEach> --%>
	
	
	</c:if>
	
 </div>
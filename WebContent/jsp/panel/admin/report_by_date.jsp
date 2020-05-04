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
			  data = google.visualization.arrayToDataTable([
   			    ['Anúncios', 'Cliques'],
   			    <c:forEach items="${list}" var="advertisement">
   					['${advertisement.advertisement_id.id}', '${advertisement.id}'],
   				</c:forEach>
   			  ]);

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

	$( function() {
		$( "#datepicker" ).datepicker();
	});

</script>

<div>
	<form action="${linkAdminReportByDate}" name="formReportByDate" method="POST">
		<div style="width: 100%; display: table;">
			<div style="display: table-layout; text-align: center; width: 100%;">
				<select class="form-control" name="selMonth" style="width: 130px; display: inline-block;">
					<option value="0" <c:if test="${month_report == '0'}">SELECTED</c:if>>JANEIRO</option>
					<option value="1" <c:if test="${month_report == '1'}">SELECTED</c:if>>FEVEREIRO</option>
					<option value="2" <c:if test="${month_report == '2'}">SELECTED</c:if>>MARÇO</option>
					<option value="3" <c:if test="${month_report == '3'}">SELECTED</c:if>>ABRIL</option>
					<option value="4" <c:if test="${month_report == '4'}">SELECTED</c:if>>MAIO</option>
					<option value="5" <c:if test="${month_report == '5'}">SELECTED</c:if>>JUNHO</option>
					<option value="6" <c:if test="${month_report == '6'}">SELECTED</c:if>>JULHO</option>
					<option value="7" <c:if test="${month_report == '7'}">SELECTED</c:if>>AGOSTO</option>
					<option value="8" <c:if test="${month_report == '8'}">SELECTED</c:if>>SETEMBRO</option>
					<option value="9" <c:if test="${month_report == '9'}">SELECTED</c:if>>OUTUBRO</option>
					<option value="10" <c:if test="${month_report == '10'}">SELECTED</c:if>>NOVEMBRO</option>
					<option value="11" <c:if test="${month_report == '11'}">SELECTED</c:if>>DEZEMBRO</option>
				</select>
				<select class="form-control" name="selYear" style="width: 80px; display: inline-block;">
					<option value="2017" <c:if test="${year_report == '2017'}">SELECTED</c:if>>2017</option>
					<option value="2016" <c:if test="${year_report == '2016'}">SELECTED</c:if>>2016</option>
					<option value="2015" <c:if test="${year_report == '2015'}">SELECTED</c:if>>2015</option>
				</select>
				<select class="form-control" name="selReportType" style="width: 200px; display: inline-block;">
					<option value="CC" <c:if test="${report_type == 'CC'}">SELECTED</c:if>>CONTABILIZADOS</option>
					<option value="GC" <c:if test="${report_type == 'GC'}">SELECTED</c:if>>TOTAL DE CLIQUES</option>
					<option value="PU" <c:if test="${report_type == 'PU'}">SELECTED</c:if>>BOTÃO COMPRAR</option>
					<option value="MA" <c:if test="${report_type == 'MA'}">SELECTED</c:if>>MAIS ACESSADOS</option>
					<option value="RA" <c:if test="${report_type == 'RA'}">SELECTED</c:if>>MAIS RECENTES</option>
				</select>
				<input type="submit" class="btn btn-default" name="btReportByDate" value="Visualizar" style="display: inline-block; margin-bottom: 3px;">
			</div>
		</div>
	</form>
		
	<br />
	<br />
						
<%-- 	<c:if test="${list.size() > 0}"> --%>

<!-- 		<div id="columnchart_material" style="width: 100%; height: 400px;"></div>
	
		<br />
		<br />
 -->			
	<table class="table table-striped table-hover table-responsive">
		<thead>
			<tr>
				<th style="width: 15%"><fmt:message key="report.column.date" /></th>
				<th style="width: 10%"><fmt:message key="report.advertisement.column.id" /></th>
				<th style="width: 35%"><fmt:message key="report.advertisement.column.title" /></th>
<%-- 					<th style="width: 15%"><a href="${linkAdvertisementsReportByDate}?type_report=gross"><fmt:message key="report.advertisement.column.gross_click" /></a></th>
					<th style="width: 10%"><a href="${linkAdvertisementsReportByDate}?type_report=count"><fmt:message key="report.advertisement.column.count_click" /></a></th>
					<th style="width: 15%"><a href="${linkAdvertisementsReportByDate}?type_report=purchase"><fmt:message key="report.advertisement.column.purchases" /></a></th>
 --%>					
				<th style="width: 10%"><fmt:message key="report.advertisement.column.status" /></th>
				<th style="width: 10%"><fmt:message key="report.advertisement.column.status" /></th>
				<th style="width: 10%"><fmt:message key="report.advertisement.column.status" /></th>
				<th style="width: 10%"><fmt:message key="report.advertisement.column.status" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${list}" var="advertisement">
				<tr title="${advertisement.advertisement_id.id} - ${advertisement.advertisement_id.title}" onclick="location.href='${giroUrl}${advertisement.advertisement_id.url}'" style="cursor: pointer;">
					<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${advertisement.created_at}"/></td>
					<td>${advertisement.advertisement_id.id}</td>
					<c:choose>
						<c:when test="${fn:length(advertisement.advertisement_id.title) > 30}">
							<c:set var="stringAux" value="${fn:substring(advertisement.advertisement_id.title, 0, 29)}" />
							<td>${stringAux} ...</td>
						</c:when>
						<c:otherwise>
							<td>${advertisement.advertisement_id.title}</td>
						</c:otherwise>
					</c:choose>
					<td>${advertisement.addresses_id.citie}</td>
<%-- 					<c:choose>
						<c:when test="${advertisement.addresses_id ne null}">
							<td>${advertisement.addresses_id.citie}</td>
						</c:when>
						<c:otherwise>
							<td><br /></td>
						</c:otherwise>
					</c:choose>
 --%>					<c:choose>
						<c:when test="${advertisement.addresses_id ne null}">
							<td>${advertisement.addresses_id.state}</td>
						</c:when>
						<c:otherwise>
							<td><br /></td>
						</c:otherwise>
					</c:choose>
					<c:choose>
						<c:when test="${advertisement.addresses_id ne null}">
							<td>${advertisement.addresses_id.country}</td>
						</c:when>
						<c:otherwise>
							<td><br /></td>
						</c:otherwise>
					</c:choose>
<%-- 						<td>${advertisement.gross_click}</td>
						<td>${advertisement.count_click}</td>
						<td>
							<c:choose>
								<c:when test= "${not empty advertisement.listHistory_purchases}">
									${advertisement.listHistory_purchases.size()}
								</c:when>
								<c:otherwise>0</c:otherwise>
							</c:choose>
						</td>
 --%>						
					<td>
						<c:choose>
							<c:when test="${advertisement.advertisement_id.status == 1}"><fmt:message key="report.advertisement.status1" /></c:when>
							<c:when test="${advertisement.advertisement_id.status == 2}"><fmt:message key="report.advertisement.status2" /></c:when>
							<c:when test="${advertisement.advertisement_id.status == 3}"><fmt:message key="report.advertisement.status3" /></c:when>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	
	<div style="width: 100%; text-align: center;">
		<jsp:include page="/templates/panel/navList.jsp"/>
	</div>


<%-- 		<c:forEach items="${list}" var="ads">
			<c:if test= "${not empty ads.listHistory_purchases}">
				<c:forEach items="${ads.listHistory_purchases}" var="purchases">
					<c:if test="${purchases.created_at >= '2017-01-01 00:00:00' && purchases.created_at <= '2017-01-09 23:59:59'}">
						<br />
						${purchases.id} - <fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${purchases.created_at}"/>
					</c:if>
				</c:forEach>
			</c:if>
		</c:forEach> --%>


<%-- 	</c:if> --%>
	
 </div>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div style="width: 100%;">
	<div style="width: 100%;">
		<div class="panel panel-default">
			<!-- Default panel contents -->
			<div class="panel-heading"><fmt:message key="order.title"/></div>
			<!-- Table -->
				<table class="table">
				<thead>
					<tr align="left">
						<th style="width: 10%"><fmt:message key="order.date"/></th>						
						<th style="width: 10%"><fmt:message key="order.lastEvent"/></th>						
						<th style="width: 10%"><fmt:message key="order.credit"/></th>						
						<th style="width: 15%"><fmt:message key="order.method"/></th>						
						<th style="width: 15%"><fmt:message key="order.status"/></th>
						<th style="width: 20%"><fmt:message key="order.description"/></th>
						<th style="width: 20%"><fmt:message key="order.transactionId"/></th>
					</tr>	
				</thead>
				<tbody>
					<c:forEach items="${listPurchasesHistoric}" var="purchase">
						<tr>
							<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${purchase.date}" /></td>
							<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${purchase.last_event}" /></td>
							<td><fmt:formatNumber type="currency" value="${purchase.credit}" /></td>
							<td>${purchase.method}</td>
							<td>
								<c:choose>
									<c:when test="${purchase.cod_status == 1}"><fmt:message key="order.status.waitingPayment"/></c:when>
									<c:when test="${purchase.cod_status == 2}"><fmt:message key="order.status.inAnalysis"/></c:when>
									<c:when test="${purchase.cod_status == 3}"><fmt:message key="order.status.paid"/></c:when>
									<c:when test="${purchase.cod_status == 4}"><fmt:message key="order.status.available"/></c:when>
									<c:when test="${purchase.cod_status == 5}"><fmt:message key="order.status.inDispute"/></c:when>
									<c:when test="${purchase.cod_status == 6}"><fmt:message key="order.status.returned"/></c:when>
									<c:when test="${purchase.cod_status == 7}"><fmt:message key="order.status.cancelled"/></c:when>
									<c:otherwise>${purchase.status}</c:otherwise>
								</c:choose>
							</td>
							<td>
								<c:choose>
									<c:when test="${purchase.cod_status == 1}"><fmt:message key="order.description.waitingPayment"/></c:when>
									<c:when test="${purchase.cod_status == 7}"><fmt:message key="order.description.transactionCancelled"/></c:when>
									<c:otherwise>${purchase.description}</c:otherwise>
								</c:choose>
							</td>				
							<td>${purchase.transaction_code}</td>
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
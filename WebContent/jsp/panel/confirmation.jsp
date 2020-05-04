<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="wrapper-pagenotfound">
	<div>
		<h2><fmt:message key="buycredits.success" /></h2>
		Seu cr�dito estar� liberado somente ap�s a confirma��o de pagamento
		<br /> 
		<br /> 
		<h3>N� do Pedido: <strong>${order.id}</strong></h3>
		A confirma��o do pedido foi enviada para ${order.user_id.email}
		<br />
		<br />
		C�DIGO: <strong>${order.transaction_code}</strong>
		<br />
		<br />
		Forma de pagamento: <strong>${order.method}</strong>
		<br />
		<br />
		<table class="table">
			<thead>
				<tr>
					<th style="width: 46%; text-align: left;">ITENS DO PEDIDO</th>
					<th style="width: 18%; text-align: center;">QUANTIDADE</th>
					<th style="width: 18%; text-align: center;">UNIT�RIO</th>
					<th style="width: 18%; text-align: center;">TOTAL</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th style="text-align: left;">GIROFERTA - CR�DITO DE <fmt:formatNumber value="${order.credit}" type="currency"/></th>
					<th style="text-align: center;">${order.quantity}</th>
					<th style="text-align: center;"><fmt:formatNumber value="${order.credit}" type="currency"/></th>
					<th style="text-align: center;"><fmt:formatNumber value="${order.quantity * order.credit}" type="currency"/></th>
				</tr>
			</tbody>
		</table>
		<br />
		<br />
		<a href="${linkHome}"><fmt:message key="page.backHome" /></a>
	</div>
</div>
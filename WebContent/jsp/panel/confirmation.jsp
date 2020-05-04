<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="wrapper-pagenotfound">
	<div>
		<h2><fmt:message key="buycredits.success" /></h2>
		Seu crédito estará liberado somente após a confirmação de pagamento
		<br /> 
		<br /> 
		<h3>Nº do Pedido: <strong>${order.id}</strong></h3>
		A confirmação do pedido foi enviada para ${order.user_id.email}
		<br />
		<br />
		CÓDIGO: <strong>${order.transaction_code}</strong>
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
					<th style="width: 18%; text-align: center;">UNITÁRIO</th>
					<th style="width: 18%; text-align: center;">TOTAL</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th style="text-align: left;">GIROFERTA - CRÉDITO DE <fmt:formatNumber value="${order.credit}" type="currency"/></th>
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
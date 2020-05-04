<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div>
	<div style="width: 100%; text-align: center;">
		<h3><fmt:message key="vendor.title.courtesy" /></h3>
	</div>
	<hr>
	<form action="${linkVendorCourtesySearch}" name="formCourtesySearch" method="POST">
		<div style="width: 100%; display: table;">
			<div style="display: table-layout; width: 100%; text-align: center;">
				<input type="text" name="tfCourtesyCpfCnpj" value="${tfCourtesyCpfCnpj}" maxlength="18" class="form-control" placeholder="" aria-describedby="basic-addon1" style="width: 175px; display: inline-block;">
				<input type="submit" class="btn btn-default" name="btCourtesySearch" value="Buscar" style="display: inline-block; margin-bottom: 3px;">
			</div>
		</div>
	</form>
	<c:if test="${message != null}">
		<br />
		<div style="width: 100%; text-align: center;">
			<span style="color: red;"><b>${message}</b></span>
		</div>	
	</c:if>
	<c:if test="${userCourtesy != null}">
		<hr>
		<div style="width: 100%; text-align: center;">
			<c:choose>
				<c:when test="${userCourtesy.type_person == 'J'}">
					Cliente: <b>${userCourtesy.company_name}</b> | Saldo: <b><fmt:formatNumber value="${userCourtesy.balance}" type="currency"/></b>
				</c:when>
				<c:when test="${userCourtesy.type_person == 'F'}">
					Cliente: <b>${userCourtesy.name} ${userCourtesy.last_name}</b> | Saldo: <b><fmt:formatNumber value="${userCourtesy.balance}" type="currency"/></b>
				</c:when>
			</c:choose>
		</div>
		<hr>
		<c:if test="${listHistoryCourtesy.size() > 0}">
			<div style="width: 100%; text-align: center;"><b>HISTÓRICO DE CRÉDITOS</b></div>
			<br />
			<c:forEach items="${listHistoryCourtesy}" var="courtesy" varStatus="items">		
				<div style="width: 100%; text-align: center;">
					${items.count}) Data: <b><fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${courtesy.created_at}"/></b> | Usuário: <b>${courtesy.vendor_id.name}</b> | Valor: <b><fmt:formatNumber value="${courtesy.credit}" type="currency"/></b>
				</div>
			</c:forEach>
			<hr>
		</c:if>
		<form action="${linkVendorCourtesyApply}" name="formCourtesyApply" method="POST">
			<div style="width: 100%; display: table;">
				<div style="display: table-layout; width: 100%; text-align: center;">
					<span>Disponível para crédito</span>
					<input type="text" name="tfCourtesyCredit" value="${fn:replace(tfCourtesyCredit,'.', ',')}" data-affixes-stay="true" data-prefix="" data-thousands="." data-decimal="," class="form-control currency" placeholder="" aria-describedby="basic-addon1" style="width: 100px; display: inline-block;">
					<input type="hidden" name="tfCourtesyCpfCnpj" value="${tfCourtesyCpfCnpj}">
					<input type="submit" class="btn btn-default" name="btCourtesyApply" value="Aplicar" style="display: inline-block; margin-bottom: 3px;">
				</div>
			</div>
		</form>
	</c:if>
</div>
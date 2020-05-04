<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div style="text-align: center;">
	<div style="width: 100%; text-align: center;">
		<h3><fmt:message key="admin.title.accounts" /></h3>
	</div>
	<hr>
	<form action="${linkAdminManageAccountsSearch}" name="formManageAccountsSearch" method="POST">
		<div style="width: 100%; display: table;">
			<div style="display: table-layout; width: 100%; text-align: center;">
				<input type="text" name="tfManageAccountsCpfCnpj" value="${tfManageAccountsCpfCnpj}" maxlength="18" class="form-control" placeholder="" aria-describedby="basic-addon1" style="width: 175px; display: inline-block;">
				<input type="submit" class="btn btn-default" name="btManageAccountsSearch" value="Buscar" style="display: inline-block; margin-bottom: 3px;">
			</div>
		</div>
	</form>
	<c:if test="${message != null}">
		<br />
		<div style="width: 100%; text-align: center;">
			<span style="color: red;"><b>${message}</b></span>
		</div>	
	</c:if>
	<c:if test="${userManageAccounts != null}">
		<hr>
		<div style="width: 100%; text-align: center;">
			<c:choose>
				<c:when test="${userManageAccounts.type_person == 'J'}">
					Cliente: <b>${userManageAccounts.company_name}</b> | Extra: <b><fmt:formatNumber value="${userManageAccounts.extra}" type="currency"/></b> | Saldo: <b><fmt:formatNumber value="${userManageAccounts.balance}" type="currency"/></b>
				</c:when>
				<c:when test="${userManageAccounts.type_person == 'F'}">
					Cliente: <b>${userManageAccounts.name} ${userManageAccounts.last_name}</b> | Extra: <b><fmt:formatNumber value="${userManageAccounts.extra}" type="currency"/></b> | Saldo: <b><fmt:formatNumber value="${userManageAccounts.balance}" type="currency"/></b>
				</c:when>
			</c:choose>
		</div>
		<hr>
		<form action="${linkAdminManageAccountsApplyActive}" name="formManageAccountsApplyActive" method="POST">
			<div style="width: 100%; display: table;">
				<div style="display: table-layout; width: 100%; text-align: center;">
					<span>Conta Ativa</span>
					<select class="form-control" name="selManageAccountsActive" style="width: 100px; display: inline-block;">
						<option value="1" <c:if test="${userManageAccounts.status == 1}">SELECTED</c:if>>SIM</option>
						<option value="0" <c:if test="${userManageAccounts.status == 0}">SELECTED</c:if>>NÃO</option>
					</select>
					<input type="hidden" name="tfManageAccountsCpfCnpj" value="${tfManageAccountsCpfCnpj}">
					<input type="submit" class="btn btn-default" name="btManageAccountsApplyActive" value="Aplicar" style="display: inline-block; margin-bottom: 3px;">
				</div>
			</div>
		</form>
		<c:if test="${userManageAccounts.type_person == 'F'}">
			<br />
			<form action="${linkAdminManageAccountsApplyProfile}" name="formManageAccountsApplyProfile" method="POST">
				<div style="width: 100%; display: table;">
					<div style="display: table-layout; width: 100%; text-align: center;">
						<span>Perfil</span>
						<select class="form-control" name="selManageAccountsProfile" style="width: 190px; display: inline-block;">
							<option value="2" <c:if test="${userManageAccounts.type_account == 2}">SELECTED</c:if>>DIRETOR</option>
							<option value="5" <c:if test="${userManageAccounts.type_account == 5}">SELECTED</c:if>>VENDEDOR</option>
							<option value="10" <c:if test="${userManageAccounts.type_account == 10}">SELECTED</c:if>>USUÁRIO</option>
						</select>
						<input type="hidden" name="tfManageAccountsCpfCnpj" value="${tfManageAccountsCpfCnpj}">
						<input type="submit" class="btn btn-default" name="btManageAccountsApplyProfile" value="Aplicar" style="display: inline-block; margin-bottom: 3px;">
					</div>
				</div>
			</form>
		</c:if>
		<br />
		<form action="${linkAdminManageAccountsApplyPassword}" name="formManageAccountsPassword" method="POST">
			<div style="width: 100%; display: table;">
				<div style="display: table-layout; width: 100%; text-align: center;">
					<span>Nova Senha</span>
					<input type="text" name="tfManageAccountsPassword" value="" class="form-control" placeholder="" aria-describedby="basic-addon1" style="width: 190px; display: inline-block;">
					<input type="hidden" name="tfManageAccountsCpfCnpj" value="${tfManageAccountsCpfCnpj}">
					<input type="submit" class="btn btn-default" name="btManageAccountsApplyPassword" value="Aplicar" style="display: inline-block; margin-bottom: 3px;">
				</div>
			</div>
		</form>
		<br />
		<form action="${linkAdminManageAccountsApplyExtraCredit}" name="formManageAccountsExtraCredit" method="POST">
			<div style="width: 100%; display: table;">
				<div style="display: table-layout; width: 100%; text-align: center;">
					<span>Crédito Extra R$</span>
					<input type="text" name="tfManageAccountsExtraCredit" value="${fn:replace('0,00','.', ',')}" data-affixes-stay="true" data-prefix="" data-thousands="." data-decimal="," class="form-control currency" placeholder="" aria-describedby="basic-addon1" style="width: 100px; display: inline-block;">
					<input type="hidden" name="tfManageAccountsCpfCnpj" value="${tfManageAccountsCpfCnpj}">
					<input type="submit" class="btn btn-default" name="btManageAccountsApplyExtraCredit" value="Aplicar" style="display: inline-block; margin-bottom: 3px;">
				</div>
			</div>
		</form>
		<br />
		<form action="${linkAdminManageAccountsApplyCurrentClick}" name="formManageAccountsCurrentClick" method="POST">
			<div style="width: 100%; display: table;">
				<div style="display: table-layout; width: 100%; text-align: center;">
					<span>Clique Atual</span>
					<select class="form-control" name="selManageAccountsCurrentClick" style="width: 190px; display: inline-block;">
						<c:forEach items="${listClickValues}" var="clickValues">
							<option value="${clickValues.id}" <c:if test="${userManageAccounts.click_values_id.id == clickValues.id}">SELECTED</c:if>><fmt:formatNumber value="${clickValues.value}" type="currency"/> - ${clickValues.description}</option>
						</c:forEach>
					</select>
					<input type="hidden" name="tfManageAccountsCpfCnpj" value="${tfManageAccountsCpfCnpj}">
					<input type="submit" class="btn btn-default" name="btManageAccountsApplyCurrentClick" value="Aplicar" style="display: inline-block; margin-bottom: 3px;">
				</div>
			</div>
		</form>
		<br />
		<form action="${linkAdminManageAccountsApplyImportXml}" name="formManageAccountsApplyImportXml" method="POST">
			<div style="width: 100%; display: table;">
				<div style="display: table-layout; width: 100%; text-align: center;">
					<span>Importar XML</span>
					<select class="form-control" name="selManageAccountsImportXml" style="width: 100px; display: inline-block;">
						<option value="1" <c:if test="${userManageAccounts.import_xml == 1}">SELECTED</c:if>>SIM</option>
						<option value="0" <c:if test="${userManageAccounts.import_xml == 0}">SELECTED</c:if>>NÃO</option>
					</select>
					<input type="hidden" name="tfManageAccountsCpfCnpj" value="${tfManageAccountsCpfCnpj}">
					<input type="submit" class="btn btn-default" name="btManageAccountsApplyImportXml" value="Aplicar" style="display: inline-block; margin-bottom: 3px;">
				</div>
			</div>
		</form>
		<br />
		<form action="${linkAdminManageAccountsApplyTypeXml}" name="formManageAccountsTypeXml" method="POST">
			<div style="width: 100%; display: table;">
				<div style="display: table-layout; width: 100%; text-align: center;">
					<span>Tipo de XML</span>
					<select class="form-control" name="selManageAccountsTypeXml" style="width: 190px; display: inline-block;">
						<option value="0" <c:if test="${userManageAccounts.type_xml == 0}">SELECTED</c:if>>0 - Nenhum </option>
						<option value="1" <c:if test="${userManageAccounts.type_xml == 1}">SELECTED</c:if>>1 - GS (feed)</option>
						<option value="2" <c:if test="${userManageAccounts.type_xml == 2}">SELECTED</c:if>>2 - GS (rss)</option>
						<option value="3" <c:if test="${userManageAccounts.type_xml == 3}">SELECTED</c:if>>3 - IMOBEX</option>
						<option value="4" <c:if test="${userManageAccounts.type_xml == 4}">SELECTED</c:if>>4 - ADTOOLS</option>
					</select>
					<input type="hidden" name="tfManageAccountsCpfCnpj" value="${tfManageAccountsCpfCnpj}">
					<input type="submit" class="btn btn-default" name="btManageAccountsApplyTypeXml" value="Aplicar" style="display: inline-block; margin-bottom: 3px;">
				</div>
			</div>
		</form>
		<br />
		<form action="${linkAdminManageAccountsApplyUrlXml}" name="formManageAccountsUrlXml" method="POST">
			<div style="width: 100%; display: table;">
				<div style="display: table-cell; width: 100%; text-align: center;">
					<span>URL do XML</span>
					<input type="text" name="tfManageAccountsUrlXml" value="${userManageAccounts.url_xml}" class="form-control" placeholder="" aria-describedby="basic-addon1" style="width: 190px; display: inline-block;">
					<input type="hidden" name="tfManageAccountsCpfCnpj" value="${tfManageAccountsCpfCnpj}">
					<input type="submit" class="btn btn-default" name="btManageAccountsApplyUrlXml" value="Aplicar" style="display: inline-block; margin-bottom: 3px;">
				</div>
			</div>
		</form>
		<br />
	</c:if>
	<br />
	<a href="${linkAdminManager}"><fmt:message key="admin.back" /></a>
</div>
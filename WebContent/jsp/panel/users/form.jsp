<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h3><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>  <fmt:message key="formUser.title" /></h3>

<jsp:include page="/templates/panel/listMessages.jsp"/>

<br />
<div class="clear-both"></div>

<form action="${linkUsersSave}" method="POST" name="formUser">
	
	<c:if test="${currentUser == null || newAccount != null}">
		<ul class="nav nav-tabs nav-justified nav-pf-pj" style="width: 48%; margin-bottom: 15px;">
		  <li role="presentation" class="<c:if test="${typePerson == 'pf' || typePerson == null}">active</c:if> pf"><a href="javascript:void(0);">Pessoa Física</a></li>
		  <li role="presentation" class="<c:if test="${typePerson == 'pj'}">active</c:if> pj"><a href="javascript:void(0);">Pessoa Jurídica</a></li>
		</ul>
	</c:if>

	<div class="box-left">
		<div class="form-group">
		
			<div class="pj" style="display: <c:if test="${typePerson != 'pj'}">none;</c:if>">
			
				<c:choose>
			
					<c:when test="${currentUser.id == 0 || currentUser.id == null || newAccount != null}">
					
						<fmt:message key="user.cnpj"/>
						<br />
						<input type="text" id="user.cnpj" name="user.cnpj" value="${user.cnpj}" class="form-control cnpj-field" placeholder="" aria-describedby="basic-addon1">
						
					</c:when>
					
					<c:otherwise>
					
						<fmt:message key="user.cnpj"/>
						<br />
						<input type="text" id="cnpj" name="cnpj" value="${user.cnpj}" class="form-control cnpj-field" placeholder="" aria-describedby="basic-addon1" disabled="disabled">
					
					</c:otherwise>
				
				</c:choose>
				
				<br>
<!--  
				<fmt:message key="user.stateRegistration"/>
				
				<br />
				
				<input type="text" id="user.state_registration" name="user.state_registration" value="${user.state_registration}" class="form-control" placeholder="" aria-describedby="basic-addon1">
				
				<br>
				
-->			
				<fmt:message key="user.companyName"/>
				
				<br />
				
				<input type="text" id="user.company_name" name="user.company_name" value="${user.company_name}" class="form-control" placeholder="" aria-describedby="basic-addon1">
				
				<br />
				
				<fmt:message key="user.contactPerson"/>
				
				<br />
				
				<input type="text" id="user.contact_person" name="user.contact_person" value="${user.contact_person}" class="form-control" placeholder="" aria-describedby="basic-addon1">
				
				<br />

			</div>
			
			<div class="pf" style="display: <c:if test="${typePerson != 'pf'}">none;</c:if>">
						
				<c:choose>
			
					<c:when test="${currentUser.id == 0 || currentUser.id == null || currentUser.type_account == 11 || newAccount != null}">
	
						<fmt:message key="user.cpf"/>
						<br />	
						<input type="text" id="user.cpf" name="user.cpf" value="${user.cpf}" class="form-control cpf-field" placeholder="" aria-describedby="basic-addon1">
					
					</c:when>
					
					<c:otherwise>
					
						<fmt:message key="user.cpf"/>
						
						<br />
					
						<input type="text" id="cpf" name="cpf" value="${user.cpf}" class="form-control cpf-field" placeholder="" aria-describedby="basic-addon1" disabled="disabled">

					</c:otherwise>
					
				</c:choose>
				
				<br>
				
				<fmt:message key="user.name"/>
				
				<br />
				
				<input type="text" id="user.name" name="user.name" value="${user.name}" class="form-control" placeholder="" aria-describedby="basic-addon1">
				
				<br>			
				
				<fmt:message key="user.lastName"/>
				
				<br />
				
				<input type="text" id="user.last_name" name="user.last_name" value="${user.last_name}" class="form-control" placeholder="" aria-describedby="basic-addon1">
				
				<br>
				
				<c:if test="${currentUser != null && newAccount == null}">

		
					<fmt:message key="user.gender"/>
				
					<br />
		
					<select class="form-control" id="user.gender" name="user.gender">
						<option value="0" <c:if test="${user.gender != 'M' && userGender != 'F'}">SELECTED</c:if>></option>
				   		<option value="M" <c:if test="${user.gender == 'M'}">SELECTED</c:if>><fmt:message key="user.genderMale"/></option>
				   		<option value="F" <c:if test="${user.gender == 'F'}">SELECTED</c:if>><fmt:message key="user.genderFemale"/></option>
				  	</select>
				  	
				  	<br />
				  	
				  	<fmt:message key="user.birthdate"/>
				  	
				  	<br />
				  	
				  	<input type="text" id="user.birthdate" name="user.birthdate" value="<fmt:formatDate value="${user.birthdate}" pattern="dd/MM/yyyy"/>" class="form-control date-field" placeholder="" aria-describedby="basic-addon1">
					
					<br>
				
				</c:if>
				
			</div>
		  	
		  	<h4>Dados de acesso</h4>
		  	
			<c:choose>
		
				<c:when test="${currentUser.id == 0 || currentUser.id == null || newAccount != null}">

					<fmt:message key="user.email"/>
					
					<br />

					<input type="text" id="user.email" name="user.email" value="${user.email}" class="form-control" placeholder="" aria-describedby="basic-addon1">
			
				</c:when>
				
				<c:otherwise>

					<fmt:message key="user.email"/>
					
					<br />

					<input type="text" id="email" name="email" value="${user.email}" class="form-control" placeholder="" aria-describedby="basic-addon1" disabled="disabled">

				</c:otherwise>
				
			</c:choose>
			
			<c:if test="${currentUser.status == 0}">
				<span class="label label-warning">E-mail não verificado</span>
				<br />
			</c:if>
			
			<c:if test="${currentUser == null || currentUser.type_account == 11 || newAccount != null}">
			
				<br>
				
				<fmt:message key="user.password"/>
				
				<br />
				
				<input type="password" id="password" name="password" value="${password}" class="form-control" placeholder="" aria-describedby="basic-addon1">
				
				<div class="password-tips">
					*<fmt:message key="user.passwordTip1"/>
					<br />
					*<fmt:message key="user.passwordTip2"/>
					<br />
					*<fmt:message key="user.passwordTip3"/>
				</div>
				
				<br />
				
				<fmt:message key="user.retypePassword"/>
				
				<br />
				
				<input type="password" id="retype_password" name="retype_password" value="${retype_password}" class="form-control" placeholder="" aria-describedby="basic-addon1">
				
			</c:if>
			
			<br />
			
			<c:choose>
			
				<c:when test="${currentUser.id == 0 || currentUser.id == null || currentUser.type_account == 11 || newAccount != null}">
				
					<fmt:message key="user.url"/>
					
					<br />
					
					<div class="input-group">
					  <span class="input-group-addon" id="basic-addon1">giroferta.com/</span>
					  <input type="text" id="user.url" onkeyup="this.value = Trim(this);" name="user.url" value="${user.url}" class="form-control" placeholder="" aria-describedby="basic-addon1">
					</div>
					
					<div class="form-tip">
						<span class="glyphicon glyphicon-thumbs-up"></span>
						Essa é a URL da sua loja e será acessível através de <i>giroferta.com/nome_da_sua_loja</i>.
						<br />
						Dica: tente ser bastante objetivo para que seus clientes lhe encontrem com facilidade ;) #dicadogiro
					</div>

				</c:when>
				
				<c:otherwise>

					<fmt:message key="user.url"/>
					
					<br />

					<input type="text" id="url" name="url" value="${user.url}" class="form-control" placeholder="" aria-describedby="basic-addon1" disabled="disabled">
			
				</c:otherwise>
				
			</c:choose>
				
			<c:if test="${currentUser != null && newAccount == null}">
			
				<br>
				
				<fmt:message key="user.link"/>
				
				<br />
				
				<input type="text" id="user.link" name="user.link" value="${user.link}" class="form-control" placeholder="" aria-describedby="basic-addon1">
	
				<br>
				
				<h4>Informações de contato</h4>
				
				<fmt:message key="user.phone"/>
				
				<br />
				
				<input type="text" id="user.phone" name="user.phone" value="${user.phone}" class="form-control phone-field" placeholder="" aria-describedby="basic-addon1">
				
				<br>			
				
				<fmt:message key="user.secondPhone"/>
				
				<br />
				
				<input type="text" id="user.second_phone" name="user.second_phone" value="${user.second_phone}" class="form-control phone-field" placeholder="" aria-describedby="basic-addon1">
				
				<br>
			
			</c:if>
			
			<%-- <h4>Informações de localização</h4>
			
			<fmt:message key="user.state"/>
			
			<br />
			
			<select class="form-control" id="ddUserState" name="ddUserState">
		   		<option value="0"></option>
				<c:forEach items="${listStates}" var="state">
					<option value="${state.id}" <c:if test="${state.id == stateId}">SELECTED</c:if>>${state.initials}</option>
				</c:forEach>
		  	</select>
			
			<br />

			<fmt:message key="store.city"/>
			
			<br />

			<div id="cities">
			  	<select class="form-control" id="ddUserCity" name="user.city_id">
			   		<option value="0"></option>
			   		<c:forEach items="${listCities}" var="city">
			   			<option value="${city.id}" <c:if test="${city.id == cityId}">SELECTED</c:if>>${city.name}</option>
			   		</c:forEach>
			  	</select>
		  	</div>
			
			<br />

			<fmt:message key="user.neighborhood"/>
			
			<br />

			<input type="text" id="user.neighborhood" name="user.neighborhood" value="${user.neighborhood}" class="form-control" placeholder="" aria-describedby="basic-addon1">
			
			<br>			
			
			<fmt:message key="user.address"/>
			
			<br />
			
			<input type="text" id="user.address" name="user.address" value="${user.address}" class="form-control" placeholder="" aria-describedby="basic-addon1">
			
			<br>			
			
			<fmt:message key="user.number"/>
			
			<br />
			
			<input type="text" id="user.number" name="user.number" value="${user.number}" class="form-control" placeholder="" aria-describedby="basic-addon1">
			
			<br>			
			
			<fmt:message key="user.complement"/>
			
			<br />
			
			<input type="text" id="user.complement" name="user.complement" value="${user.complement}" class="form-control" placeholder="" aria-describedby="basic-addon1">
			
			<br>			
			
			<fmt:message key="user.postalCode"/>
			
			<br />
			
			<input type="text" id="user.postal_code" name="user.postal_code" value="${user.postal_code}" class="form-control cep-field" placeholder="" aria-describedby="basic-addon1">
			 --%>
		</div>
	</div>
	
	<div class="clear-both"></div>
				
	<input type="checkbox" id="email_receive" name="email_receive" value="${user.email_receive}" <c:if test="${email_receive == 1}">checked="checked"</c:if>>&nbsp;<fmt:message key="user.emailReceive"/>
	
	<c:if test="${currentUser == null || newAccount != null}">
	
		<br>			
		
		<input type="checkbox" id="terms_agree" name="terms_agree" value="${user.terms_agree}" <c:if test="${terms_agree == 1}">checked="checked"</c:if>>&nbsp;<fmt:message key="user.termsAgree"/><a href='${linkUserTerms}' target='_blank'><fmt:message key="user.termsAgree2"/></a><fmt:message key="user.termsAgree3"/>
	
	</c:if>
	
	<div class="clear-both"></div>			

	<br />
	
	<%-- nao remover este campo, ele tbm e usado no spinwork --%>
	<input type="hidden" id="typePerson" name="typePerson" value="${typePerson}">
	<input type="hidden" id="user.id" name="user.id" value="${user.id}">

	<button type="submit" class="btn btn-primary" name="btSave"><fmt:message key="form.btSave"/></button>		
</form>
<!-- 
<c:if test="${currentUser != null && newAccount == null}">

	<c:choose>
		<c:when test="${currentUser.image_file_name != null && currentUser.image_file_name != '' && newAccount == null}">
			<br />
			<br />
			<img src="${linkUserImagesView}&image=${currentUser.image_file_name}" width="250" height="250">
			<br />
			<br />
			<a href="${linkUserImagesDelete}&user_id=${currentUser.id}">EXCLUIR</a>
			<br />
			<br />
		</c:when>
		<c:otherwise>
			<br />
			<form action="${linkUserImagesUpload}&user_id=${currentUser.id}" method="POST" name="formUserImages" enctype="multipart/form-data">
				<table>
					<tr>
						<td><input type="file" name="tfUpImage"></td>
						<td><input type="submit" name="btSaveImage"  value="Enviar Imagem"></td>
					</tr>
			    </table>
			</form>
			<br />
		</c:otherwise>
	</c:choose>
	
</c:if>
 -->
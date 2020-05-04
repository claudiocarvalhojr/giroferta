<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="box-left">
	<form action="${formAction}" method="POST" name="formCity">
		<div class="form-group">
			<jsp:include page="/templates/panel/listMessages.jsp"/>

			<input type="text" id="tfCityId" class="form-control" value="${cityId}" placeholder="<fmt:message key="city.id"/>" aria-describedby="basic-addon1" name="tfCityId"  disabled="disabled">
			
			<br>
			
			<input type="text" id="tfCityIbgeCode" class="form-control" value="${cityIbgeCode}" placeholder="<fmt:message key="city.ibgeCode"/>" aria-describedby="basic-addon1" name="tfCityIbgeCode">
			
			<br>
			
			<input type="text" id="tfCityName" class="form-control" value="${cityName}" placeholder="<fmt:message key="city.name"/>" aria-describedby="basic-addon1" name="tfCityName">
			
			<br>			
			
		  	<select class="form-control" id="ddCityState" name="ddCityState">
		   		<option value="0"><fmt:message key="city.state"/></option>
				<c:forEach items="${listStates}" var="state">
					<option value="${state.ID}" <c:if test="${state.ID == cityState}">SELECTED</c:if>>${state.INITIALS}</option>
				</c:forEach>
		  	</select>
			
			<br />

			<input type="hidden" id="id" name="id" value="${cityId}">
			<input type="hidden" id="current" name="current" value="${current}">
			<input type="hidden" id="maximum" name="maximum" value="${maximum}">
			<input type="hidden" id="valueSearch" name="valueSearch" value="${valueSearch}">
			
			<button type="submit" class="btn btn-default" name="btSave"><fmt:message key="form.btSave"/></button>			
		</div>
	</form>
</div>
	
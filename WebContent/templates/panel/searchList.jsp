<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div>
	<form action="${searchAction}" method="POST" name="formSearch" id="form-search">		
		<div class="input-group">
		 <input type="hidden" id="current" name="current" value="${current}">
		  <input type="hidden" id="maximum" name="maximum" value="${maximum}">
		  <input type="text" id="valueSearch" class="form-control" placeholder="<fmt:message key="panel.search" />" aria-describedby="basic-addon1" name="valueSearch" value="${valueSearch}">
		  <span class="input-group-addon" id="basic-addon1">
		  	<span class="glyphicon glyphicon-search span-bt-search" style="cursor: pointer;"></span>
		  </span>
		</div>
	</form>
</div>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript"
	src="https://stc.pagseguro.uol.com.br/pagseguro/api/v2/checkout/pagseguro.lightbox.js">
</script>

<h3><span class="glyphicon glyphicon-shopping-cart" aria-hidden="true"></span>  <fmt:message key="buycredits.buyCredits" /></h3>

<h5><fmt:message key="buycredits.message" /></h5>

<br />

<div class="btn-group" data-toggle="buttons">

  <label class="btn btn-default label-credit" valor="36">
    <input type="radio" name="options" id="option1" autocomplete="off">
	<fmt:message key="buycredits.values" var="val36" >
	   <fmt:param value="36"/>
	</fmt:message>
	<c:out value="${val36}" />
  </label>
  
  <label class="btn btn-default label-credit" valor="54">
    <input type="radio" name="options" id="option2" autocomplete="off">
    <fmt:message key="buycredits.values" var="val54" >
	   <fmt:param value="54"/>
	</fmt:message>
	<c:out value="${val54}" />
  </label>
  
  <label class="btn btn-default label-credit" valor="72">
    <input type="radio" name="options" id="option3" autocomplete="off">
    <fmt:message key="buycredits.values" var="val72" >
	   <fmt:param value="72"/>
	</fmt:message>
	<c:out value="${val72}" />
  </label>
  
  <label class="btn btn-default label-credit" valor="108">
    <input type="radio" name="options" id="option3" autocomplete="off">
    <fmt:message key="buycredits.values" var="val108" >
	   <fmt:param value="108"/>
	</fmt:message>
	<c:out value="${val108}" />
  </label>
  
  <label class="btn btn-default label-credit" valor="150">
    <input type="radio" name="options" id="option3" autocomplete="off">
    <fmt:message key="buycredits.values" var="val150" >
	   <fmt:param value="150"/>
	</fmt:message>
	<c:out value="${val150}" />
  </label>
  
  <label class="btn btn-default label-credit"  valor="300">
    <input type="radio" name="options" id="option3" autocomplete="off">
    <fmt:message key="buycredits.values" var="val300" >
	   <fmt:param value="300"/>
	</fmt:message>
	<c:out value="${val300}" />
  </label>
  
  <label class="btn btn-default label-credit" valor="540">
    <input type="radio" name="options" id="option3" autocomplete="off">
    <fmt:message key="buycredits.values" var="val540" >
	   <fmt:param value="540"/>
	</fmt:message>
	<c:out value="${val540}" />
  </label>
  
  <label class="btn btn-default label-credit" valor="1080">
    <input type="radio" name="options" id="option3" autocomplete="off">
    <fmt:message key="buycredits.values" var="val1080" >
	   <fmt:param value="1080"/>
	</fmt:message>
	<c:out value="${val1080}" />
  </label>
  
  <%-- <label class="btn btn-default label-credit-other">
    <input type="radio" name="options" id="option3" autocomplete="off">
    <fmt:message key="buycredits.other" />
  </label> --%>
</div>

<br />
<br />

<div class="clear-both"></div>

<a class="btn btn-success bt-add-credit" id="btBuyCredit" href="javascript:void(0);" role="button">
	<span class="glyphicon glyphicon-ok"></span>
	<fmt:message key="buycredits.continue"/>
</a>

<div class="clear-both"></div>

<br />
<div class="panel panel-warning">
	<div class="panel-heading"><fmt:message key="buycredits.dontWorry" /></div>
  <div class="panel-body">
    <fmt:message key="buycredits.explanation" />
  </div>
</div>

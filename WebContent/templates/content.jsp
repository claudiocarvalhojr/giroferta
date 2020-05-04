<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript">
	$(document).ready(function() {
		var protocol = window.location.protocol;
		var hostname = window.location.hostname;
		var port = window.location.port;
		var pathname = window.location.pathname;
/* 		console.log('protocol: ' + protocol);
		console.log('hostname: ' + hostname);
		console.log('port: ' + port);
		console.log('pathname: ' + pathname);
*/ 		if (protocol == 'http:' && hostname != 'localhost') {
			if (hostname == 'giroferta.com') {
				hostname = 'www.' + hostname;
			}
			var newURL = hostname + pathname;
			var httpsURL = "https://" + newURL;
/* 			console.log('newURL: ' + newURL);
			console.log('httpsURL: ' + httpsURL);
 *//* 			window.location = httpsURL; */ 
		}
	});
</script>

<div class="overlay"></div>

<jsp:include page="${yield}" />

<div class="alert alert-dismissible alert-box" role="alert">
  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
  <strong></strong>
</div>

<input type="hidden" value="${homeTotalAdv}"         id="homeTotalAdv" />
<input type="hidden" value="${homeTotalStores}"      id="homeTotalStores" />
<input type="hidden" value="${homeAditionalRadius}"  id="homeAditionalRadius" />
<input type="hidden" value="${adminRadius}"          id="adminRadius" />
<input type="hidden" value="${adminTotalAdvActives}" id="adminTotalAdvActives" />
<input type="hidden" value="${adminTotalAdv}"        id="adminTotalAdv" />
<input type="hidden" value="${adminTotalStores}"     id="adminTotalStores" />

<script type="text/javascript">
	console.log('homeTotalAdv: ' + '${homeTotalAdv}');
	console.log('homeTotalStores: ' + '${homeTotalStores}');
	console.log('homeAditionalRadius: ' + '${homeAditionalRadius}');
	console.log('adminRadius: ' + '${adminRadius}');
	console.log('adminTotalAdvActives: ' + '${adminTotalAdvActives}');
	console.log('adminTotalAdv: ' + '${adminTotalAdv}');
	console.log('adminTotalStores: ' + '${adminTotalStores}');
</script>

<br />

<%-- <div style="display: none;">
	<c:forEach items="${listAdvertisementsHome}" var="advertisement">
		<div itemscope itemtype="http://schema.org/Product">
			<span itemprop="name">${advertisement.advertisement_id.title}</span>
			<span itemscope itemtype="http://schema.org/Brand">
				<span itemprop="name">${currentAdvertisementStore.advertisement_id.brand}</span>						
			</span>
			<a href="${giroUrl}${advertisement.url}" itemprop="url">${giroUrl}${advertisement.url}</a>
			<div itemprop="offers" itemscope itemtype="http://schema.org/Offer">
				<meta itemprop="price" content="${advertisement.advertisement_id.price}" />
				<meta itemprop="pricecurrency" content="BRL" />
	   			<c:choose>
	   				<c:when test="${advertisement.advertisement_id.item_state == 'NOVO'}">
	    				<meta itemprop="itemCondition" itemtype="http://schema.org/OfferItemCondition" content="http://schema.org/NewCondition"/>
	   				</c:when>
	   				<c:when test="${advertisement.advertisement_id.item_state == 'USADO'}">
	    				<meta itemprop="itemCondition" itemtype="http://schema.org/OfferItemCondition" content="http://schema.org/UsedCondition"/>
	   				</c:when>
	   				<c:when test="${advertisement.advertisement_id.item_state == 'RECONDICIONADO'}">
	    				<meta itemprop="itemCondition" itemtype="http://schema.org/OfferItemCondition" content="http://schema.org/RefurbishedCondition"/>
	   				</c:when>
	   			</c:choose>
				<c:choose>
					<c:when test="${advertisement.advertisement_id.availability == 'EM ESTOQUE'}">
						<meta itemprop="availability" content="http://schema.org/InStock"/>
					</c:when>
					<c:when test="${advertisement.advertisement_id.availability == 'SEM ESTOQUE'}">
						<meta itemprop="availability" content="http://schema.org/OutOfStock"/>
					</c:when>
					<c:when test="${advertisement.advertisement_id.availability == 'PRÉ-VENDA'}">
						<meta itemprop="availability" content="http://schema.org/PreSale"/>
					</c:when>
				</c:choose>
			</div>
		</div>
	</c:forEach>
</div> --%>
<!-- success, info, warning, danger -->
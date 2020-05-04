<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- <script type="text/javascript">
	function clickAdvertisement(idAdvertisement){
		var lat = $('#currentUserLat').val();
		var lng = $('#currentUserLng').val();
		
	    $.ajax({
	        type: "POST",
	        url: '${giroUrl}/Controller?form=actions.AdvertisementsActions&action=resource&op=count&advertisement_id=' + idAdvertisement + '&lat=' + lat + '&lng=' + lng,
	        contentType: "application/json; charset=utf-8",
	        success: successFunc,
	        error: errorFunc
	    });
	}
	
	$(document).ready(function (){		
		clickAdvertisement('${currentAdvertisement.id}-${currentStore.id}');
	});
</script> -->

<div class="container" itemscope itemtype="http://schema.org/Product">

	<!-- Portfolio Item Heading -->
	<div class="row">
	    <div class="col-lg-12">
	        <h1 class="page-header"><span itemprop="name">${currentAdvertisement.title}</span>
	            <br /><small><a href="${giroUrl}${currentAdvertisement.user_id.url}">${currentStore.name}</a></small>
	        </h1>
	    </div>
	</div>
	<!-- /.row -->
	
	<!-- Portfolio Item Row -->
	<div class="row">
	
	    <div class="col-md-8">
	    	<c:choose>
			    <c:when test="${currentAdvertisement.listAdvertisementImages.size() > 0}">
			    	<a data-lightbox="${currentAdvertisement.id}" href="${giroUrl}/Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=${currentAdvertisement.listAdvertisementImages.get(0).image_file_name}">
			        	<img itemprop="image" data-lightbox="${currentAdvertisement.id}" class="img-responsive" src="${giroUrl}/Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=${currentAdvertisement.listAdvertisementImages.get(0).image_file_name}" alt="">
			        </a>
		        </c:when>
		        <c:otherwise>
			    	<a data-lightbox="${currentAdvertisement.id}" href="${giroUrl}/Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=giroDefaultImage">
			        	<img data-lightbox="${currentAdvertisement.id}" class="img-responsive" src="${giroUrl}/Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=giroDefaultImage" alt="">
			        </a>
		        </c:otherwise>
	        </c:choose>
	    </div>
		    
		<meta itemprop="productID" content="${currentAdvertisement.id}" />
		<meta itemprop="url" content="${giroUrl}${currentAdvertisement.url}" />
    	<c:if test="${currentAdvertisement.brand != null && currentAdvertisement.brand != ''}">
			<meta itemprop="brand" content="${currentAdvertisement.brand}" />
		</c:if>
		<c:if test="${currentAdvertisement.description != null && currentAdvertisement.description != ''}">
			<meta itemprop="description" content="${fn:substring(currentAdvertisement.description, 0, 4999)}">
		</c:if>
		
	    <div class="col-md-4" itemprop="offers" itemscope itemtype="http://schema.org/Offer">
	    	
	    	<br />
	    	<br />
	    	<br />
	    	<br />
	    
	    	<c:if test="${currentAdvertisement.brand != null && currentAdvertisement.brand != ''}">
				<span>Marca:  <b>${currentAdvertisement.brand}</b></span>						
				<br />
			</c:if>
			
	    	<c:choose>
	    		<c:when test="${currentAdvertisement.item_state != '0'}">
	    			<p>Condição: <b>${currentAdvertisement.item_state}</b></p>
	    		</c:when>
	    		<c:otherwise>	    		
	    			<c:choose>	    		
			    		<c:when test="${currentAdvertisement.item_state != ''}">
			    			<p>Condição: Não informado</p>
			    		</c:when>
			    		<c:otherwise>
		    				<p>Prestação de serviços</p>
		    			</c:otherwise>
	    			</c:choose>
	    		</c:otherwise>
	    	</c:choose>
	
			<br />
	
			<meta itemprop="price" content="${currentAdvertisement.price}" />
			<meta itemprop="pricecurrency" content="BRL" />
   			<c:choose>
   				<c:when test="${currentAdvertisement.item_state == 'NOVO'}">
    				<meta itemprop="itemCondition" itemtype="http://schema.org/OfferItemCondition" content="http://schema.org/NewCondition"/>
   				</c:when>
   				<c:when test="${currentAdvertisement.item_state == 'USADO'}">
    				<meta itemprop="itemCondition" itemtype="http://schema.org/OfferItemCondition" content="http://schema.org/UsedCondition"/>
   				</c:when>
   				<c:when test="${currentAdvertisement.item_state == 'RECONDICIONADO'}">
    				<meta itemprop="itemCondition" itemtype="http://schema.org/OfferItemCondition" content="http://schema.org/RefurbishedCondition"/>
   				</c:when>
   			</c:choose>
			<c:choose>
				<c:when test="${currentAdvertisement.availability == 'EM ESTOQUE'}">
					<meta itemprop="availability" content="http://schema.org/InStock"/>
				</c:when>
				<c:when test="${currentAdvertisement.availability == 'SEM ESTOQUE'}">
					<meta itemprop="availability" content="http://schema.org/OutOfStock"/>
				</c:when>
				<c:when test="${currentAdvertisement.availability == 'PRÉ-VENDA'}">
					<meta itemprop="availability" content="http://schema.org/PreSale"/>
				</c:when>
			</c:choose>
			
          	<c:choose>
          		<c:when test="${currentAdvertisement.price > 0}">	                    			
					por<h1 style="margin-top: 0px; margin-left: -2px; font-weight: bold; font-size: 40px;"><fmt:formatNumber value="${currentAdvertisement.price}" type="currency"/></h1>
          		</c:when>
          		<c:otherwise>
					<h2>sob consulta</h2>
          		</c:otherwise>
          	</c:choose>	                    	
			
			<c:if test="${currentAdvertisement.link != null && currentAdvertisement.link != ''}">
		        <br />
		        <a href="${currentAdvertisement.link}" class="btn btn-info bt-link-adv-buy" style="width: 145px !important; margin-bottom: 15px;" target="_blank">Comprar</a>
	        </c:if>
	        
	        <br />
	        <input type="hidden" id="giroUrl" value="${giroUrl}">
	        <button type="button" class="btn btn-success bt-message" name="btSave" advertisementId="${currentAdvertisement.id}" title="Enviar uma mensagem!">Contatar vendedor</button>

			<br />
			<br />
			
			<h3><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Anúncios</h3>
			<a href="${giroUrl}${currentAdvertisement.user_id.url}">${currentStore.name}</a>

	    </div>
	
	</div>
	<!-- /.row -->
	
	<!-- Related Projects Row -->
	<div class="row" itemscope itemtype="http://schema.org/ImageGallery">
	
	    <div class="col-lg-12">
	        <h3 class="page-header">Mais imagens</h3>

	    </div>
	    
	    <c:choose>
		    <c:when test="${listAdvertisementImages.size() > 0}">
				<c:forEach items="${listAdvertisementImages}" var="image">
					<div class="col-sm-3 col-xs-6">
				        <a data-lightbox="${currentAdvertisement.id}" href="${giroUrl}/Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=${image.image_file_name}">
				            <img itemprop="thumbnailUrl" class="img-responsive portfolio-item" src="${giroUrl}/Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=${image.image_file_name}" alt="">
				        </a>
				    </div>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<div class="col-sm-3 col-xs-6">
			        <a data-lightbox="${currentAdvertisement.id}" href="${giroUrl}/Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=giroDefaultImage">
			            <img class="img-responsive portfolio-item" src="${giroUrl}/Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=giroDefaultImage" alt="">
			        </a>
			    </div>		
			</c:otherwise>
		</c:choose>
		
	</div>
	<!-- /.row -->
	
	<hr>
	
	<div class="row">
	
		<div class="col-lg-12">
		
	        <h3 class="page-header">Descrição</h3>

	    </div>
	
		<div class="col-lg-12">
			${currentAdvertisement.description}
	
		</div>
		
	</div>
	
	<hr>
	
	<div class="row">
	
		<div class="col-lg-12">
		
	        <h3 class="page-header">Onde encontrar</h3>

	    </div>
	
		<div class="col-lg-12">
		
			Empresa: ${currentStore.company_fantasy_name}<BR />
			Telefone: ${currentStore.contact_phone}<BR />
			<br />
			Descrição: ${currentStore.company_description}<BR />
			<br />		
			Bairro: ${currentStore.neighborhood}<BR />
			Endereço: ${currentStore.address}<BR />
			Número: ${currentStore.number}<BR />
			Complemento: ${currentStore.complement}<BR />
			CEP: ${currentStore.postal_code}<BR />
	
		</div>
		
	</div>
	
	<br />
	<br />
	
</div>

<script>
	$(document).ready(function () {		
		/* botão comprar do anúncio */
		
		$('.bt-link-adv-buy').click(function () {
			
			var _gaq = _gaq || [];
			
			_gaq.push(["_trackEvent", "${currentAdvertisement.id}", "Clique botão comprar", "Link Anúncio"]);
			//_trackEvent('Clique botão comprar', '${currentAdvertisement.id}', 'Link Anúncio');
			
			clickButtonLinkAdvertisement('${giroUrl}', "${currentAdvertisement.id}-${currentStore.id}", 0, 0);
		});
	});
</script>

<%-- <BR />
<!-- adavertisments -->
<U>ADVERTISEMENT:</U><BR /><BR />
ID: ${currentAdvertisement.id}<BR />
TITLE: ${currentAdvertisement.title}<BR />
DESCRIPTION: ${currentAdvertisement.description}<BR />
BRAND: ${currentAdvertisement.brand}<BR />
PRICE: <fmt:formatNumber value="${currentAdvertisement.price}" type="currency"/><BR />
ITEM_STATE: ${currentAdvertisement.item_state}<BR />
AVAILABILITY: ${currentAdvertisement.availability}<BR />
LINK: ${currentAdvertisement.link}<BR />
<BR />
<BR />
<!-- stores -->
<U>STORE:</U><BR /><BR />
ID: ${currentStore.id}<BR />
NAME: ${currentStore.name}<BR />
FILIAL: ${currentStore.filial}<BR />
NEIGHBORHOOD: ${currentStore.neighborhood}<BR />
ADDRESS: ${currentStore.address}<BR />
NUMBER: ${currentStore.number}<BR />
COMPLEMENT: ${currentStore.complement}<BR />
POSTAL_CODE: ${currentStore.postal_code}<BR />
LATITUDE: ${currentStore.latitude}<BR />
LONGITUDE: ${currentStore.longitude}<BR />
CNPJ: ${currentStore.cnpj}<BR />
STATE_REGISTRATION: ${currentStore.state_registration}<BR />
COMPANY_NAME: ${currentStore.company_name}<BR />
COMPANY_FANTASY_NAME: ${currentStore.company_fantasy_name}<BR />
CONTACT_PHONE: ${currentStore.contact_phone}<BR />
CONTACT_EMAIL: ${currentStore.contact_email}<BR />
COMPANY_SLOGAN: ${currentStore.company_slogan}<BR />
COMPANY_DESCRIPTION: ${currentStore.company_description}<BR />
<BR />
<BR />
<!-- city/state -->
<U>CITY/STATE:</U><BR /><BR />
CITY: ${currentStore.city_id.name}<BR />
STATE: ${currentStore.city_id.state_id.name}<BR />
INITIALS: ${currentStore.city_id.state_id.initials}<BR />
<BR />
<BR />
<!-- user -->
<U>USER:</U><BR /><BR />
URL: ${currentStore.user_id.url}<BR />
COMPANY_NAME: ${currentStore.user_id.company_name}<BR />
CONTACT_PERSON: ${currentStore.user_id.contact_person}<BR />
LINK: ${currentStore.user_id.link}<BR />
<BR />
<BR />
<!-- advertisementStores -->
<U>ADVERTISEMENTSTORES (LIGAÇÃO):</U><BR /><BR />
URL: ${currentAdvertisementStore.url}<BR />
<BR />
<BR />
<!-- images -->
<U>IMAGES (LISTA):</U><BR /><BR />
<c:forEach items="${listAdvertisementImages}" var="image">
	URL: ${image.image_file_name}<BR />
</c:forEach>
<BR />
<BR /> --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div id="map-canvas"></div>
<div id="bt-about-us-home">
	<strong>Saiba Mais</strong>
	<br />		
	<span class="glyphicon glyphicon-menu-down"></span>
</div>

<div id="div-stayin">
	<span class="glyphicon glyphicon-remove close-stayin"></span>
	<span class="glyphicon glyphicon-thumbs-up"></span>
	<text>Que tal receber em primeira mão as novidades do <b>Giroferta</b>?
	Clica aqui <b>;)</b></text> 
</div>

<div class="container">

	<div class="row store-home-desktop">
<!-- 		<div class="col-lg-12"> -->
<!-- 			<h2 class="page-header"> -->
<!-- 				Nossos principais parceiros -->
<!-- 		    </h2> -->
<!-- 		</div> -->

		<br />
		<br />
	
		<c:forEach items="${listStoresHome}" var="user">
		
			<div class="col-sm-2 col-lg-2 col-md-2">
			
				<div style="border: none; opacity: 0.6;">
		            <br />
			        <a href="${user.url}">
		               	<img class="img-responsive img-store-home" src="${giroUrl}/Controller?form=actions.UsersActions&action=resource&op=viewImage&image=${user.image_file_name}" title="giroferta.com/${user.url}" alt="">
		            </a>
			    </div>
			    
<!-- 			    <div class="caption" style="text-align: center;"> -->
			    
<!-- 	                    <h5 style="height: 50px;"> -->
<%-- 	                    	<a href="${user.url}"><span>${user.listStores.get(0).name}</span></a> --%>
<!-- 	                    </h5> -->
	                    
<!-- 	            </div> -->
			</div>
			
		</c:forEach>
	</div>
	
	<div class="row store-home-mobile">
	
		<c:forEach items="${listStoresHome}" var="user">
	
			<div class="div-store-mobile">
	            <br />
		        <a href="${user.url}">
	               	<img class="img-responsive img-store-home" src="${giroUrl}/Controller?form=actions.UsersActions&action=resource&op=viewImage&image=${user.image_file_name}" title="giroferta.com/${user.url}" alt="">
	            </a>
		    </div>
		
		</c:forEach>
	</div>
	
	<br />
	<br />
	<hr />

	<div id="id-most-accessed-adv" class="row">
	
		<div class="col-lg-12">
			<h2 class="page-header">
				Os mais acessados
		    </h2>
		</div>
	
		<c:forEach items="${listMostAccessed}" var="advertisement" varStatus="items">
		
			<input type="hidden" value="${advertisement.advertisement_id.id}" id="most-accessed-ads-${items.count}">
			<input type="hidden" value="${advertisement.store_id.id}" id="most-accessed-sto-${items.count}">
				
			<div itemscope itemtype="http://schema.org/Product">
		
				<meta itemprop="productID" content="${advertisement.advertisement_id.id}" />
				<meta itemprop="name" content="${advertisement.advertisement_id.title}" />
				<meta itemprop="url" content="${giroUrl}${advertisement.advertisement_id.url}" />
			   	<c:if test="${advertisement.advertisement_id.brand != null && advertisement.advertisement_id.brand != ''}">
					<meta itemprop="brand" content="${advertisement.advertisement_id.brand}" />
				</c:if>
				<c:if test="${advertisement.advertisement_id.description != null && advertisement.advertisement_id.description != ''}">
					<meta itemprop="description" content="${fn:substring(advertisement.advertisement_id.description, 0, 4999)}">
				</c:if>
		
				<div class="col-sm-3 col-lg-3 col-md-3" itemprop="offers" itemscope itemtype="http://schema.org/Offer">
		
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
				
		            <div class="thumbnail">
		                
		                <c:choose>
			                <c:when test="${advertisement.advertisement_id.listAdvertisementImages.size() > 0}">
<%-- 				                <a href="${giroUrl}${advertisement.advertisement_id.url}" onclick="clickAdvertisementFeatured('${advertisement.advertisement_id.id}-${advertisement.store_id.id}', 'countMostAccessed', '${giroUrl}${advertisement.advertisement_id.url}');"> --%>
				                <a href="${giroUrl}${advertisement.advertisement_id.url}" class="link-most-accessed-${items.count}">
				                	<img itemprop="image" class="img-responsive" src="${giroUrl}/Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=${advertisement.advertisement_id.listAdvertisementImages.get(0).image_file_name}" alt="">
				                </a>
			                </c:when>
			                <c:otherwise>
<%-- 				                <a href="javascript:void(0);" onclick="clickAdvertisementFeatured('${advertisement.advertisement_id.id}-${advertisement.store_id.id}', 'countMostAccessed', '${giroUrl}${advertisement.advertisement_id.url}');"> --%>
								<a href="${giroUrl}${advertisement.advertisement_id.url}" class="link-most-accessed-${items.count}">
				                	<img class="img-responsive" src="resources/images/defaultImage.png" alt="">
				                </a>
			                </c:otherwise>
		                </c:choose>
		                
		                <div class="caption" style="text-align: center;">
		                    <c:choose>
			                    <c:when test="${fn:length(advertisement.advertisement_id.title) > 61}">
				                    <h4 style="height: 60px;">
<%-- 				                    	<a href="javascript:void(0);" onclick="clickAdvertisementFeatured('${advertisement.advertisement_id.id}-${advertisement.store_id.id}', 'countMostAccessed', '${giroUrl}${advertisement.advertisement_id.url}');">${fn:substring(advertisement.advertisement_id.title, 0, 61)}...</a> --%>
       									<a href="${giroUrl}${advertisement.advertisement_id.url}" class="link-most-accessed-${items.count}">${fn:substring(advertisement.advertisement_id.title, 0, 61)}...</a>
				                    </h4>
			                    </c:when>
			                    <c:otherwise>
				                    <h4 style="height: 60px;">
<%-- 				                    	<a href="javascript:void(0);" onclick="clickAdvertisementFeatured('${advertisement.advertisement_id.id}-${advertisement.store_id.id}', 'countMostAccessed', '${giroUrl}${advertisement.advertisement_id.url}');">${advertisement.advertisement_id.title}</a> --%>
				                    	<a href="${giroUrl}${advertisement.advertisement_id.url}" class="link-most-accessed-${items.count}">${advertisement.advertisement_id.title}</a>
				                    </h4>
			                    </c:otherwise>
		                    </c:choose>
		                    
		                    <span style="height: 20px; font-size: 11px;">
		                    	<c:choose>
									<c:when test="${fn:length(advertisement.store_id.name) > 35}">
										<c:set var="stringAux" value="${fn:substring(advertisement.store_id.name, 0, 34)}" />
		                    			<a href="${giroUrl}${advertisement.store_id.user_id.url}">${stringAux}...</a>
		                    		</c:when>
		                    		<c:otherwise>
		                    			<a href="${giroUrl}${advertisement.store_id.user_id.url}">${advertisement.store_id.name}</a>
		                    		</c:otherwise>
		                    	</c:choose>
		                    </span>
		                    
		                    
		                    <h3>
		                    	<c:choose>
		                    		<c:when test="${advertisement.advertisement_id.price > 0}">
		                    			<fmt:formatNumber value="${advertisement.advertisement_id.price}" type="currency"/>
		                    		</c:when>
		                    		<c:otherwise>
		                    			sob consulta
		                    		</c:otherwise>
		                    	</c:choose>
		                    </h3>
		                    
		                    
<%-- 		                    <a href="javascript:void(0);" onclick="clickAdvertisementFeatured('${advertisement.advertisement_id.id}-${advertisement.store_id.id}', 'countMostAccessed', '${giroUrl}${advertisement.advertisement_id.url}');" class="btn btn-info">Quero saber mais</a> --%>
		                    <a href="${giroUrl}${advertisement.advertisement_id.url}" class="btn btn-info link-most-accessed-${items.count}">Quero saber mais</a>
		                    
		                </div>
		            </div>
		        </div>
	        </div>
	        
		</c:forEach>
	
	</div>
	
</div>

<div class="container">

	<div id="id-recently-added-adv" class="row">

		<div class="col-lg-12">
			<h2 class="page-header">
				Os mais recentes
		    </h2>
		</div>
	
		<c:forEach items="${listNewAds}" var="advertisement" varStatus="items">
		
			<input type="hidden" value="${advertisement.advertisement_id.id}" id="recently-added-ads-${items.count}">
			<input type="hidden" value="${advertisement.store_id.id}" id="recently-added-sto-${items.count}">
				
			<div itemscope itemtype="http://schema.org/Product">
		
				<meta itemprop="productID" content="${advertisement.advertisement_id.id}" />
				<meta itemprop="name" content="${advertisement.advertisement_id.title}" />
				<meta itemprop="url" content="${giroUrl}${advertisement.advertisement_id.url}" />
			   	<c:if test="${advertisement.advertisement_id.brand != null && advertisement.advertisement_id.brand != ''}">
					<meta itemprop="brand" content="${advertisement.advertisement_id.brand}" />
				</c:if>
				<c:if test="${advertisement.advertisement_id.description != null && advertisement.advertisement_id.description != ''}">
					<meta itemprop="description" content="${fn:substring(advertisement.advertisement_id.description, 0, 4999)}">
				</c:if>
		
				<div class="col-sm-2 col-lg-2 col-md-2" itemprop="offers" itemscope itemtype="http://schema.org/Offer">
				
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
				
		            <div class="thumbnail">
		            	<br />
		                
						<c:choose>
			                <c:when test="${advertisement.advertisement_id.listAdvertisementImages.size() > 0}">
<%-- 				                <a href="javascript:void(0);" onclick="clickAdvertisementFeatured('${advertisement.advertisement_id.id}-${advertisement.store_id.id}', 'countRecentlyAdded', '${giroUrl}${advertisement.advertisement_id.url}');"> --%>
								<a href="${giroUrl}${advertisement.advertisement_id.url}" class="link-recently-added-${items.count}">
				                	<img itemprop="image" class="img-adv-home-thumb" class="img-responsive" src="${giroUrl}/Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=${advertisement.advertisement_id.listAdvertisementImages.get(0).image_file_name}" alt="">
				                </a>
			                </c:when>
			                <c:otherwise>
<%-- 				                <a href="javascript:void(0);" onclick="clickAdvertisementFeatured('${advertisement.advertisement_id.id}-${advertisement.store_id.id}', 'countRecentlyAdded', '${giroUrl}${advertisement.advertisement_id.url}');"> --%>
								<a href="${giroUrl}${advertisement.advertisement_id.url}" class="link-recently-added-${items.count}">
				                	<img class="img-adv-home-thumb" class="img-responsive" src="resources/images/defaultImage.png" alt="">
				                </a>
			                </c:otherwise>
						</c:choose>
		                
		                <div class="caption" style="text-align: center;">
		                    
		                    <h4>
		                    	<c:choose>
		                    		<c:when test="${advertisement.advertisement_id.price > 0}">	                    			
		                    			<fmt:formatNumber value="${advertisement.advertisement_id.price}" type="currency"/>
		                    		</c:when>
		                    		<c:otherwise>
		                    			sob consulta
		                    		</c:otherwise>
		                    	</c:choose>	                    	
		                    </h4>
		                    
		                    <c:choose>
			                    <c:when test="${fn:length(advertisement.advertisement_id.title) > 46}">
				                    <h5 style="height: 50px;">
<%-- 				                    	<a href="javascript:void(0);" onclick="clickAdvertisementFeatured('${advertisement.advertisement_id.id}-${advertisement.store_id.id}', 'countRecentlyAdded', '${giroUrl}${advertisement.advertisement_id.url}');">${fn:substring(advertisement.advertisement_id.title, 0, 46)}...</a> --%>
										<a href="${giroUrl}${advertisement.advertisement_id.url}" class="link-recently-added-${items.count}">${fn:substring(advertisement.advertisement_id.title, 0, 46)}...</a>
				                    </h5>
			                    </c:when>
			                    <c:otherwise>
				                    <h5 style="height: 50px;">
<%-- 				                    	<a href="javascript:void(0);" onclick="clickAdvertisementFeatured('${advertisement.advertisement_id.id}-${advertisement.store_id.id}', 'countRecentlyAdded', '${giroUrl}${advertisement.advertisement_id.url}');">${advertisement.advertisement_id.title}</a> --%>
				                    	<a href="${giroUrl}${advertisement.advertisement_id.url}" class="link-recently-added-${items.count}">${advertisement.advertisement_id.title}</a>
				                    </h5>
			                    </c:otherwise>
		                    </c:choose>
		                    
		                    <%-- <a href="javascript:void(0);" onclick="clickAdvertisementFeatured('${advertisement.advertisement_id.id}-${advertisement.store_id.id}', 'countRecentlyAdded', '${giroUrl}${advertisement.advertisement_id.url}');" class="btn btn-info">Quero saber mais</a> --%>
	                    	<%-- <a href="${giroUrl}${advertisement.advertisement_id.url}" class="btn btn-info link-recently-added-${items.count}">Quero saber mais</a> --%>		                    
		                    
		                    <span style="height: 20px; font-size: 11px;">
		                    	<c:choose>
									<c:when test="${fn:length(advertisement.store_id.name) > 25}">
										<c:set var="stringAux" value="${fn:substring(advertisement.store_id.name, 0, 24)}" />
		                    			<a href="${giroUrl}${advertisement.store_id.user_id.url}">${stringAux}...</a>
		                    		</c:when>
		                    		<c:otherwise>
		                    			<a href="${giroUrl}${advertisement.store_id.user_id.url}">${advertisement.store_id.name}</a>
		                    		</c:otherwise>
		                    	</c:choose>
		                    </span>
		                    
		                    
		                </div>
		            </div>
		        </div>
	        </div>
	        
		</c:forEach>
	
	</div>
	
</div>

<!-- <script type="text/javascript">

	$(document).ready(function () {
		
		$('#id-most-accessed-adv').live('click', function() {
			console.log('MostAccessed...');
		});
		
		$('#id-recently-added-adv').live('click', function() {
			console.log('RecentlyAdded...');
		});
		
	});
	
</script>  -->

<div id="wrapper-about-us">
	<div class="step-by-step-home">
		<span>DÊ UM PLAY NO SEU <strong>GIRO DE OFERTAS</strong><br />COM APENAS QUATRO ETAPAS BÁSICAS:</span>
		<br />
		<img alt="Passo a passo" src="${giroUrl }/resources/images/step-by-step-home.png">
	</div>	
	<div class="step-by-step-mobile">
		<span>DÊ UM PLAY NO SEU <strong>GIRO DE OFERTAS</strong><br />COM APENAS QUATRO ETAPAS BÁSICAS:</span>
		<br />
		<br />
		<img alt="Passo a passo" src="${giroUrl }/resources/images/step-by-step-mobile.png">
	</div>
	<div class="div-weve-got">
		<span class="glyphicon glyphicon-signal"></span>
		<br />
		Está na hora de fazer seus clientes verem os produtos que eles realmente procuram.
		<br />
		Nós temos a ferramenta certa para potencializar suas vendas
		<br /> e fazer com que compradores de fato encontrem vendedores.
	</div>
	<div class="div-prices-home">
		<span class="ttl-prices">SEM COMPROMISSO OU COBRANÇA MENSAL! :D</span>
		<br />
		Queremos ser justos sempre! Por isso cobramos de maneira pré paga,
		<br />
		a partir da compra de pacotes de créditos sem prazo de validade, e cada visita que
		<br />
		 seu anúncio receber, iremos descontar do saldo de sua conta no site.
		<br />
		<br />
		<br />
		<br />
		<span class="ttl-prices">VALORIZAMOS O SEU DINHEIRO! $$$</span>
		<br />
		Para cada visita que seu anúncio receber, descontaremos apenas <fmt:formatNumber value="${basicClickValue}" type="currency"/> da sua conta.
		<br />
		
		<c:set var="aux" value="${fn:replace(basicClickValue,'0.0', '')}"/>
		<c:set var="aux" value="${fn:replace(aux,'0.', '')}"/>
		<c:set var="aux" value="${fn:replace(aux,'.', '')}"/>
		
		(sim, ${aux} centavos! o.O')
		<br />
		Quando seus créditos acabarem, basta fazer uma nova recarga para continuar
		<br />
		fechando os melhores negócios.
	</div>
	<div class="div-our-skills">
		<div class="box-found-products">
			<span class="glyphicon glyphicon-map-marker"></span>
			<br />
			<label>Encontre facilmente e diretamente no mapa a loja mais perto de você e com o melhor preço!</label>
		</div>	
		<div class="box-contact-vendor">
			<span class="glyphicon glyphicon-exclamation-sign"></span>
			<br />
			<label>Contate o vendedor anunciante para tirar dúvidas referentes ao produto.</label>
		</div>
		<div class="box-dont-spend-money">
			<span class="glyphicon glyphicon-piggy-bank"></span>
			<br />
			<label>Economize dinheiro e tempo deixando de passar em diversas lojas para encontrar o que procura.</label>
		</div>
		<div class="box-be-partner">
			<span class="glyphicon glyphicon-ok-circle"></span>
			<br />
			<label>Contate-nos para ser uma loja parceira e ter diversas vantagens!</label>
		</div>
	</div>
</div>
<jsp:include page="/jsp/modalSearch.jsp"/>

<script>
	$(document).ready(function () {		
		
		$('.link-most-accessed-1').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#most-accessed-ads-1').val()+'-'+$('#most-accessed-sto-1').val(), 'countMostAccessed');
		});
		$('.link-most-accessed-2').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#most-accessed-ads-2').val()+'-'+$('#most-accessed-sto-2').val(), 'countMostAccessed');
		});
		$('.link-most-accessed-3').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#most-accessed-ads-3').val()+'-'+$('#most-accessed-sto-3').val(), 'countMostAccessed');
		});
		$('.link-most-accessed-4').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#most-accessed-ads-4').val()+'-'+$('#most-accessed-sto-4').val(), 'countMostAccessed');
		});
		$('.link-most-accessed-5').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#most-accessed-ads-5').val()+'-'+$('#most-accessed-sto-5').val(), 'countMostAccessed');
		});
		$('.link-most-accessed-6').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#most-accessed-ads-6').val()+'-'+$('#most-accessed-sto-6').val(), 'countMostAccessed');
		});
		$('.link-most-accessed-7').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#most-accessed-ads-7').val()+'-'+$('#most-accessed-sto-7').val(), 'countMostAccessed');
		});
		$('.link-most-accessed-8').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#most-accessed-ads-8').val()+'-'+$('#most-accessed-sto-8').val(), 'countMostAccessed');
		});
		
		
		$('.link-recently-added-1').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#recently-added-ads-1').val()+'-'+$('#recently-added-sto-1').val(), 'countRecentlyAdded');
		});
		$('.link-recently-added-2').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#recently-added-ads-2').val()+'-'+$('#recently-added-sto-2').val(), 'countRecentlyAdded');
		});
		$('.link-recently-added-3').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#recently-added-ads-3').val()+'-'+$('#recently-added-sto-3').val(), 'countRecentlyAdded');
		});
		$('.link-recently-added-4').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#recently-added-ads-4').val()+'-'+$('#recently-added-sto-4').val(), 'countRecentlyAdded');
		});
		$('.link-recently-added-5').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#recently-added-ads-5').val()+'-'+$('#recently-added-sto-5').val(), 'countRecentlyAdded');
		});
		$('.link-recently-added-6').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#recently-added-ads-6').val()+'-'+$('#recently-added-sto-6').val(), 'countRecentlyAdded');
		});
		$('.link-recently-added-7').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#recently-added-ads-7').val()+'-'+$('#recently-added-sto-7').val(), 'countRecentlyAdded');
		});
		$('.link-recently-added-8').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#recently-added-ads-8').val()+'-'+$('#recently-added-sto-8').val(), 'countRecentlyAdded');
		});
		$('.link-recently-added-9').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#recently-added-ads-9').val()+'-'+$('#recently-added-sto-9').val(), 'countRecentlyAdded');
		});
		$('.link-recently-added-10').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#recently-added-ads-10').val()+'-'+$('#recently-added-sto-10').val(), 'countRecentlyAdded');
		});
		$('.link-recently-added-11').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#recently-added-ads-11').val()+'-'+$('#recently-added-sto-11').val(), 'countRecentlyAdded');
		});
		$('.link-recently-added-12').click(function () {
			clickAdvertisementFeatured('${giroUrl}', $('#recently-added-ads-12').val()+'-'+$('#recently-added-sto-12').val(), 'countRecentlyAdded');
		});
		
	});
</script>


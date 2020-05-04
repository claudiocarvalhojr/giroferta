<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="container" style="margin-top: 50px;">
	<div class="row">
	    <div class="col-md-4">
	        <c:choose>
				<c:when test="${currentStore.image_file_name ne ''}">
			  		<img style="width: 300px; margin: auto; padding-top: 30px;" class="img-responsive img-rounded" src="${giroUrl}/Controller?form=actions.UsersActions&action=resource&op=viewImage&image=${currentStore.image_file_name}" alt="">
				</c:when>
				<c:when test="${currentStore.image_file_name eq ''}">
					<img style="width: 300px; margin: auto; padding-top: 30px;" class="img-responsive img-rounded" src="${giroUrl}/Controller?form=actions.UsersActions&action=resource&op=viewImage&image=giroDefaultImage" alt="">
				</c:when>
			</c:choose>
	    </div>
	    <div class="col-md-8">
	        <h1>${currentStore.listStores.get(0).company_fantasy_name}</h1>
	        <p>${currentStore.listStores.get(0).company_description}</p>
 	        <a class="btn btn-success" href="${currentStore.link}">Acessar site da loja</a>
	    </div>
	</div>
	
	<hr>

	<div class="row">
	    <div class="col-lg-12">
	        <div class="well text-center">
	            <strong>Anúncios</strong>: ${currentStore.count_advertisements} - <strong>Lojas cadastradas</strong>: ${currentStore.count_stores }
	        </div>
	    </div>
	</div>
	
	<div class="row">
		<div class="col-lg-12">
			&nbsp;<input type="hidden" value="${currentStore.url }" id="currstore-url" />
		</div>
	</div>
	
	<div class="row">
		<div class="col-lg-2">
			&nbsp;
		</div>	
		<div class="col-lg-8">			
			<div class="input-group">
			  <input type="text" class="form-control search-input-store" placeholder="Procurando algo específico?" aria-describedby="basic-addon2">
			  <div class="input-group-btn">
			    <button type="button" class="btn btn-primary do-search-store">
			    	<span class="glyphicon glyphicon-search"></span>
			    </button>
			  </div>
			</div>
		</div>
		<div class="col-lg-2">
			&nbsp;
		</div>
	</div>	
	
	<div id="id-most-accessed-adv" class="row">
	
		<div class="col-lg-12">
			<h2 class="page-header">
				Os mais acessados
		    </h2>
		</div>
	
		<c:if test="${listAdvMostAccessed.size() > 0}">
			<c:forEach items="${listAdvMostAccessed}" var="advertisement">
				
				<div itemscope itemtype="http://schema.org/Product">
			
					<meta itemprop="productID" content="${advertisement.id}" />
					<meta itemprop="name" content="${advertisement.title}" />
					<meta itemprop="url" content="${giroUrl}${advertisement.url}" />
				   	<c:if test="${advertisement.brand != null && advertisement.brand != ''}">
						<meta itemprop="brand" content="${advertisement.brand}" />
					</c:if>
					<c:if test="${advertisement.description != null && advertisement.description != ''}">
						<meta itemprop="description" content="${fn:substring(advertisement.description, 0, 4999)}">
					</c:if>
			
					<div class="col-sm-3 col-lg-3 col-md-3" itemprop="offers" itemscope itemtype="http://schema.org/Offer">
			
						<meta itemprop="price" content="${advertisement.price}" />
						<meta itemprop="pricecurrency" content="BRL" />
						<c:if test="${advertisement.reference != null && advertisement.reference != ''}">
							<meta itemprop="sku" content="${advertisement.reference}" />
						</c:if>
						<c:choose>
							<c:when test="${advertisement.item_state == 'NOVO'}">
								<meta itemprop="itemCondition" itemtype="http://schema.org/OfferItemCondition" content="http://schema.org/NewCondition"/>
							</c:when>
							<c:when test="${advertisement.item_state == 'USADO'}">
								<meta itemprop="itemCondition" itemtype="http://schema.org/OfferItemCondition" content="http://schema.org/UsedCondition"/>
							</c:when>
							<c:when test="${advertisement.item_state == 'RECONDICIONADO'}">
								<meta itemprop="itemCondition" itemtype="http://schema.org/OfferItemCondition" content="http://schema.org/RefurbishedCondition"/>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${advertisement.availability == 'EM ESTOQUE'}">
								<meta itemprop="availability" content="http://schema.org/InStock"/>
							</c:when>
							<c:when test="${advertisement.availability == 'SEM ESTOQUE'}">
								<meta itemprop="availability" content="http://schema.org/OutOfStock"/>
							</c:when>
							<c:when test="${advertisement.availability == 'PRÉ-VENDA'}">
								<meta itemprop="availability" content="http://schema.org/PreSale"/>
							</c:when>
						</c:choose>
					
			            <div class="thumbnail">
			                
			                <c:choose>
				                <c:when test="${advertisement.listAdvertisementImages.size() > 0}">
					                <a href="javascript:void(0);" onclick="clickAdvertisementFeaturedStore('${advertisement.id}-${currentStore.listStores.get(0).id}', 'countMostAccessed', '${giroUrl}${advertisement.url}', 0, 0);">
					                	<img itemprop="image" class="img-responsive" src="${giroUrl}/Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=${advertisement.listAdvertisementImages.get(0).image_file_name}" alt="">
					                </a>
				                </c:when>
				                <c:otherwise>
					                <a href="javascript:void(0);" onclick="clickAdvertisementFeaturedStore('${advertisement.id}-${currentStore.listStores.get(0).id}', 'countMostAccessed', '${giroUrl}${advertisement.url}', 0, 0);">
					                	<img class="img-responsive" src="resources/images/defaultImage.png" alt="">
					                </a>
				                </c:otherwise>
			                </c:choose>
			                
			                <div class="caption" style="text-align: center;">
			                    <c:choose>
				                    <c:when test="${fn:length(advertisement.title) > 61}">
					                    <h4 style="height: 60px;">
					                    	<a href="javascript:void(0);" onclick="clickAdvertisementFeaturedStore('${advertisement.id}-${currentStore.listStores.get(0).id}', 'countMostAccessed', '${giroUrl}${advertisement.url}', 0, 0);">${fn:substring(advertisement.title, 0, 61)}...</a>
					                    </h4>
				                    </c:when>
				                    <c:otherwise>
					                    <h4 style="height: 60px;">
					                    	<a href="javascript:void(0);" onclick="clickAdvertisementFeaturedStore('${advertisement.id}-${currentStore.listStores.get(0).id}', 'countMostAccessed', '${giroUrl}${advertisement.url}', 0, 0);">${advertisement.title}</a>
					                    </h4>
				                    </c:otherwise>
			                    </c:choose>
			                    <h3>
			                    	<c:choose>
			                    		<c:when test="${advertisement.price > 0}">
			                    			<fmt:formatNumber value="${advertisement.price}" type="currency"/>
			                    		</c:when>
			                    		<c:otherwise>
			                    			sob consulta
			                    		</c:otherwise>
			                    	</c:choose>
			                    </h3>
			                    
			                </div>
			            </div>
			        </div>
		        </div>
			</c:forEach>
		</c:if>
	
	</div>
	
	<div id="id-recently-added-adv" class="row">

		<div class="col-lg-12">
			<h2 class="page-header">
				Os mais recentes
		    </h2>
		</div>
	
		<c:if test="${listAdvRecentlyAdded.size() > 0}">
			<c:forEach items="${listAdvRecentlyAdded}" var="advertisement">
				
				<div itemscope itemtype="http://schema.org/Product">
			
					<meta itemprop="productID" content="${advertisement.id}" />
					<meta itemprop="name" content="${advertisement.title}" />
					<meta itemprop="url" content="${giroUrl}${advertisement.url}" />
				   	<c:if test="${advertisement.brand != null && advertisement.brand != ''}">
						<meta itemprop="brand" content="${advertisement.brand}" />
					</c:if>
					<c:if test="${advertisement.description != null && advertisement.description != ''}">
						<meta itemprop="description" content="${fn:substring(advertisement.description, 0, 4999)}">
					</c:if>
			
					<div class="col-sm-2 col-lg-2 col-md-2" itemprop="offers" itemscope itemtype="http://schema.org/Offer">
					
						<meta itemprop="price" content="${advertisement.price}" />
						<meta itemprop="pricecurrency" content="BRL" />
						<c:if test="${advertisement.reference != null && advertisement.reference != ''}">
							<meta itemprop="sku" content="${advertisement.reference}" />
						</c:if>
						<c:choose>
							<c:when test="${advertisement.item_state == 'NOVO'}">
								<meta itemprop="itemCondition" itemtype="http://schema.org/OfferItemCondition" content="http://schema.org/NewCondition"/>
							</c:when>
							<c:when test="${advertisement.item_state == 'USADO'}">
								<meta itemprop="itemCondition" itemtype="http://schema.org/OfferItemCondition" content="http://schema.org/UsedCondition"/>
							</c:when>
							<c:when test="${advertisement.item_state == 'RECONDICIONADO'}">
								<meta itemprop="itemCondition" itemtype="http://schema.org/OfferItemCondition" content="http://schema.org/RefurbishedCondition"/>
							</c:when>
						</c:choose>
						<c:choose>
							<c:when test="${advertisement.availability == 'EM ESTOQUE'}">
								<meta itemprop="availability" content="http://schema.org/InStock"/>
							</c:when>
							<c:when test="${advertisement.availability == 'SEM ESTOQUE'}">
								<meta itemprop="availability" content="http://schema.org/OutOfStock"/>
							</c:when>
							<c:when test="${advertisement.availability == 'PRÉ-VENDA'}">
								<meta itemprop="availability" content="http://schema.org/PreSale"/>
							</c:when>
						</c:choose>
					
			            <div class="thumbnail">
			            	<br />
			                
			                <c:choose>
				                <c:when test="${advertisement.listAdvertisementImages.size() > 0}">
					                <a href="javascript:void(0);" onclick="clickAdvertisementFeaturedStore('${advertisement.id}-${currentStore.listStores.get(0).id}', 'countRecentlyAdded', '${giroUrl}${advertisement.url}', 0, 0);">
					                	<img itemprop="image" class="img-adv-home-thumb" class="img-responsive" src="${giroUrl}/Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=${advertisement.listAdvertisementImages.get(0).image_file_name}" alt="">
					                </a>
				                </c:when>	                
				                <c:otherwise>
					                <a href="javascript:void(0);" onclick="clickAdvertisementFeaturedStore('${advertisement.id}-${currentStore.listStores.get(0).id}', 'countRecentlyAdded', '${giroUrl}${advertisement.url}', 0, 0);">
					                	<img class="img-adv-home-thumb" class="img-responsive" src="resources/images/defaultImage.png" alt="">
					                </a>
				                </c:otherwise>
			                </c:choose>
			                
			                <div class="caption" style="text-align: center;">
			                    
			                    <h4>
			                    	<c:choose>
			                    		<c:when test="${advertisement.price > 0}">	                    			
			                    			<fmt:formatNumber value="${advertisement.price}" type="currency"/>
			                    		</c:when>
			                    		<c:otherwise>
			                    			sob consulta
			                    		</c:otherwise>
			                    	</c:choose>	                    	
			                    </h4>
			                    
			                    <c:choose>
				                    <c:when test="${fn:length(advertisement.title) > 46}">
					                    <h5 style="height: 50px;">
					                    	<a href="javascript:void(0);" onclick="clickAdvertisementFeaturedStore('${advertisement.id}-${currentStore.listStores.get(0).id}', 'countRecentlyAdded', '${giroUrl}${advertisement.url}', 0, 0);">${fn:substring(advertisement.title, 0, 46)}...</a>
					                    </h5>
				                    </c:when>
				                    <c:otherwise>
					                    <h5 style="height: 50px;">
					                    	<a href="javascript:void(0);" onclick="clickAdvertisementFeaturedStore('${advertisement.id}-${currentStore.listStores.get(0).id}', 'countRecentlyAdded', '${giroUrl}${advertisement.url}', 0, 0);">${advertisement.title}</a>
					                    </h5>
				                    </c:otherwise>
			                    </c:choose>
			                    
			                    <%-- <a href="javascript:void(0);" onclick="clickAdvertisementFeaturedStore('${advertisement.id}-${currentStore.listStores.get(0).id}', 'countRecentlyAdded', '${giroUrl}${advertisement.url}', 0, 0);" class="btn btn-info">Quero saber mais</a> --%>
			                    
			                </div>
			            </div>
			        </div>
		        </div>
			</c:forEach>
		</c:if>
	</div>
</div>


<!-- <!-- CURRENT STORE (dados do user) -->
<!-- <U>STORE (dados do user):</U><BR /><BR /> -->
<%-- URL: /${currentStore.url}<BR /> --%>
<%-- COMPANY_NAME: ${currentStore.company_name}<BR /> --%>
<%-- CONTACT_PERSON: ${currentStore.contact_person}<BR /> --%>
<%-- NAME: ${currentStore.name}<BR /> --%>
<%-- LAST_NAME: ${currentStore.last_name}<BR /> --%>
<%-- LINK: ${currentStore.link}<BR /> --%>
<%-- IMAGE_FILE_NAME: ${currentStore.image_file_name}<BR /> --%>

<!-- <!-- LIST STORES ADDRESS (dados de lojas da currentStore/user) -->
<%-- <c:forEach items="${currentStore.listStores}" var="store"> --%>
<%-- 	<U>ADDRESS (dados da loja): ${store.filial}</U><BR /><BR /> --%>
<%-- 	ID: ${store.id}<BR /> --%>
<%-- 	NAME: ${store.name}<BR /> --%>
<%-- 	FILIAL: ${store.filial}<BR /> --%>
<%-- 	CITY: ${store.city_id.name}<BR /> --%>
<%-- 	STATE: ${store.city_id.state_id.name}<BR /> --%>
<%-- 	INITIALS: ${store.city_id.state_id.initials}<BR /> --%>
<%-- 	NEIGHBORHOOD: ${store.neighborhood}<BR /> --%>
<%-- 	ADDRESS: ${store.address}<BR /> --%>
<%-- 	NUMBER: ${store.number}<BR /> --%>
<%-- 	COMPLEMENT: ${store.complement}<BR /> --%>
<%-- 	POSTAL_CODE: ${store.postal_code}<BR /> --%>
<%-- 	LATITUDE: ${store.latitude}<BR /> --%>
<%-- 	LONGITUDE: ${store.longitude}<BR /> --%>
<%-- 	CNPJ: ${store.cnpj}<BR /> --%>
<%-- 	STATE_REGISTRATION: ${store.state_registration}<BR /> --%>
<%-- 	COMPANY_NAME: ${store.company_name}<BR /> --%>
<%-- 	COMPANY_FANTASY_NAME: ${store.company_fantasy_name}<BR /> --%>
<%-- 	CONTACT_PHONE: ${store.contact_phone}<BR /> --%>
<%-- 	CONTACT_EMAIL: ${store.contact_email}<BR /> --%>
<%-- 	COMPANY_SLOGAN: ${store.company_slogan}<BR /> --%>
<%-- 	COMPANY_DESCRIPTION: ${currentStoresAddress.company_description}<BR /> --%>
<!-- 	<br /> -->
<!-- 	<br /> -->
<%-- </c:forEach> --%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<script type="text/javascript">
	$(document).ready(function () {
		
		var q = getParameterByName('q');
		$('#search-res').html(q);
		
	});
</script>

<div class="container" style="margin-top: 0px;">
	
	<div class="row">
	
		<div class="col-lg-12">
			<h4 class="page-header">
				Exibindo <b>${list.size()}</b> resultados para "<b><span id="search-res"></b></span>"
		    </h4>
		</div>
	
		<c:if test="${list.size() > 0}">
			<c:forEach items="${list}" var="advertisement">
				<c:if test="${advertisement.listAdvertisementImages.size() > 0}">
				
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
					                <c:when test="${advertisement.qtda_images > 0}">
						                <a href="javascript:void(0);" onclick="clickAdvertisementFeaturedStore('${advertisement.id}-${currentStore.listStores.get(0).id}', 'countSearch', '${giroUrl}${advertisement.url}', 0, 0);">
						                	<img itemprop="image" class="img-responsive" src="${giroUrl}/Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=${advertisement.listAdvertisementImages.get(0).image_file_name}" alt="">
						                </a>
					                </c:when>
					                <c:otherwise>
						                <a href="javascript:void(0);" onclick="clickAdvertisementFeaturedStore('${advertisement.id}-${currentStore.listStores.get(0).id}', 'countSearch', '${giroUrl}${advertisement.url}', 0, 0);">
						                	<img class="img-responsive" src="resources/images/defaultImage.png" alt="">
						                </a>
					                </c:otherwise>
				                </c:choose>
				                
				                <div class="caption" style="text-align: center;">
				                    <c:choose>
					                    <c:when test="${fn:length(advertisement.title) > 61}">
						                    <h4 style="height: 60px;">
						                    	<a href="javascript:void(0);" onclick="clickAdvertisementFeaturedStore('${advertisement.id}-${currentStore.listStores.get(0).id}', 'countSearch', '${giroUrl}${advertisement.url}', 0, 0);">${fn:substring(advertisement.title, 0, 61)}...</a>
						                    </h4>
					                    </c:when>
					                    <c:otherwise>
						                    <h4 style="height: 60px;">
						                    	<a href="javascript:void(0);" onclick="clickAdvertisementFeaturedStore('${advertisement.id}-${currentStore.listStores.get(0).id}', 'countSearch', '${giroUrl}${advertisement.url}', 0, 0);">${advertisement.title}</a>
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
		        </c:if>
			</c:forEach>
		</c:if>
	
	</div>
	
	<a style="margin: 20px 0 20px 0;" href="javascript:history.back()" class="btn btn-primary">Voltar</a>
</div>
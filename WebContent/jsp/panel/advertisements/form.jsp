<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:choose>
	<c:when test="${currentUser.count_stores < 1}">
		<div class="wrapper-special">
			<div>
				<h1><fmt:message key="advertisement.noStoreTtl" /></h1>
				<h4><fmt:message key="advertisement.noStoreTxt" /></h4>
				<h4><fmt:message key="advertisement.noStoreTxt2" /></h4>
				<br />
				<a href="${linkStoresForm}" type="button" class="btn btn-primary btn-lg"><fmt:message key="advertisement.noStoreLink" /></a>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<h3><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>  <fmt:message key="formAdvertisement.title" /></h3>
		
		<c:if test="${advertisement != null}">
			<c:choose>
				<c:when test="${advertisement.status == 1}">
					<a class="label label-success" href="${linkAdvertisementsPublish}&advertisement.id=${advertisement.id}">
						<fmt:message key="nav.publishBox.publish" />
					</a>
					<br />
					<br />
				</c:when>
				<c:when test="${advertisement.status == 2}">
					<a class="label label-danger unpublish-item-buttom" href="javascript:void(0);">
						<fmt:message key="nav.unpublishBox.unpublish" />
					</a>
					<div class="unpublish-confirmation">
						<fmt:message key="nav.unpublishBox.text" />
						<br >
						<a class="btn btn-danger" href="${linkAdvertisementsUnpublish}&advertisement.id=${advertisement.id}" role="button">
							<fmt:message key="nav.unpublishBox.unpublish" />
						</a>
						<a class="btn btn-default bt-remove-cancel" href="javascript:void(0);" role="button">
							<fmt:message key="nav.removeBox.cancel" />
						</a>
					</div>
					<br />
					<br />
				</c:when>
			</c:choose>
		</c:if>

		<jsp:include page="/templates/panel/listMessages.jsp"/>
		
		<br />
		<div class="clear-both"></div>
		
		<div class="box-left">
		
			<c:if test="${advertisement.qtda_images == 0}">
				<div class="panel panel-warning">
					<div class="panel-heading"><fmt:message key="advertisement.noImagesTtl" /></div>
				  	<div class="panel-body">
					    <fmt:message key="advertisement.noImagesTxt" />
				  	</div>
				</div>
			</c:if>
		
			
			
			<c:if test="${advertisement.id != null}">
				<div class="well well-lg">
					<h4>Imagens do seu anúncio</h4>
				
					<c:if test="${listAdvertisementImages != null}">
						<c:forEach items="${listAdvertisementImages}" var="image">
							<div style="float: left;">
								<div class="div-adv-img">
									<img class="advertisement-image spinLightBox" src="${linkAdvertisementImagesView}&image=${image.image_file_name}" />				
								</div>
								<br />
								<div class="clear-both"></div>
								<a href="${linkAdvertisementImagesDelete}&advertisement_image_id=${image.id}&advertisement.id=${advertisement.id}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}">EXCLUIR</a>
							</div>			
						</c:forEach>		  
					</c:if>
				
					<div class="clear-both"></div>
				
					<c:if test="${advertisement.qtda_images < advertisement.max_images}">
						<br />
						<form id="form-upload-image" action="${linkAdvertisementImagesUpload}&advertisement.id=${advertisement.id}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}" method="POST" name="formAdvertisementImages" enctype="multipart/form-data">
							<span class="btn btn-default btn-file">
		    					Selecionar imagem<input type="file" class="filestyle" name="tfUpImage">
		    				</span>
							<button class="btn btn-primary btn-sm" type="submit"><span class="glyphicon glyphicon-upload"></span></button>
							
							<br />
							<i>*As imagens devem ser em formato widescreen de pelo menos 640x480px.*</i>
						</form>
					</c:if>
				</div>
				<br />
			</c:if>
		</div>
		
		<div class="clear-both"></div>
		
		<form action="${linkAdvertisementsSave}" method="POST" name="formAdvertisement">
			
			<div class="box-left">
				<div class="form-group">

					<fmt:message key="advertisement.stores"/>
					
					<br />
				
					<select class="form-control" id="ddAdvertisementStore" name="advertisement.store_id">
				   		<option value="0"><fmt:message key="advertisement.stores.all"/></option>
						<c:forEach items="${listStores}" var="store">
							<option value="${store.id}" <c:if test="${store.id == storeId}">SELECTED</c:if>>${store.name}</option>
						</c:forEach>
				  	</select>
					
					<br />
				
					<fmt:message key="advertisement.title"/>
					
					<br />
		
					<input type="text" id="advertisement.title" name="advertisement.title" value="${advertisement.title}" class="form-control" placeholder="Ex: iPhone 6s Plus 128GB Preto." aria-describedby="basic-addon1">
					
					<br>
					
					<fmt:message key="advertisement.description"/>
					
					<br />
					
					<textarea rows="10" cols="50" id="advertisement.description" name="advertisement.description" class="form-control" placeholder="" aria-describedby="basic-addon1">${advertisement.description}</textarea>
					
					<br>
					
					<fmt:message key="advertisement.categories"/>
					
					<br />
				
					<select class="form-control" id="ddAdvertisementCategory" name="advertisement.category_id">
				   		<%-- <option value="0"><fmt:message key="advertisement.categories.select"/></option> --%>
						<c:forEach items="${listCategories}" var="category">
							<option value="${category.id}" <c:if test="${advertisement.category_id.id == category.id}">SELECTED</c:if>>${category.name}</option>
						</c:forEach>
				  	</select>
					
					<br />
		
					<fmt:message key="advertisement.price"/>
					
					<br />
		
				    <div class="input-group">
				      <input type="text" id="advertisement.price" name="advertisement.price" value="${fn:replace(advertisement.price,'.', ',')}" data-affixes-stay="true" data-prefix="" data-thousands="." data-decimal="," class="form-control currency inpt-price-adv" placeholder="" aria-describedby="basic-addon1">
				      <span class="input-group-addon">
				        <input type="checkbox" class="ckb-subconsulta" <c:if test="${advertisement.price == 0}">checked="checked"</c:if> /> sob consulta
				      </span>
				    </div>
					
					<br>
					
					<fmt:message key="advertisement.itemState"/>
					
					<br />
					
					<select class="form-control" id="advertisement.item_state" name="advertisement.item_state">
				   		<option value="0"><fmt:message key="advertisement.itemState.select"/></option>
						<option value="NOVO" <c:if test="${advertisement.item_state == 'NOVO'}">SELECTED</c:if>>NOVO</option>
						<option value="RECONDICIONADO" <c:if test="${advertisement.item_state == 'RECONDICIONADO'}">SELECTED</c:if>>RECONDICIONADO</option>
						<option value="USADO" <c:if test="${advertisement.item_state == 'USADO'}">SELECTED</c:if>>USADO</option>
				  	</select>
					
					<br>
					
					<fmt:message key="advertisement.link"/>
					
					<br />
					
					<input type="text" id="advertisement.link" name="advertisement.link" value="${advertisement.link}" class="form-control" placeholder="Link para este produto no seu site, caso exista." aria-describedby="basic-addon1">
					
					<div class="form-tip">
						<span class="glyphicon glyphicon-thumbs-up"></span>
						Ao adicionar um link você habilitará o botão "Comprar" no seu anúncio que levará o cliente para seu site.
						<br />
						Dica: adicione o link que leva para o produto ou serviço anunciado no seu site para facilitar a compra ;) #dicadogiro
					</div>
					
					<br>
					
					<fmt:message key="advertisement.availability"/>
					
					<br />
					
					<select class="form-control" id="advertisement.availability" name="advertisement.availability">
				   		<option value="0"><fmt:message key="advertisement.availability.select"/></option>
						<option value="EM ESTOQUE" <c:if test="${advertisement.availability == 'EM ESTOQUE'}">SELECTED</c:if>>EM ESTOQUE</option>
						<option value="ESGOTADO" <c:if test="${advertisement.availability == 'ESGOTADO'}">SELECTED</c:if>>ESGOTADO</option>
						<option value="PRÉ-VENDA" <c:if test="${advertisement.availability == 'PRÉ-VENDA'}">SELECTED</c:if>>PRÉ-VENDA</option>
				  	</select>
					
					<br>
					
					<fmt:message key="advertisement.brand"/>
					
					<br />
					
					<input type="text" id="advertisement.brand" name="advertisement.brand" value="${advertisement.brand}" class="form-control" placeholder="" aria-describedby="basic-addon1">
					
					<br>
					
					<input type="hidden" id="advertisement.id" name="advertisement.id" value="${advertisement.id}">
					<input type="hidden" id="current" name="current" value="${current}">
					<input type="hidden" id="maximum" name="maximum" value="${maximum}">
					<input type="hidden" id="valueSearch" name="valueSearch" value="${valueSearch}">
					
					<button type="submit" class="btn btn-primary" name="btSave"><fmt:message key="form.btSave"/></button>			
				</div>
			</div>
			<div class="clear-both"></div>
		</form>
	</c:otherwise>
</c:choose>

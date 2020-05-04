<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.5.2/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/modernizr/2.8.2/modernizr.js"></script>

<div class="se-pre-con"></div>

<input type="hidden" value="${currentStore}" id="currentStore" />
<input type="hidden" value="${currentAdvertisement}" id="currentAdvertisement" />
<input type="hidden" value="${pathImages}" id="imagesPath" />
<input type="hidden" value="${currentUser}" id="currentUser" />
<input type="hidden" value="" id="currentMessageAdvertisementId" />
<input type="hidden" value="" id="currentUserLat" />
<input type="hidden" value="" id="currentUserLng" />

	<nav class="navbar navbar-default navbar-custom">
	<div class="header-align">
	  <div class="container-fluid">
	    <!-- Brand and toggle get grouped for better mobile display -->
	    <div class="navbar-header">
	      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
	        <span class="sr-only">Toggle navigation</span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	        <span class="icon-bar"></span>
	      </button>
	      <a class="navbar-brand" href="${linkHome}"><img class="logo-home" alt="Giroferta" src="${giroUrl}/resources/images/logo-horizontal.png"></a>
	    </div>
	
	    <!-- Collect the nav links, forms, and other content for toggling -->
	    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
	      <ul class="nav navbar-nav navbar-right">
	        <!-- <li>
				<button type="button" class="btn btn-link btn-lg search-icon-home">
				  <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
				</button>
			</li> -->
			<c:choose>
				<c:when test="${currentUser != null}">
					<%-- <li>
						<a class="white2 no-hover">
					 		<fmt:message key="header.hello" />
					 		<strong>
						 		 <c:choose>
						 		 	<c:when test="${currentUser.cpf != ''}">
						 		 		${currentUser.name}!
						 		 	</c:when>
						 		 	<c:otherwise>
						 		 		${currentUser.company_name}!
						 		 	</c:otherwise>
						 		 </c:choose>
					 		 </strong>
					 	</a>
					 </li> --%>
					 <li>
				 		 <c:if test="${currentUser.count_advertisements > 0}">
				 		 	<%-- Seu crédito atual é de <strong style="font-size: 16px;"><fmt:formatNumber value="${currentUser.balance}" type="currency"/></strong> --%>
				 		 	<a href="javascript:void(0);" class="bt-info-head">
								<img src="${giroUrl }/resources/images/ico-precos.png" />
								<fmt:formatNumber value="${currentUser.balance}" type="currency"/>
							</a>
				 		 </c:if>
					 </li>
					<li>
						<a href="${linkAdvertisementsList}" class="bt-sign-in">
							<%-- <img src="${giroUrl }/resources/images/ico-precos.png" /> --%>
							<fmt:message key="header.createAdv" />
						</a>
					</li>
				</c:when>
				<c:otherwise>
					<li>
						<a href="javascript:void(0);" class="bt-info-head bt-howto-home">
							<img src="${giroUrl }/resources/images/ico-como-anunciar.png" />
							<fmt:message key="header.howto" />
						</a>
					</li>
					<li>
						<a href="javascript:void(0);" class="bt-info-head bt-prices-home">
							<img src="${giroUrl }/resources/images/ico-precos.png" />
							<fmt:message key="header.prices" />
						</a>
					</li>
					<li>
						<c:choose>
							<c:when test="${currentAdv != null && currentSto != null}">
								<a href="${linkLogin}?currentAdv=${currentAdv}&currentSto=${currentSto}" class="bt-sign-in">
							</c:when>
							<c:otherwise>
								<a href="${linkLogin}" class="bt-sign-in">
							</c:otherwise>
						</c:choose>
							<img src="${giroUrl }/resources/images/ico-login.png" />
							<fmt:message key="header.enter" />
						</a>
					</li>
					<li>
						<c:choose>
							<c:when test="${currentAdv != null && currentSto != null}">
								<a href="${linkLogin}?currentAdv=${currentAdv}&currentSto=${currentSto}" class="bt-sign-in">
							</c:when>
							<c:otherwise>
								<a href="${linkLogin}" class="bt-sign-in">
							</c:otherwise>
						</c:choose>
							<%-- <img src="${giroUrl }/resources/images/ico-precos.png" /> --%>
							<fmt:message key="header.createAdv" />
						</a>
					</li>
				</c:otherwise>
			</c:choose>
	      	<c:if test="${currentUser != null}">
	      		<c:if test="${currentUser.count_advertisements > 0}">
		      		<li>
		      			<a href="${linkPagSeguroBuyCredit}" class="bt-buy-cre">
		      				<fmt:message key="header.buyCredit" />
		      			</a>
		      		</li>
	      		</c:if>
	      	</c:if>
	      	<c:if test="${currentUser != null}">
	        <li class="dropdown">
	          <a href="#" class="dropdown-toggle bt-menu-top" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Menu<span class="caret"></span></a>
	          <ul class="dropdown-menu">
	          	<c:if test="${currentUser.status != 0}">
		            <c:if test="${currentUser.type_account == '1' || currentUser.type_account == '2' || currentUser.type_account == '5'}">
<%-- 		            	<li><a href="${linkVendorNewAccount}"><fmt:message key="vendor.menu.newAccount" /></a></li>
		            	<li><a href="${linkVendorCourtesyForm}"><fmt:message key="vendor.menu.courtesy" /></a></li>
		            	<li><a href="${linkVendorMyAccountsList}"><fmt:message key="vendor.menu.myAccounts" /></a></li>
 --%><%-- 		            	<li><a href="#"><fmt:message key="vendor.menu.myCommissions" /></a></li> --%>
 						<li><a href="${linkSales}"><fmt:message key="admin.menu.sales" /></a></li>
		            	<li role="separator" class="divider"></li>
		            	<c:if test="${currentUser.type_account == '1' || currentUser.type_account == '2'}">
		            		<li><a href="${linkAdminManager}"><fmt:message key="admin.menu.manager" /></a></li>
		            		<li role="separator" class="divider"></li>
		            	</c:if>
		            </c:if>
		            <li><a href="${linkAdvertisementsList}"><fmt:message key="header.menu.myAdvertisements" /></a></li>
		            <li><a href="${linkStoresList}"><fmt:message key="header.menu.myStores" /></a></li>
		            <li role="separator" class="divider"></li>
		            <li><a href="${linkChatList}"><fmt:message key="header.menu.myMessages" /></a></li>
	            	<li role="separator" class="divider"></li>		            
		        </c:if>
	            <li><a href="${linkUsersFormEdit}"><fmt:message key="header.menu.myAccount" /></a></li>
	            <li><a href="${linkUsersFormUpdatePassword}"><fmt:message key="header.menu.updatePassword" /></a></li>
	            <li><a href="${linkUsersPurchasesHistoric}"><fmt:message key="header.menu.myPurchasesHistoric" /></a></li>

 	            <li role="separator" class="divider"></li>
	            <li><a href="${linkAdvertisementsReportByAdv}"><fmt:message key="header.menu.reportByAdv" /></a></li>
	            <li><a href="${linkAdvertisementsReportByDate}"><fmt:message key="header.menu.reportByDate" /></a></li>

 	            <li role="separator" class="divider"></li>
	            <li><a href="${linkLogout}"><fmt:message key="header.menu.out" /></a></li>
	          </ul>
	        </li>
	        </c:if>
	      </ul>
	    </div>
	  </div>
	  </div>
	</nav>
	
<div id="search-header">	
	<div class="input-group">
	  <input type="text" class="form-control search-input" placeholder="Procurando por algo?" aria-describedby="basic-addon2">
	  <div class="input-group-btn">
	    <button type="button" class="btn btn-primary do-search">
	    	<span class="glyphicon glyphicon-search"></span>
	    </button>
	  </div>
	</div>
</div>

<div class="search-no-cont">
	<h3>
		<fmt:message key="search.sorry" />
	</h3>
	<span>
		<fmt:message key="search.offer" />
		<br />
		<fmt:message key="search.register" />
		<br /><br />
		<div class="input-group" style="margin-bottom: 10px;">
	  		<input type="text" class="form-control interest-name" placeholder="Nome" aria-describedby="basic-addon2">
	  	</div>
		<div class="input-group">
	  		<input type="text" class="form-control interest-mail" placeholder="E-mail" aria-describedby="basic-addon2">
	  	</div>
	  	<input type="hidden" id="searched-word" name="searchedWord" />
	  	<br />
	  	<button type="button" class="btn btn-primary bt-send-interest"><fmt:message key="search.notifyMe" /></button>
	  	<button type="button" class="btn btn-link bt-no-thanks"><fmt:message key="search.noThanks" /></button>
	</span>
</div>
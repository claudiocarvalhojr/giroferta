<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">

    function initialize() {
      var mapOptions = {
        zoom: 17,       
        scrollwheel: false
      };
      mapStore = new google.maps.Map(document.getElementById('map-canvas-store'),
          mapOptions);

      if (document.getElementById("store.longitude").value != '' && document.getElementById("store.latitude").value != '') {
    	  var marcador = new google.maps.Marker({
              position: new google.maps.LatLng(document.getElementById("store.longitude").value, document.getElementById("store.latitude").value),
              icon: "${giroUrl }/resources/images/marcador3.png",
              draggable: true,
              visible: false

          }); 
      } else {    	  
	      //criando marcador
	      var marcador = new google.maps.Marker({
	          position: new google.maps.LatLng(-34.397, 150.644),
	          icon: "${giroUrl }/resources/images/marcador3.png",
	          draggable: true,
	          visible: false
	
	      });
      }
      marcador.setMap(mapStore);

      marcador.setVisible(true);
      marcador.setAnimation(google.maps.Animation.DROP);
      marcador.setPosition(mapStore.getCenter());
      
      if(navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(function(position) {
            var pos = new google.maps.LatLng(position.coords.latitude,
                                             position.coords.longitude);

            document.getElementById("store.longitude").value = position.coords.longitude;
            document.getElementById("store.latitude").value = position.coords.latitude;
          }, function() {
              handleNoGeolocation(true);
          });
      }   
      
      google.maps.event.addListener(marcador, 'dragend', function() {
        var lat = marcador.getPosition().lat();
        var lng = marcador.getPosition().lng();
        document.getElementById("store.longitude").value = lng;
        document.getElementById("store.latitude").value = lat;
      });

      //Movimenta o local do marcador para o centro quando arrastado o mapa ou feito o zoom
      google.maps.event.addDomListener(mapStore, 'idle', function(event) {
          marcador.setPosition(mapStore.getCenter());
      });

      // Try HTML5 geolocation
      if(navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
          var pos = new google.maps.LatLng(position.coords.latitude,
                                           position.coords.longitude);

          mapStore.setCenter(pos);
        }, function() {
          handleNoGeolocation(true);
        });
      } else {
        // Browser doesn't support Geolocation
        handleNoGeolocation(false);
      }
    }

    function handleNoGeolocation(errorFlag) {
      if (errorFlag) {
        var content = 'Error: The Geolocation service failed.';
      } else {
        var content = 'Error: Your browser doesn\'t support geolocation.';
      }

      var options = {
        map: mapStore,
        position: new google.maps.LatLng(60, 105),
        content: content
      };

      var infowindow = new google.maps.InfoWindow(options);
      mapStore.setCenter(options.position);
    }

    google.maps.event.addDomListener(window, 'load', initialize);
</script>

<c:choose>
	<c:when test="${currentUser.count_stores == 1 && currentUser.count_advertisements < 1}">
		<div class="wrapper-special">
			<div>
				<h1><fmt:message key="store.noAdvertisementTtl" /></h1>
				<h4><fmt:message key="store.noAdvertisementTxt" /></h4>
				<br />
				<a href="${linkAdvertisementsForm}" type="button" class="btn btn-primary btn-lg"><fmt:message key="store.noAdvertisementLink" /></a>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<h3><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>  <fmt:message key="formStore.title" /></h3>

		<jsp:include page="/templates/panel/listMessages.jsp"/>
		
		<br />
		<div class="clear-both"></div>
		
		<form action="${linkStoresSave}" method="POST" name="formStore">
		
			<div class="form-tip-lg">
				<span class="glyphicon glyphicon-thumbs-up"></span>
				<fmt:message key="store.mapMessage"/>
				<br />
				<fmt:message key="store.mapMessage2"/>
			</div>
			
			<br />

			<div id="map-canvas-store"></div>
			
			<br />
		
			<div class="box-left">
				<div class="form-group">
				
					<fmt:message key="store.name"/>
					
					<br />
					
					<input type="text" id="store.name" name="store.name" value="${store.name}" class="form-control" placeholder="" aria-describedby="basic-addon1">
					
					<br>	
					
					<fmt:message key="store.filial"/>
					
					<br />
					
					<input type="text" id="store.filial" name="store.filial" value="${store.filial}" class="form-control" placeholder="" aria-describedby="basic-addon1">
					
					<br>			
					
					<fmt:message key="store.state"/>
					
					<br />
					
					<select class="form-control" id="ddStoreState" name="ddStoreState">
				   		<option value="0"></option>
						<c:forEach items="${listStates}" var="state">
							<option value="${state.id}" <c:if test="${state.id == stateId}">SELECTED</c:if>>${state.initials}</option>
						</c:forEach>
				  	</select>
					
					<br />
					
					<fmt:message key="store.city"/>
					
					<br />
		
					<div id="cities">
					  	<select class="form-control" id="ddStoreCity" name="store.city_id">
					   		<option value="0"></option>
					   		<c:forEach items="${listCities}" var="city">
					   			<option value="${city.id}" <c:if test="${city.id == cityId}">SELECTED</c:if>>${city.name}</option>
					   		</c:forEach>
					  	</select>
				  	</div>
					
					<br />
					
					<fmt:message key="store.complement"/>
					
					<br/>
					
					<input type="text" id="store.complement" name="store.complement" value="${store.complement}" class="form-control" placeholder="" aria-describedby="basic-addon1">
					
					<br />
					
					<fmt:message key="store.contactPhone"/>
					
					<br />

					<c:choose>
						<c:when test="${store != null}">
							<input type="text" id="store.contact_phone" name="store.contact_phone" value="${store.contact_phone}" class="form-control phone-field" placeholder="" aria-describedby="basic-addon1">
						</c:when>
						<c:otherwise>
							<input type="text" id="store.contact_phone" name="store.contact_phone" value="${currentUser.phone}" class="form-control phone-field" placeholder="" aria-describedby="basic-addon1">
						</c:otherwise>
					</c:choose>
			
					<input type="hidden" id="store.id" name="store.id" value="${store.id}">
					<input type="hidden" id="current" name="current" value="${current}">
				    <input type="hidden" id="maximum" name="maximum" value="${maximum}">
				    <input type="hidden" id="valueSearch" name="valueSearch" value="${valueSearch}">
				    
				</div>
			</div>
			<div class="box-right">
					<fmt:message key="store.address"/>
					
					<br />
					
					<input type="text" id="store.address" name="store.address" value="${store.address}" class="form-control" placeholder="" aria-describedby="basic-addon1">
					
					<br />


					
					<fmt:message key="store.number"/>
					
					<br/>
					
					<input type="text" id="store.number" name="store.number" value="${store.number}" class="form-control" placeholder="" aria-describedby="basic-addon1">
					
					<br />

					
					<fmt:message key="store.postalCode"/>
					
					<br />
					
					<input type="text" id="store.postal_code" name="store.postal_code" value="${store.postal_code}" class="form-control cep-field" placeholder="" aria-describedby="basic-addon1">
					
					<input type="hidden" id="store.latitude" name="store.latitude" value="${store.latitude}" class="form-control" placeholder="<fmt:message key="store.latitude"/>" aria-describedby="basic-addon1">
					
					<input type="hidden" id="store.longitude" name="store.longitude" value="${store.longitude}" class="form-control" placeholder="<fmt:message key="store.longitude"/>" aria-describedby="basic-addon1">
					
					<br />
					
					<fmt:message key="store.neighborhood"/>
					
					<br />
		
					<input type="text" id="store.neighborhood" name="store.neighborhood" value="${store.neighborhood}" class="form-control" placeholder="" aria-describedby="basic-addon1">
					
					<br />
					
					<c:choose>
						<c:when test="${store != null}">
							<fmt:message key="store.contactEmail"/>
							<br />
							<input type="text" id="store.contact_email" name="store.contact_email" value="${store.contact_email}" class="form-control" placeholder="" aria-describedby="basic-addon1">
						</c:when>
						<c:otherwise>
							<fmt:message key="store.contactEmail"/>
							<br />
							<input type="text" id="store.contact_email" name="store.contact_email" value="${currentUser.email}" class="form-control" placeholder="" aria-describedby="basic-addon1">
						</c:otherwise>
					</c:choose>
			</div>
			
			<div class="clear-both"></div>
			
			<fmt:message key="store.companyDescription"/>
			
			<br />			
			
			<textarea rows="10" cols="50" id="store.company_description" name="store.company_description" class="form-control" placeholder="" aria-describedby="basic-addon1">${store.company_description}</textarea>
						
			<br />
			
			<button type="submit" class="btn btn-primary" name="btSave"><fmt:message key="form.btSave"/></button>
		</form>
		<!-- 
		<c:if test="${store.id != null}">
		
			<c:if test="${listStoreImages != null}">
				<c:forEach items="${listStoreImages}" var="image">
					<br />
					<br />
					<img src="${linkStoreImagesView}&image=${image.image_file_name}" width="730" height="730">
					<br />
					<br />
					<a href="${linkStoreImagesDelete}&store_image_id=${image.id}&store.id=${store.id}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}">EXCLUIR</a>
					<br />
					<br />
				</c:forEach>		  
			</c:if>
			
			<c:if test="${store.qtda_images < store.max_images}">
				<br />
				<form action="${linkStoreImagesUpload}&store.id=${store.id}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}" method="POST" name="formStoreImages" enctype="multipart/form-data">
					<table>
						<tr>
							<td><input type="file" name="tfUpImage"></td>
							<td><input type="submit"  class="btn btn-primary" name="btSaveImage"  value="Enviar Imagem"></td>
						</tr>
				    </table>
				</form>
				<br />
			</c:if>
			
		</c:if>
		 -->
	</c:otherwise>
</c:choose>
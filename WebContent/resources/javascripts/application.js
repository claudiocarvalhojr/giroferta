/**
 * Scripts da Aplicação
 */

$(document).ready(function() {
	
	// balao fique por dentro
	
	$('.close-stayin').live('click', function () {
		$('#div-stayin').fadeOut();
		
		createCookie('stayin', 'no', '1');
	});
	
	if (readCookie('stayin') == null) {	
		setTimeout(function(){
			$('#div-stayin').fadeIn();
			
			var left = $('#div-stayin').offset().left;
			
			$("#div-stayin").css({left:left}).animate({"left":"0px"}, "slow");
		}, 15000);
	}
	
	$('#div-stayin > text').live('click', function (){
		if (document.location.origin + (window.location.href.indexOf("GirofertaWebApp") > 1))
			location.href = '/GirofertaWebApp/fique-por-dentro';
		else
			location.href = '/fique-por-dentro';
	});
	
	//cadastro de newsletter no rodape da pagina inicial
	
	$('.name-newsletter').focus(function () {
		if ($(this).val() == 'Nome')
			$(this).val('');
	})
	
	$('.mail-newsletter').focus(function () {
		if ($(this).val() == 'E-mail')
			$(this).val('');
	})
	
	$('.name-newsletter').focusout(function () {
		if ($(this).val() == '')
			$(this).val('Nome');
	})
	
	$('.mail-newsletter').focusout(function () {
		if ($(this).val() == '')
			$(this).val('E-mail');
	})
	
	$('#form-upload-image').submit(function () {
		var ok = true;
		
		if($('.filestyle').val() == '') {
			alert('Você precisa selecionar a imagem primeiro!');
			ok = false;
		} else {
			ok = true;			
		}
		
		if (!ok) {		
			return ok;
		}
	});
	
	$('.btn-signup-newsletter').click(function(){
		var ok = true;
		
		if($('.name-newsletter').val() == '' || $('.name-newsletter').val() == 'Nome') {
			$('.name-newsletter').css('border', '2px solid red');
			ok = false;
		} else {
			$('.name-newsletter').css('border', '');
			ok = true;			
		}
		
		if($('.mail-newsletter').val() == '' || $('.mail-newsletter').val() == 'E-mail') {
			$('.mail-newsletter').css('border', '2px solid red');
			ok = false;
		} else {
			$('.mail-newsletter').css('border', '');
			ok = true;			
		}
		
		if (!ok)		
			return ok;
		else if (ok){
			$.ajax({
		        type: "POST",
		        url: './Controller?form=actions.NewsletterListsActions&action=resource&op=json&nameNewsletter='+$('.name-newsletter').val()+'&mailNewsletter='+$('.mail-newsletter').val(),
		        success: function(jsonData) {
		            if (jsonData) {
		            	$('.name-newsletter').val('');
		            	$('.mail-newsletter').val('');
		            	$('.alert-box').addClass('alert-success');
		            	$('.alert-box > strong').html('Cadastro efetuado com sucesso!');
		            	$('.alert-box').slideDown();
		            	setTimeout(function(){
		            		$('.alert-box').slideUp();
		            		$('.alert-box > strong').html('');
		            		$('.alert-box').removeClass('alert-success');
		            	}, 6000);
		            } else {
		            	$('.name-newsletter').val('');
		            	$('.mail-newsletter').val('');
		            	$('.alert-box').addClass('alert-danger');
	            		$('.alert-box > strong').html('E-mail inválido ou já cadastrado! Tente novamente com outro e-mail.');
		            	$('.alert-box').slideDown();
		            	setTimeout(function(){
		            		$('.alert-box').slideUp();
		            		$('.alert-box > strong').html('');
		            		$('.alert-box').removeClass('alert-danger');
		            	}, 6000);
		            }
		          },
		        error: function(jsonData) {
		        	$('.name-newsletter').val('');
	            	$('.mail-newsletter').val('');
	            	$('.alert-box').addClass('alert-warning');
            		$('.alert-box > strong').html('Não possível realizar o cadastro, tente novamente!');
	            	$('.alert-box').slideDown();
	            	setTimeout(function(){
	            		$('.alert-box').slideUp();
	            		$('.alert-box').html('');
	            	}, 6000);
		          }
		    });
		}
			
	});
	
	// efetua o clique do botão de busca
	
	$('.span-bt-search').click(function() {
		$('#form-search').submit();
	});
	
	//mostra box confirmação exclusão
	
	$('.remove-item-buttom').click(function() {
		$('.overlay').toggle();
		$(this).next('.remove-confirmation').slideDown();
	});
	
	$('.unpublish-item-buttom').click(function() {
		$('.overlay').toggle();
		$(this).next('.unpublish-confirmation').slideDown();
	});
	
	$('.bt-remove-cancel').click(function () {
		$('.overlay').toggle();
		$(this).parent().slideUp();
	});
	
	//clique do botão de compra de créditos
	
	$('.bt-add-credit').click(function () {
		$('.label-credit').each(function () {
			if($(this).hasClass('active'))
				buyCreditPagseguro($(this).attr('valor'));
		});
	});
	
	//altera os cidades a partir do estado
	
	$('#ddUserState').blur(function() {
		
		$('.header-site').addClass('over-all');
		$('.footer-site').addClass('over-all');
		
		$(".se-pre-con").fadeIn("slow");
		
		$.ajax({
			type: 'POST',
			url: './Controller?form=actions.UsersActions&action=resource&op=cities&ddUserState='+$('#ddUserState').val(),
			dataType : 'html',
			success: function(retorno) {
				$('#cities').html(retorno);

				$(".se-pre-con").fadeOut("slow");
				
				setTimeout(function(){
					$('.header-site').removeClass('over-all');
					$('.footer-site').removeClass('over-all');
				}, 500);
				
				setTimeout(function(){
					$('#ddUserCity').focus();
				}, 300);
			}
		})
		
	});
	
	$('#ddStoreState').blur(function() {
		
		$('.header-site').addClass('over-all');
		$('.footer-site').addClass('over-all');
		
		$(".se-pre-con").fadeIn("slow");
		$.ajax({
			type: 'POST',
			url: './Controller?form=actions.StoresActions&action=resource&op=cities&ddStoreState='+$('#ddStoreState').val(),
			dataType : 'html',
			success: function(retorno) {
				$('#cities').html(retorno);
				
				$(".se-pre-con").fadeOut("slow");
				
				setTimeout(function(){
					$('.header-site').removeClass('over-all');
					$('.footer-site').removeClass('over-all');
				}, 500);
				
				setTimeout(function(){
					$('#ddStoreCity').focus();
				}, 300);
			}
		})
	});
	
	//mostra os tipos de cadastro de usuario
	
	$('.nav-pf-pj > .pf').click(function () {
		$('.form-group > .pf').css('display', 'block');
		$('.form-group > .pj').css('display', 'none');
		$('.nav-pf-pj > .pj').removeClass('active');
		$(this).addClass('active');
		$('#typePerson').val('pf');
	});
	
	$('.nav-pf-pj > .pj').click(function () {
		$('.form-group > .pj').css('display', 'block');
		$('.form-group > .pf').css('display', 'none');
		$('.nav-pf-pj > .pf').removeClass('active');
		$(this).addClass('active');
		$('#typePerson').val('pj');
	});
	
	// fechar janela do avise-me
	$('.bt-no-thanks').live('click', function () {
		$('.interest-name').val('');
		$('.interest-mail').val('');
		$('.search-no-cont').fadeOut();
	});
	
	// envia avise-me
	
	$('.bt-send-interest').live('click', function (){
		var lat = $('#currentUserLat').val();
		var lng = $('#currentUserLng').val();

		var urlInterest = './Controller?form=actions.WarnMeActions&action=resource&op=send&warnMe.latitude=' + lng 
						+ '&warnMe.longitude=' + lat
						+ '&warnMe.name=' + $('interest-name').val()
						+ '&warnMe.email=' + $('.interest-mail').val() 
						+ '&warnMe.term=' + $('#searched-word').val();
		
		var ok = true;
		
		$('.search-no-cont .form-control').each(function() {
			if ($(this).val() == "") {
				$(this).css('border', '2px solid red');
				ok = false;
			}
		});
		
		if (!ok)
			return ok;
		
		$.ajax({
	        type: "POST",
	        url: urlInterest,
	        contentType: "application/json; charset=utf-8",
	        processData: false,
	        success: function (jsonData) {
	        	$('.alert-box').addClass('alert-success');
            	$('.alert-box > strong').html('Obrigado! Avisaremos em breve.');
            	$('.alert-box').slideDown();
            	setTimeout(function(){
            		$('.alert-box').slideUp();
            		$('.alert-box > strong').html('');
            		$('.alert-box').removeClass('alert-success');
            	}, 6000);
	        	$('.bt-no-thanks').trigger('click');
	        },
	        error: errorFunc
	    });
		
		return ok;
	});
	
	//mostra a janela de contato do footer
	
	$('.footer-contact-button').click(function () {
		$('.overlay').toggle();
		$('.footer-contact-form').slideDown();
	});
	
	$('.bt-cancel-contact').click(function () {
		$('#footer-contact-usr').val('');
		$('#footer-contact-mail').val('');
		$('#footer-contact-subject').val('');
		$('#footer-contact-message').val('');
		
		$('.overlay').toggle();
		$('.footer-contact-form').slideUp();
	});
	
	$('#contact-form-footer').submit(function(){
		var ok = true;
		$('.form-control').each(function() {
			if ($(this).val() == "") {
				$(this).css('border', '2px solid red');
				ok = false;
			}
		});
		return ok;
	});
	
	$('#store-contact-form').submit(function(){
		var ok = true;
		$('.form-control').each(function() {
			if ($(this).val() == "") {
				$(this).css('border', '2px solid red');
				ok = false;
			}
		});
		return ok;
	});
	
	$('.do-search-store').click(function(){
		window.location = './busca?q=' + $('.search-input-store').val() + '&loja=' + $('#currstore-url').val();
	});
	
	//Exibir a busca da home
	
	$('.search-icon-home').click(function () {
		if ($("#search-header").hasClass('opened')) {
	        $("#search-header").slideUp("slow");
	        $("#search-header").removeClass('opened');
	    } else {
			$('#search-header').slideDown("slow");
			$('#search-header').addClass('opened');
			
			setTimeout(function(){
				$('#search-header .search-input').focus();
			}, 700);
	    }
	});
	
	// Efetua busca da home
	
	$(document).keypress(function(e) {
	    if(e.which == 13 && $("#search-header .form-control").val() != "") {
	    	$('.do-search').click();
	    }
	    
	    if(e.which == 13 && $(".modal-search .form-control").val() != "") {
	    	$('.do-search-modal').click();
	    }
	    
	    if(e.which == 13 && $('.input-find-in-store').val() != "") {
	    	$('.bt-find-in-store').trigger('click');
	    }
	    
	    if(e.which == 13 && $('.search-input-store').val() != "") {
	    	$('.do-search-store').trigger('click');
	    }
	});
	
	$('.search-icon').live('click', function () {
		$('.modal-search .search-icon').css('display', 'none');
		
		setTimeout(function(){
			$('.modal-search h4, .modal-search .input-group').css('display', '');
		}, 500);
		
		$('.modal-search').animate({ "top": "50%", 'width': '380px', 'margin-left': '-190px' }, "slow" );
	});
	
	$('.recall-modal-search').click(function() {
		$('.modal-search h4, .modal-search .input-group').css('display', 'none');
				
		$('.modal-search').animate({ "top": "70px", 'width': '50px', 'margin-left': '-25px' }, "slow" );
		
		setTimeout(function(){
			$('.modal-search .search-icon').css('display', 'block');
		}, 700);		
	});
	
	$('.do-search-modal').click(function (){
		var ok = true;
		
		if($(".modal-search .form-control").val() == '' || 
		   $(".modal-search .form-control").val() == 'O que você procura?') {
			$(".modal-search .form-control").css('border', '2px solid red');
			ok = false;
		} else {
			$(".modal-search .form-control").css('border', '');
			ok = true;			
		}
		
		if (!ok){
			return ok;
		}
		
		$(".se-pre-con").fadeIn("slow");
		
		$('.modal-search h4, .modal-search .input-group').css('display', 'none');
		
		$('.modal-search .search-icon').css('display', 'block');
		
		$('.modal-search').animate({ "top": "70px", 'width': '50px', 'margin-left': '-25px' }, "slow" );
			
		/*var currentRadius = (parseInt($('#adminRadius').val()) + parseInt(getAdditionalRadius())) * 1000;
		var currentZoom = 0;
		
		alert('addRad: ' + getAdditionalRadius());
		
		alert('currentRadius: ' + currentRadius);
		
		if(currentRadius == 30) {
				alert('é 30');
			currentZoom = 8;
		} else if (currentRadius == 130) {
			alert('é 130');
			currentZoom = 6;
		} else if (currentRadius == 230) {
			alert('é 230');
			currentZoom = 5;
		} else {
			alert('é else');
			currentZoom = 5;
		}
		
		alert('currentZoom: ' + currentZoom);*/
		
		var mapOptions = {
				zoom : 9,
				scrollwheel : false
			};
			map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
			
			var styles = [
              {
                stylers: [
                          { lightness: -10 },
                          { gamma: 1.51 }
                ]
              },{
                featureType: "road",
                elementType: "geometry",
                stylers: [
                  { lightness: 100 },
                  { visibility: "simplified" }
                ]
              },{
                featureType: "road",
                elementType: "labels",
                stylers: [
                  { visibility: "on" }
                ]
              },{
        	     featureType: "poi",
        	     stylers: [
        	      { visibility: "off" }
        	     ]   
        	    }
            ];

            map.setOptions({styles: styles});
            
            var lat;
            var lng;

			if (navigator.geolocation) {
				navigator.geolocation.getCurrentPosition(function(position) {
					var pos = new google.maps.LatLng(position.coords.latitude,
							position.coords.longitude);
					
					lat = position.coords.latitude;
					lng = position.coords.longitude;
					
					$('#currentUserLat').val(lat);
					$('#currentUserLng').val(lng);

					map.setCenter(pos);
					
					 var marcador = new google.maps.Marker({
					        position: new google.maps.LatLng(lat, lng),
					        title: "Seu local atual",
					        icon : document.location.origin + (window.location.href.indexOf("GirofertaWebApp") > 1 ? "/GirofertaWebApp/resources/images/marcador5.png" : "/resources/images/marcador5.png"),
					    });
					 
					//alert('currentRadius: ' + currentRadius)
					
					/*var circle = new google.maps.Circle({
				    	  map: map,
				    	  radius: currentRadius,    // metros
				    	  fillColor: '#AA0000',
				    	  fillOpacity: 0.2,
				    	  strokeColor: '#AA0000'
				    	});
					
					circle.bindTo('center', marcador, 'position');*/
					
					var urlJson = "./Controller?form=actions.AdvertisementsActions&action=resource&op=json&advertisement=" + $(".modal-search .form-control").val() + "&lat=" + lat + "&lng=" + lng;

					carregarPontos(map, urlJson);
					
					$(".se-pre-con").fadeOut("slow");
				}, function() {
					//handleNoGeolocation(true);
					
					var pos = new google.maps.LatLng(-29.901271, -53.039038);
					
					//lat = -29.901271;
					//lng = -53.039038;
					
					$('#currentUserLat').val(lat);
					$('#currentUserLng').val(lng);
					
					map.setCenter(pos);
					
					var urlJson = "./Controller?form=actions.AdvertisementsActions&action=resource&op=json&advertisement=" + $(".modal-search .form-control").val() + "&lat=" + lat + "&lng=" + lng;

					carregarPontos(map, urlJson);
					
					$(".se-pre-con").fadeOut("slow");
				});
			} else {
				handleNoGeolocation(false);
			}
	});
	
	$('.do-search').click(function (){
		$('.header-site').addClass('over-all');
		$('.footer-site').addClass('over-all');
		
		$('#search-header').fadeOut('slow');
		$('.navbar-collapse').fadeOut('slow');
		
		$(".se-pre-con").fadeIn("slow");
		
		$('.modal-search h2, .modal-search .input-group').css('display', 'none');
		
		$('.modal-search .search-icon').css('display', 'block');
		
		$('.modal-search').animate({ "top": "50px", 'width': '50px', 'margin-left': '-25px' }, "slow" );
		
		var mapOptions = {
				zoom : 9,
				scrollwheel : false
			};
			map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
			
			var styles = [
              {
                stylers: [
                  { lightness: -10 },
                  { gamma: 1.51 }
                ]
              },{
                featureType: "road",
                elementType: "geometry",
                stylers: [
                  { lightness: 100 },
                  { visibility: "simplified" }
                ]
              },{
                featureType: "road",
                elementType: "labels",
                stylers: [
                  { visibility: "on" }
                ]
              },{
	    	     featureType: "poi",
	    	     stylers: [
	    	      { visibility: "off" }
	    	     ]   
	    	    }
            ];

            map.setOptions({styles: styles});
            
            var lat;
            var lng;

			if (navigator.geolocation) {
				navigator.geolocation.getCurrentPosition(function(position) {
					var pos = new google.maps.LatLng(position.coords.latitude,
							position.coords.longitude);
					
					lat = position.coords.latitude;
					lng = position.coords.longitude;
					
					$('#currentUserLat').val(lat);
					$('#currentUserLng').val(lng);
					
					 /*var circle = new google.maps.Circle({
				    	  map: map,
				    	  radius: 30000,    // metros
				    	  fillColor: '#AA0000',
				    	  fillOpacity: 0.2,
				    	  strokeColor: '#AA0000'
				    	});*/

					map.setCenter(pos);
					
					/*var currentRadius = $('#adminRadius').val() + $('#homeAditionalRadius').val();
					alert(currentRadius);
					
					var circle = new google.maps.Circle({
				    	  map: map,
				    	  radius: currentRadius,    // metros
				    	  fillColor: '#AA0000',
				    	  fillOpacity: 0.2,
				    	  strokeColor: '#AA0000'
				    	});
					
					circle.bindTo('center', marcador, 'position');*/

					var urlJson = "./Controller?form=actions.AdvertisementsActions&action=resource&op=json&advertisement=" + $("#search-header .form-control").val() + "&lat=" + lat + "&lng=" + lng;

					carregarPontos(map, urlJson);
					
					$(".se-pre-con").fadeOut("slow");

					setTimeout(function(){
						$('.header-site').removeClass('over-all');
						$('.footer-site').removeClass('over-all');
					}, 1000);
				}, function() {
					//handleNoGeolocation(true);
					
					var pos = new google.maps.LatLng(-29.901271, -53.039038);
					
					//lat = -29.901271;
					//lng = -53.039038;
					
					$('#currentUserLat').val(lat);
					$('#currentUserLng').val(lng);
					
					map.setCenter(pos);
					
					var urlJson = "./Controller?form=actions.AdvertisementsActions&action=resource&op=json&advertisement=" + $("#search-header .form-control").val() + "&lat=" + lat + "&lng=" + lng;

					carregarPontos(map, urlJson);
					
					$(".se-pre-con").fadeOut("slow");

					setTimeout(function(){
						$('.header-site').removeClass('over-all');
						$('.footer-site').removeClass('over-all');
					}, 1000);					
				});
			} else {
				handleNoGeolocation(false);
			}
	});
	
	//mostra a janela de contato do store
	
	$('.bt-message').live("click", function () {
		$('.overlay').toggle();
		$('.store-contact-form').slideDown();
		$('#currentMessageAdvertisementId').val($(this).attr('advertisementId'));
	});
	
	$('.bt-cancel-store-contact').click(function () {
		$('.ta-message').val('');
		$('.overlay').toggle();
		$('.store-contact-form').slideUp();
	});

	// lightbox para imagens
	
	$('.spinLightBox').live('click', function () {
		var img = $('<div class="imgSpinLightBox"><span class="glyphicon glyphicon-remove" style="padding: 2px; background-color: white; border: 1px solid black;"></span><img src="' + $(this).attr('src') + '" /></div>').fadeIn();
		$('body').append(img);
		$('.overlay').toggle();
	});
	
	$('.imgSpinLightBox > span').live("click", function () {
		$('.imgSpinLightBox').fadeOut();
		$('.overlay').toggle();
		setTimeout(function(){
			$('.imgSpinLightBox').remove();
		}, 600);
	});
	
	// scroll do saiba mais
	
	$("#bt-about-us-home").click(function() {
	    $('html, body').animate({
	        scrollTop: $("#wrapper-about-us").offset().top
	    }, 2000);
	});
	
	// scroll do como anunciar
	if (!isMobile()) {
		$(".bt-howto-home").click(function() {
		    $('html, body').animate({
		        scrollTop: $(".step-by-step-home").offset().top
		    }, 2000);
		});
		
		$(".bt-prices-home").click(function() {
		    $('html, body').animate({
		        scrollTop: $(".div-prices-home").offset().top
		    }, 2000);
		});
	}
	
	$(".bt-enviar-msg").live('click', function () {
		var ok = true;
		
		$('#store-contact-form .form-control').each(function() {
			if ($(this).val() == "") {
				$(this).css('border', '2px solid red');
				ok = false;
			}
		});
		
		if (!ok)
			return ok;
		
		$.ajax({
	        type: "POST",
	        url: $('#giroUrl').val() + 'Controller?form=actions.ChatActions&action=resource&op=create&advertisement_id=' + $('#currentMessageAdvertisementId').val() + "&chatMessage.message=" + $('#store-contact-form .form-control').val(),
	        contentType: "application/json; charset=utf-8",
	        processData: false,
	        success: function (jsonData) {
	        	$('.alert-box').addClass('alert-success');
            	$('.alert-box > strong').html('Mensagem enviada com sucesso!');
            	$('.alert-box').slideDown();
            	setTimeout(function(){
            		$('.alert-box').slideUp();
            		$('.alert-box > strong').html('');
            		$('.alert-box').removeClass('alert-success');
            	}, 6000);
	        	$('.bt-cancel-store-contact').trigger('click');
	        },
	        error: errorFunc
	    });
		
		return ok;
	});
	
	$('.bt-send-contact-footer').live('click', function (){
		var ok = true;
		
		$('#contact-form-footer .form-control').each(function() {
			if ($(this).val() == "") {
				$(this).css('border', '2px solid red');
				ok = false;
			}
		});
		
		if (!ok)
			return ok;
		
		var name = $('#footer-contact-usr').val();
		var mail = $('#footer-contact-mail').val();
		var subject = $('#footer-contact-subject').val();
		var message = $('#footer-contact-message').val();
		
		$.ajax({
	        type: "POST",
	        url: './Controller?form=actions.ContactsActions&action=resource&op=send&contact.name=' + name + '&contact.email=' + mail + '&contact.subject=' + subject + '&contact.message_text=' + message,
	        contentType: "application/json; charset=utf-8",
	        processData: false,
	        success: function (jsonData) {
	        	$('.alert-box').addClass('alert-success');
            	$('.alert-box > strong').html('Mensagem enviada com sucesso! Retornaremos o mais breve possível');
            	$('.alert-box').slideDown();
            	setTimeout(function(){
            		$('.alert-box').slideUp();
            		$('.alert-box > strong').html('');
            		$('.alert-box').removeClass('alert-success');
            	}, 6000);
	        	$('.bt-cancel-contact').trigger('click');
	        },
	        error: errorFunc
	    });
		
		return ok;
	});
	
	if ($('.ckb-subconsulta:checked').length > 0) {
		$(".inpt-price-adv").attr('disabled', 'disabled');
		$(".inpt-price-adv").val("");
	} else {
		$(".inpt-price-adv").removeAttr('disabled');
	}
	
	$('.ckb-subconsulta').click(function () {
		if ($('.ckb-subconsulta:checked').length > 0) {
			$(".inpt-price-adv").attr('disabled', 'disabled');
			$(".inpt-price-adv").val("");
		} else {
			$(".inpt-price-adv").removeAttr('disabled');
		}
	});
	
	$('.show-div-adv-in-store').live('click', function () {
		clickAdvertisement($(this).attr('idAdvert'));
		$(this).parents().next('.div-advertisement-in-store').slideDown('slow');        
	});
	
	//$('.show-div-adv-same-coord').live('click', function () {
	//	$(this).parents().next('.div-advertisement-same-coord').show('slow');        
	//});
	
	$('.bt-back-to-store').live('click', function () {
		$(this).parent().parent().hide('slow');
	});
	
	$('.bt-find-in-store').live('click', function(){
		var filter = $('.input-find-in-store').val().toLowerCase();  // no need to call jQuery here
		
		$('.media').show();

	    $('.media').each(function() {
	        /* cache a reference to the current .media (you're using it twice) */
	        var _this = $(this);
	        var title = _this.find('h4').text().toLowerCase();

	        /* 
	            title and filter are normalized in lowerCase letters
	            for a case insensitive search
	         */
	        if (title.indexOf(filter) < 0) {
	            _this.hide();
	        }
	    });
	});
	
	/* tratamentos para mobile*/
	
	if(isMobile()) {
		$('.recall-modal-search').trigger('click');
		
		// scroll do como anunciar
		
		$(".bt-howto-home").click(function() {
		    $('html, body').animate({
		        scrollTop: $(".step-by-step-mobile").offset().top
		    }, 2000);
		});
		
		$(".bt-prices-home").click(function() {
		    $('html, body').animate({
		        scrollTop: $(".div-prices-home").offset().top
		    }, 2000);
		});
	}
	
	/* --- */
	
});

/* cookies */
function createCookie(name,value,days) {
    var expires = "";
    if (days) {
        var date = new Date();
        date.setTime(date.getTime() + (days*24*60*60*1000));
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + value + expires + "; path=/";
}

function readCookie(name) {
    var nameEQ = name + "=";
    var ca = document.cookie.split(';');
    for(var i=0;i < ca.length;i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1,c.length);
        if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
    }
    return null;
}

function eraseCookie(name) {
    createCookie(name,"",-1);
}

/* fnc que detecta mobile */
function isMobile() {
	if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|ipad|iris|kindle|Android|Silk|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(navigator.userAgent) 
		    || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(navigator.userAgent.substr(0,4))) {
     return true;
   } else {
     return false;
   }
}

// função pag seguro

function buyCreditPagseguro(credit) {
	$.ajax({
        type: "POST",
        url: './Controller?form=actions.PagSeguroActions&action=resource&op=checkoutCode&credit='+credit,
        success: function(checkoutCode) {
        	isOpenLightbox = PagSeguroLightbox({
        		code :checkoutCode
        		},{
        			success: function(transactionCode) {
        				location.href="./Controller?form=actions.PagSeguroActions&action=confirmation&panel=true&checkoutCode="+checkoutCode+"&transactionCode="+transactionCode;
        			},
        			abort: function() {
        				$('.alert-box').addClass('alert-danger');
	            		$('.alert-box > strong').html('Pagamento cancelado.');
		            	$('.alert-box').slideDown();
		            	setTimeout(function(){
		            		$('.alert-box').slideUp();
		            		$('.alert-box > strong').html('');
		            		$('.alert-box').removeClass('alert-danger');
		            	}, 6000);
        			}
        		});
        	if (!isOpenLightbox){
        		location.href="https://pagseguro.uol.com.br/v2/checkout/payment.html?code="+checkoutCode;
        	}
        },
        abort: function() {
        	$('.alert-box').addClass('alert-danger');
    		$('.alert-box > strong').html('Falha ao carregar pagamento via Pagseguro.');
        	$('.alert-box').slideDown();
        	setTimeout(function(){
        		$('.alert-box').slideUp();
        		$('.alert-box > strong').html('');
        		$('.alert-box').removeClass('alert-danger');
        	}, 6000);
        }
	});
}

// formatações

jQuery(function($){
	$(".date-field").mask("99/99/9999");
	$(".phone-field").mask("(99) 9999-9999?9");
	$(".hour-field").mask("99:99");
	$(".cep-field").mask("99999-999");
	$(".cpf-field").mask("999.999.999-99");
	$(".cnpj-field").mask("99.999.999/9999-99");
	$('.currency').maskMoney();
});

// funções prontas úteis

function Trim(element){
	var valor = $(element).val();
	var novoTexto = valor.toLowerCase();
	valor.value = novoTexto;
	return novoTexto.replace(/^\s+|\s+$/g,"");
}

function getParameterByName(name) {
	name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
	var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex
			.exec(location.search);
	return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g,
			" "));
}

Number.prototype.formatMoney = function(c, d, t){
	var n = this, c = isNaN(c = Math.abs(c)) ? 2 : c, d = d == undefined ? "," : d, t = t == undefined ? "." : t, s = n < 0 ? "-" : "", i = parseInt(n = Math.abs(+n || 0).toFixed(c)) + "", j = (j = i.length) > 3 ? j % 3 : 0;
	   return s + (j ? i.substr(0, j) + t : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + t) + (c ? d + Math.abs(n - i).toFixed(c).slice(2) : "");
	};

function successFunc() {
	// alert('deu certo');
}

function errorFunc() {
	// alert('deu caca');
}

// carregando mapa

var infoWindowAnterior;

var map;

function initialize() {
	var mapOptions = {
		zoom : 9,
		scrollwheel : false
	};
	map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
	
	var styles = [
      {
        stylers: [
                  { lightness: -10 },
                  { gamma: 1.51 }
        ]
      },{
        featureType: "road",
        elementType: "geometry",
        stylers: [
          { lightness: 100 },
          { visibility: "simplified" }
        ]
      },{
        featureType: "road",
        elementType: "labels",
        stylers: [
          { visibility: "on" }
        ]
      },{
	     featureType: "poi",
	     stylers: [
	      { visibility: "off" }
	     ]   
	    }
    ];

    map.setOptions({styles: styles});
    
    var lat;
    var lng;

	// Try HTML5 geolocation
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(function(position) {
			var pos = new google.maps.LatLng(position.coords.latitude,
					position.coords.longitude);			
			
			lat = position.coords.latitude;
			lng = position.coords.longitude;
			
			$('#currentUserLat').val(lat);
			$('#currentUserLng').val(lng);
		    
   		  //criando marcador
		    var marcador = new google.maps.Marker({
		        position: new google.maps.LatLng(lat, lng),
		        title: "Seu local atual",
		        icon : document.location.origin + (window.location.href.indexOf("GirofertaWebApp") > 1 ? "/GirofertaWebApp/resources/images/marcador5.png" : "/resources/images/marcador5.png"),
		    });
		    marcador.setMap(map);
		    
		    var circle = new google.maps.Circle({
		    	  map: map,
		    	  radius: 30000,    // metros
		    	  fillColor: '#AA0000',
		    	  fillOpacity: 0.2,
		    	  strokeColor: '#AA0000'
		    	});
			    
		    circle.bindTo('center', marcador, 'position');

			// var infowindow = new google.maps.InfoWindow({
			// map: map,
			// position: pos,
			// content: '<center>Vamos começar a partir do seu local atual,<br
			// /> seja bem vindo e vasculhe a vontade!</center>'
			// });

			map.setCenter(pos);
			
			if ($("#search-header .form-control").val() == "" && $("#currentStore").val() == "" && $("#currentAdvertisement").val() == "") {
				var urlJson = "./Controller?form=actions.AdvertisementsActions&action=resource&op=jsonHome&lat=" + lat + "&lng=" + lng;			
				carregarPontos(map, urlJson);
			}
		}, function() {
			//handleNoGeolocation(true);
			
			var pos = new google.maps.LatLng(-29.901271, -53.039038);
			
			//lat = -29.901271;
			//lng = -53.039038;
			
			$('#currentUserLat').val(lat);
			$('#currentUserLng').val(lng);
			
			map.setCenter(pos);
			
			if ($("#search-header .form-control").val() == "" && $("#currentStore").val() == "") {
				var urlJson = "./Controller?form=actions.AdvertisementsActions&action=resource&op=jsonHome&lat=" + lat + "&lng=" + lng;			
				carregarPontos(map, urlJson);
			}
		});
	} else {
		// Browser doesn't support Geolocation
		handleNoGeolocation(false);
	}

	// Carrega advertisement com url digitada
	if ($("#currentAdvertisement").val() != "") {
		var urlJson = document.location.origin + (window.location.href.indexOf("GirofertaWebApp") > 1 ?
												 	("/GirofertaWebApp/Controller?form=actions.AdvertisementsActions&action=resource&op=jsonAdvertisement&currentAdvertisement=" + $("#currentAdvertisement").val()) :
												 	 ("/Controller?form=actions.AdvertisementsActions&action=resource&op=jsonAdvertisement&currentAdvertisement=" + $("#currentAdvertisement").val()));
		$('.recall-modal-search').trigger('click');
		carregarPontos(map, urlJson);
	}
	
	// Carrega store com url digitada
	if ($("#currentStore").val() != "") {
		var urlJson = "./Controller?form=actions.StoresActions&action=resource&op=json&currentStore=" + $("#currentStore").val() + "&search=" ;
		map.set('zoom', 11);
		carregarPontosLoja(map, urlJson);
	}
	
}

function handleNoGeolocation(errorFlag) {
	if (errorFlag) {
		var content = 'Error: The Geolocation service failed.';
	} else {
		var content = 'Error: Your browser doesn\'t support geolocation.';
	}

	var options = {
		map : map,
		position : new google.maps.LatLng(60, 105),
		content : content
	};

	var infowindow = new google.maps.InfoWindow(options);
	map.setCenter(options.position);
}

google.maps.event.addDomListener(window, 'load', initialize);

function clickAdvertisement(idAdvertisement){
	var lat = $('#currentUserLat').val();
	var lng = $('#currentUserLng').val();
	
    $.ajax({
        type: "POST",
        url: './Controller?form=actions.AdvertisementsActions&action=resource&op=count&advertisement_id=' + idAdvertisement + '&lat=' + lat + '&lng=' + lng,
        contentType: "application/json; charset=utf-8",
        success: successFunc,
        error: errorFunc
    });
}

function clickAdvertisementFeatured(giroUrl, idAdvertisement, op, url){
	
	var lat = $('#currentUserLat').val();
	var lng = $('#currentUserLng').val();
	
    $.ajax({
        type: "POST",
        url: giroUrl + '/Controller?form=actions.AdvertisementsActions&action=resource&op=' + op + '&advertisement_id=' + idAdvertisement + '&lat=' + lat + '&lng=' + lng,
        contentType: "application/json; charset=utf-8",
        success: successFunc,
        error: errorFunc
    });
}

function clickAdvertisementFeaturedStore(idAdvertisement, op, url, lat, lng){
    $.ajax({
        type: "POST",
        url: './Controller?form=actions.AdvertisementsActions&action=resource&op=' + op + '&advertisement_id=' + idAdvertisement + '&lat=' + lat + '&lng=' + lng,
        contentType: "application/json; charset=utf-8",
        success: function() {
        	window.location.assign(url);
        },
        error: errorFunc
    });
}

function clickButtonLinkAdvertisement(giroUrl, idAdvertisement, lat, lng){	
    $.ajax({
        type: "POST",
        url: giroUrl + '/Controller?form=actions.AdvertisementsActions&action=resource&op=purchases&advertisement_id=' + idAdvertisement + '&lat=' + lat + '&lng=' + lng,
        contentType: "application/json; charset=utf-8",
        success: successFunc,
        error: errorFunc
    });
}

function carregarPontos(map, urlJson) {
	
	$('.search-no-cont').fadeOut();
	//var map2 = map;
	var markers = [];
	var cont;
	
	$.getJSON(urlJson).done(
		function(json) {
			if (json == null) {
				$('.search-no-cont').fadeIn();
				$('#searched-word').val($('.search-input').val());
				return;
			}
			
			cont = json.length;
			
			if (cont == 0) {
				$('.search-no-cont').fadeIn();
				$('#searched-word').val($('.search-input').val());
				return;
			}												

			// inserindo busca no mapa
			for (var i = 0; i < cont; i++) {
				if (json[i].id != "") {
					var marker = new google.maps.Marker(
							{
								position : new google.maps.LatLng(
										json[i].store[0].latitude,
										json[i].store[0].longitude),
								title : json[i].store[0].name,
								id: json[i].id,
								advertisementStoresId: json[i].advertisementStoresId,
								lat: json[i].store[0].latitude,
								lng: json[i].store[0].longitude,
								customData: json[i],
								animation : google.maps.Animation.DROP,
								//label: json[i].id + "",
								icon : document.location.origin + (window.location.href.indexOf("GirofertaWebApp") > 1 ? "/GirofertaWebApp/resources/images/marcador3.png" : "/resources/images/marcador3.png"),
								map : map
							});

					var urlAdvertisement = json[i].url;
					var idAdvertisement = json[i].id;
					/*var linkAdvertisement = json[i].link;*/
					var titleAdvertisement = json[i].title;
					var imageAdvertisement = json[i].image_file_name;
					var priceAdvertisement;
					if (json[i].price != '0.00')
						priceAdvertisement = (json[i].price).formatMoney(2, ',', '.');
					else
						priceAdvertisement = 'sob consulta';			
					
					//var descriptionAdvertisement = json[i].description;
					
					var itemstateAdvertisement;
					if (json[i].item_state != 0)
						itemstateAdvertisement = json[i].item_state;
					else if (json[i].item_state == "")
						itemstateAdvertisement = 'Não informado';
					else
						itemstateAdvertisement = 'Prestação de serviços';
					
					var nameStoreAdvertisement = json[i].store[0].name;
					var addressAdvertisement = json[i].store[0].address;
					var numberAdvertisement = json[i].store[0].number;
					var neighborhoodAdvertisement = json[i].store[0].neighborhood;
					var postalcodeAdvertisement = json[i].store[0].postal_code;
					
					var stringImage = "";
					
					if (imageAdvertisement == "" || imageAdvertisement == null)
						stringImage = '<img class="advertisement-image" src=' + document.location.origin + (window.location.href.indexOf("GirofertaWebApp") > 1 ? "/GirofertaWebApp/resources/images/defaultImage.png" : "/resources/images/defaultImage.png") + "/>";
					else
						stringImage = '<img class="advertisement-image" src="' + './Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=' + imageAdvertisement + '" />';
					
					/*var lnkAdv = '';
					
					if (!(linkAdvertisement == "")) {
						lnkAdv = '<a href=' + linkAdvertisement + ' class="btn btn-info" style="margin-left: 10px; width: 140px !important;" target="_blank">Comprar</a>'
					}*/
					
					var contentString = '<div class="div-descr-advertisement">'
						+ stringImage
						+ '<span class="ttl-div-advertisement">' + titleAdvertisement + '</span><br />'
						+ nameStoreAdvertisement + '<br />' 
						+ addressAdvertisement + ', ' + numberAdvertisement + ' - ' + neighborhoodAdvertisement + ' - ' + postalcodeAdvertisement  + '<br />'
						+ '<strong>Condição: ' + itemstateAdvertisement + '</strong><br />'
						+ '<div class="clear-both show-mobile"></div>'
						+ '<span style="font-size: 25px; font-weight: bold;">R$ ' + priceAdvertisement + '</span><br />'
						+ '<div class="clear-both show-mobile"></div>'
						+ '<a href=' + urlAdvertisement + ' class="btn btn-info">Quero saber mais!</a>'
						+ '</div>';
					
					var infowindow = new google.maps.InfoWindow();
					
					var a = contentString;
					// adicionando janela ao clicar sobre o anuncio
					// cadastrado
					
			        bindInfoWindow(marker, map, infowindow, a);
					
			        function bindInfoWindow(marker, map, infowindow, a) {
			            google.maps.event.addListener(marker, 'click', function() {
			                if (infoWindowAnterior != null) {
			                    infoWindowAnterior.close();
			                }
			                infowindow.setContent(a);
			                infowindow.open(map, marker);
			                infoWindowAnterior = infowindow;
			                $('.recall-modal-search').trigger('click');	
			                clickAdvertisement(marker.advertisementStoresId);
						});
					}
					
					stringImages = '';
					countImages = '';
					
					// add para agrupar anuncios quando feito zoom
					markers.push(marker);
				} else {
					// Criando marcadores referências
					var marker = new google.maps.Marker(
							{
								position : new google.maps.LatLng(
										json[i].latitude,
										json[i].longitude),
								animation : google.maps.Animation.DROP,
								icon : document.location.origin + (window.location.href.indexOf("GirofertaWebApp") > 1 ? "/GirofertaWebApp/resources/images/marcador3.png" : "/resources/images/marcador3.png"),
								title : json[i].titulo,
								map : map
							});

				};
			};
			var mcOptions = {gridSize: 40, maxZoom: 22, zoomOnClick: true, minimumClusterSize: 2};
			var markerCluster = new MarkerClusterer(map, markers, mcOptions);
			
			// onClick OVERRIDE
			markerCluster.onClick = function(clickedClusterIcon) { 
			  return multiChoice(clickedClusterIcon.cluster_, map); 
			}
		}).fail(function() {
			alert("Error")
		});
}

function getAdditionalRadius() {
	$.ajax({
        type: "GET",
        url: './Controller?form=actions.AdvertisementsActions&action=resource&op=radius',
		dataType : 'json',
        success: function(jsonData) {
           	alert('additionalRadius: ' + jsonData);
        },
        error: function(jsonData) {
        	alert('Erro ao obter additionalRadius...');
        }
    });
}

function multiChoice(clickedCluster, map) {
  if (clickedCluster.getMarkers().length > 1) {
    var markers = clickedCluster.getMarkers();
	
    var arrayMarkersId = "";
    
	var stringAdvertisements = "";
	
    for (var clus = 0; clus < markers.length; clus++) {

    	arrayMarkersId += markers[clus].customData.advertisementStoresId + ",";
    	
    	var imageAdvertisement = markers[clus].customData.image_file_name;
    	
    	var defaultImage = "";
    	
    	if (markers[clus].customData.image_file_name == "") {
    		defaultImage = document.location.origin + (window.location.href.indexOf("GirofertaWebApp") > 1 ? "/GirofertaWebApp/resources/images/defaultImage.png" : "/resources/images/defaultImage.png");
    	} else {
    		defaultImage = 'Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=' + imageAdvertisement;
    	}
    	
    	var itemstateAdvertisement;
		if (markers[clus].customData.item_state != 0)
			itemstateAdvertisement = markers[clus].customData.item_state;
		else if (markers[clus].customData.item_state == "")
			itemstateAdvertisement = "Não informado";
		else
			itemstateAdvertisement = 'Prestação de serviços';
    	
    	stringAdvertisements += '<div class="container-adv-same-coord">'
			stringAdvertisements += '  <div class="media-left">'
			stringAdvertisements += '    <a href="' + markers[clus].customData.url + '" class="show-div-adv-same-coord">'
			stringAdvertisements += '      <img class="media-object" style="width: 70px;" src="' + defaultImage + '" alt="">'
			stringAdvertisements += '    </a>'
			stringAdvertisements += '  </div>'
			stringAdvertisements += '<div class="media-body">'
			stringAdvertisements += '    <a href="' + markers[clus].customData.url + '" class="show-div-adv-same-coord">'
			stringAdvertisements += '    <h4 class="media-heading">' + markers[clus].customData.title + '</h4>'
			stringAdvertisements += markers[clus].customData.store[0].name + '<br />'
			stringAdvertisements += itemstateAdvertisement
			stringAdvertisements += '    </a>'
			stringAdvertisements += '</div>'

			var idAdvertisement = markers[clus].customData.id;
    		/*var linkAdvertisement = markers[clus].customData.link;*/
			var titleAdvertisement = markers[clus].customData.title;
			
			var priceAdvertisement;
			if (markers[clus].customData.price != '0.00')
				priceAdvertisement = (markers[clus].customData.price).formatMoney(2, ',', '.');
			else
				priceAdvertisement = 'sob consulta';			
			
			//var descriptionAdvertisement = markers[clus].customData.description;
			var descriptionAdvertisement = "";
			
			var nameStoreAdvertisement = markers[clus].customData.store[0].name;
			var addressAdvertisement = markers[clus].customData.store[0].address;
			var numberAdvertisement = markers[clus].customData.store[0].number;
			var neighborhoodAdvertisement = markers[clus].customData.store[0].neighborhood;
			var postalcodeAdvertisement = markers[clus].customData.store[0].postal_code;
			
			var stringImage = '<div class="div-adv-img"><img class="advertisement-image spinLightBox" src="' + './Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=' + imageAdvertisement + '" /></div>';
			
			/*var lnkAdv = '';
			
			if (!(linkAdvertisement == "")) {
				lnkAdv = '<a href=' + linkAdvertisement + ' class="btn btn-info" style="margin-left: 10px; width: 140px !important;" target="_blank">Comprar</a>'
			}*/
			
			var contentString = '<div class="div-descr-advertisement-in-store">'
				+ '<span class="ttl-div-advertisement">' + titleAdvertisement + '</span><br />'
				+ nameStoreAdvertisement + '<br />' 
				+ addressAdvertisement + ', ' + numberAdvertisement + ' - ' + neighborhoodAdvertisement + ' - ' + postalcodeAdvertisement  + '<br /><br />'
				+ '<strong>Condição: ' + itemstateAdvertisement + '</strong><br />'
				+ '<span class="margin-top: 15px; margin-bottom: 15px;">'
				+ '</span>'
				+ stringImage
				+ '<div class="clear-both"></div>'
				+ '<div class="advertisement-description">' + descriptionAdvertisement + '</div>'
				+ '<span style="font-size: 30px; font-weight: bold; line-height: 55px;">R$ ' + priceAdvertisement + '</span><br />'
				+ '<div class="advertisement-box-buttons">'
				//+ '<button type="button" class="btn btn-warning" name="btSave" data-toggle="tooltip" data-placement="top" title="Favoritar Loja!"><span class="glyphicon glyphicon-star-empty"></span></button>'
				//+ '<button type="button" class="btn btn-info" name="btSave" data-toggle="tooltip" data-placement="top" title="Como chegar?"><span class="glyphicon glyphicon-map-marker"></span></button>'
				+ '<button type="button" class="btn btn-success bt-message" name="btSave" advertisementId="' + idAdvertisement + '" data-toggle="tooltip" data-placement="top" title="Enviar uma mensagem!">Contatar vendedor</button>'
				/*+ lnkAdv*/
				+ '</div>'
				+ '<button type="button" class="btn btn-primary-outline btn-sm bt-back-to-store">Voltar</button>'
				+ '</div>';

			stringAdvertisements += '</div><div class="div-advertisement-same-coord">'
								 + contentString
								 + '</div>'									

	}
    
    clickAdvertisement(arrayMarkersId.substr(0, arrayMarkersId.length - 1));
    
    var contentString = '<div class="div-descr-advertisement-same-coord">'
		 + '<div class="ttl-box-adv-same-coord">Encontramos mais de um anúncio nesse mesmo local</div>'
		+ stringAdvertisements
		+ '</div>';								

    
    var infowindow = new google.maps.InfoWindow();
	
	var a = contentString;
    
    $('.recall-modal-search').trigger('click');
	
	bindInfoWindow(markers[0], map, infowindow, a);
	
    function bindInfoWindow(marker, map, infowindow, a) {
        if (infoWindowAnterior != null) {
            infoWindowAnterior.close();
        }
        infowindow.setContent(a);
        infowindow.open(map, marker);
        infoWindowAnterior = infowindow;
	}
    
    return false;
  }
  return true;
}

function carregarPontosLoja(map, urlJson) {
	
	$('.recall-modal-search').trigger('click');
	
	$('.search-no-cont').fadeOut();
	
	var markers = [];
	var cont;
	
	$
			.getJSON(urlJson)
			.done(
					function(json) {						
						if (json == null) {
							$('.search-no-cont').fadeIn();
							$('#searched-word').val($('.search-input').val());
							return;
						}
						
						cont = json.length;
						
						if (cont == 0) {
							$('.search-no-cont').fadeIn();
							$('#searched-word').val($('.search-input').val());
							return;
						}												

						// inserindo busca no mapa
						for (var i = 0; i < cont; i++) {
							if (json[i].id != "") {
								var marker = new google.maps.Marker(
										{
											position : new google.maps.LatLng(
													json[i].latitude,
													json[i].longitude),
											title : json[i].name,
											animation : google.maps.Animation.DROP,
											icon : document.location.origin + (window.location.href.indexOf("GirofertaWebApp") > 1 ? "/GirofertaWebApp/resources/images/marcador1.png" : "/resources/images/marcador1.png"),
											map : map
										});

								var idStore = json[i].id;
								var nameStore = json[i].name;
								var addressStore = json[i].address;
								var numberStore = json[i].number;
								var neighborhoodStore = json[i].neighborhood;
								var postalcodeStore = json[i].postal_code;
								
								var advertisements = json[i].advertisements;
								
								var stringAdvertisements = '';
								
								for (var countAdvert = 0; countAdvert < advertisements.length; countAdvert++) {
									var defaultImage = "";
									
									if (advertisements[countAdvert].imagesAdvertisement[0] == null) {
							    		defaultImage = document.location.origin + (window.location.href.indexOf("GirofertaWebApp") > 1 ? "/GirofertaWebApp/resources/images/defaultImage.png" : "/resources/images/defaultImage.png");
							    	} else {
							    		defaultImage = 'Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=' + advertisements[countAdvert].imagesAdvertisement[0].image_file_name;
							    	}									
									
									stringAdvertisements += '<div class="media " style="border-bottom: 1px solid #c6c6c6; padding-bottom: 10px;">'
									stringAdvertisements += '  <div class="media-left">'
									stringAdvertisements += '    <a href="javascript:void(0);" class="show-div-adv-in-store" idAdvert="' + advertisements[countAdvert].id + '">'
									stringAdvertisements += '      <img class="media-object" style="width: 50px;" src="./Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=' + defaultImage + '" alt="">'
									stringAdvertisements += '    </a>'
									stringAdvertisements += '  </div>'
									stringAdvertisements += '<div class="media-body">'
									stringAdvertisements += '    <h4 class="media-heading">' + advertisements[countAdvert].title + '</h4>'
									stringAdvertisements += advertisements[countAdvert].description + '... <a href="javascript:void(0);" idAdvert="' + advertisements[countAdvert].id + '" class="label label-info show-div-adv-in-store">Ver mais</a>'
									stringAdvertisements += '  </div>'

									var idAdvertisement = advertisements[countAdvert].id;
									/*var linkAdvertisement = advertisements[countAdvert].link;*/
									var titleAdvertisement = advertisements[countAdvert].title;
									var priceAdvertisement;
									if (advertisements[countAdvert].price != '0.00')
										priceAdvertisement = (advertisements[countAdvert].price).formatMoney(2, ',', '.');
									else
										priceAdvertisement = 'sob consulta';			
									
									var descriptionAdvertisement = advertisements[countAdvert].description;
									
									var itemstateAdvertisement;
									if (advertisements[countAdvert].item_state != 0)
										itemstateAdvertisement = advertisements[countAdvert].item_state;
									else if (advertisements[countAdvert].item_state == "")
										itemstateAdvertisement = 'Não informado';
									else
										itemstateAdvertisement = 'Prestação de serviços';
									
									var nameStoreAdvertisement = json[i].name;
									var addressAdvertisement = json[i].address;
									var numberAdvertisement = json[i].number;
									var neighborhoodAdvertisement = json[i].neighborhood;
									var postalcodeAdvertisement = json[i].postal_code;
									
									var stringImages = '';
									
									var countImages = '';
									
									if (countImages = advertisements[countAdvert].imagesAdvertisement != null) {
										countImages = advertisements[countAdvert].imagesAdvertisement.length;
									
										for (var y = 0; y < countImages; y++) {										
											if (advertisements[countAdvert].imagesAdvertisement[y].id != "") {
												stringImages += '<div class="div-adv-img"><img class="advertisement-image spinLightBox" src="' + './Controller?form=actions.AdvertisementsActions&action=resource&op=viewImage&image=' + advertisements[countAdvert].imagesAdvertisement[y].image_file_name + '" /></div>';
											}
										}
										
									}
									
									/*var lnkAdv = '';
									
									if (!(linkAdvertisement == "")) {
										lnkAdv = '<a href=' + linkAdvertisement + ' class="btn btn-info" style="margin-left: 10px; width: 140px !important;" target="_blank">Comprar</a>'
									}*/
									
									var contentString = '<div class="div-descr-advertisement-in-store">'
										+ '<span class="ttl-div-advertisement">' + titleAdvertisement + '</span><br />'
										+ nameStoreAdvertisement + '<br />' 
										+ addressAdvertisement + ', ' + numberAdvertisement + ' - ' + neighborhoodAdvertisement + ' - ' + postalcodeAdvertisement  + '<br /><br />'
										+ '<strong>Condição: ' + itemstateAdvertisement + '</strong><br />'
										+ '<span class="margin-top: 15px; margin-bottom: 15px;">'
										+ '</span>'
										+ stringImages
										+ '<div class="clear-both"></div>'
										+ '<div class="advertisement-description">' + descriptionAdvertisement + '</div>'
										+ '<span style="font-size: 30px; font-weight: bold; line-height: 55px;">R$ ' + priceAdvertisement + '</span><br />'
										+ '<div class="advertisement-box-buttons">'
										//+ '<button type="button" class="btn btn-warning" name="btSave" data-toggle="tooltip" data-placement="top" title="Favoritar Loja!"><span class="glyphicon glyphicon-star-empty"></span></button>'
										//+ '<button type="button" class="btn btn-info" name="btSave" data-toggle="tooltip" data-placement="top" title="Como chegar?"><span class="glyphicon glyphicon-map-marker"></span></button>'
										+ '<button type="button" class="btn btn-success bt-message" name="btSave" advertisementId="' + idAdvertisement + '" data-toggle="tooltip" data-placement="top" title="Enviar uma mensagem!">Contatar vendedor</button>'
										/*+ lnkAdv*/
										+ '</div>'
										+ '<button type="button" class="btn btn-primary-outline btn-sm bt-back-to-store">Voltar</button>'
										+ '</div>';

									stringAdvertisements += '</div><div class="div-advertisement-in-store">' + contentString + '</div>'									
								}

								//var contentString = '<div class="div-descr-advertisement">'
													//+ '<span class="ttl-div-advertisement">' + nameStore + '</span><br />'
													//+ addressStore + ', ' + numberStore + ' - ' + neighborhoodStore + ' - ' + postalcodeStore + '<br /><br />'
													//+ '<div class="find-in-store">'
													//+ 'Procurando por algo específico nesta loja?<br />'
													//+ '<div class="input-group">'
													//+ '<input type="text" name="find-in-store" class="form-control input-find-in-store" />'
													//+ '<div class="input-group-btn">'
													//+ '<button type="button" class="btn btn-primary bt-find-in-store" name="btSave"><span class="glyphicon glyphicon-search"></span></button>'
													//+ '</div></div>'
													//+ '</div><br />'
													//+ '<span class="glyphicon glyphicon-certificate"></span>&nbsp;Nossos anúncios'
													//+ '<div class="div-string-advertisements">' + stringAdvertisements + '</div>'
													//+ '<div class="advertisement-box-buttons">'
													//+ '<button type="button" class="btn btn-warning" name="btSave" data-toggle="tooltip" data-placement="top" title="Favoritar Loja!"><span class="glyphicon glyphicon-star-empty"></span></button>'
													//+ '<button type="button" class="btn btn-info" name="btSave" data-toggle="tooltip" data-placement="top" title="Como chegar?"><span class="glyphicon glyphicon-map-marker"></span></button>'
													//+ '<button type="button" class="btn btn-success bt-message" name="btSave" data-toggle="tooltip" data-placement="top" title="Enviar uma mensagem!"><span class="glyphicon glyphicon-comment"></span></button>'
													//+ '</div>'
													//+ '</div>';								
								
								var infowindow = new google.maps.InfoWindow();

								var a = contentString;
								// adicionando janela ao clicar sobre o anuncio
								// cadastrado
								bindInfoWindow(marker, map, infowindow, a);

				                function bindInfoWindow(marker, map, infowindow, a) {
				                    google.maps.event.addListener(marker, 'click', function() {
				                        if (infoWindowAnterior != null) {
				                            infoWindowAnterior.close();
				                        }
				                        infowindow.setContent(a);
				                        infowindow.open(map, marker);
				                        infoWindowAnterior = infowindow;
													                    });
								}

								// add para agrupar anuncios quando feito zoom
								markers.push(marker);
							} else {
								// Criando marcadores referências
								var marker = new google.maps.Marker(
										{
											position : new google.maps.LatLng(
													json[i].latitude,
													json[i].longitude),
											animation : google.maps.Animation.DROP,
											icon : "./resources/images/marcador3.png",
											title : json[i].titulo,
											map : map
										});

							}
							;
						}
						;
						var markerCluster = new MarkerClusterer(map, markers);

					}).fail(function() {
				alert("Error");
			});
}

$(window).load(function() {
	$(".se-pre-con").fadeOut("slow");
});

/* FACEBOOK **************************************************************/

function statusChangeCallback(response) {
	if (response.status === 'connected') {
		loginSuccess(response.authResponse.access_token);
	}
}

function checkLoginState() {
	FB.getLoginStatus(function(response) {
		statusChangeCallback(response);
	});
}

(function(d, s, id) {
	var js, fjs = d.getElementsByTagName(s)[0];
	if (d.getElementById(id)) return;
	js = d.createElement(s); js.id = id;
	js.src = "//connect.facebook.net/pt_BR/sdk.js#xfbml=1&version=v2.7&appId=2063142550576669";
	fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));

function loginSuccess(token) {
	FB.api('/me', 'get', { access_token: token, fields: 'id,email,first_name,last_name' }, function(response) {      
		checkLoginSocial(response.id, response.email, response.first_name, response.last_name, 'facebook');
	});
}

/* final facebook  *******************************************************/

/* GOOGLE  ***************************************************************/

var auth2;

function handleClientLoad() {
	gapi.load('client:auth2', initAuth);
}

function initAuth() {
	gapi.client.setApiKey('X-AU0LbQHBYyuN8d0VSl30Kt');
	gapi.auth2.init({
		client_id: '752498309543-l0beuusng3lg2r19dtmop7glu5jcf690.apps.googleusercontent.com',
		scope: 'profile email'
	}).then(function() {
		auth2 = gapi.auth2.getAuthInstance();
		// Listen for sign-in state changes.
		//auth2.isSignedIn.listen(updateSigninStatus);
		updateSigninStatus();
	});
}

function updateSigninStatus() {
	if (auth2.isSignedIn.get()) {
		makeApiCall();
	} else {
		gapi.auth2.getAuthInstance().signIn().then(function() {
			makeApiCall();
		});
	}
}

function handleAuthClick() {
	handleClientLoad();
	if (auth2 != 'undefined' && auth2 != null)
		updateSigninStatus();
}

function makeApiCall() {
	var profile = auth2.currentUser.get().getBasicProfile();
	checkLoginSocial(profile.getId(), profile.getEmail(), profile.getGivenName(), profile.getFamilyName(), 'google');
}

/* final google **********************************************************/

function checkLoginSocial(id, email, firstName, lastName, socialNetwork) {
	$.ajax({
		type: 'POST',
		url: './Controller?form=actions.UsersActions&action=resource&op=loginSocial'+
			'&socialId='+id+
			'&socialEmail='+email+
			'&socialFirst_name='+firstName+
			'&socialLast_name='+lastName+
			'&socialNetwork='+socialNetwork,
		success: function(result) {
			var protocol = window.location.protocol; 
			var hostname = window.location.hostname; 
			var port = window.location.port; 
			var pathname = window.location.pathname; 
			var href = window.location.href;
			if (result) {
				if (hostname == 'localhost') {
					window.location = protocol + '//' + hostname + ':' + port + '/GirofertaWebApp/';
				}
				else {
					window.location = protocol + '//' + hostname;
        		}
			}
			else {
				if (hostname == 'localhost') {
					window.location = protocol + '//' + hostname + ':' + port + '/GirofertaWebApp/register';
				}
				else {
					window.location = protocol + '//' + hostname + "/register";
        		}
			}
		},
		error: function() {
			if (hostname == 'localhost') {
				window.location = protocol + '//' + hostname + ':' + port + '/GirofertaWebApp/register';
			}
			else {
				window.location = protocol + '//' + hostname + "/register";
    		}
		}
	});
}

$(document).ready(function() {
	$('#bt-login-facebook').click(function () {
		FB.login(function(response) {
			checkLoginState();
		}, {scope: 'public_profile,email'});
	});
});

$(document).ready(function() {
	$('#bt-login-google').click(function () {
		handleAuthClick();
	});
});

/* FINAL DAS FUNCTIONS PARA LOGIN SOCIAL - FACEBOOK e GOOGLE *****************/

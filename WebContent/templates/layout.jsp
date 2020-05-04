<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="pt-BR">
<head>
	<title>Giroferta.com</title>
		
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="msapplication-tap-highlight" content="no"/>
	<meta name="robots" content="index, follow">
	<meta name="keywords" content="anuncio de produtos diretamente no mapa">
	<meta name="description" content="O caminho do melhor preço!">	
    <meta name="viewport" content="width=device-width, height=device-height, initial-scale=1, maximum-scale=1, user-scalable=no" />

	<link rel="shortcut icon" type="image/x-icon" href="${giroUrl}/resources/images/favicon.ico" />
	<link rel="stylesheet" href="${giroUrl}/resources/stylesheets/bootstrap-theme.min.css" />
	<link rel="stylesheet" href="${giroUrl}/resources/stylesheets/bootstrap-theme.css.map" />
	<link rel="stylesheet" href="${giroUrl}/resources/stylesheets/bootstrap.min.css" />
	<link rel="stylesheet" href="${giroUrl}/resources/stylesheets/bootstrap.css.map" />
	<link rel="stylesheet" href="${giroUrl}/resources/stylesheets/application.css" />
	<link rel="stylesheet" href="${giroUrl}/resources/stylesheets/lightbox.css" />
	<link rel="stylesheet" href="${giroUrl}/resources/stylesheets/jquery-ui.structure.min.css" />

	<script type="text/javascript" src="https://apis.google.com/js/api.js"></script>
	<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyATQ6MJerturXNOx09Slqky5EN28r5HHC8"></script>
	<script type="text/javascript" src="${giroUrl}/resources/javascripts/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="${giroUrl}/resources/javascripts/bootstrap.min.js"></script>
	<script type="text/javascript" src="${giroUrl}/resources/javascripts/markerclusterer.js"></script>
	<script type="text/javascript" src="${giroUrl}/resources/javascripts/jquery.maskedinput.js"></script>
	<script type="text/javascript" src="${giroUrl}/resources/javascripts/jquery.maskMoney.min.js"></script>
	<script type="text/javascript" src="${giroUrl}/resources/javascripts/application.js"></script>
	<script type="text/javascript" src="${giroUrl}/resources/javascripts/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${giroUrl}/resources/javascripts/lightbox.js"></script>
	<script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
<%--	<script type="text/javascript" src="https://stc.pagseguro.uol.com.br/pagseguro/api/v2/checkout/pagseguro.lightbox.js"></script> --%>
	
	<script>
	  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

	  ga('create', 'UA-68840531-1', 'auto');
	  ga('send', 'pageview');
	</script>

    <meta name="google-signin-scope" content="profile email">
    <meta name="google-signin-client_id" content="752498309543-l0beuusng3lg2r19dtmop7glu5jcf690.apps.googleusercontent.com">
    <script src="https://apis.google.com/js/platform.js" async defer></script>
	
</head>
<body>

	<div itemscope itemprop="organization" itemtype="http://schema.org/Organization">
		<meta itemprop="url" content="https://www.giroferta.com" />
		<meta itemprop="name" content="Giroferta">
		<meta itemprop="logo" content="https://www.giroferta.com//resources/images/logo-horizontal.png">
		<meta itemprop="sameAs" content="http://www.facebook.com.br/girofertaoficial" />
		<meta itemprop="sameAs" content="http://www.instagram.com/giroferta" />
		<meta itemprop="sameAs" content="http://www.twitter.com/giroferta" />
	</div>
	
	<jsp:include page="header.jsp" />
	<jsp:include page="content.jsp" />
	<jsp:include page="footer.jsp" />
</body>
</html>
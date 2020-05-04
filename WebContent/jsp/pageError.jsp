<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Giroferta.com</title>
		
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="msapplication-tap-highlight" content="no"/>
	<meta name="robots" content="index, follow">
	<meta name="keywords" content="anuncio de produtos diretamente no mapa">
	<meta name="description" content="O caminho do melhor preço!">	
    <meta name="viewport" content="width=device-width, initial-scale=1">
	
	<link rel="shortcut icon" type="image/x-icon" href="${giroUrl }/resources/images/favicon.ico" />
	<link rel="stylesheet" href="${giroUrl }/resources/stylesheets/bootstrap-theme.min.css" />
	<link rel="stylesheet" href="${giroUrl }/resources/stylesheets/bootstrap-theme.css.map" />
	<link rel="stylesheet" href="${giroUrl }/resources/stylesheets/bootstrap.min.css" />
	<link rel="stylesheet" href="${giroUrl }/resources/stylesheets/bootstrap.css.map" />
	<link rel="stylesheet" href="${giroUrl }/resources/stylesheets/application.css" />
	<link rel="stylesheet" href="${giroUrl }/resources/stylesheets/jquery-ui.structure.min.css" />
	
	<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyATQ6MJerturXNOx09Slqky5EN28r5HHC8"></script>
	<script type="text/javascript" src="${giroUrl }/resources/javascripts/jquery-1.11.3.min.js"></script>
	<script type="text/javascript" src="${giroUrl }/resources/javascripts/bootstrap.min.js"></script>
	<script type="text/javascript" src="${giroUrl }/resources/javascripts/markerclusterer.js"></script>
	<script type="text/javascript" src="${giroUrl }/resources/javascripts/jquery.maskedinput.js"></script>
	<script type="text/javascript" src="${giroUrl }/resources/javascripts/jquery.maskMoney.min.js"></script>
	<script type="text/javascript" src="${giroUrl }/resources/javascripts/application.js"></script>
	<script type="text/javascript" src="${giroUrl }/resources/javascripts/jquery-ui.min.js"></script>
	
	<script>
	  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
	  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
	  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
	  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

	  ga('create', 'UA-68840531-1', 'auto');
	  ga('send', 'pageview');
	</script>
	
</head>
<body>
	<div class="wrapper-pagenotfound">
		<div>
			<h1>Ooops!!</h1>
			<h2><fmt:message key="page.pageError" /></h2>
			<br />
			<a href="${linkHome}"><fmt:message key="page.backHome" /></a>
		</div>
	</div>
</body>
</html>
<script type="text/javascript">
	$(document).ready(function() {
		var protocol = window.location.protocol;
		var hostname = window.location.hostname;
		var port = window.location.port;
		var pathname = window.location.pathname;
/* 		console.log('protocol: ' + protocol);
		console.log('hostname: ' + hostname);
		console.log('port: ' + port);
		console.log('pathname: ' + pathname);
*/ 		if (protocol == 'http:' && hostname != 'localhost') {
			if (hostname == 'giroferta.com') {
				hostname = 'www.' + hostname;
			}
			var newURL = hostname + pathname;
			var httpsURL = "https://" + newURL;
/* 			console.log('newURL: ' + newURL);
			console.log('httpsURL: ' + httpsURL);
 *//* 			window.location = httpsURL;  */
		}
	});
</script>

<div class="overlay"></div>

<%

	response.setHeader("Cache-Control", "no-cache");

	response.setHeader("Pragma", "no-cache");

	response.setDateHeader("Expires", 0);

	response.setContentType("text/html; charset=UTF-8");

%>

<div class="wrapper-panel">
	<div class="alert alert-dismissible alert-box" role="alert">
	  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	  <strong></strong>
	</div>
	<jsp:include page="${yield}" />
</div>

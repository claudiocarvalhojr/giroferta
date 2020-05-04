<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h3><fmt:message key="admin.title.jobs" /></h3>

<ul class="submenu">
  <li><a href="javascript:void(0);" onclick="triggerJob('${linkAdminFullProccess}', 100000);"><fmt:message key="admin.jobs.fullProccess" /></a></li>
  <li><a href="javascript:void(0);" onclick="triggerJob('${linkAdminImportXML}', 4000);"><fmt:message key="admin.jobs.importXML" /></a></li>
  <li><a href="javascript:void(0);" onclick="triggerJob('${linkAdminImportImages}', 9000);"><fmt:message key="admin.jobs.importImages" /></a></li>
  <li><a href="javascript:void(0);" onclick="triggerJob('${linkAdminReplyAdsStores}', 60000);"><fmt:message key="admin.jobs.replyAdsStores" /></a></li>
  <li><a href="javascript:void(0);" onclick="triggerJob('${linkAdminUpdateHighLights}', 100);"><fmt:message key="admin.jobs.updateHighLights" /></a></li>
  <li><a href="javascript:void(0);" onclick="triggerJob('${linkAdminUpdateSitemap}', 200);"><fmt:message key="admin.jobs.updateSitemap" /></a></li>
  <li><a href="javascript:void(0);" onclick="triggerJob('${linkAdminFixesImages}', 9000);"><fmt:message key="admin.jobs.fixesImages" /></a></li>
  <li><a href="javascript:void(0);" onclick="triggerJob('${linkAdminUpdateURLs}', 1000);"><fmt:message key="admin.jobs.updateURLs" /></a></li>
  <li style="margin-top: 15px;"><a href="${linkAdminManager}"><fmt:message key="admin.back" /></a></li>
</ul>

<script type="text/javascript">
	var width = 0;
	function triggerJob(request, time) {
		$.ajax({
			url: request,
			beforeSend: function() {
//				width = 0;
//				refresh();
//				move(time);
				$('#wait').show();
			},
			complete: function(){
//				if (width < 99)
//					width = 99;
				$('#wait').hide(2000);
			}
		})
	}
	function move(time) {
	    var id = setInterval(frame, time);
	    function frame() {
	        if (width < 100) {
	            width++;
	            refresh()
	        }
	        else
	        	clearInterval(id);
	    }
	}
	function refresh() {
        document.getElementById("progress").style.width = width + '%';
        document.getElementById("label").innerHTML = width + '%';
	}
</script>

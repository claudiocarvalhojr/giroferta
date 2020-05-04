<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div class="nav-list">
	<a href="${linkStoresShow}&store.id=${store.id}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}">
		<span class="glyphicon glyphicon-eye-open"></span>
	</a>
	<a href="Routes?p=${pEdit}&id=${id}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}">
		<span class="glyphicon glyphicon-pencil"></span>
	</a>
	<a href="javascript:void(0);" class="remove-item-buttom">
		<span class="glyphicon glyphicon-trash"></span>
	</a>
	<div class="remove-confirmation">
		<fmt:message key="nav.removeBox.text" />
		<br >
		<a class="btn btn-danger" href="Routes?p=${pDelete}&id=${id}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}" role="button">
			<fmt:message key="nav.removeBox.remove" />
		</a>
		<a class="btn btn-default bt-remove-cancel" href="javascript:void(0);" role="button">
			<fmt:message key="nav.removeBox.cancel" />
		</a>
	</div>
</div>
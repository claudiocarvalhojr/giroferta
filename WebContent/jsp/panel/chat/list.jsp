<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div style="width: 100%;">
	<div style="width: 100%;">
		<div style="float: left; width: 70%;">
			<br />
		</div>
		<div style="float: right; width: 30%; text-align: right">
			<jsp:include page="/templates/panel/searchList.jsp" />
		</div>
		<div style="clear: both;"><br></div>
	</div>
	<div>
		<jsp:include page="/templates/panel/listMessages.jsp"/>
	</div>
	<div style="width: 100%;">
		<div class="panel panel-default">
			<!-- Default panel contents -->
			<div class="panel-heading">
 			<fmt:message key="listChat.titleMessages"/>
			</div>
			<!-- Table -->
				<table class="table">
				<thead>
					<tr align="left">
						<th style="width: 5%"><fmt:message key="chatMessages.listFieldId"/></th>
						<th style="width: 15%"><fmt:message key="chatMessages.listFieldFrom"/></th>
						<th style="width: 15%"><fmt:message key="chatMessages.listFieldTo"/></th>
 						<th style="width: 15%"><fmt:message key="chatMessages.listFieldCreatedAt"/></th>
						<th style="width: 10%" align="right"></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${listChatMessages}" var="chatMessage" varStatus="contador">
						<c:choose>
							<c:when test="${chatMessage.message_read == 0 && chatMessage.user_receiver.id == currentUser.id}">
								<tr style="background-color: #f5f5f5 !important; font-weight: bold !important; text-shadow: 1px 1px 15px #999;">
							</c:when>
							<c:otherwise>
								<tr>
							</c:otherwise>
						</c:choose>
							<td>${chatMessage.chat_id.id}</td>
							<c:choose>
								<c:when test="${chatMessage.user_sender.type_person == 'F'}">
									<td>${chatMessage.user_sender.name}</td>
								</c:when>
								<c:otherwise>
									<td>${chatMessage.chat_id.user_sender.company_name}</td>
								</c:otherwise>
							</c:choose>
							<c:choose>
								<c:when test="${chatMessage.user_receiver.type_person == 'F'}">
									<td>${chatMessage.user_receiver.name}</td>
								</c:when>
								<c:otherwise>
									<td>${chatMessage.chat_id.user_receiver.company_name}</td>
								</c:otherwise>
							</c:choose>
 							<td><fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${chatMessage.created_at}"/></td>
							<td align="right">
								<div class="nav-list">
									<a href="${linkChatForm}&chat_id=${chatMessage.chat_id.id}&chat_message_id=${chatMessage.id}&current=${current}&maximum=${maximum}&valueSearch=${valueSearch}">
										<span class="glyphicon glyphicon-share-alt"></span>
									</a>
								</div>								
							</td>
						</tr>
					</c:forEach>
				</tbody>
	 		</table>
		</div>
	</div>
	<div style="width: 100%; text-align: center;">
		<br>
		<jsp:include page="/templates/panel/navList.jsp"/>
	</div>
</div>
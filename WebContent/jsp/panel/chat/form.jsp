<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<h3><span class="glyphicon glyphicon-plus" aria-hidden="true"></span>  <fmt:message key="formChat.title" /></h3>

<jsp:include page="/templates/panel/listMessages.jsp"/>

<br />
<div class="clear-both"></div>

<div class="box-left">
	<c:forEach items="${listChatMessages}" var="chatMessage">
		<c:choose>
			<c:when test="${chatMessage.user_sender.id != currentUser.id}">				
				<div class="div-msg-left">
			</c:when>
			<c:otherwise>
				<div class="div-msg-right">
			</c:otherwise>
		</c:choose>
			<span>
				<c:choose>
					<c:when test="${chatMessage.user_sender.type_person == 'F'}">
						${chatMessage.user_sender.name} -
					</c:when>
					<c:otherwise>
						${chatMessage.user_sender.company_name} -
					</c:otherwise>
				</c:choose>
				<fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${chatMessage.created_at}"/>
			</span>
			<c:choose>
				<c:when test="${chatMessage.message_read == 1}">
					<span class="glyphicon glyphicon-ok" title="Mensagem Lida"></span>
				</c:when>
			</c:choose>
			<br />
			<strong>${chatMessage.message_text}</strong>
			<br /><br />
		</div>
	</c:forEach>
</div>

<div class="clear-both"></div>

<form action="${linkChatSave}" method="POST" name="formChat">
	
	<div class="box-left">
		<div class="form-group">
		
			<div class="input-group">
			  <textarea id="chatMessage.message" name="chatMessage.message" class="form-control" placeholder="<fmt:message key="chatMessages.fieldMessage"/>" aria-describedby="basic-addon1"></textarea>
			  <div class="input-group-btn">
			    <button type="submit" class="btn btn-primary btn-lg" style="height: 54px !important;"><fmt:message key="form.btSend"/> <span class="glyphicon glyphicon-send"></span></button>
			  </div>
			</div>
			
			<br>
			
			<input type="hidden" id="chat_id" name="chat_id" value="${chat_id}">
			<input type="hidden" id="current" name="current" value="${current}">
			<input type="hidden" id="maximum" name="maximum" value="${maximum}">
			<input type="hidden" id="valueSearch" name="valueSearch" value="${valueSearch}">
						
		</div>
	</div>
	<div class="clear-both"></div>
</form>

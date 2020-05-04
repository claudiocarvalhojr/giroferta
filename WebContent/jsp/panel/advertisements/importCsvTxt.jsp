<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<jsp:include page="/templates/panel/listMessages.jsp"/>

<br />
<div class="clear-both"></div>

<div>
	<form action="${linkAdvertisementsImportRunCsvTxt}" method="POST" name="formImportAdvertisementsCsvTxt" enctype="multipart/form-data">
		<div>
			<h3><fmt:message key="advertisement.importYourAdvertisements"/></h3>
			<br />
			<fmt:message key="advertisement.stores"/>
			<br />
			<select class="form-control" id="cbStoreId" name="cbStoreId" style="width: 320px;">
		   		<option value="0"><fmt:message key="advertisement.stores.all"/></option>
				<c:forEach items="${listStores}" var="store">
					<option value="${store.id}">${store.name} - ${store.filial}</option>
				</c:forEach>
		  	</select>
			<br />
			<fmt:message key="advertisement.categories"/>
			<br />
			<select class="form-control" id="ddAdvertisementCategory" name="cbCategoryId" style="width: 320px;">
		   		<%-- <option value="0"><fmt:message key="advertisement.categories.select"/></option> --%>
				<c:forEach items="${listCategories}" var="category">
					<option value="${category.id}">${category.name}</option>
				</c:forEach>
		  	</select>
			<br />
 			<input type="file" class="btn btn-primary" name="btFile" style="width: 320px;">
 			<br />
 			<c:if test="${currentUser.balance >= currentUser.click_values_id.value}">
 				<input type="checkbox" id="ckPublicar" name="ckPublicar" value="1" checked="checked"> <fmt:message key="advertisement.importPublishUnpublish"/>
 				<br />
 			</c:if>
 			<br />
			<button type="submit" class="btn btn-primary" name="btImport"><fmt:message key="advertisement.linkImport"/></button>
			<br />
			<br />
			<br />
			<a href="${linkHome}"><fmt:message key="page.backHome" /></a>
		</div>
	</form>
	<br />
	<br />
	<div style="width: 100%;">
		Seus an�ncios devem estar estruturados com o seguinte formato em seu arquivo .TXT ou em seu arquivo .CSV:
		<br />
		<br />
		<b>C�DIGO DO SEU PRODUTO</b> (0 = SEM C�DIGO)<b>;<br />
		T�TULO DO SEU AN�NCIO;<br />
		DESCRI��O DO SEU AN�NCIO;<br />
		PRE�O </b>(0 = SEM PRE�O)<b>;<br />
		MARCA</b>(0 = SEM MARCA)<b>;<br />
		CONDI��O </b>(1 = NOVO, 2 = USADO, 3 = RECONDICIONADO, 4 = SERVI�O)<b>;<br />
		DISPONIBILIDADE </b>(1 = EM ESTOQUE, 2 = PR� VENDA, 3 = SEM ESTOQUE, 4 = SERVI�O)<b>;<br />
		LINK PARA O SEU PRODUTO</b>(0 = SEM LINK)<b>;<br />
		LINK DA IMAGEM PARA O SEU PRODUTO </b>(0 = SEM LINK)<b>;</b>
		<br />
		<br />
		* coloque todos os campos em uma mesma linha, separando cada campo por ponto e v�rgula sem espa�os, com quebra de linha para cada novo an�ncio.
		<br />
		<br />
		<b>Exemplo:</b>
		<br />
		<br />
		123456;T�tulo do seu an�ncio (produto);Descri��o do seu an�ncio (produto);9,99;Marca do seu produto;1;1;http://www.seusite.com.br/seu_produto;http://www.seusite.com.br/seu_produto/imagem_do_produto.jpg;
		<br />
		<br />
		<b>Dicas:</b>		
		<br />
		<br />
		1) Separe cada campo com ponto e v�rgula.
		<br />
		2) N�o utilize espa�os em branco antes e ap�s o ponto e v�rgula.
		<br />
		3) Coloque apenas um an�ncio por linha.
		<br />
		4) N�o deixe campos em branco, se n�o existir valor, utilize o valor zero.
		<br />
		5) Tente colocar sempre um c�digo de refer�ncia para seus an�ncios, isto evita que os mesmos sejam duplicados ao fazer uma nova importa��o. 
		<br />
		6) Caso queira adicionar mais imagens para o an�ncio, adicione todas ao final da linha do mesmo an�ncio, separando cada link da imagem por ponto e v�rgula, sendo limitado a at� 4 imagens para pessoa f�sica e a at� 8 imagens para pessoa jur�dica. 
		<br />
		7) N�o coloque mais do que 100 an�ncios (linhas) em seu arquivo. 
		<br />
		8) Somente ser�o aceitos arquivos em formato TXT ou CSV, podendo o arquivo ter qualquer nome.
		<br />
		9) A importa��o de arquivos atualizados mant�m seus an�ncios publicados ou despublicados conforme o estoque informado.
		<br />
		10) Baixe os modelos de arquivo abaixo para basear a constru��o do arquivo que conter� os seus an�ncios.
		<br />
		<br />
		<a href="https://www.giroferta.com/modelos/modelo.txt">Modelo TXT</a> e <a href="https://www.giroferta.com/modelos/modelo.csv">Modelo CSV</a>
		<br />
		<br />
		Caso tenha d�vidas entre em contato conosco, estamos a disposi��o para ajud�-lo.
		<br />
		<br />
		<br />
		<a href="${linkHome}"><fmt:message key="page.backHome" /></a>
		<br />
	</div>
	<br />
</div>
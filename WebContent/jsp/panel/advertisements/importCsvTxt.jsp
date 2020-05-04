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
		Seus anúncios devem estar estruturados com o seguinte formato em seu arquivo .TXT ou em seu arquivo .CSV:
		<br />
		<br />
		<b>CÓDIGO DO SEU PRODUTO</b> (0 = SEM CÓDIGO)<b>;<br />
		TÍTULO DO SEU ANÚNCIO;<br />
		DESCRIÇÃO DO SEU ANÚNCIO;<br />
		PREÇO </b>(0 = SEM PREÇO)<b>;<br />
		MARCA</b>(0 = SEM MARCA)<b>;<br />
		CONDIÇÃO </b>(1 = NOVO, 2 = USADO, 3 = RECONDICIONADO, 4 = SERVIÇO)<b>;<br />
		DISPONIBILIDADE </b>(1 = EM ESTOQUE, 2 = PRÉ VENDA, 3 = SEM ESTOQUE, 4 = SERVIÇO)<b>;<br />
		LINK PARA O SEU PRODUTO</b>(0 = SEM LINK)<b>;<br />
		LINK DA IMAGEM PARA O SEU PRODUTO </b>(0 = SEM LINK)<b>;</b>
		<br />
		<br />
		* coloque todos os campos em uma mesma linha, separando cada campo por ponto e vírgula sem espaços, com quebra de linha para cada novo anúncio.
		<br />
		<br />
		<b>Exemplo:</b>
		<br />
		<br />
		123456;Título do seu anúncio (produto);Descrição do seu anúncio (produto);9,99;Marca do seu produto;1;1;http://www.seusite.com.br/seu_produto;http://www.seusite.com.br/seu_produto/imagem_do_produto.jpg;
		<br />
		<br />
		<b>Dicas:</b>		
		<br />
		<br />
		1) Separe cada campo com ponto e vírgula.
		<br />
		2) Não utilize espaços em branco antes e após o ponto e vírgula.
		<br />
		3) Coloque apenas um anúncio por linha.
		<br />
		4) Não deixe campos em branco, se não existir valor, utilize o valor zero.
		<br />
		5) Tente colocar sempre um código de referência para seus anúncios, isto evita que os mesmos sejam duplicados ao fazer uma nova importação. 
		<br />
		6) Caso queira adicionar mais imagens para o anúncio, adicione todas ao final da linha do mesmo anúncio, separando cada link da imagem por ponto e vírgula, sendo limitado a até 4 imagens para pessoa física e a até 8 imagens para pessoa jurídica. 
		<br />
		7) Não coloque mais do que 100 anúncios (linhas) em seu arquivo. 
		<br />
		8) Somente serão aceitos arquivos em formato TXT ou CSV, podendo o arquivo ter qualquer nome.
		<br />
		9) A importação de arquivos atualizados mantém seus anúncios publicados ou despublicados conforme o estoque informado.
		<br />
		10) Baixe os modelos de arquivo abaixo para basear a construção do arquivo que conterá os seus anúncios.
		<br />
		<br />
		<a href="https://www.giroferta.com/modelos/modelo.txt">Modelo TXT</a> e <a href="https://www.giroferta.com/modelos/modelo.csv">Modelo CSV</a>
		<br />
		<br />
		Caso tenha dúvidas entre em contato conosco, estamos a disposição para ajudá-lo.
		<br />
		<br />
		<br />
		<a href="${linkHome}"><fmt:message key="page.backHome" /></a>
		<br />
	</div>
	<br />
</div>
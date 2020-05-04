<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<nav>
  <ul class="pagination">
       <c:choose>
		<c:when test="${current > 0}">
		    <li>
      			<span aria-hidden="true">
					<a aria-label="Previous" href="${navActionFirst}"><fmt:message key="nav.first"/></a>					
				</span>
		    </li>
		    <li>
      			<span aria-hidden="true">
		    		<a aria-label="Previous" href="${navActionPrevious}"><fmt:message key="nav.previous"/></a>
		    	</span>
		    </li>
		</c:when>
		<c:otherwise>
			<li>
      			<span aria-hidden="true">
					<fmt:message key="nav.first"/>
				</span>
		    </li>
		    <li>
      			<span aria-hidden="true">
					<fmt:message key="nav.previous"/>
				</span>
		    </li>
		</c:otherwise>
	</c:choose>
    <li>
    	<a href="javascript:void(0);" style="cursor: default !important; color: black !important;">
    		<c:choose>
	    		<c:when test="${totalResults > 0}">
					<fmt:message key="nav.showingThe"/> ${(current + maximum) - (maximum - 1)} <fmt:message key="nav.to"/>
				</c:when>
				<c:otherwise>
					<fmt:message key="nav.showingThe"/> ${0} <fmt:message key="nav.to"/>
				</c:otherwise>
			</c:choose> 
			<c:choose>
				<c:when test="${(current + maximum) <= totalResults}">
					${current + maximum} <fmt:message key="nav.in"/> ${totalResults}
				</c:when>
				<c:otherwise>${totalResults} <fmt:message key="nav.in"/> ${totalResults}</c:otherwise>
			</c:choose>
		</a>
	</li>
       <c:choose>
		<c:when test="${fn:length(list) == maximum}"> 
			<c:choose>
				<c:when test="${(current + maximum) < totalResults}">
					<li>
       					<span aria-hidden="true">
							<a id="btNext" aria-label="Next" href="${navActionNext}"><fmt:message key="nav.next"/></a>
						</span>
 					</li>
					<c:choose>
						<c:when test="${(totalResults % maximum) == 0}">
							<li>
        						<span aria-hidden="true">
									<a aria-label="Next" href="${navActionLast}"><fmt:message key="nav.last"/></a>
								</span>
							</li>
						</c:when>
						<c:otherwise>
							<li>
        						<span aria-hidden="true">
									<a aria-label="Next" href="${navActionLast}"><fmt:message key="nav.last"/></a>
								</span>
							</li>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<li>
        				<span aria-hidden="true">
							<fmt:message key="nav.next"/>
						</span>
  						</li>
					<li>
   						<span aria-hidden="true">
							<fmt:message key="nav.last"/>
						</span>
 					</li>
				</c:otherwise>
			</c:choose> 
		</c:when>
		<c:otherwise>
			<li>
   				<span aria-hidden="true">
					<fmt:message key="nav.next"/>
				</span>
			</li>
			<li>
 				<span aria-hidden="true">
					<fmt:message key="nav.last"/>
				</span>
			</li>
		</c:otherwise>
	</c:choose>
  </ul>
</nav>
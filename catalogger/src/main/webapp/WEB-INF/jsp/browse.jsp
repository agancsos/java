<%@ page import="java.util.Date,java.util.Random,java.util.HashMap" %>
<%@ page import="com.catalogger.components.ApiHelpers,com.catalogger.components.ConsoleSession" %>
<%@ page import="com.catalogger.services.CustomerService,com.catalogger.services.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
	response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setDateHeader("Expires", -1);
	ConsoleSession consoleSession = ApiHelpers.extractConsoleSession(request);
%>
<jsp:include page="header.jsp"/>
<c:set var="objType" value="${param.objType}"/>
<c:set var="searchText" value="${param.s}"/>
<c:choose>
	<c:when test="${cookie['X-API-TOKEN'] == '' && (objType == 'Customers' || objType == 'Borrows')}">
		<div style='color: darkred;'>User is not authorized...</div>
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${fn:length(items) < 1}">
				No data to display... <br/>
    		</c:when> 
			<c:otherwise>
				<div class='grid-container'>
					<div class='grid-left'>
						<table class="plain-table">
						<c:forEach items="${items}" var="item">
							<c:choose>
								<c:when test="${objType == 'Borrows'}">
									<c:choose>
										<c:when test="${item.id == param.id}">
											<tr class="selected-page" onclick="window.location='${pageContext.request.contextPath}/console/browse/?objType=${objType}&s=${searchText}&custId=${param.custId}&id=${item.id}'">
										</c:when>
										<c:otherwise>
											<tr onclick="window.location='${pageContext.request.contextPath}/console/browse/?objType=${objType}&s=${searchText}&custId=${param.custId}&id=${item.id}'">
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${item.id == selectedItem.id}">
											<tr class="selected-page" onclick="window.location='${pageContext.request.contextPath}/console/browse/?objType=${objType}&s=${searchText}&id=${item.id}'">
										</c:when>
										<c:otherwise>
											<tr onclick="window.location='${pageContext.request.contextPath}/console/browse/?objType=${objType}&s=${searchText}&id=${item.id}'">
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
							<td>${item.label}</td>
							</tr>
						</c:forEach>
						</table>
					</div>
					<div class='grid-right'>
						<jsp:include page="resource-form.jsp"/>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>
<jsp:include page="footer.jsp"/>

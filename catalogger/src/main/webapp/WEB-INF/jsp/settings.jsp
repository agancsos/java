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
<c:choose>
    <c:when test="${cookie['X-API-TOKEN'] == '' && (objType == 'Customers' || objType == 'Borrows')}">
        <div style='color: darkred;'>User is not authorized...</div>
    </c:when>
    <c:otherwise>
        <c:choose>
            <c:when test="${fn:length(operations) < 1}">
                No data to display... <br/>
            </c:when>
            <c:otherwise>
                <div class='grid-container'>
                    <div class='grid-left'>
                        <table class="plain-table">
                        <c:forEach items="${operations}" var="operation">
                            <c:choose>
                                <c:when test="${operation == selectedOperation}">
                                    <tr class="selected-page" onclick="window.location='${pageContext.request.contextPath}/console/settings/?op=${operation}&s=${param.s}'">
                            	</c:when>
                                <c:otherwise>
                                    <tr onclick="window.location='${pageContext.request.contextPath}/console/settings/?op=${operation}&s=${param.s}'">
                                </c:otherwise>
                            </c:choose>
                            <td>${operation}</td>
                            </tr>
                        </c:forEach>
                        </table>
                    </div>
                    <div class='grid-right'>
						<jsp:include page="settings-form.jsp"/>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>
<jsp:include page="footer.jsp"/>

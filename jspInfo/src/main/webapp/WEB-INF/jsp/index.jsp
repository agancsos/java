<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.Date,java.util.Random" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
	response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setDateHeader("Expires", -1);
%>
<html>
	<head>
		<%
			Random rand = new Random();
        	String sessionToken = "";
            if (request.getCookies() != null) {
                for (Cookie c : request.getCookies()) {
                    if (c.getName().equals("X-API-TOKEN") && !c.getValue().equals("")) {
                        sessionToken = c.getValue();
                    }
                }
            }
		%>
		<title>jspInfo</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/main.css"/>
	</head>
	<body>
		<div id="banner">
			<div id="banner-inner">
				<div style="font-size: 30pt;" class="logo">jspInfo</div><br/>
			</div>
		</div>
		<div id="links">
			<div id="links-inner">
			</div>
		</div>
		<div id="main">
			<div id="main-inner">
				<table class="plain-table">
					<c:forEach items="${properties}" var="prop">
						<tr>
							<th>${prop.key}</th>
							<td>${prop.value}</td>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		<div id="footer">
			<div style="font-size: 8pt;" id="footer-inner">
				&copy; <%= (new Date()).getYear() + 1900 %> Abel Gancsos <br/>
				All Rights Reserved
			</div>
		</div>
	</body>
</html>


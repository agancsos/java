<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.Date,java.util.Random,com.requests.components.ApiHelpers,com.requests.models.User" %>
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
            String sessionToken = "";
            if (request.getCookies() != null) {
                for (Cookie c : request.getCookies()) {
                    if (c.getName().equals("X-API-TOKEN")) {
                        sessionToken = c.getValue();
                    }
                }
            }
        %>
		<title>PrayerRequests | Friend Approval</title>
		<%
            out.println(String.format("<link rel='stylesheet' href='/main.css?v=%d'/>", rand.nextInt(1, 20000)));
        %>
		<script type="text/javascript" src="/functions-all.js"></script>
	</head>
	<body>
		<div id="banner">
			<div id="banner-inner">
				<div style="font-size: 30pt;" class="logo">PrayerRequests</div><br/>
                <div style="font-size: 14pt;" class="motto">Cast all your cares upon HIM for HE careth for you...</div>
			</div>
		</div>
		<div id="links">
			<div id="links-inner">
				<table id = 'link-buttons'>
                <tr>
                <%
                    if (!sessionToken.equals("")) {
                        User user = ApiHelpers.extractUser(sessionToken);
                        out.println(String.format("<th style='text-align:left;'>Welcome back, <a href='/console/logout' title='Logout'>%s %s</a>", user.getFirstName(), user.getLastName()));
                    }
                %>
                <th onclick="ViewService.gotoPage('/console/')">Home</th>
                <%
                    if (!sessionToken.equals("")) {
                        User user = ApiHelpers.extractUser(sessionToken);
                        out.println(String.format("<th onclick=\"ViewService.gotoPage('/console/journal')\">Requests</th>"));
                        out.println(String.format("<th onclick=\"ViewService.gotoPage('/console/friends')\">Friends</th>"));
                    } else {
						out.println("<meta http-equiv='refresh' content='0; URL=/console/' />");
                    }
                %>
                </tr>
                </table>
			</div>
		</div>
		<div id="main">
			<div id="main-inner">
				<c:choose>
    				<c:when test="${user.userId == -1}">
						Invalid user... <br/>
    				</c:when>    
    				<c:otherwise>
						For your protection and to ensure that you know the person for this request, we ask that you verify their registered email address.<br/>
						<form method="POST" class="signup-form" action="./verify">
							<input type="hidden" name="userid" value="${user.userId}"/>
							<label for="verify-email">Email</label><input type="text" name="verify-email" id="verify-email" placeholder="johnd@gmail.com"/><br/>
							<input type="submit" name="verify-submit" value="Verify"/>
						</form>
    				</c:otherwise>
				</c:choose>
				<%
					
				%>
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

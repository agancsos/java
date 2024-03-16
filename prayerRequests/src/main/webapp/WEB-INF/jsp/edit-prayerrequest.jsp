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
			Random rand = new Random();
            String sessionToken = "";
            if (request.getCookies() != null) {
                for (Cookie c : request.getCookies()) {
                    if (c.getName().equals("X-API-TOKEN")) {
                        sessionToken = c.getValue();
                    }
                }
            }
        %>
		<title>PrayerRequests | Edit Request</title>
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
                        out.println(String.format("<th class=\"selected-page\" onclick=\"ViewService.gotoPage('/console/journal')\">Requests</th>"));
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
    				<c:when test="${prayerRequest.prayerRequestId == -1}">
						Invalid prayer request... <br/>
    				</c:when>    
    				<c:otherwise>
						<form id="add-request-form" method="POST" action="save-prayerrequest">
							<input type="hidden" name="requestId" value="${prayerRequest.prayerRequestId}"/>
                        	<textarea name="request" placeholder="Please enter your prayer request or praise">${prayerRequest.request}</textarea><br/>
							<c:choose>
								<c:when test="${prayerRequest.isPublic == true}">
									<label for="public">Public</label><input type="checkbox" name="public" id="public" checked value="1"/><br/>
								</c:when>
								<c:otherwise>
									<label for="public">Public</label><input type="checkbox" name="public" id="public" value="1"/><br/>
								</c:otherwise>
							</c:choose>
							<c:choose>
                                <c:when test="${prayerRequest.isShared == true}">
                                    <label for="shared">Shared</label><input type="checkbox" name="shared" id="shared" checked value="1"/><br/>
                                </c:when>
                                <c:otherwise>
                                    <label for="shared">Shared</label><input type="checkbox" name="shared" id="shared" value="1"/><br/>
                                </c:otherwise>
                            </c:choose>
                        	<input type="submit" name="add-request-submit" value="Save"/>
                    	</form>
    				</c:otherwise>
				</c:choose>
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

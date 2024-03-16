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
		<title>PrayerRequests | Friends</title>
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
                        out.println(String.format("<th class=\"selected-page\" onclick=\"ViewService.gotoPage('/console/friends')\">Friends</th>"));
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
    				<c:when test="${fn:length(results) < 1}">
						No friends to display... <br/>
    				</c:when>
					<c:otherwise> 
						<table id="requests-table">
                        <tr>
                        	<th>User</th>
                        	<th>Action</th>
                        </tr>
						<c:forEach items="${results}" var="friend">
							<tr>
								<c:set var="comps" value="${fn:split(friend, ':')}"/>
								<c:choose>
									<c:when test="${comps[2] == 1}">
										<c:choose>
											<c:when test="${comps[4] == 0}">
												<td><a href="./request?req=${comps[0]}">${comps[3]}</a></td>
												<td><a href="./remove?user=${comps[0]}">Remove Friend</a></td>
											</c:when>
											<c:otherwise>
												<td><a href="./request?req=${comps[1]}">${comps[3]}</a></td>
												<td><a href="./remove?user=${comps[1]}">Remove Friend</a></td>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>
										<c:choose>
                                            <c:when test="${comps[4] == 0}">
                                                <td>${comps[3]}</td>
                                                <td><a href="./approve?user=${comps[0]}">Accept</a></td>
                                            </c:when>
                                            <c:otherwise>
                                                <td>${comps[3]}</td>
                                                <td><a href="./approve?user=${comps[1]}">Accept</a></td>
                                            </c:otherwise>
                                        </c:choose>
									</c:otherwise>
								</c:choose>
							</tr>
						</c:forEach>
                        </table>	
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

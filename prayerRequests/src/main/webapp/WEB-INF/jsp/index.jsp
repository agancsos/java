<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.Date,java.util.Random,com.requests.components.ApiHelpers,com.requests.models.User" %>
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
		<title>PrayerRequests</title>
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
                <th class="selected-page" onclick="ViewService.gotoPage('/console/')">Home</th>
				<%
					if (!sessionToken.equals("")) {
						User user = ApiHelpers.extractUser(sessionToken);
						out.println(String.format("<th  onclick=\"ViewService.gotoPage('/console/journal')\">Requests</th>"));
						out.println(String.format("<th onclick=\"ViewService.gotoPage('/console/friends')\">Friends</th>"));
					} else {
						out.println("<th>");
						out.println("<form id='login-form' method='POST' action='/console/login'>");
						out.println("<input type='text' name='login-username' placeholder='johnd@gmail.com'/>");
						out.println("<input type='password' name='login-password' placeholder='*********'/>");
						out.println("<input type='submit' name='login-submit' value='Login'/>");
						out.println("</form>");
						out.println("</th>");
					}
				%>
				</tr>
				</table>
			</div>
		</div>
		<div id="main">
			<div id="main-inner">
				<%
					if (!sessionToken.equals("")) {
					} else {
						out.println("<div id=\"welcome-div\">");
						out.println("</div>");
						out.println("<div id=\"signup-div\">");
							out.println("<form class=\"signup-form\" method=\"POST\" action=\"/console/register\">");
								out.println("<label for=\"firstname-input\">First Name</label><input type=\"text\" name=\"firstname-input\" id=\"firstname-input\" placeholder=\"John\"/><br/>");
								out.println("<label for=\"lastname-input\">Last Name</label><input type=\"text\" name=\"lastname-input\" id=\"lastname-input\" placeholder=\"Davies\"/><br/>");
								out.println("<label for=\"email-input\">Email</label><input type=\"text\" name=\"email-input\" id=\"email-input\" placeholder=\"johnd@gmail.com\"/><br/>");
								out.println("<label for=\"password-input\">Password</label><input type=\"password\" name=\"password-input\" id=\"password-input\" placeholder=\"*********\"/><br/>");
								out.println("<input type=\"submit\" name=\"signup-submit\" value=\"Signup\"/>");
							out.println("</form>");
						out.println("</div>");
					}
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

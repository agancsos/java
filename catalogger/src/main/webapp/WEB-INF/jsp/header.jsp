<%@ page import="java.util.Date,java.util.Random,java.util.HashMap,com.catalogger.gui.components.LinkController" %>
<%@ page import="com.catalogger.services.CustomerService,com.catalogger.services.BorrowService,java.util.ArrayList" %>
<%@ page import="com.catalogger.components.ApiHelpers,com.catalogger.components.ConsoleSession,com.catalogger.services.AuthenticationService" %>
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
			Random rand                       = new Random();
			ConsoleSession consoleSession     = ApiHelpers.extractConsoleSession(request);
			AuthenticationService authService = AuthenticationService.getInstance();
		%>
		<title>Catalogger</title>
		<%
			out.print(String.format("<link rel=\"stylesheet\" href=\"%s/main.css?id=%d\"/>", request.getContextPath(), rand.nextInt(9999)));
		%>
		<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/functions-all.js"></script>
	</head>
	<body>
		<div id="banner">
			<div id="banner-inner">
				<div style="font-size: 30pt;" class="h1">Catalogger</div><br/>
			</div>
		</div>
		<div id="links">
			<div id="links-inner">
				<table id = 'link-buttons'>
					<tr>
					<%
						ArrayList<LinkController> links = new ArrayList<LinkController>();
						if (consoleSession.getSessionUser() != null) {
							links.add(new LinkController(String.format("Welcome back, <a href='%s/console/signout' class='signout-href'>%s %s</a>",
								request.getContextPath(), consoleSession.getSessionUser().getFirstName(), consoleSession.getSessionUser().getLastName()), "", ""));
						}
						links.add(new LinkController("Home", "Home", "/console"));
						String[] objTypes = { "Authors", "Publishers", "Titles", "Customers" };

						for (String objType : objTypes) {
							links.add(new LinkController(objType, objType, String.format("/console/browse/?objType=%s", objType)));
						}
						links.add(new LinkController("Settings", "Settings", "/console/settings/"));
                        links.add(new LinkController("Admin", "Admin", "/console/admin/"));
                        links.add(new LinkController("Search", "Search", ""));

						if (consoleSession.getSessionToken().equals("")) {
							out.print("<th>");
							out.print(String.format("<form id='login-form' method='POST' action='%s/console/setter'>", request.getContextPath()));
							out.print("<input type='text' name='username-in' placeholder='jdoe'/>");
							out.print("<input type='password' name='password-in' placeholder='********'/>");
							out.print("<input type='submit' name='signin-submit' value='Login'/>");
							out.print("</form>");
							out.print("</th>");
						}

						for (LinkController link : links) {
							if (consoleSession.getSessionUser() == null  && !link.getLabel().equals("Home") && !link.getLabel().equals("Feedback") && !link.getPath().contains("objType") && !link.getLabel().equals("Search")) {
								continue;
							} else if (link.getLabel().equals("Admin") && (consoleSession.getSessionUser() == null || !authService.isGroupMember(consoleSession.getSessionUser(), 2))) {
								continue;
							} else if (consoleSession.getSessionUser() == null && (link.getPath().toLowerCase().contains("customer") || link.getPath().toLowerCase().contains("borrows"))) {
								continue;
							} else if (consoleSession.getSessionUser() == null && link.getLabel().equals("Settings")) {
								continue;
							}
							out.print("<th");
							if (!link.getLabel().equals("Search")) {
								out.print(String.format(" onclick=\"ViewService.gotoPage('%s%s')\"", request.getContextPath(), link.getPath()));
								if (link.getName().toLowerCase().equals(String.format("%s", consoleSession.getPageName())) || link.getName().equals("Home") && consoleSession.getPageName().equals("index")
									|| (link.getPath().contains("browse") && request.getParameterMap().containsKey("objType") && link.getPath().contains(request.getParameter("objType")) )) {
									out.print(" class='selected-page' ");
								}
								out.print(String.format(">%s", link.getLabel()));
							} else {
								out.print("><form id='search-form' method='GET'>");
								if (request.getParameterMap().containsKey("op")) {
									out.print(String.format("<input type='hidden' name='op' value='%s'/>", request.getParameter("op").toString()));
								}
								if (request.getParameterMap().containsKey("id")) {
                                    out.print(String.format("<input type='hidden' name='id' value='%s'/>", request.getParameter("id").toString()));
                                }
								out.print(String.format("<input type='text' name='s' value='%s' placeholder='Search....'/>", consoleSession.getSearchText()));
								out.print("</form>");
							}
							out.print("</th>");
						}
					%>
					</tr>
				</table>
			</div>
		</div>
		<div id="main">
			<div id="main-inner">

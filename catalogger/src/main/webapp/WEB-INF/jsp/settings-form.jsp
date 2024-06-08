<%@ page import="java.util.Date,java.util.Random,java.util.HashMap,com.catalogger.gui.components.LinkController" %>
<%@ page import="com.catalogger.services.CustomerService,com.catalogger.services.BorrowService,java.util.ArrayList" %>
<%@ page import="com.catalogger.components.ApiHelpers,com.catalogger.components.ConsoleSession,com.catalogger.services.*" %>
<%@ page import="com.catalogger.gui.components.FormField,com.catalogger.components.FormHelpers" %>
<%@ page import="com.catalogger.models.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
	response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setDateHeader("Expires", -1);
%>
<%
	ConsoleSession consoleSession     = ApiHelpers.extractConsoleSession(request);
	AuthenticationService authService = AuthenticationService.getInstance();
%>
<%
	if (request.getAttribute("selectedOperation") == null) {
		out.print("No item selected");
	} else {
		if (((FormField[])request.getAttribute("fields")).length > 0) {
			out.print(String.format("<form class='plain-form' method='POST' action='%s/console/settings/save?op=%s'>", request.getContextPath(), request.getParameter("op").toString()));
			try {
				for (FormField field : (FormField[])request.getAttribute("fields")) {
					if (consoleSession.getSessionUser() == null) {
						field.setEditable(false);
					}
					out.print(String.format("<label for='%s'>%s</label>", field.getLabel(), field.getLabel().toUpperCase()));
					out.print(field.toString());
				}
			} catch (Exception ex) {
				out.print(String.format("<div style='color=darkred;'>%s</div>", ex.getMessage()));
			}
			if ((boolean)request.getAttribute("submitEnabled")) {
				out.print("<input type='submit' name='save-submit' value='Save'/>");
			}	
			out.print("</form>");
		}
	}
%>

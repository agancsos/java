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
	if (request.getAttribute("selectedItem") == null) {
		out.print("No item selected");
	} else {
		out.print(String.format("<form class='plain-form' method='POST' action='%s/console/browse/save?objType=%s&id=%s'>",
			request.getContextPath(), request.getParameter("objType"), request.getParameter("id")));
		try {
			for (FormField field : FormHelpers.getFields(consoleSession.getSessionUser(), (Item)request.getAttribute("selectedItem"))) {
				if (consoleSession.getSessionUser() == null) {
					field.setEditable(false);
				}
				out.print(String.format("<label for='%s'>%s</label>", field.getLabel(), field.getLabel().toUpperCase()));
				out.print(field.toString());
			}
		} catch (Exception ex) {
			out.print(String.format("<div style='color=darkred;'>%s</div>", ex.getMessage()));
		}
		if (consoleSession.getSessionUser() != null && ((Item)request.getAttribute("selectedItem")).getObjectType() == 6) {
			HashMap<String, Object> lateInfo = new HashMap<String, Object>();
			lateInfo = BorrowService.getInstance().isLate((Borrow)request.getAttribute("selectedItem"));
			out.print("<label for='daysLate'>DAYSLATE</label>");
            out.print(String.format("<input type='text' disabled readonly value='%d'/>", (int)lateInfo.get("days")));
            out.print("<br/>");
		}
        if (consoleSession.getSessionUser() != null && ((Item)request.getAttribute("selectedItem")).getObjectType() != 6) {
			if (request.getParameter("objType").equals("Customers") && ((Item)request.getAttribute("selectedItem")).getId() > 0) {
				out.print("<input type='submit' class='view-borrows-button' name='borrows-submit' value='View Borrows'/>");
			}
            out.print("<input type='submit' name='save-submit' value='Save'/>");
            out.print("<input type='submit' name='delete-submit' class='del-button' value='Delete'/>");
        } else if (consoleSession.getSessionUser() != null && ((Item)request.getAttribute("selectedItem")).getObjectType() == 6) {
			if (((Borrow)request.getAttribute("selectedItem")).getCheckinDate().equals("")) {
				out.print("<input type='submit' name='checkin-button' value='Checkin'/>");
			}
			if (((Borrow)request.getAttribute("selectedItem")).getLate()) {
               	out.print("<input type='submit' name='paid-button' value='Mark Paid'/>");
			}
		}
		out.print("</form>");
	}
%>

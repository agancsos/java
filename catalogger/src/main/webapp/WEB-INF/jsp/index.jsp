<%@ page import="java.util.Date,java.util.Random,java.util.HashMap" %>
<%@ page import="com.catalogger.services.CustomerService,com.catalogger.services.BorrowService" %>
<%@ page import="com.catalogger.components.ApiHelpers,com.catalogger.components.ConsoleSession" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
	response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setDateHeader("Expires", -1);
	ConsoleSession consoleSession = ApiHelpers.extractConsoleSession(request);
%>
<jsp:include page="header.jsp"/>
<%
	if (consoleSession.getSessionUser() != null) {
		BorrowService borrowService = BorrowService.getInstance();
		CustomerService custService = CustomerService.getInstance();
		Object[] borrows            = borrowService.getLateBorrows();
		out.print("<div class='grid-container'>");
       	out.print("<div class='grid-left'>");
		if (borrows.length > 0) {
       		out.print("<table class='plain-table'>");
			out.print("<tr><th>TITLE</th><th>CUSTOMER</th><th>CHECKOUT</th><th>CHECKIN</th><th>DAYS LATE</th></tr>");
       		for (Object object : borrows) {
       			out.print("<tr>");
       			out.print(String.format("<td>%d</td>", ((HashMap<String, Object>)object).get("id")));
				out.print(String.format("<td>%d</td>", ((HashMap<String, Object>)object).get("customerId")));
				out.print(String.format("<td>%s</td>", ((HashMap<String, Object>)object).get("checkoutDate")));
				out.print(String.format("<td>%s</td>", ((HashMap<String, Object>)object).get("checkinDate")));
				out.print(String.format("<td>%s</td>", ((HashMap<String, Object>)object).get("days")));
				out.print("</tr>");
       		}
       		out.print("</table>");
		}
       	out.print("</div>");
       	out.print("<div class='grid-right'>");
		out.print(String.format("<form method='POST' class='plain-form' action='%s/console/checkout'>", request.getContextPath()));
		out.print("<textarea name='titles-input' placeholder='List of ISBNs'></textarea>");
		out.print("<label for='customer-input'>Customer</label><input type='text' name='customer-input' id='customer-input'/>");
		out.print("<input type='submit' name='borrow-submit' value='Checkout'/>");
		out.print("</form>");
       	out.print("</div>");
		out.print("</div>");
	}
%>
<jsp:include page="footer.jsp"/>

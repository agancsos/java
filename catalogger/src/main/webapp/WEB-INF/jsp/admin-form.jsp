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
		String operation = (String)request.getAttribute("selectedOperation");
		if (operation.equals("Log Viewer")) {
			out.print("<table class='plain-table'>");
			out.print("<tr>");
			for (String header : "ID,Level,Category,Message,Date".split(",")) {
				out.print(String.format("<th>%s</th>", header.toUpperCase()));
			}
			out.print("</tr>");
			for (Message msg : ((Message[])request.getAttribute("messages"))) {
				if (!consoleSession.getSearchText().equals("") && (!String.format("%s", TraceLevel.values()[msg.getLevel()]).contains(consoleSession.getSearchText())
					&& !String.format("%s", TraceCategory.values()[msg.getCategory()]).contains(consoleSession.getSearchText())
					&& !msg.getText().contains(consoleSession.getSearchText())
					&& !msg.getLastUpdatedDate().contains(consoleSession.getSearchText()))) {
					continue;
				}
				out.print("<tr>");
				out.print(String.format("<td>%d</td>", msg.getId()));
				out.print(String.format("<td>%s</td>", TraceLevel.values()[msg.getLevel()]));
				out.print(String.format("<td>%s</td>", TraceCategory.values()[msg.getCategory()]));
				out.print(String.format("<td>%s</td>", msg.getText()));
				out.print(String.format("<td>%s</td>", msg.getLastUpdatedDate()));
				out.print("</tr>");
			}
			out.print("</table>");
		} else if (operation.equals("Users")) {
			out.print("<table class='form-table'>");
			out.print(String.format("<tr><form method='POST' action='%s/console/admin/save?op=%s'>", request.getContextPath(), request.getParameter("op").toString()));
			out.print("<td><input type='hidden' name='cat' value='users'/><input type='text' name='new-user-name' placeholder='jdoe' value='' required /></td>");
			out.print("<td><input type='password' name='new-user-pass' placeholder='********' value='' required/></td>");
			out.print("<td><input type='text' name='new-user-fname' placeholder='John' value='' required></td>");
			out.print("<td><input type='text' name='new-user-lname' placeholder='Doe' value='' required></td>");
			out.print("<td colspan='4'><input type='submit' name='new-user-add' class='add-button'  value='NEW OBJECT'/></form></td>");
			out.print("</tr>");
			out.print("<tr><th>Username</th><th>Last Updated</th><th>Locked</th><th>Group Membership</th><th>Reset Password</th><th>Delete</th></tr>");
			for (User user : (User[])request.getAttribute("users")) {
				if (!consoleSession.getSearchText().equals("") && !user.getEmail().equals(consoleSession.getSearchText())) {
					continue;
				}
				out.print("<tr");
				if (request.getParameterMap().containsKey("id") && Integer.parseInt(request.getParameter("id").toString()) == user.getId()) {
					out.print(" class='selected-page'");
				}
				out.print(String.format("<td><form method='POST' class='form-table' action='%s/console/admin/save?op=%s'>", request.getContextPath(), request.getParameter("op").toString()));
				out.print(String.format("<input type='hidden' name='user-id' value='%s'/><td>%s</td>", user.getEmail(), user.getEmail()));
				out.print(String.format("<td>%s</td><td>%s</td><td><input type='submit' name='user-showgroups' value='GROUP MEMBERSHIP'/></td>", user.getLastUpdatedDate(), (user.getState() == 3 ? "LOKED" : "NOT LOCKED")));
				out.print("<td><input type='submit' name='user-reset-password' value='RESET PASSWORD'/></td><td><input type='submit' name='user-delete' value='DELETE' class='del-button'/></td>");
				out.print("</tr>");
			}
			out.print("</table>");

			if (request.getParameterMap().containsKey("u")) {
				User user2 = authService.getUserByName(request.getParameter("u").toString());
				out.print("<br /><h3>Group Membership</h3><hr/>");
				out.print("<table class='form-table'>");
				for (String group : (String[])request.getAttribute("groups")) {
					String[] pairs = group.split(":");
					out.print(String.format("<tr><form method='POST' action='%s/console/admin/save?op=%s'>", request.getContextPath(), request.getParameter("op").toString()));
					out.print(String.format("<td><input type='hidden' name='group-id' value='%s'/><input type='hidden' name='user-id' value='%d'/>%s</td><td><input type='submit' name='switch' value='%s'/></form></td>",
						pairs[0], user2.getId(), pairs[1], (authService.isGroupMember(user2, Integer.parseInt(pairs[0])) ? "REMOVE" : "ADD")));
					out.print("</tr>");
				}
				out.print("</table>");
			}
		} else if (operation.equals("Groups")) {
			String[] internalGroups = {
				"ADMINISTRATORS",
				"DEVELOPER",
				"USERS",
				"DBA"
			};
			out.print("<table class='form-table'>");
			out.print(String.format("<tr><form method='POST' action='%s/console/admin/save?op=%s'>", request.getContextPath(), request.getParameter("op").toString()));
			out.print("<td><input type='text' name='group-name' placeholder='ADMIN' required/></td><td><input type='submit' value='NEW GROUP' class='add-button' name='add-submit'/></form></td></tr>");
			for (String group : (String[])request.getAttribute("groups")) {
				String[] pairs = group.split(":");
				if (!consoleSession.getSearchText().equals("") && !pairs[1].contains(consoleSession.getSearchText())) {
                    continue;
                }
				out.print("<tr>");
				out.print(String.format("<tr><form method='POST' action='%s/console/admin/save?op=%s'>", request.getContextPath(), request.getParameter("op").toString()));
				out.print(String.format("<td><input type='hidden' name='group-id' value='%s'>%s</td>", pairs[0], pairs[1]));
				out.print("<td><input type='submit' class='del-button' value='DELETE' name='del-submit'");
				if (ApiHelpers.stringListContains(internalGroups, pairs[1])) {
					out.print(" disabled ");
				}
				out.print("/>");
				out.print("</form></td>");
				out.print("</tr>");
				out.print("</tr>");
			}
			out.print("</table>");
		} else if (operation.equals("Purge Audits")) {
			out.print(String.format("<form class='plain-form' method='POST' action='%s/console/admin/save?op=%s'>", request.getContextPath(), request.getParameter("op").toString()));		
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
            out.print("<input type='submit' name='save-submit' value='Purge Audits'/>");
            out.print("</form>");				
		}
	}
%>

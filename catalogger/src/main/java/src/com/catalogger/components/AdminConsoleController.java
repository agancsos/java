package com.catalogger.components;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.catalogger.services.*;
import com.catalogger.models.*;
import com.catalogger.gui.components.FormField;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

@Controller
@RequestMapping(value="/console/admin")
class AdminConsoleController {
	private AuthenticationService authService       = null;

	@Autowired
	public AdminConsoleController() {
		this.authService    = AuthenticationService.getInstance();
	}

	@RequestMapping(value="/")
    public String landing(Model model, HttpServletRequest request) {
		ArrayList<FormField> fields   = new ArrayList<FormField>();
		ConsoleSession consoleSession = ApiHelpers.extractConsoleSession(request);
        String[] operations           = {
            "Log Viewer",
            "Users",
            "Groups",
			"Purge Audits"
        };
        model.addAttribute("operations", operations);
        String selectedOperation = operations[0];
        if (request.getParameterMap().containsKey("op")) {
            selectedOperation = request.getParameter("op");
        }

        switch (selectedOperation) {
			case "Log Viewer":
				ArrayList<Message> messages = new ArrayList<Message>();
				messages = DbTraceService.getInstance().getMessages();
				model.addAttribute("messages", messages.toArray(new Message[messages.size()]));
				break;
			case "Users":
				model.addAttribute("groups", this.authService.getGroups());
				model.addAttribute("users", this.authService.getUsers());
				break;
			case "Groups":
				model.addAttribute("groups", this.authService.getGroups());
				break;
			case "Purge Audits":
				String[] fs = {
					"Days"
				};
				for (String f : fs) {
					FormField temp = new FormField("<input type='text'", f, "30");
					temp.setEditable(true);
					fields.add(temp);
				}
				break;
            default: break;
        }

        model.addAttribute("selectedOperation", selectedOperation);
        model.addAttribute("fields", fields.toArray(new FormField[fields.size()]));
        return "admin";
    }

	@RequestMapping(value="/save", method=RequestMethod.POST)
    public String saveSettings(Model model, HttpServletRequest request, @RequestParam("op") String operation, @CookieValue("X-API-TOKEN") String token) {
        ConsoleSession consoleSession = ApiHelpers.extractConsoleSession(request);
        switch (operation) {
            case "Users":
				if (request.getParameterMap().containsKey("new-user-add")) {
					String email = request.getParameter("new-user-name").toString();
					String password = SecurityService.getBase64Encoded(request.getParameter("new-user-pass").toString());
					String firstName = request.getParameter("new-user-fname").toString();
					String lastName = request.getParameter("new-user-lname").toString();
					try {
						this.authService.register(firstName, lastName, email, password);
					} catch (Exception ex) {
					}
                } else if (request.getParameterMap().containsKey("user-showgroups")) {
					return String.format("redirect:/console/admin/?op=%s&u=%s", operation, request.getParameter("user-id").toString());
				} else if (request.getParameterMap().containsKey("user-reset-password")) {
					User user   = this.authService.getUser(Integer.parseInt(request.getParameter("user-id").toString()));
					this.authService.resetPassword(user);
				} else if (request.getParameterMap().containsKey("switch")) {
					String switchOperation = request.getParameter("switch").toString();
					int groupId = Integer.parseInt(request.getParameter("group-id").toString());
					User user   = this.authService.getUser(Integer.parseInt(request.getParameter("user-id").toString()));
					if (switchOperation.equals("REMOVE")) {
						this.authService.removeGroupMember(user, groupId);
					} else if (switchOperation.equals("ADD")) {
						this.authService.addGroupMember(user, groupId);
					}
                } else if (request.getParameterMap().containsKey("user-delete")) {
					User user   = this.authService.getUser(Integer.parseInt(request.getParameter("user-id").toString()));
					this.authService.removeUser(user);
				}
                break;
            case "Groups":
				if (request.getParameterMap().containsKey("del-submit")) {
					int groupId = Integer.parseInt(request.getParameter("group-id").toString());
					this.authService.removeGroup(groupId);
				} else if (request.getParameterMap().containsKey("del-submit")) {
					String groupName = request.getParameter("group-name").toString();
					this.authService.addGroup(groupName);
				}
                break;
            case "Purge Audits":
				int days = Integer.parseInt(request.getParameter("Days").toString());
				DbTraceService.getInstance().purgeAudits(days);
                break;
            default: break; 
        }           
        return String.format("redirect:/console/admin/?op=%s", operation);
    }
}



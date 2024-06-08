package com.catalogger.components;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.catalogger.services.AuthenticationService;
import com.catalogger.services.SecurityService;
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
import java.time.LocalDateTime;
import org.json.JSONObject;

@Controller
@RequestMapping(value="/console/settings")
class SettingsConsoleController {
	private AuthenticationService authService       = null;

	@Autowired
	public SettingsConsoleController() {
		this.authService    = AuthenticationService.getInstance();
	}

	@RequestMapping(value="/")
    public String landing(Model model, HttpServletRequest request, @CookieValue("X-API-TOKEN") String token) {
		ArrayList<FormField> fields   = new ArrayList<FormField>();
		ConsoleSession consoleSession = ApiHelpers.extractConsoleSession(request);
		String[] operations           = {
			"Password",
			"Notifications",
			"Session"
		};
		model.addAttribute("operations", operations);
		String selectedOperation = operations[0];
		if (request.getParameterMap().containsKey("op")) {
			selectedOperation = request.getParameter("op");
		}

		switch (selectedOperation) {
			case "Password":
				model.addAttribute("submitEnabled", true);
				String[] fs = {
					"Current",
					"New",
					"Confirm"
				};
				for (String f : fs) {
                    FormField temp = new FormField("<input type='password'", f, "");
                    temp.setEditable(true);
                    fields.add(temp);
                }
				break;
			case "Notifications":
				model.addAttribute("submitEnabled", true);
				String[] notifications = {
					"Daily Report"
				};
				for (String f : notifications) {
					String value = "false";
					if (consoleSession.getSessionUser() != null && consoleSession.getSessionUser().getProperties() != null && consoleSession.getSessionUser().getProperties().has(f)) {
						value = (consoleSession.getSessionUser().getProperties().getBoolean(f) ? "true" : "false");
					}
                    FormField temp = new FormField("<input type='checkbox'", f, value);
                    temp.setEditable(true);
                    fields.add(temp);
                }
				break;
			case "Session":
				model.addAttribute("submitEnabled", false);
				HashMap<String, String> props = new HashMap<String, String>();
				props.put("Token", token);
				props.put("Session", request.getSession().getId());
				props.put("Now", LocalDateTime.now().toString());
				for (HashMap.Entry<String, String> f : props.entrySet()) {
					FormField temp = new FormField("<input type='text'", f.getKey(), f.getValue());
					temp.setEditable(false);
					fields.add(temp);
				} 
				break;
			default: break;
		}

		model.addAttribute("selectedOperation", selectedOperation);
		model.addAttribute("fields", fields.toArray(new FormField[fields.size()]));
        return "settings";
    }

	@RequestMapping(value="/save", method=RequestMethod.POST)
    public String saveSettings(Model model, HttpServletRequest request, @RequestParam("op") String operation, @CookieValue("X-API-TOKEN") String token) {
		ConsoleSession consoleSession = ApiHelpers.extractConsoleSession(request);
		switch (operation) {
			case "Password":
				String tempCurrent = SecurityService.getBase64Encoded(request.getParameter("current").toString());
				String tempNew     = SecurityService.getBase64Encoded(request.getParameter("new").toString());
				String tempConfirm = SecurityService.getBase64Encoded(request.getParameter("confirm").toString());
				if (consoleSession.getSessionUser().getToken().equals(tempCurrent) && tempNew.equals(tempConfirm)) {
					this.authService.updatePassword(consoleSession.getSessionUser(), tempNew);
				}				
				break;
			case "Notifications":
				JSONObject obj   = new JSONObject();
        		for (HashMap.Entry<String, String[]> param : request.getParameterMap().entrySet()) {
            		if (param.getKey().equals("save-submit") || param.getKey().equals("op")) {
                		continue;
            		}
            		try {
                		obj.put(param.getKey(), Integer.parseInt(param.getValue()[0]));
            		} catch (Exception ex) {
						if (param.getValue()[0].equals("on")) {
							obj.put(param.getKey(), true);
						} else {
                			obj.put(param.getKey(), param.getValue()[0]);
						}
            		}
        		}
                this.authService.updateProperties(consoleSession.getSessionUser(), obj.toString());
				break;
			default: break;
		}
		return String.format("redirect:/console/settings/?op=%s", operation);
	}
}



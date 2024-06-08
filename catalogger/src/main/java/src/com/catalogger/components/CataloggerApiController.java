package com.catalogger.components;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import com.catalogger.models.*;
import com.catalogger.SR;
import com.catalogger.services.TitleService;
import com.catalogger.services.DbTraceService;
import com.catalogger.services.AuthenticationService;
import com.catalogger.services.DataService;
import com.helpers.SystemHelpers;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value="/api")
class CataloggerApiController {
	private AuthenticationService authService = null;

	@Autowired
	public CataloggerApiController() {
		this.authService = AuthenticationService.getInstance();
	}


	@GetMapping(value="/", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String home(HttpServletRequest request) {
		return "{\"result\": \"Pong\"}";
	}

	@GetMapping(value="/version", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String version(HttpServletRequest request) {
		return String.format("\"version\": \"%s\"}", SR.applicationVersion);
    }

	@GetMapping(value="/messages", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getMessages(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token) {
		try {
			ApiHelpers.ensureAuthenticated(token);
			if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
				return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
			}
			String rst = "{\"result\":[";
			ArrayList<Message> messages = DbTraceService.getInstance().getMessages();
			for (int i = 0; i < messages.size(); i++) {
				if (i > 0) { rst += ","; }
				rst += messages.get(i).toJsonString();
			}
			rst += "]}";
			return rst;
		} catch(Exception ex) {
			return String.format("{\"message\": \"%s\"}", ex.getMessage());
		}
	}

	@GetMapping(value="/heartbeat", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String heartbeats(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token) {
		User user = ApiHelpers.extractUser(token);
 		DataService.getInstance().runServiceQuery(String.format("UPDATE USERS SET LAST_UPDATED_DATE = CURRENT_TIMESTAMP WHERE USER_ID = '%d'", user.getId()));
		return "{\"result\": 0}";       
    }

	@GetMapping(value="/create", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String createObject(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestParam("type") String objectType) {
		try {
            ApiHelpers.ensureAuthenticated(token);
			switch (objectType) {
            	case "author":
                	return (new Author()).toJsonString();
            	case "publisher":
                	return (new Publisher()).toJsonString();
            	case "title":
                	Title rst = new Title();
                	rst.setAuthor(new Author());
                	rst.setPublisher(new Publisher());
                	return rst.toJsonString();
            	case "title_copy":
                	return (new TitleCopy()).toJsonString();
            	case "borrow":
                	return (new Borrow()).toJsonString();
            	case "customer":
                	return (new Customer()).toJsonString();
            	default:
                	return "{}";
        	}
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

	@GetMapping(value="/isbn", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String checkISBN(HttpServletRequest request, @RequestParam("isbn") String isbn) {
		return TitleService.getInstance().isbnLookup(isbn).toJsonString();
	}

	@GetMapping(value="/instance", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getServerInstance(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token) {
		try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
				return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
			HashMap<String, String> commands = new HashMap<String, String>();
			commands.put("GO VERSION", "go version");
			commands.put("HOSTNAME", "hostname");
			commands.put("#CONTAINER", "ps -ax");
			String rst = "{";
			int i = 0;
			for (HashMap.Entry<String, String> command : commands.entrySet()) {
				if (i > 0) { rst += ","; }
				try {
					if (command.getKey().substring(0, 1).equals("#")) {
						rst += String.format("\"%s\": %t", command.getKey().replace("#", ""), (SystemHelpers.runCmd(command.getValue()).split("\n").length > 4));
					} else {
						rst += String.format("\"%s\": \"%s\"", command.getKey(), SystemHelpers.runCmd(command.getValue()).replace("\n", "\\n"));
					}
				} catch (Exception ex) {
				}
				i++;
			}
			rst += "}";
            return rst;
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }
}



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
import javax.servlet.http.HttpServletRequest;
import com.catalogger.models.*;
import com.catalogger.SR;
import com.catalogger.services.PublisherService;
import com.catalogger.services.AuthenticationService;
import com.catalogger.services.DbTraceService;

@RestController
@RequestMapping(value="/api/publishers")
class PublisherApiController {
	private PublisherService pubService        = null;
	private AuthenticationService authService  = null;

	@Autowired
	public PublisherApiController() {
		this.pubService  = PublisherService.getInstance();
		this.authService = AuthenticationService.getInstance();
	}


	@GetMapping(value="/", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String home(HttpServletRequest request) {
		return "{\"result\": \"Pong\"}";
	}

	@GetMapping(value="/get", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getPublisher(HttpServletRequest request, @RequestParam("id") int id) {
		return this.pubService.getPublisher(id, true).toJsonString();
    }

	@GetMapping(value="/list", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	String listPublishers(HttpServletRequest request) {
        String rst = "{\"result\":[";
        Publisher[] publishers = this.pubService.getPublishers(true);
        for (int i = 0; i < publishers.length; i++) {
            if (i > 0) { rst += ","; }
            rst += publishers[i].toJsonString();
        }
        rst += "]}";
		return rst;
	}

	@GetMapping(value="/add", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String addPublisher(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
		try {
			ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
			Publisher temp = new Publisher();
            temp.reloadFromJson(raw);
            return String.format("{\"result\": %d}", (this.pubService.addPublisher(temp) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

	@GetMapping(value="/update", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String updatePublisher(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
		try {
			ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
			Publisher temp = new Publisher();
            temp.reloadFromJson(raw);
            return String.format("{\"result\": %d}", (this.pubService.modifyPublisher(temp) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

	@GetMapping(value="/remove", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String removePublisher(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
		try {
			ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
			Publisher temp = new Publisher();
            temp.reloadFromJson(raw);
            return String.format("{\"result\": %d}", (this.pubService.removePublisher(temp) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }
}



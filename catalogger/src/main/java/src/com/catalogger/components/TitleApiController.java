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
import com.catalogger.services.AuthorService;
import com.catalogger.services.TitleService;
import com.catalogger.services.PublisherService;
import com.catalogger.services.AuthenticationService;
import com.catalogger.services.DbTraceService;

@RestController
@RequestMapping(value="/api/titles")
class TitleApiController {
	private AuthorService authorService        = null;
	private PublisherService pubService        = null;
	private TitleService titleService          = null;
	private AuthenticationService authService  = null;

	@Autowired
	public TitleApiController() {
		this.authorService = AuthorService.getInstance();
		this.pubService    = PublisherService.getInstance();
		this.titleService  = TitleService.getInstance();
		this.authService   = AuthenticationService.getInstance();
	}


	@GetMapping(value="/", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String home(HttpServletRequest request) {
		return "{\"result\": \"Pong\"}";
	}

	@GetMapping(value="/get", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getTitle(HttpServletRequest request, @RequestParam("id") int id) {
		return this.titleService.getTitle(id, true).toJsonString();
    }

	@GetMapping(value="/isbn", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getTitle(HttpServletRequest request, @RequestParam("isbn") String isbn) {
        return this.titleService.isbnLookup(isbn).toJsonString();
    }

	@GetMapping(value="/list", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	String listTitles(HttpServletRequest request) {
		String rst = "{\"result\":[";
        Title[] titles = this.titleService.getTitles(true);
        for (int i = 0; i < titles.length; i++) {
            if (i > 0) { rst += ","; }
            rst += titles[i].toJsonString();
        }
        rst += "]}";
        return rst;
	}

	@GetMapping(value="/add", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String addTitle(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
		try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
            Title temp = new Title();
            temp.reloadFromJson(raw);
            return String.format("{\"result\": %d}", (this.titleService.addTitle(temp) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

	@GetMapping(value="/update", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String updateTitle(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
		try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
            Title temp = new Title();
            temp.reloadFromJson(raw);
            return String.format("{\"result\": %d}", (this.titleService.modifyTitle(temp) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

	@GetMapping(value="/remove", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String removeTitle(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
		try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
            Title temp = new Title();
            temp.reloadFromJson(raw);
            return String.format("{\"result\": %d}", (this.titleService.removeTitle(temp) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

	@GetMapping(value="/addcopy", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String addTitleCopy(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
        try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
			JSONObject dict = new JSONObject(raw);
            Title temp = this.titleService.getTitle(dict.getInt("title"), true);
            return String.format("{\"result\": %d}", (this.titleService.addTitleCopy(temp) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

	@GetMapping(value="/removecopy", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String removeTitleCopy(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
        try {
            ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
            TitleCopy temp = new TitleCopy();
            temp.reloadFromJson(raw);
            return String.format("{\"result\": %d}", (this.titleService.removeTitleCopy(temp) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }
}



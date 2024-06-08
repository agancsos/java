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
import com.catalogger.services.AuthenticationService;
import com.catalogger.services.DbTraceService;

@RestController
@RequestMapping(value="/api/authors")
class AuthorApiController {
	private AuthorService authorService          = null;
	private AuthenticationService authService    = null;

	@Autowired
	public AuthorApiController() {
		this.authorService = AuthorService.getInstance();
		this.authService   = AuthenticationService.getInstance();
	}


	@GetMapping(value="/", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String home(HttpServletRequest request) {
		return "{\"result\": \"Pong\"}";
	}

	@GetMapping(value="/get", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String getAuthor(HttpServletRequest request, @RequestParam("id") int id) {
		return this.authorService.getAuthor(id, true).toJsonString();
    }

	@GetMapping(value="/list", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	String listAuthors(HttpServletRequest request) {
        String rst = "{\"result\":[";
        Author[] authors = this.authorService.getAuthors(true);
        for (int i = 0; i < authors.length; i++) {
            if (i > 0) { rst += ","; }
            rst += authors[i].toJsonString();
        }
        rst += "]}";
        return rst;
	}

	@GetMapping(value="/add", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String addAuthor(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
		try {
			ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
			Author temp = new Author();
			temp.reloadFromJson(raw);
			return String.format("{\"result\": %d}", (this.authorService.addAuthor(temp) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

	@GetMapping(value="/update", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String updateAuthor(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
		try {
			ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
			Author temp = new Author();
            temp.reloadFromJson(raw);
            return String.format("{\"result\": %d}", (this.authorService.modifyAuthor(temp) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }

	@GetMapping(value="/remove", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String removeAuthor(HttpServletRequest request, @RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
		try {
			ApiHelpers.ensureAuthenticated(token);
            if (!this.authService.isGroupMember(ApiHelpers.extractUser(token), SR.adminGroupId)) {
                return String.format("\"{message\": \"%s\"}", SR.notAuthorizedMessage);
            }
			Author temp = new Author();
            temp.reloadFromJson(raw);
            return String.format("{\"result\": %d}", (this.authorService.removeAuthor(temp) ? 1 : 0));
        } catch(Exception ex) {
            return String.format("{\"message\": \"%s\"}", ex.getMessage());
        }
    }
}



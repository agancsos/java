package com.catalogger.components;
import com.catalogger.services.ConfigurationService;
import com.catalogger.services.SecurityService;
import com.catalogger.services.AuthenticationService;
import com.catalogger.models.User;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.json.JSONObject;

@RestController
@RequestMapping(value="/api")
class CommonApiController {
	private ConfigurationService configService      = ConfigurationService.getInstance("");
	private AuthenticationService authService       = null;

	@Autowired
	public CommonApiController() {
		this.authService    = AuthenticationService.getInstance();
	}

	@PostMapping(value="/auth", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String authenticate(@RequestBody String raw) throws Exception {
		JSONObject temp = new JSONObject(raw);
		if (! temp.has("credential")) {
			throw new Exception("Credentials missing");
		}
		String[] comps = SecurityService.getBase64Decoded(temp.getString("credentials")).split(":");
		if (! this.authService.authenticate(comps[0], SecurityService.getBase64Encoded(comps[1]))) {
			throw new Exception("Invalid user");
		}
		return String.format("{\"result\":\"%s\"}", this.authService.generateToken(comps[0], SecurityService.getBase64Encoded(comps[1])));	
	}

	@PostMapping(value="/signup", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String register(@RequestBody String raw) throws Exception {
        JSONObject temp = new JSONObject(raw);
        if (! temp.has("credential")) {
            throw new Exception("Credentials missing");
        }
        String[] comps = SecurityService.getBase64Decoded(temp.getString("credentials")).split(":");
        if (! this.authService.authenticate(comps[0], SecurityService.getBase64Encoded(comps[1]))) {
            throw new Exception("Invalid user");
        }
        return String.format("{\"result\":\"%s\"}", this.authService.generateToken(comps[0], SecurityService.getBase64Encoded(comps[1])));
    }
}


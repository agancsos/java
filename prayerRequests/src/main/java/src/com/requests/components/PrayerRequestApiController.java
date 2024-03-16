package com.requests.components;
import com.requests.services.ConfigurationService;
import com.requests.services.PrayerRequestService;
import com.requests.services.SecurityService;
import com.requests.services.AuthenticationService;
import com.requests.models.PrayerRequest;
import com.requests.models.User;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.json.JSONObject;

@RestController
@RequestMapping(value="/api/requests")
class TransactionApiController {
	private ConfigurationService configService      = ConfigurationService.getInstance("");
	private PrayerRequestService requestService     = null;
	private AuthenticationService authService       = null;

	@Autowired
	public TransactionApiController() {
		this.requestService = PrayerRequestService.getInstance();
		this.authService    = AuthenticationService.getInstance();
	}


	@GetMapping(value="/list", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String listPrayerRequests(@RequestHeader("X-API-TOKEN") String token, @RequestParam("user") int userId) {
		try {
			ApiHelpers.ensureAuthenticated(token);
			User user = ApiHelpers.extractUser(token);
			PrayerRequest[] temp = this.requestService.listPrayerRequests(userId, true);
			String rst = "{\"results\":[";
			int j = 0;
			for (int i = 0; i < temp.length; i++) {
				if (temp[i].getUserId() != user.getUserId() && !(temp[i].getIsPublic() || (temp[i].getIsShared() && this.authService.areFriends(user.getUserId(), userId)))) {
					continue;
				}
				if (j > 0) {
					rst += ",";
				}
				rst += temp[i].toJsonString();
				j++;
			}
			rst += "]";
			rst += "}";
			return rst;
		} catch (Exception ex) {
			return "{\"result\": 0}";
		}
	}

	@GetMapping(value="/get", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    String getPrayerRequest(@RequestHeader("X-API-TOKEN") String token, @RequestParam("id") int id) {
		try {
			ApiHelpers.ensureAuthenticated(token);
			User user = ApiHelpers.extractUser(token);
        	PrayerRequest temp = this.requestService.getPrayerRequest(id);
			if (temp.getUserId() != user.getUserId() && !(temp.getIsPublic() || (temp.getIsShared() && this.authService.areFriends(user.getUserId(), temp.getUserId())))) {
				return "{\"result\": 0}";
			}
        	return temp.toJsonString();
		} catch (Exception ex) {
			return "{\"result\": 0}";
		}
    }

	@PostMapping(value="/add", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String add(@RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
		try {
			ApiHelpers.ensureAuthenticated(token);
			try {
				PrayerRequest request = new PrayerRequest(raw);
				request.setMethod("API");
				this.requestService.addPrayerRequest(request);
				return "{\"result\": 1}";
			} catch (Exception ex) {
				ex.printStackTrace();
				return "{\"result\": 0}";
			}
		} catch (Exception ex) {
			return "{\"result\": 0}";
		}
	}
}


package com.requests.components;
import com.requests.services.ConfigurationService;
import com.requests.services.FriendRequestService;
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
@RequestMapping(value="/api/friends")
class FriendRequestApiController {
	private ConfigurationService configService      = ConfigurationService.getInstance("");
	private FriendRequestService friendService      = null;
	private AuthenticationService authService       = null;

	@Autowired
	public FriendRequestApiController() {
		this.friendService  = FriendRequestService.getInstance();
		this.authService    = AuthenticationService.getInstance();
	}

	@GetMapping(value="/list", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String listFriendRequests(@RequestHeader("X-API-TOKEN") String token, @RequestParam("user") int userId) {
		try {
			ApiHelpers.ensureAuthenticated(token);
			String[] temp = this.friendService.listFriendRequests(userId);
			String rst = "{\"results\":[";
			for (int i = 0; i < temp.length; i++) {
				if (i > 0) {
					rst += ",";
				}
				rst += temp[i];
			}
			rst += "]";
			rst += "}";
			return rst;
		} catch (Exception ex) {
			return "{\"result\": 0}";
		}
	}

	@PostMapping(value="/add", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	String add(@RequestHeader("X-API-TOKEN") String token, @RequestParam("user") int targetUser) {
		try {
			ApiHelpers.ensureAuthenticated(token);
			this.friendService.addFriendRequest(ApiHelpers.extractUser(token).getUserId(), targetUser);
			return "{\"result\": 1}";
		} catch (Exception ex) {
			return "{\"result\": 0}";
		}
	}

	@PostMapping(value="/remove", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String remove(@RequestHeader("X-API-TOKEN") String token, @RequestParam("user") int targetUser) {
		try {
        	ApiHelpers.ensureAuthenticated(token);
        	this.friendService.removeFriend(ApiHelpers.extractUser(token).getUserId(), targetUser);
        	return "{\"result\": 1}";
		} catch (Exception ex) {
            return "{\"result\": 0}";
        }
    }

	@PostMapping(value="/approve", produces=MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String approve(@RequestHeader("X-API-TOKEN") String token, @RequestBody String raw) {
		try {
        	ApiHelpers.ensureAuthenticated(token);
			JSONObject obj = new JSONObject(raw);
			if (!obj.has("targetUser") && !obj.has("email")) {
				return "{\"result\": 0}";
			}
			int targetUser = obj.getInt("targetUser");
			String email   = obj.getString("email");
			if (this.friendService.confirmEmail(targetUser, email)) {
				this.friendService.updateFriendRequest(ApiHelpers.extractUser(token).getUserId(), targetUser);
        		return "{\"result\": 1}";
			}
			return "{\"result\": 0}";
		} catch (Exception ex) {
			return "{\"result\": 0}";
		}
    }
}


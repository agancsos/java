package com.requests.components;
import com.requests.services.ConfigurationService;
import com.requests.services.PrayerRequestService;
import com.requests.services.FriendRequestService;
import com.requests.services.AuthenticationService;
import com.requests.services.SecurityService;
import com.requests.models.User;
import com.requests.models.PrayerRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

@Controller
@RequestMapping(value="/console")
class PrayerRequestsConsoleController {
	private ConfigurationService configService      = ConfigurationService.getInstance("");
	private PrayerRequestService requestService     = null;
	private FriendRequestService friendService      = null;
	private AuthenticationService authService       = null;

	@Autowired
	public PrayerRequestsConsoleController() {
		this.requestService = PrayerRequestService.getInstance();
		this.friendService  = FriendRequestService.getInstance();
		this.authService    = AuthenticationService.getInstance();
	}

	@RequestMapping(value="/")
	public String landing() {
		return "index";
	}

	// PrayerRequests
	@RequestMapping(value="/requests")
	String listPrayerRequests(Model model, @CookieValue("X-API-TOKEN") String token, @RequestParam("user") int userId) {
		User user                      = ApiHelpers.extractUser(token);	
		PrayerRequest[] temp           = this.requestService.listPrayerRequests(userId, true);
		ArrayList<PrayerRequest> temp2 = new ArrayList<PrayerRequest>();
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].getUserId() != user.getUserId() && !(temp[i].getIsPublic() || (temp[i].getIsShared() && this.authService.areFriends(user.getUserId(), userId)))) {
				continue;
			}
			temp2.add(temp[i]);
		}
		model.addAttribute("results", temp2.toArray(new PrayerRequest[temp2.size()]));
		return "list-prayerrequests";
	}

	@RequestMapping(value="/journal")
    String listMyPrayerRequests(Model model, @CookieValue("X-API-TOKEN") String token) {
        User user                      = ApiHelpers.extractUser(token);
        PrayerRequest[] temp           = this.requestService.listPrayerRequests(user.getUserId(), true);
        model.addAttribute("results", temp);
        return "list-prayerrequests";
    }

	@RequestMapping(value="/request")
    String getPrayerRequest(Model model, @CookieValue("X-API-TOKEN") String token, @RequestParam("req") int requestId) {
        User user                      = ApiHelpers.extractUser(token);
        PrayerRequest temp             = this.requestService.getPrayerRequest(requestId);
        if (temp.getUserId() != user.getUserId() && !(temp.getIsPublic() || (temp.getIsShared() && this.authService.areFriends(user.getUserId(), temp.getUserId())))) {
            model.addAttribute(new PrayerRequest());
        } else {
            model.addAttribute("request", temp);
        }
        return "get-prayerrequest";
    }

	@RequestMapping(value="/add-prayerrequest")
    String addPrayerRequest(Model model, HttpServletRequest request, @CookieValue("X-API-TOKEN") String token) {
        User user                      = ApiHelpers.extractUser(token);
		String text                    = request.getParameter("request");
		boolean isPublic               = (request.getParameter("public") != null && request.getParameter("public").equals("1") ? true : false);
		boolean isShared               = (request.getParameter("shared") != null && request.getParameter("shared").equals("1") ? true : false);
		PrayerRequest tempRequest      = new PrayerRequest();
		tempRequest.setUserId(user.getUserId());
		tempRequest.setRequest(text);
		tempRequest.setIsPublic(isPublic);
		tempRequest.setIsShared(isShared);
		tempRequest.setMethod("Console");
		try {
			this.requestService.pushPrayerRequest(tempRequest);
		} catch (Exception e) {
		}
		PrayerRequest[] temp           = this.requestService.listPrayerRequests(user.getUserId(), true);
        model.addAttribute("results", temp);
        return "redirect:/console/journal";
    }

	@RequestMapping(value="/remove-prayerrequest")
    String removePrayerRequest(Model model, HttpServletRequest request, @CookieValue("X-API-TOKEN") String token, @RequestParam("req") int requestId) {
        User user                      = ApiHelpers.extractUser(token);
		PrayerRequest prayerRequest    = this.requestService.getPrayerRequest(requestId);
		if (prayerRequest.getUserId() == user.getUserId()) {
        	try {
            	this.requestService.removePrayerRequest(prayerRequest);
        	} catch (Exception e) {
        	}
		}
        PrayerRequest[] temp           = this.requestService.listPrayerRequests(user.getUserId(), true);
        model.addAttribute("results", temp);
        return "redirect:/console/journal";
    }

	@RequestMapping(value="/answer-prayerrequest")
    String answerPrayerRequest(Model model, HttpServletRequest request, @CookieValue("X-API-TOKEN") String token, @RequestParam("req") int requestId) {
        User user                      = ApiHelpers.extractUser(token);
        PrayerRequest prayerRequest    = this.requestService.getPrayerRequest(requestId);
        if (prayerRequest.getUserId() == user.getUserId()) {
			prayerRequest.setStatus("ANSWERED");
            try {
                this.requestService.updatePrayerRequest(prayerRequest);
            } catch (Exception e) {
            }
        }
        PrayerRequest[] temp           = this.requestService.listPrayerRequests(user.getUserId(), true);
        model.addAttribute("results", temp);
        return "redirect:/console/journal";
    }

	@RequestMapping(value="/edit-prayerrequest")
    String editPrayerRequest(Model model, HttpServletRequest request, @CookieValue("X-API-TOKEN") String token, @RequestParam("req") int requestId) {
        User user                      = ApiHelpers.extractUser(token);
        PrayerRequest prayerRequest    = this.requestService.getPrayerRequest(requestId);
        if (prayerRequest.getUserId() == user.getUserId()) {
            model.addAttribute("prayerRequest", prayerRequest);
        }
        return "edit-prayerrequest";
    }

	@RequestMapping(value="/save-prayerrequest", method=RequestMethod.POST)
    String savePrayerRequest(Model model, HttpServletRequest request, HttpServletResponse response, @CookieValue("X-API-TOKEN") String token) {
        User user                      = ApiHelpers.extractUser(token);
		String text                    = request.getParameter("request");
        boolean isPublic               = (request.getParameter("public") != null && request.getParameter("public").equals("1") ? true : false);
        boolean isShared               = (request.getParameter("shared") != null && request.getParameter("shared").equals("1") ? true : false);
        PrayerRequest tempRequest      = this.requestService.getPrayerRequest(Integer.parseInt(request.getParameter("requestId")));
        tempRequest.setUserId(user.getUserId());
        tempRequest.setRequest(text);
        tempRequest.setIsPublic(isPublic);
        tempRequest.setIsShared(isShared);
        try {
            this.requestService.updatePrayerRequest(tempRequest);
        } catch (Exception e) {
        }
        PrayerRequest[] temp           = this.requestService.listPrayerRequests(user.getUserId(), true);
        model.addAttribute("results", temp);
        return "redirect:/console/journal";
    }

	// FriendRequests
	@RequestMapping(value="/befriend")
    String requestFriend(Model model, @CookieValue("X-API-TOKEN") String token, @RequestParam("user") int userId) {
        User user                      = ApiHelpers.extractUser(token);
        try {
            this.friendService.addFriendRequest(user.getUserId(), userId);
        } catch (Exception e) {
        }
		String[] friends               = this.friendService.listFriendRequests(user.getUserId());
        model.addAttribute("results", friends);
        return "redirect:/list-friends";
    }

	@RequestMapping(value="/friends")
	String listFriendRequests(Model model, @CookieValue("X-API-TOKEN") String token) {
        User user                      = ApiHelpers.extractUser(token);
		String[] friends               = this.friendService.listFriendRequests(user.getUserId());
        model.addAttribute("results", friends);
        return "list-friends";
    }

	@RequestMapping(value="/remove")
    String removeFriend(Model model, @CookieValue("X-API-TOKEN") String token, @RequestParam("user") int userId) {
		User user                      = ApiHelpers.extractUser(token);
		try {
			this.friendService.removeFriend(user.getUserId(), userId);
		} catch (Exception e) {
		}
		String[] friends               = this.friendService.listFriendRequests(user.getUserId());
        model.addAttribute("results", friends);
        return "redirect:/list-friends";
    }
	
	@RequestMapping(value="/approve")
    String approveFriend(Model model, @CookieValue("X-API-TOKEN") String token, @RequestParam("user") int userId) {
		User user = this.authService.getUser(userId);
		model.addAttribute("user", user);
        return "approve-friend";
    }

	@RequestMapping(value="/verify")
    String verifyApproveFriend(Model model, HttpServletRequest request, @CookieValue("X-API-TOKEN") String token) {
		String friendEmail = request.getParameter("verify-email");
		String targetUser  = request.getParameter("userid");
        User user = ApiHelpers.extractUser(token);
		if (this.friendService.confirmEmail(Integer.parseInt(targetUser), friendEmail)) {
			try {
				this.friendService.updateFriendRequest(user.getUserId(), Integer.parseInt(targetUser));
			} catch (Exception e) {
			}
		}
		String[] friends               = this.friendService.listFriendRequests(user.getUserId());
        model.addAttribute("results", friends);
        return "redirect:/list-friends";
    }

	// Authentication and registration
	@RequestMapping(value="/login", method=RequestMethod.POST)
	String setLogin(HttpServletRequest request, HttpServletResponse response) {
		String email    = request.getParameter("login-username");
        String password = SecurityService.getBase64Encoded(request.getParameter("login-password"));
        if (this.authService.authenticate(email, password)) {
        	String token = authService.generateToken(email, password);
            Cookie c = new Cookie("X-API-TOKEN", token);
			c.setPath("/");
            response.addCookie(c);
        } else {
        }
		return "redirect:/console/";
	}

	@RequestMapping(value="/register")
    String setRegister(Model model, HttpServletRequest request) {
		String firstName = request.getParameter("firstname-input");
        String lastName  = request.getParameter("lastname-input");
        String email     = request.getParameter("email-input");
        String password  = SecurityService.getBase64Encoded(request.getParameter("password-input"));
        try {
            this.authService.register(firstName, lastName, email, password);
        } catch (Exception e) {
        }
		return "redirect:/console/";
    }

	@RequestMapping(value="/logout")
    String setLogout(HttpServletRequest request, HttpServletResponse response) {
        Cookie c = new Cookie("X-API-TOKEN", "");
		c.setPath("/");
        response.addCookie(c);
		return "redirect:/console/";
    }
}


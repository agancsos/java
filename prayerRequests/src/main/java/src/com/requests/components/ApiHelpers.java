package com.requests.components;
import com.requests.models.User;
import com.requests.services.AuthenticationService;

public class ApiHelpers {
	public static void ensureAuthenticated(String token) throws Exception {
		if (AuthenticationService.getInstance().getUser(token) == null) {
			throw new Exception("Invalid user");
		}
    }

    public static User extractUser(String token) {
		return AuthenticationService.getInstance().getUser(token);
    }
}


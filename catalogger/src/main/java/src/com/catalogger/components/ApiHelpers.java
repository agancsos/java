package com.catalogger.components;
import com.catalogger.models.User;
import com.catalogger.services.AuthenticationService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;

public class ApiHelpers {
	public static void ensureAuthenticated(String token) throws Exception {
		if (AuthenticationService.getInstance().getUser(token) == null) {
			throw new Exception("Invalid user");
		}
    }

    public static User extractUser(String token) {
		return AuthenticationService.getInstance().getUser(token);
    }

	public static ConsoleSession extractConsoleSession(HttpServletRequest request) {
		ConsoleSession rst = new ConsoleSession();
		String pageName                   = request.getRequestURI();
    	String[] pageNameComps            = pageName.split("/");
    	pageName                          = pageNameComps[pageNameComps.length - 1].replace(".jsp", "");
    	String sessionToken               = "";
    	String searchText                 = "";
    	AuthenticationService authService = AuthenticationService.getInstance();
    	User sessionUser                  = null;
    	if (request.getParameterMap().containsKey("s")) {
        	searchText = request.getParameter("s").toString();
    	}
    	if (request.getCookies() != null) {
        	for (Cookie c : request.getCookies()) {
            	if (c.getName().equals("X-API-TOKEN") && !c.getValue().equals("")) {
                	sessionToken = c.getValue();
                	sessionUser  = ApiHelpers.extractUser(sessionToken);
            	}
        	}
    	}
		rst.setPageName(pageName);
		rst.setSessionToken(sessionToken);
		rst.setSearchText(searchText);
		rst.setSessionUser(sessionUser);
		return rst;
	}

	public static boolean stringListContains(String[] list, String seed) {
		for (String a : list) {
			if (a.equals(seed)) {
				return true;
			}
		}
		return false;
	}
}



package com.catalogger.components;
import javax.servlet.http.HttpServletRequest;
import com.catalogger.models.User;

public class ConsoleSession {
	private String pageName     = "";
	private String sessionToken = "";
	private String searchText   = "";
	private User sessionUser    = null;
	
	public ConsoleSession() {
	}

	public String getPageName() { return this.pageName; }
	public String getSessionToken() { return this.sessionToken; }
	public String getSearchText() { return this.searchText; }
	public User getSessionUser() { return this.sessionUser; }

	public void setPageName(String a) { this.pageName = a; }
	public void setSessionToken(String a) { this.sessionToken = a; }
	public void setSearchText(String a) { this.searchText = a; }
	public void setSessionUser(User a) { this.sessionUser = a; }
}


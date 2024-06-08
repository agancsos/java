package com.catalogger.models;
import org.json.JSONObject;

public class User {
	private int userId             = -1;
	private String username        = "";
	private String email           = "";
	private String token           = "";
	private String firstName       = "";
	private String lastName        = "";
	private int state              = 0;
	private JSONObject props       = null;
	private String lastUpdatedDate = "";

	public User() {
	}

	public User(int id) {
		this.userId = id;
	}

	public User(int id, String email) {
		this.userId = id;
		this.email = email;
		this.username = email;
	}

	public User(int id, String email, String token) {
		this.userId = id;
		this.email = email;
		this.token = token;
		this.username = email;
	}

	public String toJsonString() {
		return String.format("{\"userId\": %d, \"firstName\": \"%s\", \"lastName\": \"%s\", \"username\": \"%\", \"email\": \"%s\", \"token\": \"%s\", \"lastUpdatedDate\":\"%s\"}",
			this.userId,
			this.firstName,
			this.lastName,
			this.username,
			this.email,
			this.token,
			this.lastUpdatedDate);
	}

	public int getUserId() { return this.userId; }
	public int getId() { return this.userId; }
	public String getEmail() { return this.email; }
	public String getUsername() { return this.username; }
	public String getToken() { return this.token; }
	public String getFirstName() { return this.firstName; }
	public String getLastName() { return this.lastName; }
	public JSONObject getProperties() { return (this.props != null ? this.props : new JSONObject("{}")); }
	public String getLastUpdatedDate() { return this.lastUpdatedDate; }
	public int getState() { return this.state; }
	public void setFirstName(String a) { this.firstName = a; }
	public void setLastName(String a) { this.lastName = a; }
	public void setEmail(String a) { this.email = a; }
	public void setUsername(String a) { this.username = a; }
	public void setId(int a) { this.userId = a; }
	public void setToken(String a) { this.token = a; }
	public void setProperties(String a) { this.props = new JSONObject(a); }
	public void setState(int a) { this.state = a; }
	public void setLastUpdatedDate(String a) { this.lastUpdatedDate = a; }
}


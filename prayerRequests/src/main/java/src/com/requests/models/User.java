package com.requests.models;

public class User {
	private int userId       = -1;
	private String email     = "";
	private String token     = "";
	private String firstName = "";
	private String lastName  = "";

	public User() {
	}

	public User(int id) {
		this.userId = id;
	}

	public User(int id, String email) {
		this.userId = id;
		this.email = email;
	}

	public User(int id, String email, String token) {
		this.userId = id;
		this.email = email;
		this.token = token;
	}

	public String toJsonString() {
		return String.format("{\"userId\": %d, \"firstName\": \"%s\", \"lastName\": \"%s\", \"email\": \"%s\", \"token\": \"%s\"}",
			this.userId,
			this.firstName,
			this.lastName,
			this.email,
			this.token);
	}

	public int getUserId() { return this.userId; }
	public String getEmail() { return this.email; }
	public String getToken() { return this.token; }
	public String getFirstName() { return this.firstName; }
	public String getLastName() { return this.lastName; }
	public void setFirstName(String a) { this.firstName = a; }
	public void setLastName(String a) { this.lastName = a; }
	public void setEmail(String a) { this.email = a; }
}

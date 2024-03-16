package com.requests.services;
import com.data.types.DataTable;
import com.requests.models.User;
import java.util.Random;

public class AuthenticationService {
	private static AuthenticationService instance = null;
	private DataService dataService               = null;

	private AuthenticationService() {
		this.dataService = DataService.getInstance();
	}

	public static AuthenticationService getInstance() {
		if (AuthenticationService.instance == null) {
			AuthenticationService.instance = new AuthenticationService();
		}
		return AuthenticationService.instance;
	}

	public boolean authenticate(String username, String password) {
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT 1 FROM USERS WHERE USER_EMAIL = '%s' AND USER_PASSWORD = '%s' AND USER_STATUS = '1'", username, password));
		return tab.getRows().size() == 1;
	}

	public String generateToken(String username, String password) {
		String rst     = "";
		String[] chars   = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
		boolean exists = true;
		while (exists) {
			Random rand = new Random();
			rst         = "";
			int tokenLength = rand.nextInt(30, 120);
			for (int i = 0; i < tokenLength; i++) {
				rst += chars[rand.nextInt(0, chars.length - 1)];
			}
			exists = this.dataService.serviceQuery(String.format("SELECT 1 FROM SESSIONS WHERE SESSION_TOKEN = '%s'", rst)).getRows().size() == 1;
		}
		this.dataService.runServiceQuery(String.format("INSERT INTO SESSIONS (USER_ID, SESSION_TOKEN) VALUES ((SELECT USER_ID FROM USERS WHERE USER_EMAIL = '%s'), '%s')",
			username,
			rst));
		return rst;
	}

	public User getUser(String sessionToken) {
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT * FROM USERS WHERE USER_ID = (SELECT USER_ID FROM SESSIONS  WHERE SESSION_TOKEN = '%s')", sessionToken));
		if (tab.getRows().size() < 1) {
			return null;
		}
		User user = new User(Integer.parseInt(tab.getRows().get(0).getColumn("user_id").getColumnValue()));
		user.setEmail(tab.getRows().get(0).getColumn("user_email").getColumnValue());
		user.setFirstName(tab.getRows().get(0).getColumn("user_firstname").getColumnValue());
		user.setLastName(tab.getRows().get(0).getColumn("user_lastname").getColumnValue());
		return user;
	}

	public User getUser(int userId) {
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT * FROM USERS WHERE USER_ID = '%d'", userId));
        if (tab.getRows().size() < 1) {
            return null;
        }
        User user = new User(Integer.parseInt(tab.getRows().get(0).getColumn("user_id").getColumnValue()));
        user.setEmail(tab.getRows().get(0).getColumn("user_email").getColumnValue());
        user.setFirstName(tab.getRows().get(0).getColumn("user_firstname").getColumnValue());
        user.setLastName(tab.getRows().get(0).getColumn("user_lastname").getColumnValue());
        return user;
	}

	public void register(String firstName, String lastName, String email, String password) throws Exception {
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT 1 FROM USERS WHERE USER_EMAIL = '%s'", email));
		if (tab.getRows().size() != 1) {
			throw new Exception("User already exists");
		}
		this.dataService.runServiceQuery(String.format("INSERT INTO USERS (USER_FIRSTNAME, USER_LASTNAME, USER_EMAIL, USER_PASSWORD) VALUES ('%s', '%s', '%s', '%s')",
			firstName,
			lastName,
			email,
			password));
	}

	public boolean areFriends(int sourceUser, int targetUser) {
		return this.dataService.serviceQuery(String.format("SELECT 1 FROM USER_FRIENDS WHERE ((SOURCE_USER_ID = '%d' AND TARGET_USER_ID = '%d') OR (SOURCE_USER_ID = '%d' AND TARGET_USER_ID = '%d')) AND FRIENDS_CONFIRMED == '1'",
			sourceUser,
			targetUser,
			targetUser,
			sourceUser)).getRows().size() == 2;
	}
}


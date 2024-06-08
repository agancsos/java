package com.catalogger.services;
import com.data.types.DataTable;
import com.data.types.DataRow;
import com.catalogger.models.User;
import java.util.Random;
import java.util.ArrayList;

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
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT 1 FROM USERS WHERE USER_EMAIL = '%s' AND USER_PASSWORD = '%s' AND USER_STATE = '1'", username, password));
		return tab.getRows().size() == 1;
	}

	public String generateToken(String username, String password) {
		String rst     = "";
		String[] chars   = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z,0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
		boolean exists = true;
		while (exists) {
			Random rand = new Random();
			rst         = "";
			int tokenLength = rand.nextInt(91) + 30;
			for (int i = 0; i < tokenLength; i++) {
				rst += chars[rand.nextInt(chars.length - 1)];
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
		user.setUsername(tab.getRows().get(0).getColumn("user_username").getColumnValue());
		user.setFirstName(tab.getRows().get(0).getColumn("user_firstname").getColumnValue());
		user.setLastName(tab.getRows().get(0).getColumn("user_lastname").getColumnValue());
		user.setToken(tab.getRows().get(0).getColumn("user_password").getColumnValue());
        user.setProperties(tab.getRows().get(0).getColumn("user_json").getColumnValue());
		user.setState(Integer.parseInt(tab.getRows().get(0).getColumn("user_state").getColumnValue()));
		user.setLastUpdatedDate(tab.getRows().get(0).getColumn("last_updated_date").getColumnValue());
		return user;
	}

	public User getUserByName(String name) {
        DataTable tab = this.dataService.serviceQuery(String.format("SELECT * FROM USERS WHERE USER_USERNAME = '%s' OR USER_EMAIL = '%s'", name, name));
        if (tab.getRows().size() < 1) {
            return null;
        }
        User user = new User(Integer.parseInt(tab.getRows().get(0).getColumn("user_id").getColumnValue()));
        user.setEmail(tab.getRows().get(0).getColumn("user_email").getColumnValue());
        user.setUsername(tab.getRows().get(0).getColumn("user_username").getColumnValue());
        user.setFirstName(tab.getRows().get(0).getColumn("user_firstname").getColumnValue());
        user.setLastName(tab.getRows().get(0).getColumn("user_lastname").getColumnValue());
        user.setToken(tab.getRows().get(0).getColumn("user_password").getColumnValue());
        user.setProperties(tab.getRows().get(0).getColumn("user_json").getColumnValue());
        user.setState(Integer.parseInt(tab.getRows().get(0).getColumn("user_state").getColumnValue()));
		user.setLastUpdatedDate(tab.getRows().get(0).getColumn("last_updated_date").getColumnValue());
        return user;
    }

	public User getUser(int userId) {
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT * FROM USERS WHERE USER_ID = '%d'", userId));
        if (tab.getRows().size() < 1) {
            return null;
        }
        User user = new User(Integer.parseInt(tab.getRows().get(0).getColumn("user_id").getColumnValue()));
        user.setEmail(tab.getRows().get(0).getColumn("user_email").getColumnValue());
		user.setUsername(tab.getRows().get(0).getColumn("user_username").getColumnValue());
        user.setFirstName(tab.getRows().get(0).getColumn("user_firstname").getColumnValue());
        user.setLastName(tab.getRows().get(0).getColumn("user_lastname").getColumnValue());
		user.setToken(tab.getRows().get(0).getColumn("user_password").getColumnValue());
		user.setProperties(tab.getRows().get(0).getColumn("user_json").getColumnValue());
		user.setState(Integer.parseInt(tab.getRows().get(0).getColumn("user_state").getColumnValue()));
		user.setLastUpdatedDate(tab.getRows().get(0).getColumn("last_updated_date").getColumnValue());
        return user;
	}

	public User[] getUsers() {
		ArrayList<User> rst = new ArrayList<User>();
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT * FROM USERS"));
		for (DataRow row : tab.getRows()) {
			rst.add(this.getUser(Integer.parseInt(row.getColumn("user_id").getColumnValue())));
		}
		return rst.toArray(new User[rst.size()]);
	}

	public void updatePassword(User user, String password) {
		this.dataService.runServiceQuery(String.format("UPDATE USERS SET USER_PASSWORD = '%s', LAST_UPDATED_DATE = CURRENT_TIMESTAMP WHERE USER_ID = '%d'", password, user.getId()));
	}

	public void updateProperties(User user, String properties) {
		this.dataService.runServiceQuery(String.format("UPDATE USERS SET USER_JSON = '%s', LAST_UPDATED_DATE = CURRENT_TIMESTAMP WHERE USER_ID = '%d'", properties, user.getId()));
	}

	public void register(String firstName, String lastName, String email, String password) throws Exception {
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT 1 FROM USERS WHERE USER_EMAIL = '%s'", email));
		if (tab.getRows().size() != 1) {
			throw new Exception("User already exists");
		}
		this.dataService.runServiceQuery(String.format("INSERT INTO USERS (USER_FIRSTNAME, USER_LASTNAME, USER_EMAIL, USER_USERNAME, USER_PASSWORD) VALUES ('%s', '%s', '%s', '%s', '%s')",
			firstName,
			lastName,
			email,
			email,
			password));
	}

	public String[] getGroups() {
		ArrayList<String> groups = new ArrayList<String>();
		DataTable tab = this.dataService.serviceQuery("SELECT * FROM GROUPS");
		for (DataRow row : tab.getRows()) {
			groups.add(String.format("%s:%s", row.getColumn("group_id").getColumnValue(), row.getColumn("group_label").getColumnValue()));
		}
		return groups.toArray(new String[groups.size()]);
	}

	public void removeGroup(int groupId) {
		if (this.dataService.runServiceQuery(String.format("DELETE FROM GROUP_MEMBERS WHERE GROUP_ID = '%d'", groupId))) {
			this.dataService.runServiceQuery(String.format("DELETE FROM GROUPS WHERE GROUP_ID = '%d'", groupId));
		}
	}

	public void addGroup(String label) {
		if (this.dataService.serviceQuery(String.format("SELECT 1 FROM GROUPS WHERE GROUP_LABEL = '%s'", label)).getRows().size() > 0) {
			return;
		}
		this.dataService.runServiceQuery(String.format("INSERT INTO GROUPS (GROUP_LABEL) VALUES ('%s')", label));
	}

	public void addGroupMember(User user, int groupId) {
		this.dataService.runServiceQuery(String.format("INSERT INTO GROUP_MEMBERS (USER_ID, GROUP_ID) VALUES ('%d', '%d')", user.getId(), groupId));
	}

	public void removeGroupMember(User user, int groupId) {
		this.dataService.runServiceQuery(String.format("DELETE FROM GROUP_MEMBERS WHERE USER_ID = '%d' AND GROUP_ID ='%d'", user.getId(), groupId));
	} 
	
	public void resetPassword(User user) {
		this.dataService.runServiceQuery(String.format("UPDATE USERS SET USER_STATE = '1', USER_PASSWORD = '%s', LAST_UPDATED_DATE = CURRENT_TIMESTAMP WHERE USER_ID = '%d'",
			SecurityService.getBase64Encoded("Welcome123"), user.getId()));
	}

	public void removeUser(User user) {
		if (user.getEmail().equals("Administrator")) {
			return;
		}
		this.dataService.runServiceQuery(String.format("UPDATE USERS SET USER_STATE = '3', LAST_UPDATED_DATE = CURRENT_TIMESTAMP WHERE USER_ID = '%d'", user.getId()));
	}

	public boolean isGroupMember(User user, int group) {
		return this.dataService.serviceQuery(String.format("SELECT 1 FROM GROUP_MEMBERS WHERE USER_ID = '%d' AND (GROUP_ID = '%d' OR GROUP_ID = '2')", user.getId(), group)).getRows().size() > 0;
	}
}



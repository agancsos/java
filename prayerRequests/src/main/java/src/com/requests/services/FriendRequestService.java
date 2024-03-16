package com.requests.services;
import com.requests.models.User;
import java.util.ArrayList;
import com.data.types.DataTable;
import com.data.types.DataRow;
import com.data.types.DataColumn;

public class FriendRequestService {
	private ConfigurationService configService   = null;
	private static FriendRequestService instance = null;
	private DataService dataService              = null;
	private AuthenticationService authService    = null;

	private FriendRequestService() {
		this.configService = ConfigurationService.getInstance("");
		this.dataService   = DataService.getInstance();
		this.authService   = AuthenticationService.getInstance();
	}

	public static FriendRequestService getInstance() {
		if (FriendRequestService.instance == null) {
			FriendRequestService.instance = new FriendRequestService();
		}
		return FriendRequestService.instance;
	}

	public void addFriendRequest(int sourceUser, int targetUser) throws Exception {
		this.dataService.runServiceQuery(String.format("INSERT INTO USER_FRIENDS (SOURCE_USER_ID, TARGET_USER_ID) VALUES ('%d', '%d')",
			sourceUser,
			targetUser
		));
	}

	public boolean confirmEmail(int targetUser, String email) {
		return this.dataService.serviceQuery(String.format("SELECT 1 FROM USERS WHERE USER_ID = '%d' AND USER_EMAIL = '%s'", targetUser, email)).getRows().size() > 0;
	}

	public void updateFriendRequest(int sourceUser, int targetUser) throws Exception {
        this.dataService.runServiceQuery(String.format("UPDATE USER_FRIENDS SET FRIENDS_CONFIRMED = '1', LAST_UPDATED_DATE = CURRENT_TIMESTAMP WHERE SOURCE_USER_ID = '%d' AND TARGET_USER_ID = '%d'",
			sourceUser,
            targetUser
        ));
    }

	public void removeFriend(int sourceUser, int targetUser) throws Exception {
		this.dataService.runServiceQuery(String.format("DELETE FROM USER_FRIENDS WHERE (SOURCE_USER_ID = '%d' AND TARGET_USER_ID = '%d') OR (SOURCE_USER_ID = '%d' AND TARGET_USER_ID = '%d')",
			sourceUser,
			targetUser,
			targetUser,
			sourceUser));
	}

	public String[] listFriendRequests(int userId) {
		ArrayList<String> rst = new ArrayList<String>();
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT * FROM USER_FRIENDS WHERE (SOURCE_USER_ID = '%d' OR TARGET_USER_ID = '%d')", userId, userId));
		for (DataRow row : tab.getRows()) {
			boolean areFriends = this.authService.areFriends(Integer.parseInt(row.getColumn("source_user_id").getColumnValue()), Integer.parseInt(row.getColumn("target_user_id").getColumnValue()));
			String userFullName = "";
			int direction = 0;
			if (Integer.parseInt(row.getColumn("source_user_id").getColumnValue()) == userId) {
				User user = this.authService.getUser(Integer.parseInt(row.getColumn("target_user_id").getColumnValue()));
				userFullName = String.format("%s %s", user.getFirstName(), user.getLastName());
				direction = 1;
			} else {
				User user = this.authService.getUser(Integer.parseInt(row.getColumn("source_user_id").getColumnValue()));
                userFullName = String.format("%s %s", user.getFirstName(), user.getLastName());
			}
			rst.add(String.format("%s:%s:%d:%s:%d", row.getColumn("source_user_id").getColumnValue(), row.getColumn("target_user_id").getColumnValue(), (areFriends ? 1 : 0), userFullName, direction));
		}
		return rst.toArray(new String[rst.size()]);
	}
}


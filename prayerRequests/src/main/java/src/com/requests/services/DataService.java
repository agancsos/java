package com.requests.services;
import com.data.types.DataTable;
import com.data.DataConnectionPostgreSQL;

class DataService {
	private ConfigurationService configService = null;
	private static DataService instance        = null;

	private DataService() {
		this.configService = ConfigurationService.getInstance("");
	}

	public static DataService getInstance() {
		if (DataService.instance == null) {
			DataService.instance = new DataService();
		}
		return DataService.instance;
	}

	public void createSchema() {
		DataConnectionPostgreSQL conn = this.getConnection();
		conn.setDatabaseUsername((String)this.configService.getProperty("database.dbaUsername", ""));
		conn.setDatabasePassword(SecurityService.getBase64Decoded((String)this.configService.getProperty("database.dbaPassword", "")));
		conn.setDatabaseName("postgres");
		String[] adminQueries = new String[] {
			String.format("CREATE DATABASE %s", (String)this.configService.getProperty("database.database", "")),
			String.format("CREATE USER %s with password '%s'", (String)this.configService.getProperty("database.username", ""), SecurityService.getBase64Decoded((String)this.configService.getProperty("database.password", ""))),
			String.format("GRANT ALL PRIVILEGES ON DATABASE %s to %s", (String)this.configService.getProperty("database.database", ""), (String)this.configService.getProperty("database.username", ""))
		};
		
		String[] queries = new String[] {
			"CREATE TABLE USERS(USER_ID SERIAL NOT NULL PRIMARY KEY, USER_FIRSTNAME VARCHAR(255) NOT NULL, USER_LASTNAME VARCHAR(400) NOT NULL, USER_EMAIL VARCHAR(400) NOT NULL, USER_PASSWORD VARCHAR(4000) NOT NULL, USER_STATUS INTEGER DEFAULT '1', LAST_UPDATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP)",
			"CREATE TABLE SESSIONS(SESSION_ID SERIAL NOT NULL PRIMARY KEY, USER_ID INTEGER NOT NULL, SESSION_TOKEN VARCHAR(120) NOT NULL, LAST_UPDATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID))",
			"CREATE TABLE USER_FRIENDS (SOURCE_USER_ID INTEGER NOT NULL, TARGET_USER_ID INTEGER NOT NULL, FRIENDS_CONFIRMED INTEGER DEFAULT '0', LAST_UPDATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY (SOURCE_USER_ID, TARGET_USER_ID), FOREIGN KEY (SOURCE_USER_ID) REFERENCES USERS(USER_ID), FOREIGN KEY (TARGET_USER_ID) REFERENCES USERS (USER_ID))",
			"CREATE TABLE PRAYER_REQUESTS(PRAYER_REQUEST_ID SERIAL NOT NULL PRIMARY KEY, USER_ID INTEGER NOT NULL, PRAYER_REQUEST_ISPUBLIC INTEGER DEFAULT '0', PRAYER_REQUEST_ISSHARED INTEGER DEFAULT '0', PRAYER_REQUEST_METHOD VARCHAR(10) DEFAULT 'CONSOLE', PRAYER_REQUEST_STATUS VARCHAR(255) DEFAULT 'PRAYING', PRAYER_REQUEST_TEXT VARCHAR(4000) NOT NULL, PRAYER_REQUEST_PROCESSED INTEGER DEFAULT '0', LAST_UPDATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
		};

		for (String query : adminQueries) {
			conn.runQuery(query);
		}

		conn.setDatabaseName((String)this.configService.getProperty("database.database", ""));
        conn.runQuery(String.format("GRANT ALL PRIVILEGES ON SCHEMA public TO %s", (String)this.configService.getProperty("database.username", "")));
		
		conn = this.getConnection();
		for (String query : queries) {
			conn.runQuery(query);
		}
	}

	private DataConnectionPostgreSQL getConnection() {
		DataConnectionPostgreSQL conn = new DataConnectionPostgreSQL();
		conn.setDatabaseHost((String)this.configService.getProperty("database.server", ""));
		conn.setDatabaseName((String)this.configService.getProperty("database.database", ""));
		conn.setDatabaseUsername((String)this.configService.getProperty("database.username", ""));
		conn.setDatabasePassword(SecurityService.getBase64Decoded((String)this.configService.getProperty("database.password", "")));
		return conn;
	}

	public boolean runServiceQuery(String query) {
		DataConnectionPostgreSQL conn = this.getConnection();
		return conn.runQuery(query);
	}

	public DataTable serviceQuery(String query) {
		DataConnectionPostgreSQL conn = this.getConnection();
		return conn.query(query);
	}
}


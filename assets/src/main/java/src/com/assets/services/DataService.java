package com.assets.services;
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
			"CREATE TABLE ASSETS (ASSET_ID SERIAL NOT NULL PRIMARY KEY, ASSET_IP_ADDRESS VARCHAR(30) NOT NULL, ASSET_DNS_NAME VARCHAR(400) DEFAULT '', ASSET_STATUS VARCHAR(30) DEFAULT 'ACTIVE', CREATED_DATE TIMESTAMP, LAST_UPDATED_DATE DEFAULT CURRENT_TIMESTAMP)"
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

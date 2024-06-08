package com.catalogger.services;
import com.data.types.DataTable;
import com.data.DataConnectionPostgreSQL;
import org.json.JSONObject;
import org.json.JSONArray;
import com.helpers.SystemHelpers;

public class DataService {
	private ConfigurationService configService = null;
	private static DataService instance        = null;

	private DataService() {
		this.configService = ConfigurationService.getInstance(String.format("%sconfig.json", this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath().replace("/WEB-INF/classes", "")));
		this.createSchema();
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
		
		JSONArray queries = null;
		String databasePackagePath = this.configService.getConfigurationPath().replace("config.json", "database.json");
		try {
			String raw = SystemHelpers.readFile(databasePackagePath);
			queries = (new JSONObject(raw)).getJSONObject("packages").getJSONObject("create").getJSONArray("queries");
		} catch (Exception ex) {
		}

		for (String query : adminQueries) {
			conn.runQuery(query);
		}

		conn.setDatabaseName((String)this.configService.getProperty("database.database", ""));
        conn.runQuery(String.format("GRANT ALL PRIVILEGES ON SCHEMA public TO %s", (String)this.configService.getProperty("database.username", "")));
		
		conn = this.getConnection();
		for (int i = 0; i < queries.length(); i++) {
            JSONObject queryObj = queries.getJSONObject(i);
			if (queryObj.getString("query").toLowerCase().contains("insert into groups")) {
				String groupName = queryObj.getString("query").split("'")[1];
				if (conn.query(String.format("SELECT 1 FROM GROUPS WHERE GROUP_LABEL = '%s'", groupName)).getRows().size() > 0) {
					continue;
				}
			}
            conn.runQuery(queryObj.getString("query"));
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



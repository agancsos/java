package com.jmagent.services;
import com.data.IDataConnection;
import com.data.DataConnectionSQLite;
import com.data.types.DataTable;

public class DataService {
    private String userDataPath;
    private static DataService instance;
    private IDataConnection handler;

    private DataService() {
        this.handler = new DataConnectionSQLite();
        ((DataConnectionSQLite)this.handler).setDatabaseFile(String.format("%s/jobs.jm", this.userDataPath));
    }

    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }

    public boolean runServiceQuery(String query) {
        return this.handler.runQuery(query);
    }

    public DataTable serviceQuery(String query) {
        return this.handler.query(query);
    }

    public void setUserDataPath(String path) {
        this.userDataPath = path; 
        ((DataConnectionSQLite)this.handler).setDatabaseFile(String.format("%s/jobs.jm", this.userDataPath));
    }
    public String getUserDataPath() { return this.userDataPath; }
}
package com.jmagent.services;
import com.data.types.DataRow;
import com.data.types.DataTable;
import com.jmagent.models.Profile;
import com.jmagent.models.Filter;

public class ProfileService {
    private DataService data;
    private static ProfileService instance;
    private ConfigurationService cs;

    private ProfileService() {
        this.data = DataService.getInstance();
        this.cs = ConfigurationService.getInstance(null);
    }

    public static ProfileService getInstance() {
        if (instance == null) {
            instance = new ProfileService();
        }
        return instance;
    }

    public Profile getDefaultProfile() {
        Profile p = new Profile();
        DataTable rows = this.data.serviceQuery("SELECT * FROM PROFILE");
        if (rows.getRows().size() > 0) {
            DataRow row = rows.getRows().get(0);
            p.setID(Integer.parseInt(row.getColumn("PROFILE_ID").getColumnValue()));
            p.setName(row.getColumn("PROFILE_NAME").getColumnValue());
            p.setLastUpdatedDate(row.getColumn("LAST_UPDATED_DATE").getColumnValue());
            p.setFilter(new Filter(row.getColumn("PROFILE_FILTER").getColumnValue()));
        }
        return p;
    }
}
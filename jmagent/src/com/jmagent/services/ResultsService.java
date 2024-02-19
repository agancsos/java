package com.jmagent.services;
import com.jmagent.models.Result;

public class ResultsService {
    private DataService data;
    private static ResultsService instance;
    private ConfigurationService cs;

    private ResultsService() {
        this.data = DataService.getInstance();
        this.cs = ConfigurationService.getInstance(null);
    }

    public static ResultsService getInstance() {
        if (instance == null) {
            instance = new ResultsService();
        }
        return instance;
    }

    public boolean contains(Result result) {
		return this.data.serviceQuery(String.format("SELECT 1 FROM COMPANY WHERE RESULT_SOURCE_ENDPOINT = '%s'", result.getSourceEndpoint())).getRows().size() > 0;
    }

    public Integer add(Result result) {
		if (!this.contains(result) && !this.cs.getProperty("safeMode").equals("1")) {
            if (this.data.runServiceQuery("INSERT INTO RESULT (PROFILE_ID, RESULT_CODE, RESULT_SOURCE_ENDPOINT, COMPANY_ID, POSTED_DATE, RESULT_APPLIED, RESULT_TITLE, RESULT_DESCRIPTION, RESULT_REQUIRED, RESULT_OPTIONAL, RESULT_BENEFITS, RESULT_OTHERDETAILS, RESULT_MIN_YEARS_NEEDED) VALUES ("
                + "'" + result.getProfile().getID().toString() + "'"
                + ",'" + result.getCode() + "'"
                + ",'" + result.getSourceEndpoint() + "'"
                + ",'" + result.getCompany().getID().toString() + "'"
                + ",'" + result.getPostedDate() + "'"
                + ",'" + (result.getApplied() ? "1" : "0") + "'"
				+ ",'" + result.getTitle() + "'"
				+ ",'" + result.getDescription().replace("'", "''") + "'"
				+ ",'" + result.getRequired() + "'"
				+ ",'" + result.getOptional() + "'"
				+ ",'" + result.getBenefits() + "'"
				+ ",'" + result.getOtherDetails() + "'"
				+ ",'" + result.getMinYearsNeeded().toString() + "'"
				+ ")")) {
                return Integer.parseInt(this.data.serviceQuery(String.format("SELECT RESULT_ID FROM RESULT WHERE RESULT_SOURCE_ENDPOINT = '%s'",
                    result.getSourceEndpoint())).getRows().get(0).getColumn("RESULT_ID").getColumnValue());
            }
        }
        return -1;
    }
}

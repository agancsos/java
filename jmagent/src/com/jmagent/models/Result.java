package com.jmagent.models;

public class Result implements Item {
    private Integer resultId = -1;
    private Profile profile = new Profile();
    private String code = "";
    private Company company = new Company();
    private String lastUpdatedDate = "";
    private String postedDate = "";
    private boolean applied = false;
    private String title = "";
    private String description = "";
    private String required = "";
    private String optional = "";
    private String benefits = "";
    private String otherDetails = "";
    private Integer minYearsNeeded = 0;
    private String sourceEndpoint = "";
    private String baseEndpoint = "";
    private ResultState state = ResultState.NEW;

    public Result() {

    }

    public Result(String json) {

    }

    public String toJsonString() {
        String result = "{";
        result += String.format("\"id\":\"%d\"", this.resultId);
        result += String.format(",\"code\":\"%s\"", this.code);
        result += String.format(",\"minYears\":\"%d\"", this.minYearsNeeded);
        result += String.format(",\"state\":\"%d\"", this.state.ordinal());
        result += String.format(",\"applied\":\"%s\"", this.applied ? "1" : "0");
        result += String.format(",\"title\":\"%s\"", this.title);
        result += String.format(",\"description\":\"%s\"", this.description);
        result += String.format(",\"required\":\"%s\"", this.required);
        result += String.format(",\"optional\":\"%s\"", this.optional);
        result += String.format(",\"benefits\":\"%s\"", this.benefits);
        result += String.format(",\"otherDetails\":\"%s\"", this.otherDetails);
        result += String.format(",\"lastUpdatedDate\":\"%s\"", this.lastUpdatedDate);
        result += String.format(",\"sourceEndpoint\":\"%s\"", this.sourceEndpoint);
        result += String.format(",\"company\":%s", this.company.toJsonString());
        result += "}";
        return result;
    }

    public void setID(Integer id) { this.resultId = id; }
    public void setProfile(Profile profile) { this.profile = profile; }
    public void setCode(String code) { this.code = code; }
    public void setCompany(Company company) { this.company = company; }
    public void setLastUpdatedDate(String lastUpdated) { this.lastUpdatedDate = lastUpdated; }
    public void setPostedDate(String postedDate) { this.postedDate = postedDate; }
    public void setApplied(boolean applied) { this.applied = applied; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setRequired(String required) { this.required = required; }
    public void setOptional(String optional) { this.optional = optional; }
    public void setBenefits(String benefits) { this.benefits = benefits; }
    public void setOtherDetails(String otherDetails) { this.otherDetails = otherDetails; }
    public void setMinYearsNeeded(Integer minYears) { this.minYearsNeeded = minYears; }
    public void setSourceEndpoint(String endpoint) { this.sourceEndpoint = endpoint; }
    public void setBaseEndpoint(String endpoint) { this.baseEndpoint = endpoint; }
    public void setState(ResultState state) { this.state = state; }
    public Integer getID() { return this.resultId; }
    public Profile getProfile() { return this.profile; }
    public String getCode() { return this.code; }
    public Company getCompany() { return this.company; }
    public String getLastUpdatedDate() { return this.lastUpdatedDate; }
    public String getPostedDate() { return this.postedDate; }
    public boolean getApplied() { return this.applied; }
    public String getTitle() { return this.title; }
    public String getDescription() { return this.description; }
    public String getRequired() { return this.required; }
    public String getOptional() { return this.optional; }
    public String getBenefits() { return this.benefits; }
    public String getOtherDetails() { return this.otherDetails; }
    public Integer getMinYearsNeeded() { return this.minYearsNeeded; }
    public String getSourceEndpoint() { return this.sourceEndpoint; }
    public String getBaseEndpoint() { return this.baseEndpoint; }
    public ResultState getState() { return this.state; }
}

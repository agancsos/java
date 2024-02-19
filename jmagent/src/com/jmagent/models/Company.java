package com.jmagent.models;

public class Company implements Item {
    private Integer companyId;
    private String name;
    private String street;
    private String city;
    private String state;
    private String country;
    private String description;
    private String industry;
    private Integer employees;
    private Float rating;
    private boolean isPublic;
    private String lastUpdatedDate;
    private String applicantEndpoint;

    public Company() {
		this.employees = 0;
		this.rating = 0.0F;
    }

    public Company(String json) {

    }

    public String toJsonString() {
        String result = "{";
        result += String.format("\"id\":\"%d\"", this.companyId);
        result += String.format(",\"name\":\"%s\"", this.name);
        result += String.format(",\"street\":\"%s\"", this.street);
        result += String.format(",\"city\":\"%s\"", this.city);
        result += String.format(",\"state\":\"%s\"", this.state);
        result += String.format(",\"country\":\"%s\"", this.country);
        result += String.format(",\"description\":\"%s\"", this.description);
        result += String.format(",\"industry\":\"%s\"", this.industry);
        result += String.format(",\"employees\":\"%d\"", this.employees);
        result += String.format(",\"rating\":\"%f\"", this.rating);
        result += String.format(",\"isPublic\":\"%s\"", this.isPublic ? "1" : "0");
        result += String.format(",\"lastUpdatedDate\":\"%s\"", this.lastUpdatedDate);
        result += String.format(",\"applicantEndpoint\":\"%s\"", this.applicantEndpoint);
        result += "}";
        return result;
    }
    public void setID(int id) { this.companyId = id; }
    public void setName(String name) { this.name = name; }
    public void setStreet(String street) { this.street = street; }
    public void setCity(String city) { this.city = city; }
    public void setState(String state) { this.state = state; }
    public void setCountry(String country) { this.country = country; }
    public void setDescription(String description) { this.description = description; }
    public void setIndustry(String industry) { this.industry = industry; }
    public void setEmployees(int employees) { this.employees = employees; }
    public void setRating(Float rating) { this.rating = rating; }
    public void setIsPublic(boolean isPublic) { this.isPublic = isPublic; }
    public void setLastUpdatedDate(String updateDate) { this.lastUpdatedDate = updateDate; }
    public void setApplicantEndpoint(String endpoint) { this.applicantEndpoint = endpoint; }
    public Integer getID() { return this.companyId; }
    public String getName() { return this.name; }
    public String getStreet() { return this.street; }
    public String getCity() { return this.city; }
    public String getState() { return this.state; }
    public String getCountry() { return this.country; }
    public String getDescription() { return this.description; }
    public String getIndustry() { return this.industry; }
    public Integer getEmployees() { return this.employees; }
    public Float getRating() { return this.rating; }
    public boolean getIsPublic() { return this.isPublic; }
    public String getLastUpdatedDate() { return this.lastUpdatedDate; }
    public String getApplicantEndpoint() { return this.applicantEndpoint; }
}

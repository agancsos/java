package com.jmagent.models;

public class Profile implements Item {
    private Integer profileId = -1;
    private String name = "";
    private String lastUpdatedDate = "";
    private Filter filter = new Filter();

    public Profile() {
    }

    public Profile(String json) {

    }

    public String toJsonString() {
        return "";
    }

    public void setID(Integer id) { this.profileId = id; }
    public void setName(String name) { this.name = name; }
    public void setLastUpdatedDate(String updatedDate) { this.lastUpdatedDate = updatedDate; }
    public void setFilter(Filter filter) { this.filter = filter; }
    public Integer getID() { return this.profileId; }
    public String getName() { return this.name; }
    public String getLastUpdatedDate() { return this.lastUpdatedDate; }
    public Filter getFilter() { return this.filter; }
}
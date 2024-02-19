package com.jmagent.models;
import org.w3c.dom.*;

public class IEntry {
    protected String company;
    protected String location;
    protected String title;
    protected String details;
    protected String endpoint;
    protected String code;
    public String getCompany() { return this.company; }
    public String getLocation() { return this.location; }
    public String getTitle() { return this.title; }
    public String getDetails() { return this.details; }
    public String getEndpoint() { return this.endpoint; }
    public String getCode() { return this.code; }
    public void setCompany(String company) { this.company = company; }
    public void setLocation(String location) { this.location = location; }
    public void setTitle(String title) { this.title = title; }
    public void setDetails(String details) { this.details = details; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public void setCode(String code) { this.code = code; }
}
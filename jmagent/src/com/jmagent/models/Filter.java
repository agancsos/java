package com.jmagent.models;
import org.json.*;

public class Filter implements Item {
    private String keywords = "";
    private String location = "Remote";
    private Double compensation = 0.0;
    private Double distance = 0.0;
    private String jobType = "";
    private String tech = "";
    private String level = "";

    public Filter() {

    }

    public Filter(String json) {
        JSONObject jsonObject = new JSONObject(json);
        this.keywords = (String)jsonObject.get("keywords");
        this.location = (String)jsonObject.get("location");
        this.compensation = Double.parseDouble((String)jsonObject.get("compensation"));
        this.distance = Double.parseDouble((String)jsonObject.get("distance"));
        this.jobType = (String)jsonObject.get("jobType");
        this.tech = (String)jsonObject.get("tech");
        this.level = jsonObject.has("level") ? (String)jsonObject.get("level") : "";
    }

    public String toJsonString() {
        String result = "{";
        result += String.format("\"keywords\":\"%s\"", this.keywords);
        result += String.format(",\"location\":\"%s\"", this.location);
        result += String.format(",\"compensation\":\"%f\"", this.compensation);
        result += String.format(",\"distance\":\"%f\"", this.distance);
        result += String.format(",\"jobType\":\"%s\"", this.jobType);
        result += String.format(",\"tech\":\"%s\"", this.tech);
        result += String.format(",\"level\":\"%s\"", this.level);
        result += "}";
        return result;
    }
    public void setKeywords(String keywords) { this.keywords = keywords; }
    public void setLocation(String location) { this.location = location; }
    public void setCompensation(Double compensation) { this.compensation = compensation; }
    public void setDistance(Double distance) { this.distance = distance; }
    public void setJobType(String jobType) { this.jobType = jobType; }
    public void setTech(String tech) { this.tech = tech; }
    public void setLevel(String level) { this.level = level; }
    public String getKeywords() { return this.keywords; }
    public String getLocation() { return this.location; }
    public Double getCompensation() { return this.compensation; }
    public Double getDistance() { return this.distance; }
    public String getJobType() { return this.jobType; }
    public String getTech() { return this.tech; }
    public String getLevel() { return this.level; }
}
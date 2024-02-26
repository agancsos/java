package com.assets.models;
import org.json.JSONObject;

public class Asset {
	private int assetId            = -1;
	private String createdDate     = "";
	private String lastUpdatedDate = "";
	private String ipAddress       = "";
	private String dnsName         = "";
	private String status          = "";

	public Asset() {
	}

	public Asset(String raw) {
		JSONObject temp = new JSONObject(raw);
        if (temp.has("assetId")) {
            this.assetId = temp.getInt("assetId");
        }
        if (temp.has("ipAddress")) {
            this.ipAddress = temp.getString("ipAddress");
        }
        if (temp.has("createdDate")) {
            this.createdDate = temp.getString("createdDate");
        }
        if (temp.has("lastUpdatedDate")) {
            this.lastUpdatedDate = temp.getString("lastUpdatedDate");
        }
        if (temp.has("dnsName")) {
            this.dnsName = temp.getString("dnsName");
        }
        if (temp.has("status")) {
            this.status = temp.getString("status");
        }
	}

	public String toJsonString() {
		return String.format("{\"assetId\": %d, \"ipAddress\": \"%s\", \"createdDate\": \"%s\", \"lastUpdatedDate\": \"%s\", \"dnsName\": \"%s\", \"status\": \"%s\"}",
            this.assetId,
            this.ipAddress,
            this.createdDate,
            this.lastUpdatedDate,
            this.dnsName,
            this.status
        );
	}

	public int getAssetId() { return this.assetId; }
	public String getCreatedDate() { return this.createdDate; }
	public String getLastUpdatedDate() { return this.lastUpdatedDate; }
	public String getIpAddress() { return this.ipAddress; }
	public String getDnsName() { return this.dnsName; }
	public String getStatus() { return this.status; }

	public void setAssetId(int a) { this.assetId = a; }
	public void setCreatedDate(String a) { this.createdDate = a; }
	public void setLastUpdatedDate(String a) { this.lastUpdatedDate = a; }
	public void setIpAddress(String a) { this.ipAddress = a; }
	public void setDnsName(String a) { this.dnsName = a; }
	public void setStatus(String a) { this.status = a; }
}


package com.requests.models;
import org.json.JSONObject;

public class PrayerRequest {
	private int prayerRequestId    = -1;
	private int userId             = -1;
	private String lastUpdatedDate = "";
	private String method          = "";
	private String status          = "PRAYING";
	private String request         = "";
	private boolean processed      = false;
	private boolean isPublic       = false;
	private boolean isShared       = false;

	public PrayerRequest() {
	}

	public PrayerRequest(String raw) {
		JSONObject temp = new JSONObject(raw);
        if (temp.has("prayerRequestId")) {
            this.prayerRequestId = temp.getInt("prayerRequestId");
        }
		if (temp.has("userId")) {
            this.userId = temp.getInt("userId");
        }
		if (temp.has("isPublic")) {
            this.isPublic = (temp.getInt("isPublic") < 1 ? false : true);
        }
		if (temp.has("isShared")) {
            this.isShared = (temp.getInt("isShared") < 1 ? false : true);
        }
        if (temp.has("method")) {
            this.method = temp.getString("method");
        }
        if (temp.has("status")) {
            this.status = temp.getString("status");
        }
        if (temp.has("lastUpdatedDate")) {
            this.lastUpdatedDate = temp.getString("lastUpdatedDate");
        }
        if (temp.has("request")) {
            this.request = temp.getString("request");
        }
		if (temp.has("processed")) {
			this.processed = (temp.getInt("processed") < 1 ? false : true);
		}
	}

	public String toJsonString() {
		return String.format("{\"prayerRequestId\": %d, \"userId\": %d, \"isPublic\": %d, \"isShared\": %d, \"method\": \"%s\", \"status\": \"%s\", \"lastUpdatedDate\": \"%s\", \"request\": \"%s\", \"processed\": %d}",
            this.prayerRequestId,
			this.userId,
			(this.isPublic ? 1 : 0),
			(this.isShared ? 1 : 0),
            this.method,
            this.status,
            this.lastUpdatedDate,
            this.request,
			(this.processed ? 1 : 0)
        );
	}

	public int getPrayerRequestId() { return this.prayerRequestId; }
	public int getUserId() { return this.userId; }
	public boolean getIsPublic() { return this.isPublic; }
	public boolean getIsShared() { return this.isShared; }
	public String getMethod() { return this.method; }
	public String getLastUpdatedDate() { return this.lastUpdatedDate; }
	public String getStatus() { return this.status; }
	public String getRequest() { return this.request; }
	public boolean getProcessed() { return this.processed; }

	public void setPrayerRequestId(int a) { this.prayerRequestId = a; }
	public void setUserId(int a) { this.userId = a; }
	public void setIsPublic(boolean a) { this.isPublic = a; }
	public void setIsShared(boolean a) { this.isShared = a; }
	public void setMethod(String a) { this.method = a; }
	public void setLastUpdatedDate(String a) { this.lastUpdatedDate = a; }
	public void setStatus(String a) { this.status = a; }
	public void setRequest(String a) { this.request = a; }
	public void setProcessed(boolean a) { this.processed = a; }
}


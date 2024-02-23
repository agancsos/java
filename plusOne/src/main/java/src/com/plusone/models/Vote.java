package com.plusone.models;
import org.json.JSONObject;

public class Vote {
	private int voteId             = -1;
	private int pollId             = -1;
	private int optionId           = -1;
	private String createdDate     = "";
	private String lastUpdatedDate = "";
	private String sourceIp        = "";
	private boolean processed      = false;

	public Vote() {
	}

	public Vote(String raw) {
		JSONObject temp = new JSONObject(raw);
		if (temp.has("voteId")) {
			this.voteId = temp.getInt("voteId");
		}
		if (temp.has("pollId")) {
			this.pollId = temp.getInt("pollId");
		}
		if (temp.has("optionId")) {
			this.optionId = temp.getInt("optionId");
		}
		if (temp.has("createdDate")) {
			this.createdDate = temp.getString("createdDate");
        }
		if (temp.has("lastUpdatedDate")) {
			this.lastUpdatedDate = temp.getString("lastUpdatedDate");
        }
		if (temp.has("sourceIp")) {
			this.sourceIp = temp.getString("sourceIp");
        }
		if (temp.has("processed")) {
			int temp2 = temp.getInt("processed");
			this.processed = (temp2 > 0 ? true : false);
        }
	}

	public String toJsonString() {
		return String.format("{\"voteId\": %d, \"optionId\": %d, \"pollId\": %d, \"sourceIp\": \"%s\", \"createdDate\": \"%s\", \"lastUpdatedDate\": \"%s\", \"processed\": %d}",
			this.voteId,
			this.optionId,
			this.pollId,
			this.sourceIp,
			this.createdDate,
			this.lastUpdatedDate,
			(processed ? 1 : 0)
		);
	}

	public int getVoteId() { return this.voteId; }
	public int getPollId() { return this.pollId; }
	public int getOptionId() { return this.optionId; }
	public String getSourceIp() { return this.sourceIp; }
	public String getCreatedDate() { return this.createdDate; }
	public String getLastUpdatedDate() { return this.lastUpdatedDate; }
	public boolean getProcessed() { return this.processed; }

	public void setVoteId(int a) { this.voteId = a; }
	public void setPollId(int a) { this.pollId = a; }
	public void setOptionId(int a) { this.optionId = a; }
	public void setSourceIp(String a) { this.sourceIp = a; }
	public void setCreatedDate(String a) { this.createdDate = a; }
	public void setLastUpdatedDate(String a) { this.lastUpdatedDate = a; }
	public void setProcessed(boolean a) { this.processed = a; }
}


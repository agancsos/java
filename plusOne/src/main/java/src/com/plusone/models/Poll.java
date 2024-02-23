package com.plusone.models;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Poll {
	private int pollId                   = -1;
	private String text                  = "";
	private HashMap<String, String> options = new HashMap<String, String>();
	private String createdDate           = "";
	private String lastUpdatedDate       = "";

	public Poll() {
	}

	public Poll(String raw) {
		JSONObject temp = new JSONObject(raw);
		if (temp.has("pollId")) {
			this.pollId = temp.getInt("pollId");
		}
		if (temp.has("text")) {
			this.text = temp.getString("text");
		}
		if (temp.has("options")) {
			this.options = (HashMap<String, String>)temp.get("optionId");
		}
		if (temp.has("createdDate")) {
			this.createdDate = temp.getString("createdDate");
        }
		if (temp.has("lastUpdatedDate")) {
			this.lastUpdatedDate = temp.getString("lastUpdatedDate");
        }
	}

	public String toJsonString() {
		String rst = String.format("{\"pollId\": %d, \"text\": \"%s\", \"createdDate\": \"%s\", \"lastUpdatedDate\": \"%s\", \"options\":{",
			this.pollId,
			this.text,
			this.createdDate,
			this.lastUpdatedDate
		);
		int i = 0;
		for (Map.Entry<String, String> pair : this.options.entrySet()) {
			if (i > 0) {
				rst += ",";
			}
			rst += String.format("\"%s\": \"%s\"", pair.getKey(), pair.getValue());
		}
		rst += "}}";
		return rst;
	}

	public int getPollId() { return this.pollId; }
	public String getText() { return this.text; }
	public String getCreatedDate() { return this.createdDate; }
	public String getLastUpdatedDate() { return this.lastUpdatedDate; }
	public HashMap<String, String> getOptions() { return this.options; }

	public void setPollId(int a) { this.pollId = a; }
	public void setText(String a) { this.text = a; }
	public void setCreatedDate(String a) { this.createdDate = a; }
	public void setLastUpdatedDate(String a) { this.lastUpdatedDate = a; }
	public void setOptions(HashMap<String, String> a) { this.options = a; }
}


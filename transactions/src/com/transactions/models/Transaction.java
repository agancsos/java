package com.transactions.models;
import org.json.JSONObject;

public class Transaction {
	private int transactionId      = -1;
	private String symbol          = "";
	private String createdDate     = "";
	private String lastUpdatedDate = "";
	private double dollarAmount    = 0.00;
	private boolean processed      = false;

	public Transaction() {
	}

	public Transaction(String raw) {
		JSONObject temp = new JSONObject(raw);
		if (temp.has("transactionId")) {
			this.transactionId = temp.getInt("transactionId");
		}
		if (temp.has("symbol")) {
			this.symbol = temp.getString("symbol");
        }
		if (temp.has("createdDate")) {
			this.createdDate = temp.getString("createdDate");
        }
		if (temp.has("lastUpdatedDate")) {
			this.lastUpdatedDate = temp.getString("lastUpdatedDate");
        }
		if (temp.has("amount")) {
			this.dollarAmount = temp.getDouble("amount");
        }
		if (temp.has("processed")) {
			int temp2 = temp.getInt("processed");
			this.processed = (temp2 > 0 ? true : false);
        }
	}

	public String toJsonString() {
		return String.format("{\"transactionId\": %d, \"symbol\": \"%s\", \"createdDate\": \"%s\", \"lastUpdatedDate\": \"%s\", \"amount\": %f, \"processed\": %d}",
			this.transactionId,
			this.symbol,
			this.createdDate,
			this.lastUpdatedDate,
			this.dollarAmount,
			(processed ? 1 : 0)
		);
	}

	public int getTransactionId() { return this.transactionId; }
	public String getSymbol() { return this.symbol; }
	public String getCreatedDate() { return this.createdDate; }
	public String getLastUpdatedDate() { return this.lastUpdatedDate; }
	public double getDollarAmount() { return this.dollarAmount; }
	public boolean getProcessed() { return this.processed; }

	public void setTransactionId(int a) { this.transactionId = a; }
	public void setSymbol(String a) { this.symbol = a; }
	public void setCreatedDate(String a) { this.createdDate = a; }
	public void setLastUpdatedDate(String a) { this.lastUpdatedDate = a; }
	public void setDollarAmount(double a) { this.dollarAmount = a; }
	public void setProcessed(boolean a) { this.processed = a; }
}


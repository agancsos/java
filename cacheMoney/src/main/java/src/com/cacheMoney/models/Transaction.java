package com.cacheMoney.models;
import org.json.JSONObject;

public class Transaction {
	private int transactionId      = -1;
	private int accountId          = -1;
	private String lastUpdatedDate = "";
	private double amount          = 0.00;
	private double balance         = 0.00;
	private String method          = "";
	private int direction          = 0;
	private boolean processed      = false;

	public Transaction() {
	}

	public Transaction(String raw) {
		JSONObject temp = new JSONObject(raw);
        if (temp.has("transactionId")) {
            this.transactionId = temp.getInt("transactionId");
        }
		if (temp.has("accountId")) {
			this.accountId = temp.getInt("accountId");
		}
        if (temp.has("method")) {
            this.method = temp.getString("method");
        }
        if (temp.has("direction")) {
            this.direction = temp.getInt("direction");
        }
        if (temp.has("lastUpdatedDate")) {
            this.lastUpdatedDate = temp.getString("lastUpdatedDate");
        }
        if (temp.has("amount")) {
            this.amount = temp.getDouble("amount");
        }
		if (temp.has("balance")) {
			this.balance = temp.getDouble("balance");
		}
		if (temp.has("processed")) {
			this.processed = (temp.getInt("processed") < 1 ? false : true);
		}
	}

	public String toJsonString() {
		return String.format("{\"transactionId\": %d, \"accountId\": %d, \"method\": \"%s\", \"direction\": %d, \"lastUpdatedDate\": \"%s\", \"amount\": %f, \"balance\": %f, \"processed\": %d}",
            this.transactionId,
			this.accountId,
            this.method,
            this.direction,
            this.lastUpdatedDate,
            this.amount,
			this.balance,
			(this.processed ? 1 : 0)
        );
	}

	public int getTransactionId() { return this.transactionId; }
	public int getAccountId() { return this.accountId; }
	public String getMethod() { return this.method; }
	public String getLastUpdatedDate() { return this.lastUpdatedDate; }
	public int getDirection() { return this.direction; }
	public double getAmount() { return this.amount; }
	public double getBalance() { return this.balance; }
	public boolean getProcessed() { return this.processed; }

	public void setTransactionId(int a) { this.transactionId = a; }
	public void setAccountId(int a) { this.accountId = a; }
	public void setMethod(String a) { this.method = a; }
	public void setLastUpdatedDate(String a) { this.lastUpdatedDate = a; }
	public void setDirection(int a) { this.direction = a; }
	public void setAmount(double a) { this.amount = a; }
	public void setBalance(double a) { this.balance = a; }
	public void setProcessed(boolean a) { this.processed = a; }
}


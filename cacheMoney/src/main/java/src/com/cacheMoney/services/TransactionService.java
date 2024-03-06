package com.cacheMoney.services;
import com.cacheMoney.models.Transaction;
import java.util.ArrayList;
import com.data.types.DataTable;
import com.data.types.DataRow;
import com.data.types.DataColumn;

public class TransactionService {
	private ConfigurationService configService = null;
	private static TransactionService instance = null;
	private MessageService msgService          = null;
	private DataService dataService            = null;
	private CacheService cacheService          = null;

	private TransactionService() {
		this.configService = ConfigurationService.getInstance("");
		this.msgService    = MessageService.getInstance();
		this.dataService   = DataService.getInstance();
		this.cacheService  = CacheService.getInstance();
		this.dataService.createSchema();
	}

	public static TransactionService getInstance() {
		if (TransactionService.instance == null) {
			TransactionService.instance = new TransactionService();
		}
		return TransactionService.instance;
	}

	public void pushTransaction(Transaction transaction) throws Exception {
        if (!(Boolean)this.configService.getProperty("mq.disabled", false) && this.msgService.isReachable(false)) {
            this.msgService.putMessagege((String)this.configService.getProperty("mq.queue", ""), transaction.toJsonString());
        } else {
            this.addTransaction(transaction);
        }
    }

	public void addTransaction(Transaction transaction) throws Exception {
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT TRANSACTION_DIRECTION, TRANSACTION_AMOUNT FROM TRANSACTIONS WHERE ACCOUNT_ID = '%d'", transaction.getAccountId()));
		double balance = 0.00;
		for (DataRow row : tab.getRows()) {
			int direction = Integer.parseInt(row.getColumn("transaction_direction").getColumnValue());
			double amount = Double.parseDouble(row.getColumn("transaction_amount").getColumnValue());
			if (direction > 0) {
				balance += amount;
			} else {
				balance -= amount;
			}
		}
		if (transaction.getDirection() > 0) {
			balance += transaction.getAmount();
		} else {
			balance -= transaction.getAmount();
		}
		transaction.setBalance(balance);
		this.dataService.runServiceQuery(String.format("INSERT INTO TRANSACTIONS (ACCOUNT_ID, TRANSACTION_METHOD, TRANSACTION_DIRECTION, TRANSACTION_AMOUNT, TRANSACTION_PROCESSED, TRANSACTION_BALANCE) VALUES ('%s', '%s', '%d', '%f', '%d', '%f')",
			transaction.getAccountId(),
			transaction.getMethod(),
			transaction.getDirection(),
			transaction.getAmount(),
			(transaction.getProcessed() ? 1 : 0),
			transaction.getBalance()
		));
		this.cacheService.updateCache(transaction.getAccountId(), this.listTransactions(transaction.getAccountId(), false));
	}

	public Transaction getTransaction(int id) {
		Transaction rst = new Transaction();
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT * FROM TRANSACTIONS WHERE TRANSACTION_ID = '%d'", id));
		if (tab.getRows().size() > 0) {
			DataRow row = tab.getRows().get(0);
			rst.setTransactionId(id);
			rst.setAccountId(Integer.parseInt(row.getColumn("account_id").getColumnValue()));
			rst.setMethod(row.getColumn("transaction_method").getColumnValue());
			rst.setLastUpdatedDate(row.getColumn("last_updated_date").getColumnValue());
			rst.setDirection(Integer.parseInt(row.getColumn("transaction_direction").getColumnValue()));
			rst.setAmount(Double.parseDouble(row.getColumn("transaction_amount").getColumnValue()));
			rst.setBalance(Double.parseDouble(row.getColumn("transaction_balance").getColumnValue()));
			rst.setProcessed((Integer.parseInt(row.getColumn("transaction_processed").getColumnValue()) < 1 ? false : true));
		}
		return rst;
	}

	public  Transaction[] listTransactions(int accountId, boolean useCache) {
		ArrayList<Transaction> rst = new ArrayList<Transaction>();
		if (useCache && this.cacheService.contains(accountId)) {
			return this.cacheService.getTransactions(accountId);
		}
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT TRANSACTION_ID FROM TRANSACTIONS WHERE ACCOUNT_ID = '%d' ORDER BY LAST_UPDATED_DATE DESC", accountId));
		for (DataRow row : tab.getRows()) {
			int id = Integer.parseInt(row.getColumn("transaction_id").getColumnValue());
			rst.add(this.getTransaction(id));
		}
		this.cacheService.updateCache(accountId, rst.toArray(new Transaction[rst.size()]));
		return rst.toArray(new Transaction[rst.size()]);
	}
}


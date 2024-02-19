package com.transactions.services;
import com.transactions.models.Transaction;
import java.util.ArrayList;
import com.data.types.DataTable;
import com.data.types.DataRow;
import com.data.types.DataColumn;

public class TransactionService {
	private ConfigurationService configService = null;
	private static TransactionService instance = null;
	private MessageService msgService          = null;
	private DataService dataService            = null;

	private TransactionService() {
		this.configService = ConfigurationService.getInstance("");
		this.msgService    = MessageService.getInstance();
		this.dataService   = DataService.getInstance();
		this.dataService.createSchema();
	}

	public static TransactionService getInstance() {
		if (TransactionService.instance == null) {
			TransactionService.instance = new TransactionService();
		}
		return TransactionService.instance;
	}

	public void addTransaction(Transaction trans) throws Exception {
		this.dataService.runServiceQuery(String.format("INSERT INTO TRANSACTIONS (TRANSACTION_SYMBOL, CREATED_DATE, TRANSACTION_AMOUNT, TRANSACTION_PROCESSED) VALUES ('%s', CURRENT_TIMESTAMP, '%f', '%d')",
			trans.getSymbol(),
			trans.getDollarAmount(),
			(trans.getProcessed() ? 1 : 0)
		));
	}

	public void pushTransaction(Transaction trans) throws Exception {
		if (this.msgService.isReachable(false)) {
			this.msgService.putMessagege((String)this.configService.getProperty("mq.queue", ""), trans.toJsonString());
		} else {
			this.addTransaction(trans);
		}
	}

	public Transaction getTransaction(int id) {
		Transaction rst = new Transaction();
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT * FROM TRANSACTIONS WHERE TRANSACTION_ID = '%d'", id));
		if (tab.getRows().size() > 0) {
			DataRow row = tab.getRows().get(0);
			rst.setTransactionId(id);
			rst.setSymbol(row.getColumn("transaction_symbol").getColumnValue());
			rst.setCreatedDate(row.getColumn("created_date").getColumnValue());
			rst.setLastUpdatedDate(row.getColumn("last_updated_date").getColumnValue());
			rst.setProcessed((Integer.parseInt(row.getColumn("transaction_processed").getColumnValue()) > 0 ? true : false));
			rst.setDollarAmount(Double.parseDouble(row.getColumn("transaction_amount").getColumnValue()));
		}
		return rst;
	}

	public  Transaction[] listTransactions(String symbol) {
		ArrayList<Transaction> rst = new ArrayList<Transaction>();
		DataTable tab = this.dataService.serviceQuery(String.format("SELECT TRANSACTION_ID FROM TRANSACTIONS WHERE TRANSACTION_SYMBOL = '%s'", symbol));
		for (DataRow row : tab.getRows()) {
			int id = Integer.parseInt(row.getColumn("transaction_id").getColumnValue());
			rst.add(this.getTransaction(id));
		}
		return rst.toArray(new Transaction[rst.size()]);
	}
}


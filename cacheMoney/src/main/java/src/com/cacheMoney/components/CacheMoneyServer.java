package com.cacheMoney.components;
import com.cacheMoney.services.ConfigurationService;
import com.cacheMoney.services.TransactionService;
import com.cacheMoney.services.MessageService;
import com.cacheMoney.models.Transaction;
import java.lang.Thread;
import javax.jms.Message;
import javax.jms.TextMessage;

public class CacheMoneyServer implements Runnable {
	private ConfigurationService configService    = null;
	private TransactionService transactionService = null;
    private MessageService msgService             = null;
	private String[] springArgs;

    public CacheMoneyServer(String path, String[] args) {
        this.readConfig(path);
		this.springArgs = args;
		this.transactionService = TransactionService.getInstance();
		this.msgService = MessageService.getInstance();
    }

    private void readConfig(String path) {
        this.configService = ConfigurationService.getInstance(path);
    }

	private void poll() {
		if (!(Boolean)this.configService.getProperty("mq.disabled", false) && this.msgService.isReachable(false)) {
            try {
                Message[] messages = this.msgService.readMessages((String)this.configService.getProperty("mq.queue", ""));
                System.out.println(String.format(">>>> Messages: %d", messages.length));
                for (Message msg : messages) {
                    if (msg instanceof TextMessage) {
                        String raw = ((TextMessage) msg).getText();
                        Transaction transaction = new Transaction(raw);
                        transaction.setProcessed(true);
                        this.transactionService.addTransaction(transaction);
                    }
                }
            } catch (Exception ex) {
            }
        }
	}

	public void run() {
		this.poll();
	}

    public void start() {
		new CacheMoneyAPI(this.springArgs).start();

		while (true) {
			new Thread(new CacheMoneyServer("", this.springArgs)).start();
			try {
				Thread.sleep(10000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
    }
}


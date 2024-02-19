package com.transactions.components;
import com.transactions.services.ConfigurationService;
import com.transactions.services.MessageService;
import com.transactions.services.TransactionService;
import com.transactions.models.Transaction;
import java.lang.Thread;
import javax.jms.Message;
import javax.jms.TextMessage;

public class TransactionServer implements Runnable {
	private ConfigurationService configService = null;
	private MessageService msgService          = null;
	private TransactionService transService    = null;
	private String[] springArgs;

    public TransactionServer(String path, String[] args) {
        this.readConfig(path);
		this.springArgs = args;
    }

    private void readConfig(String path) {
        this.configService = ConfigurationService.getInstance(path);
		this.msgService = MessageService.getInstance();
		this.transService = TransactionService.getInstance();
    }

	private void poll() {
		if (this.msgService.isReachable(false)) {
			try {
				Message[] messages = this.msgService.readMessages((String)this.configService.getProperty("mq.queue", ""));
				System.out.println(String.format(">>>> Messages: %d", messages.length));
				for (Message msg : messages) {
					if (msg instanceof TextMessage) {
						String raw = ((TextMessage) msg).getText();
						Transaction trans = new Transaction(raw);
						trans.setProcessed(true);
						this.transService.addTransaction(trans);
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
		new TransactionAPI(this.springArgs).start();

		while (true) {
			new Thread(new TransactionServer("", this.springArgs)).start();
			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
    }
}


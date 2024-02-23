package com.plusone.components;
import com.plusone.services.ConfigurationService;
import com.plusone.services.MessageService;
import com.plusone.services.VoteService;
import com.plusone.models.Vote;
import java.lang.Thread;
import javax.jms.Message;
import javax.jms.TextMessage;

public class PlusOneServer implements Runnable {
	private ConfigurationService configService = null;
	private MessageService msgService          = null;
	private VoteService voteService            = null;
	private String[] springArgs;

    public PlusOneServer(String path, String[] args) {
        this.readConfig(path);
		this.springArgs = args;
    }

    private void readConfig(String path) {
        this.configService = ConfigurationService.getInstance(path);
		this.msgService = MessageService.getInstance();
		this.voteService = VoteService.getInstance();
    }

	private void poll() {
		if (!(Boolean)this.configService.getProperty("mq.disabled", false) && this.msgService.isReachable(false)) {
			try {
				Message[] messages = this.msgService.readMessages((String)this.configService.getProperty("mq.queue", ""));
				System.out.println(String.format(">>>> Messages: %d", messages.length));
				for (Message msg : messages) {
					if (msg instanceof TextMessage) {
						String raw = ((TextMessage) msg).getText();
						Vote vote = new Vote(raw);
						vote.setProcessed(true);
						this.voteService.addVote(vote);
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
		new PlusOneAPI(this.springArgs).start();

		while (true) {
			new Thread(new PlusOneServer("", this.springArgs)).start();
			try {
				Thread.sleep(10000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
    }
}


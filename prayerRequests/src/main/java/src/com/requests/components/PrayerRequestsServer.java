package com.requests.components;
import com.requests.services.ConfigurationService;
import com.requests.services.PrayerRequestService;
import com.requests.services.MessageService;
import com.requests.models.PrayerRequest;
import java.lang.Thread;
import javax.jms.Message;
import javax.jms.TextMessage;

public class PrayerRequestsServer implements Runnable {
	private ConfigurationService configService    = null;
	private PrayerRequestService requestService   = null;
    private MessageService msgService             = null;
	private String[] springArgs;

    public PrayerRequestsServer(String path, String[] args) {
        this.readConfig(path);
		this.springArgs = args;
		this.requestService = PrayerRequestService.getInstance();
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
                        PrayerRequest request = new PrayerRequest(raw);
                        request.setProcessed(true);
                        this.requestService.addPrayerRequest(request);
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
		new PrayerRequestsAPI(this.springArgs).start();

		while (true) {
			new Thread(new PrayerRequestsServer("", this.springArgs)).start();
			try {
				Thread.sleep(10000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
    }
}


package com.catalogger.components;
import com.catalogger.services.DataService;

public class CataloggerServer implements Runnable {
	private String[] springArgs;

	public CataloggerServer(String[] args) {
		this.springArgs        = args;
	}

	public void run() {
	}

	public void start() {
		DataService.getInstance().createSchema();
		new CataloggerAPI(this.springArgs).run();
	}
}


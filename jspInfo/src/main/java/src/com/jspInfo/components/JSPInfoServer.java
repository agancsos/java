package com.jspInfo.components;

public class JSPInfoServer implements Runnable {
	private String[] springArgs;

	public JSPInfoServer(String[] args) {
		this.springArgs        = args;
	}

	public void run() {
	}

	public void start() {
		new JSPInfoAPI(this.springArgs).run();
	}
}


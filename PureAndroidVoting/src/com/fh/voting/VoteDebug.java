package com.fh.voting;

import java.util.List;

import android.content.Context;

public class VoteDebug {
	private SharedPreferenceManager preferences;
	private Server server;
	
	public VoteDebug(Context context) {
        this.preferences = new SharedPreferenceManager(context);
        this.server = new Server(preferences.getPhoneId());
	}
	
	public void ResetClient() {
		this.preferences.reset();
	}
	
	public void createTestData() throws Exception {
		this.server.createTestData();
	}

	public List<String> getServerLogRecords() throws Exception {
		return this.server.getLogRecords();
	}
}

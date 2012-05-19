package com.fh.voting;

import android.app.Application;

import com.fh.voting.model.Cache;

public class VotingApplication extends Application {
	private Cache m_cache = new Cache();
	private SharedPreferenceManager preferences;
	private Server server;

	@Override
	public void onCreate() {
		super.onCreate();
		this.preferences = new SharedPreferenceManager(this);
		this.server = new Server(preferences.getPhoneId());
		this.m_cache.set_server(this.server);
	}

	public Cache get_cache() {
		return m_cache;
	}
}

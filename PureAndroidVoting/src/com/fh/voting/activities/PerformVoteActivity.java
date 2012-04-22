package com.fh.voting.activities;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;

import com.fh.voting.R;
import com.fh.voting.Server;
import com.fh.voting.SharedPreferenceManager;

public class PerformVoteActivity extends Activity {
	private Server server;
	private SharedPreferenceManager preferences;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.perform_vote);

		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);

		this.preferences = new SharedPreferenceManager(this);
		this.server = new Server(preferences.getPhoneId());
	}

}

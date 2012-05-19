package com.fh.voting.activities;

import android.app.Activity;
import android.os.Bundle;

import com.fh.voting.Server;
import com.fh.voting.SharedPreferenceManager;
import com.fh.voting.db.DatabaseHelper;
import com.fh.voting.model.ModelManager;

public abstract class DatabaseWrapperActivity extends Activity {
	protected DatabaseHelper dbHelper = null;
	protected Server server;
	protected SharedPreferenceManager preferences;
	protected ModelManager modelManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (dbHelper == null) {
			dbHelper = new DatabaseHelper(this);
		}

		this.preferences = new SharedPreferenceManager(this);
		this.server = new Server(preferences.getPhoneId());
		this.modelManager = new ModelManager(preferences.getPhoneId(), server, dbHelper);

	}

	@Override
	protected void onPause() {
		super.onPause();
		dbHelper.close();
		dbHelper = null;
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (dbHelper == null) {
			dbHelper = new DatabaseHelper(this);
		}
	}
}

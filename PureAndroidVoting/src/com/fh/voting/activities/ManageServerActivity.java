package com.fh.voting.activities;

import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.fh.voting.R;
import com.fh.voting.VoteDebug;
import com.fh.voting.async.AsyncTaskManager;
import com.fh.voting.async.ITask;
import com.fh.voting.async.TaskExecutor;

public class ManageServerActivity extends Activity {
	private VoteDebug voteDebug;
	private ListView lstLogs;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.manage_server);

		this.voteDebug = new VoteDebug(this);
		this.lstLogs = (ListView) this.findViewById(R.id.lstLogs);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Initiating Menu XML file (main_menu.xml)
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.manage_server_menu, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		// load logs
		this.loadLogsAsync();
	};

	public void onCreateTestData(View v) {
		// create new test data on server
		this.createTestDataAsync();
	}

	public void onRefreshLogs(View v) {
		// reload logs
		this.loadLogsAsync();
	}

	private void loadLogsAsync() {
		// start new async task
		final ManageServerActivity activity = this;
		ITask loader = new ITask() {
			@Override
			public Object onWorkCallback(TaskExecutor executor, Object... params) {
				List<String> logs = activity.loadLogsSync();
				Collections.reverse(logs);
				return logs;
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onCompleteCallback(Object result) {
				activity.onLoadLogs((List<String>) result);
			}
		};

		new AsyncTaskManager(this).execute("Loading info from server...", loader);
	}

	private void onLoadLogs(List<String> logRecords) {
		this.lstLogs.setAdapter(new ArrayAdapter<String>(this, R.layout.log_list_item, logRecords));
	}

	private List<String> loadLogsSync() {
		try {
			return this.voteDebug.getServerLogRecords();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void createTestDataAsync() {
		// start new async task
		final ManageServerActivity activity = this;
		ITask loader = new ITask() {
			@Override
			public Object onWorkCallback(TaskExecutor executor, Object... params) {
				activity.createTestDataSync();
				return null;
			}

			@Override
			public void onCompleteCallback(Object result) {
			}
		};

		new AsyncTaskManager(this).execute("Loading info from server...", loader);
	}

	private void createTestDataSync() {
		try {
			this.voteDebug.createTestData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.refreshLogs:
			this.loadLogsAsync();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

package com.fh.voting.activities;

import java.util.List;

import com.fh.voting.R;
import com.fh.voting.VoteDebug;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ManageServerActivity extends Activity {
	private VoteDebug voteDebug;
	private ListView lstLogs;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.manage_server);
	    
	    this.voteDebug = new VoteDebug(this);
	    this.lstLogs = (ListView)this.findViewById(R.id.lstLogs);
	}
	
    public void onCreateTestData(View v) {
		try {
			this.voteDebug.createTestData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void onRefreshLogs(View v) {
    	try {
			List<String> logRecords = this.voteDebug.getServerLogRecords();
			this.lstLogs.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, logRecords));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

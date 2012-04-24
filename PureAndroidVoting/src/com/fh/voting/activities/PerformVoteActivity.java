package com.fh.voting.activities;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

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

		//customize title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.perform_vote);
	    getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
	    TextView titleText = (TextView)findViewById(R.id.title_text);
        titleText.setText(R.string.app_name);


		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);

		this.preferences = new SharedPreferenceManager(this);
		this.server = new Server(preferences.getPhoneId());
	}

}

package com.fh.voting.activities;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fh.voting.Constants;
import com.fh.voting.R;
import com.fh.voting.model.Vote;
import com.fh.voting.model.VoteOption;

public class PerformVoteActivity extends DatabaseWrapperActivity {
	private int m_voteId = -1;
	private Vote m_vote;

	private LinkedHashMap<Integer, VoteOption> m_options = new LinkedHashMap<Integer, VoteOption>();

	private ListView lstOptions;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// customize title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.perform_vote);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		TextView titleText = (TextView) findViewById(R.id.title_text);
		titleText.setText(R.string.do_vote_activity);

		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);

		// init
		this.lstOptions = (ListView) this.findViewById(R.id.lstOptions);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// restore vote id
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			this.m_voteId = bundle.getInt(Constants.bundle_vote_id, -1);
		}

		if (this.m_voteId < 0) {
			Utils.alertErrorDialog(this, "Failed to load Vote!");
			this.finish();
		}

		try {
			this.m_vote = this.modelManager.getVote(this.m_voteId);
			ArrayList<VoteOption> options = this.modelManager.getOptions(this.m_vote);
			this.m_options.clear();
			ArrayList<String> optionsText = new ArrayList<String>();
			for (VoteOption option : options) {
				this.m_options.put(option.getId(), option);
				optionsText.add(option.getText());
			}

			int layoutId = 0;
			if (this.m_vote.getIsMultipleChoice()) {
				this.lstOptions.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				layoutId = android.R.layout.simple_list_item_multiple_choice;
			} else {
				this.lstOptions.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				layoutId = android.R.layout.simple_list_item_single_choice;
			}
			this.lstOptions.setAdapter(new ArrayAdapter<String>(this, layoutId, optionsText));
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			Utils.alertErrorDialog(this, "Failed to load Vote!");
		}
	}

	public void onSubmit(View v) {
		// save selected vote option(s)
	}
}

package com.fh.voting.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fh.voting.Constants;
import com.fh.voting.R;
import com.fh.voting.model.Vote;
import com.fh.voting.model.VoteChoice;
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
			// redirect
			if (this.m_vote.getStatus() != Vote.Status.Started) {
				Bundle b = new Bundle();
				b.putInt(Constants.bundle_vote_id, this.m_vote.getId());
				this.forwardActivity(VoteOverview.class, b);
			}

			ArrayList<VoteOption> options = this.modelManager.getOptions(this.m_vote);
			this.m_options.clear();
			HashMap<Integer, Integer> lookupIdIndex = new HashMap<Integer, Integer>();
			ArrayList<String> optionsText = new ArrayList<String>();
			for (VoteOption option : options) {
				this.m_options.put(option.getId(), option);
				optionsText.add(option.getText());
				lookupIdIndex.put(option.getId(), optionsText.size() - 1);
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

			// select checked choices
			ArrayList<VoteChoice> choices = this.modelManager.getMyChoices(this.m_vote);
			for (VoteChoice choice : choices) {
				int index = lookupIdIndex.get(choice.getOptionId());
				this.lstOptions.setItemChecked(index, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			Utils.alertErrorDialog(this, "Failed to load Vote!");
		}
	}

	private void forwardActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent(PerformVoteActivity.this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);

		// remove me from history
		this.finish();
	}

	public void onSubmit(View v) {
		// save selected vote option(s)
		ArrayList<VoteOption> options = new ArrayList<VoteOption>();

		Object[] items = this.m_options.values().toArray();

		// get checked
		SparseBooleanArray checked = this.lstOptions.getCheckedItemPositions();
		int len = this.lstOptions.getCount();
		for (int i = 0; i < len; i++) {
			if (checked.get(i)) {
				// get index, store id
				options.add((VoteOption) items[i]);
			}
		}

		// check
		if (options.size() <= 0) {
			Utils.alertDialog(this, "At least one item shall be selected!", null);
			return;
		}

		try {
			this.modelManager.performVote(this.m_vote, options);
			this.finish();
		} catch (Exception e) {
			e.printStackTrace();
			Utils.alertErrorDialog(this, "Failed to Vote. Try again, it may work then...");
		}
	}
}

package com.fh.voting.activities;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.fh.voting.Constants;
import com.fh.voting.R;
import com.fh.voting.model.Vote;
import com.fh.voting.model.VoteResult;
import com.fh.voting.parsers.DateTimeConverter;

public class VoteOverview extends DatabaseWrapperActivity {
	private int m_voteId = -1;
	private Vote m_vote;
	private VoteResult m_result;

	private Button btnSubmit;
	private TextView txtDuration;
	private TextView txtVoteTitle;
	private TextView txtVoteDetail;
	private TextView txtStatus;
	private TextView txtVotes;
	private TextView txtWinner;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// customize title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.vote_overview);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		TextView titleText = (TextView) findViewById(R.id.title_text);
		titleText.setText(R.string.vote_overview_activity);

		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);

		// init
		this.btnSubmit = (Button) this.findViewById(R.id.btnSubmit);
		this.txtDuration = (TextView) this.findViewById(R.id.txtDuration);
		this.txtVoteTitle = (TextView) this.findViewById(R.id.txtVoteTitle);
		this.txtVoteDetail = (TextView) this.findViewById(R.id.txtVoteDetail);
		this.txtStatus = (TextView) this.findViewById(R.id.txtStatus);
		this.txtVotes = (TextView) this.findViewById(R.id.txtVotes);
		this.txtWinner = (TextView) this.findViewById(R.id.txtWinner);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// restore vote id
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			this.m_voteId = bundle.getInt(Constants.bundle_vote_id, -1);
		}

		this.loadVote();
	}

	private void loadVote() {
		try {
			this.m_vote = this.modelManager.getVote(this.m_voteId);
			this.m_result = this.modelManager.getVoteResult(this.m_vote);
		} catch (Exception e) {
			e.printStackTrace();
			Utils.alertErrorDialog(this, "Failed to load Vote");
		}

		this.txtVoteTitle.setText(this.m_vote.getTitle());
		this.txtVoteDetail.setText(this.m_vote.getText());

		// duration
		this.txtDuration.setText(String.format("From %s to %s",
				DateTimeConverter.toDisplayString(this.m_vote.getStartDate()),
				DateTimeConverter.toDisplayString(this.m_vote.getEndDate())));

		// status
		String status = "New";
		if (this.m_vote.getStatus() == Vote.Status.Public) {
			status = "Not started";
		}
		if (this.m_vote.getStatus() == Vote.Status.Started) {
			status = "Active";
		}
		if (this.m_vote.getStatus() == Vote.Status.Ended) {
			status = "Ended";
		}
		this.txtStatus.setText(status);

		// submission
		this.txtVotes.setText(String.format("%d submission(s)", this.m_result.getAll().size()));

		// winner
		this.txtWinner.setText(String.format("\"%s\" winning with %d submissions", this.m_result.getWinnerText(),
				this.m_result.getMaxScore()));

		// enable/disable button
		this.btnSubmit.setEnabled(this.m_vote.getStatus() == Vote.Status.Started);
	}

	public void onSubmit(View v) {
		// navigate to voting activity
		Bundle bundle = new Bundle();
		bundle.putInt(Constants.bundle_vote_id, this.m_vote.getId());
		this.openActivity(PerformVoteActivity.class, bundle);
	}

	private void openActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent(VoteOverview.this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}
}

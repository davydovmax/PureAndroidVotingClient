package com.fh.voting.activities;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.fh.voting.Constants;
import com.fh.voting.R;
import com.fh.voting.model.Vote;

public class ManageVoteActivity extends DatabaseWrapperActivity {
	private int m_voteId = -1;
	private Vote m_vote;

	Button btnSubmit;
	Button btnCancel;

	EditText txtTitle;
	EditText txtDescription;

	private RadioButton rbIsPublic;
	private RadioButton rbIsPrivate;

	private RadioButton rbIsSingleChoice;
	private RadioButton rbIsMultiChoice;

	// CheckBox chbAgreed;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// customize title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.manage_vote);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

		TextView titleText = (TextView) findViewById(R.id.title_text);
		titleText.setText(R.string.create_vote);

		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);

		// UI
		this.btnSubmit = (Button) this.findViewById(R.id.btnSubmit);
		this.btnCancel = (Button) this.findViewById(R.id.btnCancell);
		this.txtTitle = (EditText) this.findViewById(R.id.txtTitle);
		this.txtDescription = (EditText) this.findViewById(R.id.txtDescription);

		this.rbIsPublic = (RadioButton) this.findViewById(R.id.rbIsPublic);
		this.rbIsPrivate = (RadioButton) this.findViewById(R.id.rbIsPrivate);
		this.rbIsSingleChoice = (RadioButton) this.findViewById(R.id.rbIsSingleChoice);
		this.rbIsMultiChoice = (RadioButton) this.findViewById(R.id.rbIsMultiChoice);

		// this.chbAgreed = (CheckBox) this.findViewById(R.id.chbAgreed);
	}

	@Override
	protected void onResume() {
		super.onResume();

		// restore vote id
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			this.m_voteId = bundle.getInt(Constants.bundle_vote_id, -1);
		}

		// set title and load vote
		TextView titleText = (TextView) findViewById(R.id.title_text);
		if (this.m_voteId >= 0) {
			titleText.setText(R.string.manage_vote);
			this.loadVote();
		}
	}

	private void loadVote() {
		this.m_vote = this.modelManager.getVote(this.m_voteId);
		this.txtTitle.setText(this.m_vote.getTitle());
		this.txtDescription.setText(this.m_vote.getText());

		this.rbIsPublic.setChecked(!this.m_vote.getIsPrivate());
		this.rbIsPrivate.setChecked(this.m_vote.getIsPrivate());

		this.rbIsSingleChoice.setChecked(!this.m_vote.getIsMultipleChoice());
		this.rbIsMultiChoice.setChecked(this.m_vote.getIsMultipleChoice());
	}

	public void onSubmit(View v) {
		// create new vote
		// TODO: check arguments
		String title = this.txtTitle.getText().toString();
		String text = this.txtDescription.getText().toString();
		boolean isPrivate = this.rbIsPrivate.isChecked();
		boolean isMultiChoice = this.rbIsMultiChoice.isChecked();

		Calendar c = new GregorianCalendar();
		c.add(Calendar.DAY_OF_MONTH, 1);
		Date publicationDate = c.getTime();

		c.add(Calendar.DAY_OF_MONTH, 1);
		Date startDate = c.getTime();

		c.add(Calendar.DAY_OF_MONTH, 1);
		Date endDate = c.getTime();

		try {
			this.modelManager.createVote(title, text, isPrivate, isMultiChoice, publicationDate, startDate, endDate);
		} catch (Exception e) {
			// TODO: capture error
			e.printStackTrace();
			return;
		}

		// close activity
		this.finish();
	}

	public void onCancel(View v) {
		// close activity
		this.finish();
	}
}

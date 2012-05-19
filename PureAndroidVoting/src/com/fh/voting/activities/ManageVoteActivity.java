package com.fh.voting.activities;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fh.voting.Constants;
import com.fh.voting.R;
import com.fh.voting.model.Vote;

public class ManageVoteActivity extends DatabaseWrapperActivity {
	private int m_voteId = -1;
	private Vote m_vote;
	private Date m_date;

	private final int DATE_DIALOG = 1103;
	private final int TIME_DIALOG = 1104;

	private Button btnSubmit;
	private Button btnCancel;

	private EditText txtTitle;
	private EditText txtDescription;

	private RadioButton rbIsPublic;
	private RadioButton rbIsPrivate;

	private RadioButton rbIsSingleChoice;
	private RadioButton rbIsMultiChoice;

	private SeekBar seekVoteDuration;
	private TextView txtVoteDuration;

	private Button btnDatePicker;
	private Button btnTimePicker;

	private TimePickerDialog.OnTimeSetListener mTimeSetListener;
	private DatePickerDialog.OnDateSetListener mDateSetListener;

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

		this.rbIsSingleChoice = (RadioButton) this.findViewById(R.id.rbIsSingleChoice);
		this.rbIsMultiChoice = (RadioButton) this.findViewById(R.id.rbIsMultiChoice);

		this.btnDatePicker = (Button) findViewById(R.id.btnDatePicker);
		this.btnTimePicker = (Button) findViewById(R.id.btnTimePicker);

		this.txtVoteDuration = (TextView) findViewById(R.id.txtVoteDuration);
		this.seekVoteDuration = (SeekBar) findViewById(R.id.seekVoteDuration);

		final ManageVoteActivity activity = this;
		this.seekVoteDuration.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				activity.txtVoteDuration.setText(String.format("%d h", progress + 1));
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}
		});
		this.seekVoteDuration.setProgress(1);

		btnDatePicker.setText("");
		btnDatePicker.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG);
			}
		});

		btnTimePicker.setText("");
		btnTimePicker.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_DIALOG);
			}
		});

		// date-time picker handlers
		mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				Calendar c = new GregorianCalendar();
				c.setTime(activity.m_date);
				c.set(Calendar.HOUR_OF_DAY, hourOfDay);
				c.set(Calendar.MINUTE, minute);
				activity.setDate(c.getTime());
			}
		};

		mDateSetListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				Calendar c = new GregorianCalendar();
				c.setTime(activity.m_date);
				c.set(year, monthOfYear, dayOfMonth);
				activity.setDate(c.getTime());
			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();

		this.m_date = Calendar.getInstance().getTime();

		// restore vote id
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			this.m_voteId = bundle.getInt(Constants.bundle_vote_id, -1);
		}

		// set date
		this.setDate(this.m_date);

		// set title and load vote
		TextView titleText = (TextView) findViewById(R.id.title_text);
		if (this.m_voteId >= 0) {
			titleText.setText(R.string.manage_vote);
			this.loadVote();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// save time
		savedInstanceState.putLong("bundle_date_in_ms", this.m_date.getTime());

		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get time
		long ms = savedInstanceState.getLong("bundle_date_in_ms", Calendar.getInstance().getTimeInMillis());
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(ms);
		this.m_date = c.getTime();
	}

	private void setDate(Date date) {
		this.m_date = date;
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
		this.btnDatePicker.setText(dateFormat.format(date));
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
		this.btnTimePicker.setText(timeFormat.format(date));
	}

	private boolean isValidProperties() {
		String title = this.txtTitle.getText().toString();
		String text = this.txtDescription.getText().toString();

		if (title.trim().isEmpty() || text.trim().isEmpty()) {
			return false;
		}

		// all is OK
		return true;
	}

	private void updateSubmitButtonState() {
		this.btnSubmit.setEnabled(this.isValidProperties());
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG:
			return new TimePickerDialog(this, mTimeSetListener, this.m_date.getHours(), this.m_date.getMinutes(), true);
		case DATE_DIALOG:
			return new DatePickerDialog(this, mDateSetListener, this.m_date.getYear() + 1900, this.m_date.getMonth(),
					this.m_date.getDate());
		}
		return null;
	}

	private void loadVote() {
		this.m_vote = this.modelManager.getVote(this.m_voteId);

		this.txtTitle.setText(this.m_vote.getTitle());
		this.txtDescription.setText(this.m_vote.getText());

		this.rbIsPublic.setChecked(!this.m_vote.getIsPrivate());
		this.rbIsPrivate.setChecked(this.m_vote.getIsPrivate());

		this.rbIsSingleChoice.setChecked(!this.m_vote.getIsMultipleChoice());
		this.rbIsMultiChoice.setChecked(this.m_vote.getIsMultipleChoice());

		this.setDate(this.m_vote.getStartDate());

		// get number of hours
		GregorianCalendar c1 = new GregorianCalendar();
		c1.setTimeInMillis(this.m_vote.getStartDate().getTime());
		GregorianCalendar c2 = new GregorianCalendar();
		c2.setTimeInMillis(this.m_vote.getEndDate().getTime());
		long delta = c2.getTimeInMillis() - c1.getTimeInMillis();
		double hours = delta / (1000 * 60 * 60);
		if (hours < 1) {
			hours = 1;
		}
		if (hours > 71) {
			hours = 71;
		}
		this.seekVoteDuration.setProgress((int) Math.round(hours));
	}

	public void onSubmit(View v) {
		// TODO: edit vote
		// TODO: check arguments

		if (this.m_vote == null) {
			// create new vote
			String title = this.txtTitle.getText().toString();
			String text = this.txtDescription.getText().toString();
			boolean isPrivate = this.rbIsPrivate.isChecked();
			boolean isMultiChoice = this.rbIsMultiChoice.isChecked();

			Calendar c = new GregorianCalendar();
			c.setTimeInMillis(this.m_date.getTime());
			Date startDate = c.getTime();

			c.add(Calendar.HOUR_OF_DAY, this.seekVoteDuration.getProgress());
			Date endDate = c.getTime();

			try {
				this.modelManager.createVote(title, text, isPrivate, isMultiChoice, startDate, endDate);
			} catch (Exception e) {
				// TODO: capture error
				e.printStackTrace();
				return;
			}
		}
		// close activity
		this.finish();
	}

	public void onCancel(View v) {
		// close activity
		this.finish();
	}
}

package com.fh.voting.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;

import com.fh.voting.Constants;
import com.fh.voting.R;
import com.fh.voting.model.User;
import com.fh.voting.model.Vote;
import com.fh.voting.model.VoteInvitation;
import com.fh.voting.model.VoteOption;

public class ManageVoteActivity extends DatabaseWrapperActivity {
	private int m_voteId = -1;
	private Vote m_vote;
	private Date m_date;
	private LinkedHashMap<Integer, User> m_users = new LinkedHashMap<Integer, User>();
	private ArrayList<Integer> m_invitedUserIds = new ArrayList<Integer>();
	private ArrayList<String> m_voteOptions = new ArrayList<String>();

	private Button btnSubmit;
	private RelativeLayout layoutUsers;
	private RelativeLayout layoutOptionsButton;

	private final int DATE_DIALOG = 1103;
	private final int TIME_DIALOG = 1104;

	private EditText txtTitle;
	private EditText txtDescription;

	private RadioButton rbIsPublic;
	private RadioButton rbIsPrivate;

	private RadioButton rbIsSingleChoice;
	private RadioButton rbIsMultiChoice;

	private SeekBar seekVoteDuration;
	private TextView txtVoteDuration;

	private TextView txtInvitedUsers;
	private TextView txtVoteOptions;

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
		this.layoutUsers = (RelativeLayout) this.findViewById(R.id.layoutUsers);
		this.layoutOptionsButton = (RelativeLayout) this.findViewById(R.id.layoutOptionsButton);

		this.txtInvitedUsers = (TextView) this.findViewById(R.id.txtInvitedUsers);
		this.txtVoteOptions = (TextView) this.findViewById(R.id.txtVoteOptions);

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

		this.layoutOptionsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// set up dialog
				final Dialog dialog = new Dialog(activity);
				dialog.setContentView(R.layout.vote_options);
				dialog.setTitle("Add and edit Vote options");
				dialog.setCancelable(false);

				if (activity.m_voteOptions.size() == 0) {
					activity.m_voteOptions.add("New option #0");
				}

				// set up list
				final ListView lstOptions = (ListView) dialog.findViewById(R.id.lstOptions);
				lstOptions.setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1,
						activity.m_voteOptions));

				// edit dialog
				LayoutInflater factory = LayoutInflater.from(activity);

				// Set an EditText view to get user input
				final View editDialogView = factory.inflate(R.layout.vote_option_dialog, null);
				final AlertDialog editDialog = new AlertDialog.Builder(activity)
						.setTitle(R.string.edit_vote_option_dialog).setView(editDialogView)
						.setPositiveButton(R.string.button_save, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								EditText txtItemId = (EditText) editDialogView.findViewById(R.id.txtItemId);
								EditText txtVoteOption = (EditText) editDialogView.findViewById(R.id.txtVoteOption);

								int index = Integer.parseInt(txtItemId.getText().toString());
								activity.m_voteOptions.set(index, txtVoteOption.getText().toString());

								((ArrayAdapter<?>) lstOptions.getAdapter()).notifyDataSetChanged();
							}
						}).setNegativeButton(R.string.button_delete, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								EditText txtItemId = (EditText) editDialogView.findViewById(R.id.txtItemId);

								int index = Integer.parseInt(txtItemId.getText().toString());
								activity.m_voteOptions.remove(index);
								((ArrayAdapter<?>) lstOptions.getAdapter()).notifyDataSetChanged();
							}
						}).create();

				// open option dialog
				lstOptions.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
						String option = (String) parent.getItemAtPosition(position);

						EditText txtItemId = (EditText) editDialogView.findViewById(R.id.txtItemId);
						EditText txtVoteOption = (EditText) editDialogView.findViewById(R.id.txtVoteOption);

						txtVoteOption.setText(option);
						txtItemId.setText(((Integer) position).toString());
						editDialog.show();
					}
				});

				// set up button
				Button buttonAdd = (Button) dialog.findViewById(R.id.btnAdd);
				buttonAdd.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						activity.m_voteOptions.add(0, "New option");
						((ArrayAdapter<?>) lstOptions.getAdapter()).notifyDataSetChanged();
					}
				});

				// set up button
				Button buttonSave = (Button) dialog.findViewById(R.id.btnSave);
				buttonSave.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						activity.onOptionsSelected(activity.m_voteOptions);

						// close
						dialog.dismiss();
					}
				});

				// show dialog
				dialog.show();
			}
		});

		// create invitation dialog
		this.layoutUsers.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// set up dialog
				final Dialog dialog = new Dialog(activity);
				dialog.setContentView(R.layout.vote_invitations);
				dialog.setTitle("Select users for private Vote");
				dialog.setCancelable(true);

				// get users
				ArrayList<String> userNames = new ArrayList<String>();
				final LinkedHashMap<Integer, Integer> lookupIndexId = new LinkedHashMap<Integer, Integer>();
				final LinkedHashMap<Integer, Integer> lookupIdIndex = new LinkedHashMap<Integer, Integer>();
				for (User u : activity.m_users.values()) {
					userNames.add(u.getName());
					int index = userNames.size() - 1;
					lookupIndexId.put(index, u.getId());
					lookupIdIndex.put(u.getId(), index);
				}

				// set up list
				final ListView lstUsers = (ListView) dialog.findViewById(R.id.lstUsers);
				lstUsers.setAdapter(new ArrayAdapter<String>(activity,
						android.R.layout.simple_list_item_multiple_choice, userNames));
				lstUsers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

				// select checked users
				ArrayList<Integer> selectedUsers = activity.getInvitedUserIds();
				for (Integer id : selectedUsers) {
					int index = lookupIdIndex.get(id);
					lstUsers.setItemChecked(index, true);
				}

				// set up button
				Button buttonSave = (Button) dialog.findViewById(R.id.btnSave);
				buttonSave.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// return users
						ArrayList<Integer> selectedItems = new ArrayList<Integer>();
						int len = lstUsers.getCount();
						SparseBooleanArray checked = lstUsers.getCheckedItemPositions();
						for (int i = 0; i < len; i++) {
							if (checked.get(i)) {
								// get index, store id
								int id = lookupIndexId.get(i);
								selectedItems.add(id);
							}
						}

						activity.onUsersSelected(selectedItems);

						// close dialog
						dialog.dismiss();
					}
				});

				// show dialog
				dialog.show();
			}
		});
	}

	protected ArrayList<String> getVoteOptions() {
		return m_voteOptions;
	}

	protected void onOptionsSelected(ArrayList<String> m_voteOptions) {
		this.m_voteOptions = m_voteOptions;

		// update options box
		if (this.m_voteOptions.size() == 0) {
			this.txtVoteOptions.setText(R.string.no_vote_options_yet);
		} else {
			String options = "";
			for (String option : this.m_voteOptions) {
				options += option + ", ";
			}
			options = options.substring(0, options.length() - 2);
			if (options.length() > 30) {
				options = options.substring(0, 29) + "…";
			}
			this.txtVoteOptions.setText(options);
		}
	}

	protected ArrayList<Integer> getInvitedUserIds() {
		return this.m_invitedUserIds;
	}

	protected void onUsersSelected(ArrayList<Integer> invitedUserIds) {
		this.m_invitedUserIds = invitedUserIds;
		// update users box
		if (this.m_invitedUserIds.size() == 0) {
			this.txtInvitedUsers.setText(R.string.no_invitations_yet);
		} else {
			String invites = "";
			for (Integer id : this.m_invitedUserIds) {
				invites += this.m_users.get(id).getName() + ", ";
			}
			invites = invites.substring(0, invites.length() - 2);
			if (invites.length() > 30) {
				invites = invites.substring(0, 29) + "…";
			}
			this.txtInvitedUsers.setText(invites);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		// restore vote id
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			this.m_voteId = bundle.getInt(Constants.bundle_vote_id, -1);
		}

		// init time
		if (this.m_date == null) {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.HOUR_OF_DAY, 1);
			this.m_date = c.getTime();
		}

		// set date, time
		this.setDate(this.m_date);

		// invites
		List<User> users = this.modelManager.getUsers();
		this.m_users.clear();
		for (User user : users) {
			// do not add myself
			if (user.getId() == this.modelManager.getMyUser().getId()) {
				continue;
			}
			this.m_users.put(user.getId(), user);
		}
		this.onUsersSelected(this.m_invitedUserIds);

		// options
		this.onOptionsSelected(this.m_voteOptions);

		// set title and load vote
		TextView titleText = (TextView) findViewById(R.id.title_text);
		if (this.m_voteId >= 0) {
			titleText.setText(R.string.manage_vote);
			this.btnSubmit.setText(R.string.save_vote);
			this.loadVote();
			this.loadVoteInvitations();
			this.loadVoteOptions();
		}
	}

	private void loadVoteOptions() {
		try {
			ArrayList<VoteOption> options = this.modelManager.getOptions(this.m_vote);
			this.m_voteOptions.clear();
			for (VoteOption option : options) {
				this.m_voteOptions.add(option.getText());
			}
			this.onOptionsSelected(this.m_voteOptions);
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			Utils.alertErrorDialog(this, "Failed to load Vote options from server.");
		}
	}

	private void loadVoteInvitations() {
		try {
			ArrayList<VoteInvitation> invitations = this.modelManager.getInvitations(this.m_vote);
			this.m_invitedUserIds.clear();
			for (VoteInvitation invitation : invitations) {
				this.m_invitedUserIds.add(invitation.getUserId());
			}
			this.onUsersSelected(this.m_invitedUserIds);
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			Utils.alertErrorDialog(this, "Failed to load invitations from server.");
		}

	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// save time
		savedInstanceState.putLong("bundle_date_in_ms", this.m_date.getTime());
		savedInstanceState.putStringArrayList("bundle_vote_options", this.m_voteOptions);
		savedInstanceState.putIntegerArrayList("bundle_invite_ids", this.m_invitedUserIds);

		super.onSaveInstanceState(savedInstanceState);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get users
		this.m_invitedUserIds = savedInstanceState.getIntegerArrayList("bundle_invite_ids");

		// get vote options
		this.m_voteOptions = savedInstanceState.getStringArrayList("bundle_vote_options");

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

	private boolean validateProperties() {
		if (this.txtTitle.getText().toString().trim().isEmpty()) {
			Utils.alertDialog(this, "Title can not be empty!", txtTitle);
			return false;
		}

		if (this.txtDescription.getText().toString().trim().isEmpty()) {
			Utils.alertDialog(this, "Description can not be empty!", txtDescription);
			return false;
		}

		if (this.m_date.getTime() - Calendar.getInstance().getTimeInMillis() <= 0) {
			Utils.alertDialog(this, "Start time shall be in future!", btnDatePicker);
			return false;
		}

		return true;
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

	private boolean saveVote() {
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
			if (this.m_vote == null) {
				this.m_vote = this.modelManager.createVote(title, text, isPrivate, isMultiChoice, startDate, endDate);
			} else {
				this.m_vote = this.modelManager.updateVote(this.m_vote, title, text, isPrivate, isMultiChoice,
						startDate, endDate);
			}

			// invite users
			if (isPrivate) {
				ArrayList<User> invited = new ArrayList<User>();
				for (Integer id : this.m_invitedUserIds) {
					invited.add(this.m_users.get(id));
				}
				this.modelManager.inviteUsers(this.m_vote, invited);
			}

			// set vote options
			this.modelManager.setVoteOptions(this.m_vote, this.m_voteOptions);
		} catch (Exception e) {
			e.printStackTrace();
			Utils.alertErrorDialog(this, "Failed to create/update vote. Try again, it may work then...");
			return false;
		}

		return true;
	}

	private boolean publishVote() {
		try {
			this.modelManager.publishVote(this.m_vote);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Utils.alertErrorDialog(this, "Failed to publish vote. Try again, it may work then...");
			return false;
		}
	}

	private boolean validatePublishing() {
		if (this.m_vote == null) {
			Utils.alertDialog(this, "Vote should be saved before publish!", null);
			return false;
		}

		if (this.m_invitedUserIds.size() == 0 && this.rbIsPrivate.isChecked()) {
			Utils.alertDialog(this, "You should invite someone to private Vote!", null);
			return false;
		}

		if (this.m_voteOptions.size() == 0) {
			Utils.alertDialog(this, "You should set some Vote options!", null);
			return false;
		}

		return true;
	}

	public void onPublish(View v) {
		// check arguments
		if (!this.validateProperties()) {
			return;
		}

		boolean result = this.saveVote();
		if (!result) {
			return;
		}

		// check invitations and options
		if (!this.validatePublishing()) {
			return;
		}

		result = this.publishVote();
		if (!result) {
			return;
		}

		// close activity
		// TODO: redirect to vote overview
		this.finish();
	}

	public void onSubmit(View v) {
		// check arguments
		if (!this.validateProperties()) {
			return;
		}

		this.saveVote();
	}

	public void onCancel(View v) {
		// close activity
		this.finish();
	}
}

package com.fh.voting.activities;

import java.util.ArrayList;
import java.util.Collection;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fh.voting.Constants;
import com.fh.voting.R;
import com.fh.voting.VoteDebug;
import com.fh.voting.async.AsyncTaskManager;
import com.fh.voting.async.ITask;
import com.fh.voting.async.TaskExecutor;
import com.fh.voting.lists.EmptyListItem;
import com.fh.voting.lists.IListItem;
import com.fh.voting.lists.SectionListAdapter;
import com.fh.voting.lists.SectionListItem;
import com.fh.voting.lists.VoteListItem;
import com.fh.voting.model.Vote;

public class HomeActivity extends DatabaseWrapperActivity {
	private VoteDebug voteDebug;
	private ListView lstCategories;

	private final int mMyVotesIndex = 0;
	private int mPendingVotesIndex = -1;
	private int mPublicVotesIndex = -1;

	private boolean _isLoading = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// customize title
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		TextView titleText = (TextView) findViewById(R.id.title_text);
		titleText.setText(R.string.app_name);

		Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);

		this.voteDebug = new VoteDebug(this);
		this.lstCategories = (ListView) this.findViewById(R.id.lstCategories);

		// list click handler
		final HomeActivity homeActivity = this;
		this.lstCategories.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				IListItem item = (IListItem) parent.getItemAtPosition(position);
				homeActivity.OnListItemClick(position, item);
			}
		});

		// reset cache!!!
		this.modelManager.resetCahce();

		// check registration and register if needed
		// will trigger registration activity
		if (this.isRegistrationRequired()) {
			return;
		}

		if (this.preferences.isFirstStart()) {
			this.preferences.setNotFirstStart();
		}

		// load votes from web service
		// TODO: refactor
		this.loadVotesAsync();
	}

	private void OnListItemClick(int position, IListItem item) {
		// section clicked, try open respective activity
		if (item instanceof SectionListItem) {
			// My Votes section clicked, opening MyVotesActivity
			if (position == this.mMyVotesIndex) {
				this.openActivity(MyVotesActivity.class);
				return;
			}

			// other sections clicked, open if there are votes
			IListItem nextItem = (IListItem) this.lstCategories.getItemAtPosition(position + 1);
			if (!(nextItem instanceof EmptyListItem)) {
				if (position == this.mPendingVotesIndex) {
					this.openActivity(PendingVotesActivity.class);
					return;
				} else if (position == this.mPublicVotesIndex) {
					this.openActivity(PublicVotesActivity.class);
					return;
				}
			}

			// no Votes, nothing to open
			Toast.makeText(HomeActivity.this, "Thera are no Votes to show  :(", Toast.LENGTH_SHORT).show();
		}
		// empty list item clicked
		else if (item instanceof EmptyListItem) {
			// try open activity for new Vote
			if (position == this.mMyVotesIndex + 1) {
				this.openActivity(ManageVoteActivity.class);
			} else {
				// no votes, nothing to open
				Toast.makeText(HomeActivity.this, "Thera are no Votes to show  :(", Toast.LENGTH_SHORT).show();
			}
		}
		// Vote item clicked
		else {
			VoteListItem voteItem = (VoteListItem) item;
			Bundle bundle = new Bundle();
			bundle.putInt(Constants.bundle_vote_id, voteItem.getVote().getId());

			if (position < this.mPendingVotesIndex) {
				// manage my Vote
				this.openActivity(ManageVoteActivity.class, bundle);
			} else {
				// perform Vote
				this.openActivity(PerformVoteActivity.class, bundle);
			}
		}
	}

	private void openActivity(Class<?> cls) {
		Intent intent = new Intent(HomeActivity.this, cls);
		startActivity(intent);
	}

	private void openActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent(HomeActivity.this, cls);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Initiating Menu XML file (main_menu.xml)
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.layout.main_menu, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (!this._isLoading) {
			this.onLoadVotes();
		}
	};

	private boolean isRegistrationRequired() {
		// check registration on server
		boolean isServerRegistered = false;
		try {
			isServerRegistered = this.server.getIsRegistered();
		} catch (Exception e1) {
			isServerRegistered = false;
		}

		// start registration activity if needed
		if (!isServerRegistered || !this.preferences.isRegistered()) {
			// show registration activity
			Intent intent = new Intent(HomeActivity.this, RegisterActivity.class);
			startActivity(intent);
			return true;
		}

		return false;
	}

	private void loadVotesAsync() {
		this._isLoading = true;
		final HomeActivity homeActivity = this;
		ITask loader = new ITask() {
			@Override
			public Object onWorkCallback(TaskExecutor executor, Object... params) {
				homeActivity.loadVotesSync();
				return null;
			}

			@SuppressWarnings("unchecked")
			@Override
			public void onCompleteCallback(Object result) {
				homeActivity.onLoadVotes();
			}
		};

		new AsyncTaskManager(this).execute("Loading info from server...", loader);
	}

	private void onLoadVotes() {
		// result list
		ArrayList<IListItem> items = new ArrayList<IListItem>();

		// load my votes
		items.add(new SectionListItem("My Votes"));
		Collection<Vote> votes = this.modelManager.getMyVotes();
		if (votes.size() == 0) {
			items.add(new EmptyListItem("You have no own votes, but you can create one. Just press this item."));
		} else {
			// add top 5 votes
			int i = 5;
			for (Vote v : votes) {
				items.add(new VoteListItem(v));
				if (--i <= 0) {
					break;
				}
			}
		}

		// pending votes
		items.add(new SectionListItem("Pending Votes"));
		this.mPendingVotesIndex = items.size() - 1;
		votes = this.modelManager.getPendingVotes();
		if (votes.size() == 0) {
			items.add(new EmptyListItem("No pending votes or invitations."));
		} else {
			// add top 5 votes
			int i = 5;
			for (Vote v : votes) {
				items.add(new VoteListItem(v));
				if (--i <= 0) {
					break;
				}
			}
		}

		// top public votes
		items.add(new SectionListItem("Top Votes"));
		this.mPublicVotesIndex = items.size() - 1;
		votes = this.modelManager.getTopVotes();
		if (votes.size() == 0) {
			items.add(new EmptyListItem("No available public votes."));
		} else {
			// add top 5 votes
			int i = 5;
			for (Vote v : votes) {
				items.add(new VoteListItem(v));
				if (--i <= 0) {
					break;
				}
			}
		}

		SectionListAdapter adapter = new SectionListAdapter(this, R.layout.header_list_item, R.layout.empty_list_item,
				R.layout.vote_list_item, items);
		this.lstCategories.setAdapter(adapter);
		this._isLoading = false;
	}

	private void loadVotesSync() {
		// load data
		try {
			this.modelManager.loadUsers();
			this.modelManager.loadVotes();

		} catch (Exception e) {
			// something went wrong on server
			// TODO: show error toast
			e.printStackTrace();
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.manage_server:
			this.openActivity(ManageServerActivity.class);
			return true;

		case R.id.reset_app:
			Toast.makeText(HomeActivity.this, "Application was reset to defaults", Toast.LENGTH_SHORT).show();
			this.voteDebug.ResetClient();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
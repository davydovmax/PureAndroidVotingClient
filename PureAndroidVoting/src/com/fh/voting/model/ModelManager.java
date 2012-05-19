package com.fh.voting.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fh.voting.Server;
import com.fh.voting.db.DatabaseHelper;

public class ModelManager {
	private Server m_server;
	private DatabaseHelper m_dbHelper;
	private User _user;
	private String m_phoneId;

	public ModelManager(String phoneId, Server server, DatabaseHelper dbHelper) {
		this.m_phoneId = phoneId;
		this.m_server = server;
		this.m_dbHelper = dbHelper;

	}

	public void resetCahce() {
		this.m_dbHelper.deleteDatabase();
	}

	public void loadUsers() throws Exception {
		// load votes created by user
		ArrayList<User> users = this.m_server.getUsers();
		for (int i = 0; i < users.size(); ++i) {
			User user = users.get(i);

			// store to DB
			m_dbHelper.syncItem(user);
		}
	}

	public void loadVotes() throws Exception {
		this.loadMyVotes();
		this.loadPendingVotes();
		this.loadTopVotes();
	}

	public void loadMyVotes() throws Exception {
		// load votes created by user
		ArrayList<Vote> my_votes = this.m_server.getMyVotes();
		for (int i = 0; i < my_votes.size(); ++i) {
			Vote vote = my_votes.get(i);

			// store to DB
			m_dbHelper.syncItem(vote);
		}
	}

	public void loadPendingVotes() throws Exception {
		// // load votes created by user
		// ArrayList<Vote> votes = this.m_server.getPendingVotes();
		// for (int i = 0; i < votes.size(); ++i) {
		// Vote vote = votes.get(i);
		//
		// // store to DB
		// m_dbHelper.syncItem(vote);
		// }
	}

	public void loadTopVotes() throws Exception {
		// load votes created by user
		ArrayList<Vote> votes = this.m_server.getTopVotes();
		for (int i = 0; i < votes.size(); ++i) {
			Vote vote = votes.get(i);

			// store to DB
			m_dbHelper.syncItem(vote);
		}
	}

	public void loadVote(int voteId) throws Exception {
		// vote
		// vote options
		// vote invitations
		//
	}

	public List<Vote> getMyVotes() {
		return this.m_dbHelper.get_votesByOwner(this.getMyUser());
	}

	public List<Vote> getTopVotes() {
		return this.m_dbHelper.get_topVotes(this.getMyUser());
	}

	public List<Vote> getPendingVotes() {
		return this.m_dbHelper.get_pendingVotes(this.getMyUser());
	}

	public User getMyUser() {
		if (this._user == null) {
			this._user = this.m_dbHelper.getUser(this.m_phoneId);
		}

		return this._user;
	}

	public Vote getVote(int voteId) {
		return (Vote) this.m_dbHelper.getById(Vote.class, voteId);
	}

	public Vote createVote(String title, String text, boolean isPrivate, boolean isMultiChoice, Date publicationDate,
			Date startDate, Date endDate) throws Exception {
		Vote vote = this.m_server.createVote(title, text, this.getMyUser().getId(), isPrivate, isMultiChoice,
				publicationDate, startDate, endDate);

		// store to DB
		this.m_dbHelper.syncItem(vote);

		return vote;
	}
}

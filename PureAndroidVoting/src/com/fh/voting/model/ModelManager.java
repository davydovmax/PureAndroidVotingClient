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
		// load votes created by user
		ArrayList<Vote> votes = this.m_server.getPendingVotes();
		ArrayList<Integer> lookup = new ArrayList<Integer>();
		for (int i = 0; i < votes.size(); ++i) {
			Vote vote = votes.get(i);

			// store to DB
			m_dbHelper.syncItem(vote);
			lookup.add(vote.getId());
		}

		VoteLookup voteLookup = m_dbHelper.getVoteLookup();
		voteLookup.setPendingVotes(lookup);
		m_dbHelper.saveVoteLookup(voteLookup);
	}

	public void loadTopVotes() throws Exception {
		// load votes created by user
		ArrayList<Vote> votes = this.m_server.getTopVotes();
		ArrayList<Integer> lookup = new ArrayList<Integer>();
		for (int i = 0; i < votes.size(); ++i) {
			Vote vote = votes.get(i);

			// store to DB
			m_dbHelper.syncItem(vote);
			lookup.add(vote.getId());
		}

		VoteLookup voteLookup = m_dbHelper.getVoteLookup();
		voteLookup.setTopVotes(lookup);
		m_dbHelper.saveVoteLookup(voteLookup);
	}

	public List<Vote> getMyVotes() {
		return this.m_dbHelper.get_votesByOwner(this.getMyUser());
	}

	public List<Vote> getTopVotes() {
		return this.m_dbHelper.get_topVotes(this.getMyUser());
	}

	public List<Vote> getPendingVotes() {
		ArrayList<Vote> result = new ArrayList<Vote>();
		VoteLookup voteLookup = m_dbHelper.getVoteLookup();
		for (Integer id : voteLookup.getPendingVotes()) {
			result.add((Vote) m_dbHelper.getById(Vote.class, id));
		}

		return result;
	}

	public List<User> getUsers() {
		return this.m_dbHelper.getUsers();
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

	public Vote createVote(String title, String text, boolean isPrivate, boolean isMultiChoice, Date startDate,
			Date endDate) throws Exception {
		Vote vote = this.m_server.createVote(title, text, this.getMyUser().getId(), isPrivate, isMultiChoice,
				startDate, endDate);

		// store to DB
		this.m_dbHelper.syncItem(vote);

		return vote;
	}

	public Vote updateVote(Vote vote, String title, String text, boolean isPrivate, boolean isMultiChoice,
			Date startDate, Date endDate) throws Exception {
		Vote updatedVote = this.m_server.updateVote(vote.getId(), title, text, this.getMyUser().getId(), isPrivate,
				isMultiChoice, startDate, endDate);

		// store to DB
		this.m_dbHelper.syncItem(updatedVote);

		return updatedVote;
	}

	public Vote publishVote(Vote vote) throws Exception {
		Vote updatedVote = this.m_server.publishVote(vote.getId());
		this.m_dbHelper.syncItem(updatedVote);

		return updatedVote;
	}

	public void inviteUsers(Vote vote, ArrayList<User> invited) throws Exception {
		ArrayList<Integer> userIds = new ArrayList<Integer>();
		for (User user : invited) {
			userIds.add(user.getId());
		}

		this.m_server.setInvitations(vote.getId(), userIds);

		// TODO: sync invitations to DB
		// this.m_dbHelper.syncItem(updatedVote);
	}

	public ArrayList<VoteInvitation> getInvitations(Vote vote) throws Exception {
		return this.m_server.getInvitations(vote.getId());
	}

	public void setVoteOptions(Vote vote, ArrayList<String> options) throws Exception {
		this.m_server.setOptions(vote.getId(), options);

		// TODO: sync options to DB
		// this.m_dbHelper.syncItem(updatedVote);
	}

	public ArrayList<VoteOption> getOptions(Vote vote) throws Exception {
		return this.m_server.getOptions(vote.getId());
	}

	public void performVote(Vote vote, ArrayList<VoteOption> options) throws Exception {
		ArrayList<Integer> optionIds = new ArrayList<Integer>();
		for (VoteOption option : options) {
			optionIds.add(option.getId());
		}

		this.m_server.performVote(vote.getId(), optionIds);
	}

	public ArrayList<VoteChoice> getMyChoices(Vote vote) throws Exception {
		return this.m_server.getMyChoices(vote.getId());
	}

	public VoteResult getVoteResult(Vote vote) throws Exception {
		return this.m_server.getVoteResult(vote.getId());
	}
}

package com.fh.voting.model;

import com.fh.voting.db.DataTransferObject;

public class VoteInvitation extends DataTransferObject {
	private int userId;
	private int voteId;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getVoteId() {
		return voteId;
	}

	public void setVoteId(int voteId) {
		this.voteId = voteId;
	}
}

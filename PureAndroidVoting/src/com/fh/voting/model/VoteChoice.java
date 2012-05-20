package com.fh.voting.model;

import com.fh.voting.db.DataTransferObject;

public class VoteChoice extends DataTransferObject {
	private int optionId;
	private int userId;
	private int voteId;

	public int getOptionId() {
		return optionId;
	}

	public void setOptionId(int optionId) {
		this.optionId = optionId;
	}

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

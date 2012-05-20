package com.fh.voting.model;

import com.fh.voting.db.DataTransferObject;

public class VoteOption extends DataTransferObject {
	private String text;
	private int voteId;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getVoteId() {
		return voteId;
	}

	public void setVoteId(int voteId) {
		this.voteId = voteId;
	}
}

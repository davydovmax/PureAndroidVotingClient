package com.fh.voting.lists;

import com.fh.voting.model.Vote;

public class VoteListItem implements IListItem {
	private Vote vote;
	
	public VoteListItem(Vote vote) {
		this.setVote(vote);
	}

	public Vote getVote() { return vote; }
	public void setVote(Vote vote) { this.vote = vote; }	
	
	@Override
	public String getTitle() {
		return this.vote.getTitle();
	}
}

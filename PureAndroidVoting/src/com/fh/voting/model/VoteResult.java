package com.fh.voting.model;

import java.util.ArrayList;

import com.fh.voting.db.DataTransferObject;
import com.fh.voting.parsers.VoteResultScore;

public class VoteResult extends DataTransferObject {
	private int voteId;
	private ArrayList<VoteResultScore> all;
	private int maxScore;
	private String winnerText;

	public int getVoteId() {
		return voteId;
	}

	public void setVoteId(int voteId) {
		this.voteId = voteId;
	}

	public VoteResult(int voteId, int maxScore, String winnerText, ArrayList<VoteResultScore> all) {
		this.voteId = voteId;
		this.maxScore = maxScore;
		this.winnerText = winnerText;
		this.all = all;
	}

	public int getMaxScore() {
		return maxScore;
	}

	public String getWinnerText() {
		return winnerText;
	}

	public ArrayList<VoteResultScore> getAll() {
		return all;
	}
}

package com.fh.voting.parsers;

public class VoteResultScore {
	private int score;
	private String title;

	public VoteResultScore(int score, String title) {
		this.score = score;
		this.title = title;
	}

	public int getScore() {
		return score;
	}

	public String getTitle() {
		return title;
	}
}

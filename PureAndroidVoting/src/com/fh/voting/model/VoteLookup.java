package com.fh.voting.model;

import java.util.ArrayList;

public class VoteLookup {
	private ArrayList<Integer> m_myVotes = new ArrayList<Integer>();
	private ArrayList<Integer> m_pendingVotes = new ArrayList<Integer>();
	private ArrayList<Integer> m_topVotes = new ArrayList<Integer>();

	public ArrayList<Integer> getMyVotes() {
		return m_myVotes;
	}

	public ArrayList<Integer> getTopVotes() {
		return m_topVotes;
	}

	public ArrayList<Integer> getPendingVotes() {
		return m_pendingVotes;
	}

	public void setMyVotes(ArrayList<Integer> lookup) {
		m_myVotes = lookup;
	}

	public void setTopVotes(ArrayList<Integer> lookup) {
		m_topVotes = lookup;
	}

	public void setPendingVotes(ArrayList<Integer> lookup) {
		m_pendingVotes = lookup;
	}
}

package com.fh.voting.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.fh.voting.Server;

public class Cache {
	private Server m_server;
	private Boolean m_isLoaded = false;

	private LinkedHashMap<Integer, Vote> m_myVotes = new LinkedHashMap<Integer, Vote>();
	LinkedHashMap<Integer, Vote> m_pendingVotes = new LinkedHashMap<Integer, Vote>();
	LinkedHashMap<Integer, Vote> m_topVotes = new LinkedHashMap<Integer, Vote>();

	public void set_server(Server server) {
		this.m_server = server;
	}

	public void LoadRequiredData() throws Exception {
		if (this.m_isLoaded) {
			// already loaded
			return;
		}

		// load votes created by user
		m_myVotes.clear();
		ArrayList<Vote> my_votes = this.m_server.getMyVotes();
		for (int i = 0; i < my_votes.size(); ++i) {
			Vote vote = my_votes.get(i);
			this.m_myVotes.put(vote.getId(), vote);
		}

		// load votes created by user
		m_topVotes.clear();
		ArrayList<Vote> top_votes = this.m_server.getTopVotes();
		for (int i = 0; i < top_votes.size(); ++i) {
			Vote vote = top_votes.get(i);
			this.m_topVotes.put(vote.getId(), vote);
		}

		// set loaded state to true
		this.m_isLoaded = true;
	}

	public LinkedHashMap<Integer, Vote> get_myVotes() {
		return m_myVotes;
	}

	public LinkedHashMap<Integer, Vote> get_topVotes() {
		return m_topVotes;
	}

	public LinkedHashMap<Integer, Vote> get_pendingVotes() {
		return m_pendingVotes;
	}
}

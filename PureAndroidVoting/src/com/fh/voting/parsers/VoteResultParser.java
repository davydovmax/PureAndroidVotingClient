package com.fh.voting.parsers;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fh.voting.model.VoteResult;

public class VoteResultParser {
	public VoteResult parseVoteResult(String voteResult) throws JSONException, ParseException {
		return parseVoteResult(new JSONObject(voteResult));
	}

	public VoteResult parseVoteResult(JSONObject voteResultObject) throws JSONException, ParseException {
		int maxScore = voteResultObject.getInt("max_score");
		String winner = voteResultObject.getString("winner");
		int voteId = voteResultObject.getInt("vote_id");

		ArrayList<VoteResultScore> scores = new ArrayList<VoteResultScore>();
		JSONArray array = voteResultObject.getJSONArray("scores");
		for (int i = 0; i < array.length(); ++i) {
			JSONObject s = array.getJSONObject(i);
			scores.add(new VoteResultScore(s.getInt("score"), s.getString("text")));
		}

		VoteResult voteResult = new VoteResult(voteId, maxScore, winner, scores);
		return voteResult;
	}
}

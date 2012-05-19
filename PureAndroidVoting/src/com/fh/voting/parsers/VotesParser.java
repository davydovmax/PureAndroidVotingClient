package com.fh.voting.parsers;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fh.voting.model.Vote;

public class VotesParser {
	public ArrayList<Vote> parseVotes(String votes) throws JSONException, ParseException {
		JSONArray array = new JSONArray(votes);
		int size = array.length();
		ArrayList<Vote> voteItems = new ArrayList<Vote>();
		for (int i = 0; i < size; i++) {
			Vote vote = this.parseVote(array.getString(i));
			voteItems.add(vote);
		}

		return voteItems;
	}

	public Vote parseVote(String vote) throws JSONException, ParseException {
		return parseVote(new JSONObject(vote));
	}

	public Vote parseVote(JSONObject voteObject) throws JSONException, ParseException {
		Vote vote = new Vote();
		vote.setId(voteObject.getInt("id"));
		vote.setStatus(Vote.Status.values()[voteObject.getInt("status")]);
		vote.setAuthorId(voteObject.getInt("author_id"));
		vote.setAuthorId(voteObject.getInt("author_id"));
		vote.setTitle(voteObject.getString("title"));
		vote.setText(voteObject.getString("text"));
		vote.setIsPrivate(voteObject.getBoolean("is_private"));
		vote.setIsMultipleChoice(voteObject.getBoolean("is_multiple_choice"));
		vote.setPublicationDate(DateTimeConverter.parse(voteObject.getString("publication_date")));
		vote.setStartDate(DateTimeConverter.parse(voteObject.getString("start_date")));
		vote.setEndDate(DateTimeConverter.parse(voteObject.getString("end_date")));
		return vote;
	}
}

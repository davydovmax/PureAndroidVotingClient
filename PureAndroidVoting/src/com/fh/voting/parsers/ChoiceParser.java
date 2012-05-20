package com.fh.voting.parsers;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fh.voting.model.VoteChoice;

public class ChoiceParser {
	public ArrayList<VoteChoice> parseVoteChoices(String voteChoices) throws JSONException, ParseException {
		JSONArray array = new JSONArray(voteChoices);
		int size = array.length();
		ArrayList<VoteChoice> VoteChoiceItems = new ArrayList<VoteChoice>();
		for (int i = 0; i < size; i++) {
			VoteChoice VoteChoice = this.parseVoteChoice(array.getString(i));
			VoteChoiceItems.add(VoteChoice);
		}

		return VoteChoiceItems;
	}

	public VoteChoice parseVoteChoice(String voteChoice) throws JSONException, ParseException {
		return parseVoteChoice(new JSONObject(voteChoice));
	}

	public VoteChoice parseVoteChoice(JSONObject voteChoiceObject) throws JSONException, ParseException {
		VoteChoice voteChoice = new VoteChoice();
		voteChoice.setId(voteChoiceObject.getInt("id"));
		voteChoice.setUserId(voteChoiceObject.getInt("user_id"));
		voteChoice.setVoteId(voteChoiceObject.getInt("vote_id"));
		voteChoice.setOptionId(voteChoiceObject.getInt("option_id"));
		return voteChoice;
	}
}

package com.fh.voting.parsers;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fh.voting.model.VoteOption;

public class OptionParser {
	public ArrayList<VoteOption> parseVoteOptions(String voteOptions) throws JSONException, ParseException {
		JSONArray array = new JSONArray(voteOptions);
		int size = array.length();
		ArrayList<VoteOption> VoteOptionItems = new ArrayList<VoteOption>();
		for (int i = 0; i < size; i++) {
			VoteOption VoteOption = this.parseVoteOption(array.getString(i));
			VoteOptionItems.add(VoteOption);
		}

		return VoteOptionItems;
	}

	public VoteOption parseVoteOption(String voteOption) throws JSONException, ParseException {
		return parseVoteOption(new JSONObject(voteOption));
	}

	public VoteOption parseVoteOption(JSONObject voteOptionObject) throws JSONException, ParseException {
		VoteOption voteOption = new VoteOption();
		voteOption.setId(voteOptionObject.getInt("id"));
		voteOption.setText(voteOptionObject.getString("text"));
		voteOption.setVoteId(voteOptionObject.getInt("vote_id"));
		return voteOption;
	}
}

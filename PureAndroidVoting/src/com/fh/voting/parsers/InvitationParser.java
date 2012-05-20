package com.fh.voting.parsers;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fh.voting.model.VoteInvitation;

public class InvitationParser {
	public ArrayList<VoteInvitation> parseVoteInvitations(String voteInvitations) throws JSONException, ParseException {
		JSONArray array = new JSONArray(voteInvitations);
		int size = array.length();
		ArrayList<VoteInvitation> VoteInvitationItems = new ArrayList<VoteInvitation>();
		for (int i = 0; i < size; i++) {
			VoteInvitation VoteInvitation = this.parseVoteInvitation(array.getString(i));
			VoteInvitationItems.add(VoteInvitation);
		}

		return VoteInvitationItems;
	}

	public VoteInvitation parseVoteInvitation(String voteInvitation) throws JSONException, ParseException {
		return parseVoteInvitation(new JSONObject(voteInvitation));
	}

	public VoteInvitation parseVoteInvitation(JSONObject voteInvitationObject) throws JSONException, ParseException {
		VoteInvitation voteInvitation = new VoteInvitation();
		voteInvitation.setId(voteInvitationObject.getInt("id"));
		voteInvitation.setUserId(voteInvitationObject.getInt("user_id"));
		voteInvitation.setVoteId(voteInvitationObject.getInt("vote_id"));
		return voteInvitation;
	}
}

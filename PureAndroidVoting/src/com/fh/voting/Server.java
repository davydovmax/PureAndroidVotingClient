package com.fh.voting;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.fh.voting.model.User;
import com.fh.voting.model.Vote;
import com.fh.voting.model.VoteInvitation;
import com.fh.voting.model.VoteOption;
import com.fh.voting.parsers.DateTimeConverter;
import com.fh.voting.parsers.InvitationParser;
import com.fh.voting.parsers.OptionParser;
import com.fh.voting.parsers.UsersParser;
import com.fh.voting.parsers.VotesParser;

public class Server {
	private String server;
	private String phoneId;

	public Server(String phoneId) {
		this.server = "http://voteserver.herokuapp.com";
		this.phoneId = phoneId;
	}

	private HttpClient buildHttpClient() {
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpClient client = new DefaultHttpClient(params);
		return client;
	}

	private String performGET(String requestString) throws Exception {
		Log.d("Server", "Perform GET " + requestString);

		String result = null;
		try {
			HttpClient client = buildHttpClient();
			HttpGet get = new HttpGet(requestString);
			HttpResponse response = client.execute(get);
			result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		} catch (IOException e) {
			Log.d("Server", "Can't get response. URL = " + requestString);
			throw new Exception("Can't get response. URL = " + requestString);
		}

		return result;
	}

	private String performPUT(String requestString) throws Exception {
		Log.d("Server", "Perform PUT " + requestString);

		String result = null;
		try {
			HttpClient client = buildHttpClient();
			HttpPut get = new HttpPut(requestString);
			HttpResponse response = client.execute(get);
			result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
		} catch (IOException e) {
			String message = "Can't get response. URL = " + requestString + ", Error Message: " + e.getMessage();
			Log.d("Server", message);
			throw new Exception(message);
		}

		return result;
	}

	public boolean getIsRegistered() throws Exception {
		String requestString = String.format("%s/%s/is_registered", this.server, this.phoneId);
		String response = this.performGET(requestString);
		JSONObject responseObj = new JSONObject(response);
		return responseObj.getBoolean("is_registered");
	}

	public void registerUser(String fullname, String email) throws Exception {
		String requestString = String.format("%s/%s/register?fullname=%s&email=%s", this.server, this.phoneId,
				URLEncoder.encode(fullname), URLEncoder.encode(email));
		this.performPUT(requestString);
	}

	public void createTestData() throws Exception {
		String requestString = String.format("%s/%s/fill_test_data", this.server, this.phoneId);
		this.performPUT(requestString);
	}

	public List<String> getLogRecords() throws Exception {
		String requestString = String.format("%s/logs?json", this.server);
		String response = this.performGET(requestString);

		// parse array of strings
		JSONArray array = new JSONArray(response);
		int size = array.length();
		ArrayList<String> logRecords = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			logRecords.add(array.getString(i));
		}

		return logRecords;
	}

	public ArrayList<Vote> getMyVotes() throws Exception {
		String requestString = String.format("%s/%s/my", this.server, this.phoneId);
		String response = this.performGET(requestString);
		VotesParser parser = new VotesParser();
		return parser.parseVotes(response);
	}

	public ArrayList<Vote> getTopVotes() throws Exception {
		String requestString = String.format("%s/%s/top", this.server, this.phoneId);
		String response = this.performGET(requestString);
		VotesParser parser = new VotesParser();
		return parser.parseVotes(response);
	}

	public ArrayList<User> getUsers() throws Exception {
		String requestString = String.format("%s/users", this.server);
		String response = this.performGET(requestString);
		UsersParser parser = new UsersParser();
		return parser.parseUsers(response);
	}

	public Vote createVote(String title, String text, int authorId, boolean isPrivate, boolean isMultiChoice,
			Date startDate, Date endDate) throws Exception {
		String requestString = String.format(
				"%s/%s/my?title=%s&text=%s&start_date=%s&end_date=%s&is_multiple_choice=%d&is_private=%d", this.server,
				this.phoneId, URLEncoder.encode(title), URLEncoder.encode(text),
				URLEncoder.encode(DateTimeConverter.toString(startDate)),
				URLEncoder.encode(DateTimeConverter.toString(endDate)), isMultiChoice ? 1 : 0, isPrivate ? 1 : 0);
		String response = this.performPUT(requestString);
		VotesParser parser = new VotesParser();
		return parser.parseVote(response);
	}

	public Vote updateVote(int id, String title, String text, int id2, boolean isPrivate, boolean isMultiChoice,
			Date startDate, Date endDate) throws Exception {
		String requestString = String.format(
				"%s/%s/my/%d?title=%s&text=%s&start_date=%s&end_date=%s&is_multiple_choice=%d&is_private=%d",
				this.server, this.phoneId, id, URLEncoder.encode(title), URLEncoder.encode(text),
				URLEncoder.encode(DateTimeConverter.toString(startDate)),
				URLEncoder.encode(DateTimeConverter.toString(endDate)), isMultiChoice ? 1 : 0, isPrivate ? 1 : 0);
		String response = this.performPUT(requestString);
		VotesParser parser = new VotesParser();
		return parser.parseVote(response);
	}

	public Vote publishVote(int id) throws Exception {
		String requestString = String.format("%s/%s/my/%d/publish", this.server, this.phoneId, id);
		String response = this.performPUT(requestString);
		VotesParser parser = new VotesParser();
		return parser.parseVote(response);
	}

	public void setInvitations(int id, ArrayList<Integer> userIds) throws Exception {
		String requestString = String.format("%s/%s/my/%d/invitations?users=", this.server, this.phoneId, id);
		for (Integer userId : userIds) {
			requestString += userId.toString() + ",";
		}
		this.performPUT(requestString);
	}

	public ArrayList<VoteInvitation> getInvitations(int voteId) throws Exception {
		String requestString = String.format("%s/%s/votes/%d/invitations", this.server, this.phoneId, voteId);
		String response = this.performGET(requestString);
		InvitationParser parser = new InvitationParser();
		return parser.parseVoteInvitations(response);
	}

	public void setOptions(int id, ArrayList<String> options) throws Exception {
		String requestString = String.format("%s/%s/my/%d/options?options=", this.server, this.phoneId, id);
		String optionsString = "";
		for (String option : options) {
			optionsString += option.toString() + ",";
		}
		this.performPUT(requestString + URLEncoder.encode(optionsString));
	}

	public ArrayList<VoteOption> getOptions(int voteId) throws Exception {
		String requestString = String.format("%s/%s/votes/%d/options", this.server, this.phoneId, voteId);
		String response = this.performGET(requestString);
		OptionParser parser = new OptionParser();
		return parser.parseVoteOptions(response);
	}
}

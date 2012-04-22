package com.fh.voting;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
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

import com.fh.voting.model.Vote;
import com.fh.voting.parsers.VotesParser;

import android.util.Log;

public class Server {
	private String server;
	private String phoneId;
	
	public Server(String phoneId) {
		this.server = "http://voteserver.herokuapp.com";
		this.phoneId = phoneId;
	}
	
	private HttpClient buildHttpClient() {
		  HttpParams params = new BasicHttpParams();
		  params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
		    HttpVersion.HTTP_1_1);
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
		String requestString = String.format("%s/%s/register?fullname=%s&email=%s", this.server,
				this.phoneId, URLEncoder.encode(fullname), URLEncoder.encode(email));
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
}

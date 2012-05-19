package com.fh.voting.parsers;

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fh.voting.model.User;

public class UsersParser {
	public ArrayList<User> parseUsers(String users) throws JSONException, ParseException {
		JSONArray array = new JSONArray(users);
		int size = array.length();
		ArrayList<User> userItems = new ArrayList<User>();
		for (int i = 0; i < size; i++) {
			User user = this.parseUser(array.getString(i));
			userItems.add(user);
		}

		return userItems;
	}

	public User parseUser(String user) throws JSONException, ParseException {
		return parseUser(new JSONObject(user));
	}

	public User parseUser(JSONObject userObject) throws JSONException, ParseException {
		User user = new User();
		user.setId(userObject.getInt("id"));
		user.setName(userObject.getString("fullname"));
		user.setPhoneId(userObject.getString("phone_id"));
		user.setEmail(userObject.getString("email"));
		user.setRegistrationDate(DateTimeConverter.parse(userObject.getString("date_registered")));
		return user;
	}
}

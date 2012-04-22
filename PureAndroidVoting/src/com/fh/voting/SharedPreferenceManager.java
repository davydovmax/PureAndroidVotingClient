package com.fh.voting;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;

/**
 * Class for settings management. It works via reading/writing values into SharedPreference files.
 * 
 * @author unknown
 *
 */
public class SharedPreferenceManager {

	private static final String PREFARENCES_NAME = "voting_preferences";	
	private static final String KEY_FIRST_START = "first_start";
	private static final String KEY_IS_REGISTERED = "is_registered";
	private static final String KEY_PHONE_ID = "phone_id";
	
	private SharedPreferences preferences;
	private Context context;

	public SharedPreferenceManager(Context context) {
		this.context = context;
		this.preferences = context.getSharedPreferences(PREFARENCES_NAME, Context.MODE_PRIVATE);
	}
	
	public boolean isFirstStart() {
		return preferences.getBoolean(KEY_FIRST_START, true);
	}
	
	public boolean isRegistered() {
		return preferences.getBoolean(KEY_IS_REGISTERED, false);
	}
	
	public void setNotFirstStart() {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(KEY_FIRST_START, false);
		editor.commit();
	}
	
	public void setRegistered() {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(KEY_IS_REGISTERED, true);
		editor.commit();
	}

	public int getLocalVersion(String entityName) {
		return preferences.getInt(entityName, 0);
	}

	public void saveNewVersion(String entityName, int serverVersion) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(entityName, serverVersion);
		editor.commit();
	}
	
	public String getPhoneId() {
		String android_id = preferences.getString(KEY_PHONE_ID, "");
		if(android_id.isEmpty()) {
			android_id = Secure.getString(this.context.getContentResolver(), Secure.ANDROID_ID);
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString(KEY_PHONE_ID, android_id);
			editor.commit();
		}
		
		return android_id;
	}
	
	public void reset() {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(KEY_FIRST_START, true);
		editor.putBoolean(KEY_IS_REGISTERED, false);
		editor.commit();
	}
}

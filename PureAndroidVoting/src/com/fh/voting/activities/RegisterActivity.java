package com.fh.voting.activities;

import com.fh.voting.R;
import com.fh.voting.Server;
import com.fh.voting.SharedPreferenceManager;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private SharedPreferenceManager preferences;
	private Server server;
	Button btnRegister;
	CheckBox chbAgreed;
	EditText txtName;
	EditText txtEmail;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.register);
	    
	    Window window = getWindow();
		window.setFormat(PixelFormat.RGBA_8888);
	   
	    this.btnRegister = (Button)this.findViewById(R.id.btnRegister);
	    this.chbAgreed = (CheckBox)this.findViewById(R.id.chbAgreed);
	    this.txtName = (EditText)this.findViewById(R.id.txtName);
	    this.txtEmail = (EditText)this.findViewById(R.id.txtEmail);
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    
	    // enable registration button
	    this.updateRegisterButton();
	}
	
	public void onCheckboxAgreedClicked(View v) {
	    // enable registration button
		this.updateRegisterButton();
	}
	
	private boolean isValidInput() {
		boolean valid = this.chbAgreed.isChecked();
		valid = valid & this.txtName.getText().length() > 0;
		// TODO: check with regexp
		return valid & this.txtEmail.getText().length() > 0;
	}
	
	private void updateRegisterButton() {
		this.btnRegister.setEnabled(this.isValidInput());
	}
	
	@Override
	public void onBackPressed() {
		// exit
        moveTaskToBack(true);
	}
	
	public void onRegisterClicked(View v) {
		this.preferences = new SharedPreferenceManager(this);
        this.server = new Server(preferences.getPhoneId());
	    
		SharedPreferenceManager preferences = new SharedPreferenceManager(this);
		
		// register 
		try {
			server.registerUser(this.txtName.getText().toString(), this.txtEmail.getText().toString());
			preferences.setRegistered();
			this.finish();
		} catch (Exception e) {
			// show error
			Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onCancelClicked(View v) {
		// exit
		moveTaskToBack(true);
	}
}

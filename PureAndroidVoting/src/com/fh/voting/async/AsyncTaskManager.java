package com.fh.voting.async;

import android.app.ProgressDialog;
import android.content.Context;

public final class AsyncTaskManager {
	private final ProgressDialog mProgressDialog;

    public AsyncTaskManager(Context context) {
		// Setup progress dialog
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(false);
    }
    
    public void execute(String message, ITask task, Object... params) {
    	// Show dialog if it wasn't shown yet or was removed on configuration (rotation) change
    	if (!mProgressDialog.isShowing()) {
    	    mProgressDialog.show();
    	}
    	// Show current message in progress dialog
    	mProgressDialog.setMessage(message);
    	
    	// Start task
    	TaskExecutor asyncExecutor = new TaskExecutor(this, task);
    	asyncExecutor.execute(params);
    }
    
    public void onComplete() {
		// Close progress dialog
		mProgressDialog.dismiss();
    }
}
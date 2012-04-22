package com.fh.voting.async;

import android.os.AsyncTask;

public class TaskExecutor extends AsyncTask<Object, Object, Object> {
	AsyncTaskManager mManager;
	ITask mTask;
	
    public TaskExecutor(AsyncTaskManager manager, ITask task) {
    	this.mTask = task;
    	this.mManager = manager;
    }

    @Override
    protected Object doInBackground(Object... params) {
        return this.mTask.onWorkCallback(this, params);
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        this.mManager.onComplete();
        this.mTask.onCompleteCallback(result);
    }
}
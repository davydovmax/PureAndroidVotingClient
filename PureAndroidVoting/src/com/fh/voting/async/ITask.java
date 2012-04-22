package com.fh.voting.async;

public interface ITask {
	public Object onWorkCallback(TaskExecutor executor, Object... params);
	public void onCompleteCallback(Object result);
}

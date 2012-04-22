package com.fh.voting.lists;

public class EmptyListItem implements IListItem {
	private String title;
	
	public EmptyListItem(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

}

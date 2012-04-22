package com.fh.voting.lists;

public class SectionListItem implements IListItem {
	private String title;
	
	public SectionListItem(String title) {
		this.title = title;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

}

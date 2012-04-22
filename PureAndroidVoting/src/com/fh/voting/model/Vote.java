package com.fh.voting.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Vote {
	private int id;
	private int authorId;
	private String title;
	private String text;
	private boolean isPrivate;
	private boolean isMultipleChoice;
	private Date publicationDate;
	private Date startDate;
	private Date endDate;
	private Date resultsDate;
	
	public Vote() {
		this.setId(0);
		this.setAuthorId(0);
		this.setTitle("");
		this.setText("");
		this.setIsPrivate(false);
		this.setIsMultipleChoice(false);

		Calendar c = new GregorianCalendar();
		
		// publish tomorrow
		c.add(Calendar.DAY_OF_MONTH, 1);
		publicationDate = c.getTime();
		
		// start next day after publish
		c.add(Calendar.DAY_OF_MONTH, 1);
		startDate = c.getTime();
		
		// end after 1 day
		c.add(Calendar.DAY_OF_MONTH, 1);
		endDate = c.getTime();
		
		// results after 1 day
		c.add(Calendar.DAY_OF_MONTH, 1);
		resultsDate = c.getTime();
	}
	
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public int getAuthorId() { return authorId; }
	public void setAuthorId(int authorId) { this.authorId = authorId; }
	
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }

	public boolean getIsPrivate() { return isPrivate; }
	public void setIsPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }

	public boolean isIsMultipleChoice() { return this.isMultipleChoice; }
	public void setIsMultipleChoice(boolean isMultipleChoice) { this.isMultipleChoice = isMultipleChoice; }

	public Date getPublicationDate() { return this.publicationDate; }
	public void setPublicationDate(Date publicationDate) { this.publicationDate = publicationDate; }

	public Date getStartDate() { return startDate; }
	public void setStartDate(Date startDate) { this.startDate = startDate; }

	public Date getEndDate() { return endDate; }
	public void setEndDate(Date endDate) { this.endDate = endDate; }

	public Date getResultsDate() { return resultsDate; }
	public void setResultsDate(Date resultsDate) { this.resultsDate = resultsDate; }
}

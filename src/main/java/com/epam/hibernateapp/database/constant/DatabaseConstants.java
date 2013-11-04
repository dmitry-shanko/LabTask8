package com.epam.hibernateapp.database.constant;

public enum DatabaseConstants 
{
	;
	private String content;
	private DatabaseConstants (String content) 
	{
		this.content = content;
	}
	
	public String getContent()
	{
		return content;
	}
}

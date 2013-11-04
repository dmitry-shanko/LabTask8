package com.epam.hibernateapp.util;

public enum GlobalConstants 
{
	ENCODING_PARAM("encoding"),
	;

	private String content;
	private GlobalConstants (String content) 
	{
		this.content = content;
	}

	public String getContent()
	{
		return content;
	}
}

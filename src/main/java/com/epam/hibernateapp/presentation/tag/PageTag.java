package com.epam.hibernateapp.presentation.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageTag extends TagSupport
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4392811119411458087L;

	private static final Logger log = LoggerFactory.getLogger(PageTag.class);

	private String action;
	private String entityPerPageAttrName;
	private String pageNumberAttrName;
	private String currentEntityPerPage;
	private String currentPageNumber;
	private String entityPerPageMessage;
	private String pageNumberMessage;
	private String buttonMessage;
	private Integer maxPageNumber;

	/**
	 * @return the pageNumberAttrName
	 */
	public String getPageNumberAttrName() {
		return pageNumberAttrName;
	}

	/**
	 * @param pageNumberAttrName the pageNumberAttrName to set
	 */
	public void setPageNumberAttrName(String pageNumberAttrName) {
		this.pageNumberAttrName = pageNumberAttrName;
	}

	/**
	 * @return the currentEntityPerPage
	 */
	public String getCurrentEntityPerPage() {
		return currentEntityPerPage;
	}

	/**
	 * @param currentEntityPerPage the currentEntityPerPage to set
	 */
	public void setCurrentEntityPerPage(String currentEntityPerPage) {
		this.currentEntityPerPage = currentEntityPerPage;
	}

	/**
	 * @return the currentPageNumber
	 */
	public String getCurrentPageNumber() {
		return currentPageNumber;
	}

	/**
	 * @param currentPageNumber the currentPageNumber to set
	 */
	public void setCurrentPageNumber(String currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}

	/**
	 * @return the entityPerPageMessage
	 */
	public String getEntityPerPageMessage() {
		return entityPerPageMessage;
	}

	/**
	 * @param entityPerPageMessage the entityPerPageMessage to set
	 */
	public void setEntityPerPageMessage(String entityPerPageMessage) {
		this.entityPerPageMessage = entityPerPageMessage;
	}

	/**
	 * @return the pageNumberMessage
	 */
	public String getPageNumberMessage() {
		return pageNumberMessage;
	}

	/**
	 * @param pageNumberMessage the pageNumberMessage to set
	 */
	public void setPageNumberMessage(String pageNumberMessage) {
		this.pageNumberMessage = pageNumberMessage;
	}

	/**
	 * @return the buttonMessage
	 */
	public String getButtonMessage() {
		return buttonMessage;
	}

	/**
	 * @param buttonMessage the buttonMessage to set
	 */
	public void setButtonMessage(String buttonMessage) {
		this.buttonMessage = buttonMessage;
	}

	/**
	 * @return the entityPerPageAttrName
	 */
	public String getEntityPerPageAttrName() {
		return entityPerPageAttrName;
	}

	/**
	 * @param entityPerPageAttrName the entityPerPageAttrName to set
	 */
	public void setEntityPerPageAttrName(String entityPerPageAttrName) {
		this.entityPerPageAttrName = entityPerPageAttrName;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the maxPageNumber
	 */
	public Integer getMaxPageNumber() {
		return maxPageNumber;
	}

	/**
	 * @param maxPageNumber the maxPageNumber to set
	 */
	public void setMaxPageNumber(Integer maxPageNumber) {
		this.maxPageNumber = maxPageNumber;
	}

	@Override
	public int doStartTag() throws JspException 
	{
		try 
		{
			String content = generateForm();
			log.debug("Attempt to generate html form in {}: content={}", getClass(), content);
			pageContext.getOut().write(generateLinks().concat(content));	
		} 
		catch (IOException e) 
		{
			throw new JspException("Can't write output String", e);
		}
		return SKIP_BODY;
	}

	private String generateLinks()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<br>");
		if (maxPageNumber < 2)
		{
			sb.append(" 1");
			return sb.toString();
		}
		else
		{			
			int currentPageNumberInt = 1;
			try
			{
				currentPageNumberInt = Integer.parseInt(currentPageNumber);
			}
			catch (NumberFormatException e)
			{				
			}
			if (currentPageNumberInt > 1)
			{
				sb.append("<a href=\"");
				sb.append(action);
				sb.append("?");
				sb.append(entityPerPageAttrName);
				sb.append("=");
				sb.append(currentEntityPerPage);
				sb.append("&&");
				sb.append(pageNumberAttrName);
				sb.append("=1");
				sb.append("\">");
				sb.append("<<");
				sb.append("</a> ");

				sb.append("<a href=\"");
				sb.append(action);
				sb.append("?");
				sb.append(entityPerPageAttrName);
				sb.append("=");
				sb.append(currentEntityPerPage);
				sb.append("&&");
				sb.append(pageNumberAttrName);
				sb.append("=");
				sb.append(currentPageNumberInt - 1);
				sb.append("\">");
				sb.append("<");
				sb.append("</a> ");
			}
			sb.append(generatePageLinks());
			if (currentPageNumberInt < maxPageNumber)
			{
				sb.append("<a href=\"");
				sb.append(action);
				sb.append("?");
				sb.append(entityPerPageAttrName);
				sb.append("=");
				sb.append(currentEntityPerPage);
				sb.append("&&");
				sb.append(pageNumberAttrName);
				sb.append("=");
				try
				{
					sb.append(Integer.parseInt(currentPageNumber) + 1);
				}
				catch (NumberFormatException e)
				{
					sb.append("1");
				}
				sb.append("\">");
				sb.append(">");
				sb.append("</a> ");

				sb.append("<a href=\"");
				sb.append(action);
				sb.append("?");
				sb.append(entityPerPageAttrName);
				sb.append("=");
				sb.append(currentEntityPerPage);
				sb.append("&&");
				sb.append(pageNumberAttrName);
				sb.append("=");
				sb.append(maxPageNumber);
				sb.append("\">");
				sb.append(">>");
				sb.append("</a> ");
			}
			return sb.toString();
		}
	}

	private String generatePageLinks()
	{
		StringBuilder sb = new StringBuilder();
		int currentPageNumberInt = 1;
		try
		{
			currentPageNumberInt = Integer.parseInt(currentPageNumber);
		}
		catch (NumberFormatException e)
		{				
		}
		int startPage = currentPageNumberInt - ((currentPageNumberInt % 10) == 0 ? 10 : (currentPageNumberInt % 10));
		startPage = (startPage > 0 ? startPage : 1);
		int endPage = ((startPage + 10) >= maxPageNumber ? maxPageNumber : (startPage + 10));
		startPage = (startPage > 1 ? startPage + 1 : startPage);
		if (startPage > 10)
		{
			sb.append(" ");
			sb.append("<a href=\"");
			sb.append(action);
			sb.append("?");
			sb.append(entityPerPageAttrName);
			sb.append("=");
			sb.append(currentEntityPerPage);
			sb.append("&&");
			sb.append(pageNumberAttrName);
			sb.append("=");
			sb.append(startPage - 1);
			sb.append("\">");
			sb.append("...");
			sb.append("</a> ");
		}
		for (int i = 0; (startPage <= endPage) && (i < 10); i++, startPage++)
		{
			if (currentPageNumberInt != startPage)
			{
				sb.append(" ");
				sb.append("<a href=\"");
				sb.append(action);
				sb.append("?");
				sb.append(entityPerPageAttrName);
				sb.append("=");
				sb.append(currentEntityPerPage);
				sb.append("&&");
				sb.append(pageNumberAttrName);
				sb.append("=");
				sb.append(startPage);
				sb.append("\">");
				sb.append(startPage);
				sb.append("</a> ");
			}
			else
			{
				sb.append(" " + currentPageNumberInt);
			}
		}
		if (endPage < maxPageNumber)
		{
			sb.append(" ");
			sb.append("<a href=\"");
			sb.append(action);
			sb.append("?");
			sb.append(entityPerPageAttrName);
			sb.append("=");
			sb.append(currentEntityPerPage);
			sb.append("&&");
			sb.append(pageNumberAttrName);
			sb.append("=");
			sb.append(startPage);
			sb.append("\">");
			sb.append("...");
			sb.append("</a> ");
		}
		sb.append(" ");
		return sb.toString();
	}
	private String generateForm()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<form method=\"post\" action=");
		sb.append(action);
		sb.append(">");
		sb.append((entityPerPageMessage == null ? "entityPerPage" : entityPerPageMessage));
		sb.append(": ");
		sb.append("<input type=\"text\" name=");
		sb.append(entityPerPageAttrName);
		sb.append(" value=");
		sb.append((currentEntityPerPage == null ? "" : currentEntityPerPage));
		sb.append(">&nbsp;&nbsp;&nbsp;");
		sb.append((pageNumberMessage == null ? "pageNumber" : pageNumberMessage));
		sb.append(": ");
		sb.append("<input type=\"text\" name=");
		sb.append(pageNumberAttrName);
		sb.append(" value=");
		sb.append((currentPageNumber == null ? "" : currentPageNumber));
		sb.append(">");
		sb.append("<button class=\"button\" type=\"submit\">");
		sb.append((buttonMessage == null ? "Go" : buttonMessage));
		sb.append("</button></form>");
		return sb.toString();
	}	
}

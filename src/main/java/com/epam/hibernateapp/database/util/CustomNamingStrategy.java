package com.epam.hibernateapp.database.util;

import org.hibernate.cfg.DefaultNamingStrategy;

public class CustomNamingStrategy extends DefaultNamingStrategy
{
//	private static final DefaultNamingStrategy parent = new DefaultNamingStrategy();
	/**
	 * 
	 */
	private static final long serialVersionUID = -4021007185840634832L;

	public String classToTableName(String className)
	{
		return "T_LAB_TASK_8_" + super.classToTableName(className).toUpperCase();
	}

	public String propertyToColumnName(String proName)
	{
		return super.propertyToColumnName(proName).toUpperCase();
	}

	public String columnName(String columnName)
	{
		return columnName;
	}

	public String tableName(String tableName)
	{
		return tableName;
	}
	
/*	public static String staticClassToTableName(String className)
	{
		return "T_LAB_TASK_8_" + parent.classToTableName(className).toUpperCase();
	}
	
	public static String staticPropertyToColumnName(String proName)
	{
		return parent.propertyToColumnName(proName);
	}
	
	public static String staticColumnName(String columnName)
	{
		return parent.columnName(columnName);
	}
	
	public static String staticTalbeName(String tableName)
	{
		return parent.tableName(tableName);
	}*/
}

package com.epam.hibernateapp.database;

import java.io.Serializable;
import java.util.List;

import com.epam.hibernateapp.database.exception.DaoException;

public interface GeneralDao<T, ID extends Serializable> 
{
	T get(ID id) throws DaoException;
	T add(T o) throws DaoException;
	List<T> getAll(int firstResult, int lastResult) throws DaoException;
	void quickAdd(List<T> list) throws DaoException;
	long getTotalCount() throws DaoException;
}

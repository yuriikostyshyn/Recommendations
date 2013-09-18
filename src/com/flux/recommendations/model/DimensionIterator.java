package com.flux.recommendations.model;

import java.util.Iterator;

public interface DimensionIterator<T> extends Iterator<T> {

	public boolean hasNext();

	public T next();
	
	public int rowId(); //return row id of element returned by next()
	
	public int columnId(); //return column id of element returned by next()

	public void remove();

	public void add(int row, int column, T newElement); // add new element after element last returned by method next

	public void addBefore(int row, int column, T newElement);

}

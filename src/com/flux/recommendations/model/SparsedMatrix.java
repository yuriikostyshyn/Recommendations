package com.flux.recommendations.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

public class SparsedMatrix {

	private Node header;
	private List<Node> rowHeaders;
	private List<Node> columnHeaders;
	private int width;
	private int height;

	public SparsedMatrix(int width, int height) {
		header = new Node(-1, -1, null, null, null);
		header.setTop(header);
		header.setLeft(header);
		this.width = width;
		this.height = height;
	}

	public Node getHeader() {
		return header;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void insertNewElement(int rowId, int columnId, BigDecimal value) {
		Node rowHeader = getRowHeader(rowId);
		Node newNode = putElementIntoRow(rowHeader, null, columnId);
		newNode.setValue(value);
		Node columnHeader = getColumnHeader(columnId);
		putElementIntoColumn(columnHeader, newNode, rowId); // should be
															// upgraded to
															// increase
															// perfomance
	}

	public BigDecimal getValue(int rowId, int columnId) {
		Node element = header;
		Node nextRow = header.getTop();
		while (nextRow.getRow() >= rowId) {
			if (nextRow.getRow() == rowId) {
				element = nextRow;
				break;
			} else {
				nextRow = nextRow.getTop();
			}
		}

		if (element.getRow() == rowId) {
			Node nextColumn = element.getLeft();
			while (nextColumn.getColumn() >= columnId) {
				if (nextColumn.getColumn() == columnId) {
					element = nextColumn;
					break;
				} else {
					nextColumn = nextColumn.getLeft();
				}
			}

			if (element.getColumn() == columnId) {
				return element.getValue();
			}
		}

		return null;

	}

	public void setValue(int rowId, int columnId, BigDecimal value) {
		Node element = header;
		Node nextRow = header.getTop();
		while (nextRow.getRow() >= rowId) {
			if (nextRow.getRow() == rowId) {
				element = nextRow;
				break;
			} else {
				nextRow = nextRow.getTop();
			}
		}

		if (element.getRow() == rowId) {
			Node nextColumn = element.getLeft();
			while (nextColumn.getColumn() >= columnId) {
				if (nextColumn.getColumn() == columnId) {
					element = nextColumn;
					break;
				} else {
					nextColumn = nextColumn.getLeft();
				}
			}

			if (element.getColumn() == columnId) {
				element.setValue(value);
				return;
			}
		}

		throw new NoSuchElementException("there is no such element in matrix");

	}

	private Node getRowHeader(int rowId) {
		return getElementByColumnHeaderAndRowId(header, rowId);
	}

	private Node getElementByColumnHeaderAndRowId(Node header, int rowId) {
		return putElementIntoColumn(header, null, rowId);
	}

	private Node putElementIntoColumn(Node header, Node elementToPut, int rowId) {
		Node element = header;
		Node previousRow = header;
		Node nextRow = header.getTop();
		while (nextRow.getRow() >= rowId) {
			if (nextRow.getRow() == rowId) {
				element = nextRow;
				if (elementToPut != null) {
					element.setValue(elementToPut.getValue());
				}
				break;
			} else {
				previousRow = nextRow;
				nextRow = nextRow.getTop();
			}
		}
		if (element == header) {
			if (elementToPut != null) {
				element = elementToPut;
				element.setTop(nextRow);
				previousRow.setTop(element);
			} else {
				element = new Node(rowId, header.getColumn(), null, nextRow, null);
				element.setLeft(element);
				previousRow.setTop(element);
			}
		}
		return element;
	}

	private Node getColumnHeader(int columnId) {
		return getElementByRowHeaderAndColumnId(header, columnId);
	}

	private Node getElementByRowHeaderAndColumnId(Node header, int columnId) {
		return putElementIntoRow(header, null, columnId);
	}

	private Node putElementIntoRow(Node header, Node elementToPut, int columnId) {
		Node element = header;
		Node previousColumn = header;
		Node nextColumn = header.getLeft();
		while (nextColumn.getColumn() >= columnId) {
			if (nextColumn.getColumn() == columnId) {
				element = nextColumn;
				if (elementToPut != null) {
					element.setValue(elementToPut.getValue());
				}
				break;
			} else {
				previousColumn = nextColumn;
				nextColumn = nextColumn.getLeft();
			}
		}
		if (element == header) {
			if (elementToPut != null) {
				element = elementToPut;
				element.setLeft(nextColumn);
				previousColumn.setLeft(element);
			} else {
				element = new Node(header.getRow(), columnId, null, null, nextColumn);
				element.setTop(element);
				previousColumn.setLeft(element);
			}
		}
		return element;
	}

	// TODO DimensionIterator

	protected DimensionIterator<BigDecimal> iterator(Dimension dimension) {
		return iterator(dimension, -1);
	}

	public DimensionIterator<BigDecimal> iterator(Dimension dimension, int id) {
		return dimension.equals(Dimension.COLUMN) ? new ColumnIterator(id) : new RowIterator(id);
	}

	protected class RowIterator implements DimensionIterator<BigDecimal> {
		private Node start;
		private Node previous;
		private Node current;

		protected RowIterator(int id) {
			if (id < 0) {
				this.start = header;
				this.current = header;
				this.previous = header;
			} else {
				Node that = header.getTop();
				while (that.getRow() != id) {
					if (that.getRow() > id) {
						that = that.getTop();
					} else {
						throw new NoSuchElementException();
					}
				}
				start = that;
				current = start;
				previous = start;
			}
		}

		@Override
		public boolean hasNext() {
			return current.getLeft() != start;
		}

		@Override
		public BigDecimal next() {
			if (hasNext()) {
				previous = current;
				current = current.getLeft();
				return current.getValue();
			}

			return null;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub

		}

		@Override
		public void add(int row, int column, BigDecimal newElement) {
			Node newNode = new Node(row, column, newElement, null, current.getLeft());
			current.setLeft(newNode);
			previous = current;
			current = newNode;
		}

		@Override
		public void addBefore(int row, int column, BigDecimal newElement) {
			if (previous != start && current != start) {
				Node newNode = new Node(row, column, newElement, null, current);
				previous.setLeft(newNode);
				previous = newNode;
			} else {
				throw new UnsupportedOperationException("you can't use this method before start of iteration");
			}
		}

		@Override
		public int rowId() {
			return current.getRow();
		}

		@Override
		public int columnId() {
			return current.getColumn();
		}
	}

	protected class ColumnIterator implements DimensionIterator<BigDecimal> {

		private Node start;
		private Node previous;
		private Node current;

		protected ColumnIterator(int id) {
			if (id < 0) {
				this.start = header;
				this.current = header;
				this.previous = header;
			} else {
				Node that = header.getLeft();
				while (that.getColumn() != id) {
					if (that.getColumn() > id) {
						that = that.getLeft();
					} else {
						throw new NoSuchElementException();
					}
				}

				start = that;
				current = start;
				previous = start;
			}
		}

		@Override
		public boolean hasNext() {
			return current.getTop() != start;
		}

		@Override
		public BigDecimal next() {
			if (hasNext()) {
				if (previous == null) {
					previous = current;
				}
				current = current.getTop();
				return current.getValue();
			}

			return null;
		}

		@Override
		public void remove() {
			// TODO Auto-generated method stub

		}

		@Override
		public void add(int row, int column, BigDecimal newElement) {
			Node newNode = new Node(row, column, newElement, current.getTop(), null);
			current.setTop(newNode);
			previous = current;
			current = newNode;
		}

		@Override
		public void addBefore(int row, int column, BigDecimal newElement) {
			if (previous != start && current != start) {
				Node newNode = new Node(row, column, newElement, current, null);
				previous.setTop(newNode);
				previous = newNode;
			} else {
				throw new UnsupportedOperationException("you can't use this method before start of iteration");
			}
		}

		@Override
		public int rowId() {
			return current.getRow();
		}

		@Override
		public int columnId() {
			return current.getColumn();
		}
	}

	public static final class Node {
		private int row;
		private int column;
		private Node top;
		private Node left;
		private BigDecimal value;

		public Node(int row, int column, BigDecimal value, Node top, Node left) {
			this.row = row;
			this.column = column;
			this.value = value;
			this.top = top;
			this.left = left;
		}

		public int getRow() {
			return row;
		}

		public void setRow(int row) {
			this.row = row;
		}

		public int getColumn() {
			return column;
		}

		public void setColumn(int column) {
			this.column = column;
		}

		public Node getTop() {
			return top;
		}

		public void setTop(Node top) {
			this.top = top;
		}

		public Node getLeft() {
			return left;
		}

		public void setLeft(Node left) {
			this.left = left;
		}

		public BigDecimal getValue() {
			return value;
		}

		public void setValue(BigDecimal value) {
			this.value = value;
		}
	}

	public enum Dimension {
		COLUMN(1), ROW(2);

		private final int dimensionNumber;

		private Dimension(int dimensionNumber) {
			this.dimensionNumber = dimensionNumber;
		}

		public int getDimensionNumber() {
			return dimensionNumber;
		}
	}
}

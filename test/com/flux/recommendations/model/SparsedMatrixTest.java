package com.flux.recommendations.model;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import com.flux.recommendations.model.SparsedMatrix.Dimension;

public class SparsedMatrixTest {
	private SparsedMatrix underTest;

	@Before
	public void setUp() {
		underTest = new SparsedMatrix(5, 5);
	}

	@Test
	public void shouldAddElementsInRightOrder() {
		underTest.insertNewElement(1, 3, new BigDecimal(2.5));
		underTest.insertNewElement(2, 3, new BigDecimal(3.5));
		underTest.insertNewElement(1, 4, new BigDecimal(4.5));
		underTest.insertNewElement(3, 2, new BigDecimal(5.5));
		DimensionIterator<BigDecimal> iterator = underTest.iterator(Dimension.ROW, 1);
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
			System.out.println(iterator.columnId());
		}

		System.out.println("Column 4");
		iterator = underTest.iterator(Dimension.COLUMN, 4);
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
			System.out.println(iterator.columnId());
		}
		System.out.println("Done");
	}

}

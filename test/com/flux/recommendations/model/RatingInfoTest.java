package com.flux.recommendations.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RatingInfoTest {

	@Test
	public void shouldReturnMeanRatings() {
		List<BigDecimal[]> ratings = new ArrayList<BigDecimal[]>();
		BigDecimal[] array = new BigDecimal[4];
		array[0] = new BigDecimal(1);
		array[1] = null;
		array[2] = new BigDecimal(4);
		array[3] = null;
		ratings.add(array);
		array = new BigDecimal[4];
		array[0] = new BigDecimal(5);
		array[1] = null;
		array[2] = new BigDecimal(3);
		array[3] = null;
		ratings.add(array);
		array = new BigDecimal[4];
		array[0] = null;
		array[1] = new BigDecimal(4);
		array[2] = new BigDecimal(3);
		array[3] = null;
		ratings.add(array);
		array = new BigDecimal[4];
		array[0] = null;
		array[1] = null;
		array[2] = new BigDecimal(3);
		array[3] = new BigDecimal(3);
		ratings.add(array);

		String[] itemNames = new String[] { "1", "2", "3", "4" };

		RatingInfo underTest = new RatingInfo();
		underTest.setItemNames(itemNames);
		underTest.setRatings(ratings);

		MeanRating[] expectedResult = new MeanRating[4];
		expectedResult[0] = new MeanRating("1", new BigDecimal(3.000));
		expectedResult[1] = new MeanRating("2", new BigDecimal(4.000));
		expectedResult[2] = new MeanRating("3", new BigDecimal(3.250));
		expectedResult[3] = new MeanRating("4", new BigDecimal(3.000));

		MeanRating[] actualResult = underTest.getMeanRatings();
		assertEquals(Arrays.asList(expectedResult).toString(),
				Arrays.asList(actualResult).toString());

	}
}

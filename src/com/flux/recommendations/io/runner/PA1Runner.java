package com.flux.recommendations.io.runner;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.flux.recommendations.comparator.MeanComparator;
import com.flux.recommendations.io.ListFileReader;
import com.flux.recommendations.logic.RatingsComputator;
import com.flux.recommendations.model.MeanRating;
import com.flux.recommendations.model.RatingInfo;
import com.flux.recommendations.model.SparsedMatrix;

public class PA1Runner {

	public static void main(String[] args) throws FileNotFoundException {
		ListFileReader fileReader = new ListFileReader("recsys-data-ratings.csv");
		SparsedMatrix result = fileReader.getRatings();

		List<MeanRating> list = RatingsComputator.computeMostOftenWith(result, 121);
		Collections.sort(list, new MeanComparator());
		for (MeanRating rating : list) {
			System.out.println(rating.toString());
		}
		System.out.println("Done");
	}

}

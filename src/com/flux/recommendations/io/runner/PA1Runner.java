package com.flux.recommendations.io.runner;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.flux.recommendations.comparator.MeanComparator;
import com.flux.recommendations.io.ListFileReader;
import com.flux.recommendations.io.ResultWriter;
import com.flux.recommendations.logic.RatingsComputator;
import com.flux.recommendations.model.MeanRating;
import com.flux.recommendations.model.RatingInfo;
import com.flux.recommendations.model.SparsedMatrix;

public class PA1Runner {

	public static void main(String[] args) throws FileNotFoundException {
		ListFileReader fileReader = new ListFileReader("recsys-data-ratings.csv");
		SparsedMatrix result = fileReader.getRatings();

		Map<Integer, List<MeanRating>> simpleResult = new LinkedHashMap<Integer, List<MeanRating>>();
		int firstId = 745;
		List<MeanRating> list = RatingsComputator.computeMostOftenWith(result, firstId);
		simpleResult.put(firstId, list.subList(0, 10));
		Collections.sort(list, new MeanComparator());
		for (int i = 0; i < 10; i++) {
			System.out.println(list.get(i).toString());
		}

		int secondId = 36955;
		list = RatingsComputator.computeMostOftenWith(result, secondId);
		Collections.sort(list, new MeanComparator());
		simpleResult.put(secondId, list.subList(0, 10));
		for (int i = 0; i < 10; i++) {
			System.out.println(list.get(i).toString());
		}

		int thirdId = 9331;
		list = RatingsComputator.computeMostOftenWith(result, thirdId);
		Collections.sort(list, new MeanComparator());
		simpleResult.put(thirdId, list.subList(0, 10));
		for (int i = 0; i < 10; i++) {
			System.out.println(list.get(i).toString());
		}
		System.out.println("Advanced");
		System.out.println(firstId);

		ResultWriter.writeInLine(simpleResult, "PA1-simple.txt");

		Map<Integer, List<MeanRating>> advancedResult = new LinkedHashMap<Integer, List<MeanRating>>();
		list = RatingsComputator.computeAdvancedMostOftenWith(result, 745);
		Collections.sort(list, new MeanComparator());
		advancedResult.put(firstId, list.subList(0, 10));
		for (int i = 0; i < 10; i++) {
			System.out.println(list.get(i).toString());
		}

		System.out.println(secondId);
		list = RatingsComputator.computeAdvancedMostOftenWith(result, 36955);
		Collections.sort(list, new MeanComparator());
		advancedResult.put(secondId, list.subList(0, 10));
		for (int i = 0; i < 10; i++) {
			System.out.println(list.get(i).toString());
		}

		System.out.println(thirdId);
		list = RatingsComputator.computeAdvancedMostOftenWith(result, 9331);
		Collections.sort(list, new MeanComparator());
		advancedResult.put(thirdId, list.subList(0, 10));
		for (int i = 0; i < 10; i++) {
			System.out.println(list.get(i).toString());
		}

		ResultWriter.writeInLine(advancedResult, "PA1-advanced.txt");
		System.out.println("Done");
	}

}

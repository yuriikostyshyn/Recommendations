package com.flux.recommendations.io.runner;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.flux.recommendations.comparator.MeanComparator;
import com.flux.recommendations.io.FileReader;
import com.flux.recommendations.model.MeanRating;
import com.flux.recommendations.model.RatingInfo;

public class Runner {

	public static void main(String[] args) throws FileNotFoundException {
		FileReader fileReader = new FileReader("recsys1data.txt");
		RatingInfo ratings = fileReader.getRatings();
		MeanRating[] meanRatings = ratings.getMeanRatings();
		sortArrayAndPrintTopElements(meanRatings, 6);
		System.out.println("Rating Counts:");
		
		MeanRating[] ratingCounts = ratings.getRatingsCount();
		sortArrayAndPrintTopElements(ratingCounts, 6);
		
		MeanRating[] ratingsPercentage = ratings.getRatingPercentage();
		sortArrayAndPrintTopElements(ratingsPercentage, 6);
		
		MeanRating[] mostOftenOccursWith = ratings.getMostOftenOccursWith(1);
		sortArrayAndPrintTopElements(mostOftenOccursWith, 6);
	}

	private static void sortArrayAndPrintTopElements(MeanRating[] data, int topCount) {
		Arrays.sort(data, new MeanComparator());
		for (int i = 0; i < topCount; i++) {
			System.out.println(data[i].toString());
		}
	}
}

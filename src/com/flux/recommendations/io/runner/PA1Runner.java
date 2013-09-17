package com.flux.recommendations.io.runner;

import java.io.FileNotFoundException;
import java.util.List;

import com.flux.recommendations.io.ListFileReader;
import com.flux.recommendations.model.RatingInfo;

public class PA1Runner {

	public static void main(String[] args) throws FileNotFoundException {
		ListFileReader fileReader = new ListFileReader("recsys-data-ratings.csv");
		RatingInfo result = fileReader.getRatings();
		System.out.println("Done");
	}

}

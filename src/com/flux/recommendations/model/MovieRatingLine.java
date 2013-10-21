package com.flux.recommendations.model;

import java.math.BigDecimal;
import java.util.HashMap;

public class MovieRatingLine {
	private String movieName;
	private HashMap<Integer, BigDecimal> userRatings;

	public MovieRatingLine(String data) {
		String[] parsedData = data.split(",", -1);
		movieName = parsedData[0];

		for (int i = 1; i < parsedData.length; i++) {
			if (parsedData[i] != null) {
				userRatings.put(i - 1, new BigDecimal(parsedData[i]));
			}
		}
	}

	public MovieRatingLine() {
		userRatings = new HashMap<Integer, BigDecimal>();
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	public HashMap<Integer, BigDecimal> getUserRatings() {
		return userRatings;
	}

	public void setUserRatings(HashMap<Integer, BigDecimal> userRatings) {
		this.userRatings = userRatings;
	}
}

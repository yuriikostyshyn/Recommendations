package com.flux.recommendations.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.swing.text.rtf.RTFEditorKit;

import com.flux.recommendations.model.RatingInfo;

public class ListFileReader {
	private String fileName;

	public ListFileReader(String fileName) {
		this.fileName = fileName;
	}

	public RatingInfo getRatings() throws FileNotFoundException {
		Map<String, List<RatingRecord>> ratingRecords = new HashMap<String, List<RatingRecord>>();

		File fileToRead = new File(fileName);
		Scanner scanner = new Scanner(fileToRead);
		int maxId = 0;
		while (scanner.hasNextLine()) {
			RatingRecord record = new RatingRecord(scanner.nextLine());
			if (record.getMovieId() > maxId) {
				maxId = record.getMovieId();
			}
			if (ratingRecords.containsKey(record.getUserId())) {
				ratingRecords.get(record.getUserId()).add(record);
			} else {
				List<RatingRecord> list = new ArrayList<RatingRecord>();
				list.add(record);
				ratingRecords.put(record.getUserId(), list);
			}
		}
		scanner.close();

		String[] itemIds = new String[maxId];
		List<BigDecimal[]> ratings = new ArrayList<BigDecimal[]>();
		for (Entry<String, List<RatingRecord>> records : ratingRecords.entrySet()) {
			BigDecimal[] userRatings = new BigDecimal[maxId];
			initArray(userRatings);
			ratings.add(userRatings);
			for (RatingRecord record : records.getValue()) {
				userRatings[record.getMovieId() - 1] = new BigDecimal(record.getRating().toPlainString());
				itemIds[record.getMovieId() - 1] = new Integer(record.getMovieId()).toString();
			}
		}

		RatingInfo result = new RatingInfo();
		result.setItemNames(itemIds);
		result.setRatings(ratings);

		return result;

	}

	private void initArray(BigDecimal[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = new BigDecimal(0);
		}
	}

	private class RatingRecord {
		private String userId;
		private int movieId;
		private BigDecimal rating;

		public RatingRecord(String record) {
			String[] values = record.split(",");
			if (values.length == 3) {
				userId = values[0];
				movieId = Integer.parseInt(values[1]);
				rating = new BigDecimal(values[2]);
			} else {
				throw new RuntimeException("Unsupported format of data");
			}

		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public int getMovieId() {
			return movieId;
		}

		public void setMovieId(int movieId) {
			this.movieId = movieId;
		}

		public BigDecimal getRating() {
			return rating;
		}

		public void setRating(BigDecimal rating) {
			this.rating = rating;
		}
	}

}

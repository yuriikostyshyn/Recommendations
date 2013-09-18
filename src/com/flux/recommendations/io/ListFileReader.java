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
import com.flux.recommendations.model.SparsedMatrix;

public class ListFileReader {
	private String fileName;

	public ListFileReader(String fileName) {
		this.fileName = fileName;
	}

	public SparsedMatrix getRatings() throws FileNotFoundException {
		List<RatingRecord> ratingRecords = new ArrayList<RatingRecord>();

		File fileToRead = new File(fileName);
		Scanner scanner = new Scanner(fileToRead);
		int maxId = 0;
		int maxUserId = 0;

		while (scanner.hasNextLine()) {
			RatingRecord record = new RatingRecord(scanner.nextLine());
			if (record.getMovieId() > maxId) {
				maxId = record.getMovieId();
			}
			if (record.getUserId() > maxUserId) {
				maxUserId = record.getUserId();
			}
			ratingRecords.add(record);
		}
		scanner.close();

		SparsedMatrix result = new SparsedMatrix(maxId, maxUserId);

		for (RatingRecord record : ratingRecords) {
			result.insertNewElement(record.getUserId(), record.getMovieId(), record.getRating());
		}

		return result;

	}

	private void initArray(BigDecimal[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = new BigDecimal(0);
		}
	}

	private class RatingRecord {
		private int userId;
		private int movieId;
		private BigDecimal rating;

		public RatingRecord(String record) {
			String[] values = record.split(",");
			if (values.length == 3) {
				userId = Integer.parseInt(values[0]);
				movieId = Integer.parseInt(values[1]);
				rating = new BigDecimal(values[2]);
			} else {
				throw new RuntimeException("Unsupported format of data");
			}

		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
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

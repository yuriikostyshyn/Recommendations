package com.flux.recommendations.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public class RatingInfo {
	private String[] itemNames;
	private List<BigDecimal[]> ratings;

	public RatingInfo() {
		this.ratings = new ArrayList<BigDecimal[]>();
	}

	public String[] getItemNames() {
		return itemNames;
	}

	public void setItemNames(String[] itemNames) {
		this.itemNames = itemNames;
	}

	public List<BigDecimal[]> getRatings() {
		return ratings;
	}

	public void setRatings(List<BigDecimal[]> ratings) {
		this.ratings = ratings;
	}

	public MeanRating[] getMeanRatings() {
		MeanRating[] result = new MeanRating[itemNames.length];
		BigDecimal[] intermediateResult = new BigDecimal[itemNames.length];
		initArray(intermediateResult);
		int[] ratingCounts = new int[itemNames.length];

		for (int i = 0; i < itemNames.length; i++) {
			for (BigDecimal[] row : ratings) {
				if (row[i] != null) {
					intermediateResult[i] = intermediateResult[i].add(row[i]);
					ratingCounts[i]++;
				}
			}

			result[i] = new MeanRating(itemNames[i], new BigDecimal(intermediateResult[i].toPlainString()));
			result[i].setMeanData(result[i].getMeanData().divide(new BigDecimal(ratingCounts[i]), new MathContext(3)));
		}

		return result;
	}

	public MeanRating[] getRatingsCount() {
		MeanRating[] result = new MeanRating[itemNames.length];
		int[] ratingCounts = new int[itemNames.length];

		for (int i = 0; i < itemNames.length; i++) {
			for (BigDecimal[] row : ratings) {
				if (row[i] != null && row[i].intValue() != 0) {
					ratingCounts[i]++;
				}
			}

			result[i] = new MeanRating(itemNames[i], new BigDecimal(ratingCounts[i]));
		}

		return result;
	}

	public MeanRating[] getRatingPercentage() {
		MeanRating[] result = new MeanRating[itemNames.length];
		int[] intermediateResult = new int[itemNames.length];
		int[] ratingCounts = new int[itemNames.length];

		for (int i = 0; i < itemNames.length; i++) {
			for (BigDecimal[] row : ratings) {
				if (row[i] != null) {
					if (row[i].compareTo(new BigDecimal(4)) >= 0) {
						intermediateResult[i]++;
					}
					ratingCounts[i]++;
				}
			}

			result[i] = new MeanRating(itemNames[i], new BigDecimal(intermediateResult[i]));
			result[i].setMeanData(result[i].getMeanData().divide(new BigDecimal(ratingCounts[i]), new MathContext(3)));
			result[i].setMeanData(result[i].getMeanData().multiply(new BigDecimal(100), new MathContext(3)));
		}

		return result;
	}

	public MeanRating[] getMostOftenOccursWithByMovieId(int id) {
		int arrayPosition = 0;
		for (int i = 0; i < itemNames.length; i++) {
			if (new Integer(id).equals(Integer.parseInt(itemNames[i]))) {
				arrayPosition = i;
				break;
			}
		}
		return getMostOftenOccursWith(arrayPosition);
	}

	public MeanRating[] getMostOftenOccursWith(int id) {
		MeanRating[] result = new MeanRating[itemNames.length];
		int[] intermediateResult = new int[itemNames.length];
		int ratingCount = 0;
		for (BigDecimal[] row : ratings) {
			if (row[id] != null && row[id].intValue() != 0) {
				for (int i = 0; i < itemNames.length; i++) {
					if (i != id && row[i] != null && row[i].intValue() != 0) {
						intermediateResult[i]++;
					}
				}
				ratingCount++;
			}
		}

		for (int i = 0; i < itemNames.length; i++) {
			result[i] = new MeanRating(itemNames[i], new BigDecimal(intermediateResult[i]));
			result[i].setMeanData(result[i].getMeanData().divide(new BigDecimal(ratingCount), new MathContext(3)));
			result[i].setMeanData(result[i].getMeanData().multiply(new BigDecimal(100), new MathContext(3)));
		}

		return result;
	}

	public MeanRating[] getAdvancedMostOftenOccursWithByMovieId(int id) {
		int arrayPosition = 0;
		for (int i = 0; i < itemNames.length; i++) {
			if (new Integer(id).equals(Integer.parseInt(itemNames[i]))) {
				arrayPosition = i;
				break;
			}
		}
		return getAdvancedMostOftenOccursWith(arrayPosition);
	}

	public MeanRating[] getAdvancedMostOftenOccursWith(int id) {
		MeanRating[] result = new MeanRating[itemNames.length];
		int[] intermediateResultWith = new int[itemNames.length];
		int[] intermediateResultWithout = new int[itemNames.length];
		int ratingCountWith = 0;
		int ratingCountWithout = 0;
		for (BigDecimal[] row : ratings) {
			for (int i = 0; i < itemNames.length; i++) {
				if (i != id && row[i] != null && row[i].intValue() != 0) {
					if (row[id] != null && row[id].intValue() != 0) {
						intermediateResultWith[i]++;
					} else {
						intermediateResultWithout[i]++;

						ratingCountWith++;
					}
					ratingCountWithout++;
				}

			}

			if (row[id] != null && row[id].intValue() != 0) {
				ratingCountWith++;
			} else {
				ratingCountWithout++;
			}

		}

		for (int i = 0; i < itemNames.length; i++) {
			result[i] = new MeanRating(itemNames[i], new BigDecimal(intermediateResultWith[i]));
			result[i].setMeanData(result[i].getMeanData().divide(new BigDecimal(ratingCountWith), new MathContext(3)));
			result[i].setMeanData(result[i].getMeanData().divide(new BigDecimal(intermediateResultWithout[i]), new MathContext(3)));
			result[i].setMeanData(result[i].getMeanData().multiply(new BigDecimal(ratingCountWithout), new MathContext(3)));
			result[i].setMeanData(result[i].getMeanData().multiply(new BigDecimal(100), new MathContext(3)));
		}

		return result;
	}

	public void addNewRow(String valuesString) {
		String[] stringArray = valuesString.split(",", -1);
		addNewRow(stringArray);
	}

	private void addNewRow(String[] stringArray) {
		BigDecimal[] row = new BigDecimal[stringArray.length];
		for (int i = 0; i < stringArray.length; i++) {
			if (stringArray[i] != null && !stringArray[i].isEmpty()) {
				row[i] = new BigDecimal(stringArray[i]);
			} else {
				row[i] = null;
			}
		}
		ratings.add(row);
	}

	private void initArray(BigDecimal[] array) {
		for (int i = 0; i < array.length; i++) {
			array[i] = new BigDecimal(0);
		}
	}
}

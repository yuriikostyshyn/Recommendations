package com.flux.recommendations.logic;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flux.recommendations.model.MeanRating;
import com.flux.recommendations.model.SparsedMatrix;
import com.flux.recommendations.model.SparsedMatrix.Node;

public class RatingsComputator {

	public static List<MeanRating> computeMostOftenWith(SparsedMatrix matrix, int movieId) {
		final List<MeanRating> result = new ArrayList<MeanRating>();
		final Node baseColumnHeader = matrix.getColumnHeader(movieId);
		Set<Integer> baseUserIds = new HashSet<Integer>();

		Node baseColumnCurrent = baseColumnHeader.getTop();
		while (baseColumnCurrent != baseColumnHeader) {
			baseUserIds.add(baseColumnCurrent.getRow());
			baseColumnCurrent = baseColumnCurrent.getTop();
		}

		Node currentColumnHeader = matrix.getHeader().getLeft();
		while (currentColumnHeader != matrix.getHeader() && currentColumnHeader.getColumn() != movieId) {
			MeanRating currentMeanRating = new MeanRating(((Integer) currentColumnHeader.getColumn()).toString(), new BigDecimal(0.0));
			int currentWithCount = 0;
			Node currentColumnElement = currentColumnHeader.getTop();
			while (currentColumnElement != currentColumnHeader) {
				if (baseUserIds.contains(currentColumnElement.getRow())) {
					currentWithCount++;
				}
				currentColumnElement = currentColumnElement.getTop();
			}

			currentMeanRating.setMeanData(new BigDecimal(currentWithCount).divide(new BigDecimal(baseUserIds.size()), new MathContext(3)));
			result.add(currentMeanRating);

			currentColumnHeader = currentColumnHeader.getLeft();
		}

		return result;
	}

	public static List<MeanRating> computeAdvancedMostOftenWith(SparsedMatrix matrix, int movieId) {
		final List<MeanRating> result = new ArrayList<MeanRating>();
		final Node baseColumnHeader = matrix.getColumnHeader(movieId);
		Set<Integer> baseUserIds = new HashSet<Integer>();

		Node baseColumnCurrent = baseColumnHeader.getTop();
		while (baseColumnCurrent != baseColumnHeader) {
			baseUserIds.add(baseColumnCurrent.getRow());
			baseColumnCurrent = baseColumnCurrent.getTop();
		}

		Node currentColumnHeader = matrix.getHeader().getLeft();
		while (currentColumnHeader != matrix.getHeader()) {
			MeanRating currentMeanRating = new MeanRating(((Integer) currentColumnHeader.getColumn()).toString(), new BigDecimal(0.0));
			int currentWithCount = 0;
			int currentWithoutCount = 0;
			Node currentColumnElement = currentColumnHeader.getTop();
			while (currentColumnElement != currentColumnHeader) {
				if (baseUserIds.contains(currentColumnElement.getRow())) {
					currentWithCount++;
				} else {
					currentWithoutCount++;
				}
				currentColumnElement = currentColumnElement.getTop();
			}

			BigDecimal ratingValue = new BigDecimal(currentWithCount).divide(new BigDecimal(baseUserIds.size()), new MathContext(3));
			ratingValue = ratingValue.divide(new BigDecimal(currentWithoutCount), new MathContext(3)).multiply(
					new BigDecimal(matrix.getHeight() - baseUserIds.size()), new MathContext(3));
			currentMeanRating.setMeanData(ratingValue);
			result.add(currentMeanRating);

			currentColumnHeader = currentColumnHeader.getLeft();
		}

		return result;
	}

}

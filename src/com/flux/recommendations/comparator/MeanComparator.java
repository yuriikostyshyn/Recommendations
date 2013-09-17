package com.flux.recommendations.comparator;

import java.util.Comparator;

import com.flux.recommendations.model.MeanRating;

public class MeanComparator implements Comparator<MeanRating> {

	@Override
	public int compare(MeanRating o1, MeanRating o2) {
		return -o1.getMeanData().compareTo(o2.getMeanData());
	}

}

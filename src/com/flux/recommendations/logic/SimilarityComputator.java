package com.flux.recommendations.logic;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flux.recommendations.model.UserRatingsList;
import com.flux.recommendations.model.UserRatingsTable;

public class SimilarityComputator {
    private final UserRatingsTable ratingsTable;
    private final Map<Integer, BigDecimal> meanRatings;
    private final Map<Integer, BigDecimal> userRatingLengths;

    public SimilarityComputator(UserRatingsTable ratingsTable) {
        this.ratingsTable = ratingsTable;
        this.meanRatings = new HashMap<Integer, BigDecimal>();
        this.userRatingLengths = new HashMap<Integer, BigDecimal>();
        computeMeanRatings();
        computeNormalizedLengths();
    }

    private void computeMeanRatings() {
        List<UserRatingsList> ratings = ratingsTable.getRatingsTable();

        for (Integer userId : ratingsTable.getUserIds()) {
            meanRatings.put(userId, new BigDecimal(0.0));
        }

        for (int i = 0; i < ratings.size(); i++) {
            for (BigDecimal movieRating : ratings.get(i).getRatingsMap().values()) {
                meanRatings.put(i, meanRatings.get(i).add(movieRating));
            }
            meanRatings.put(i, meanRatings.get(i).divide(new BigDecimal(ratings.get(i).getRatingsMap().size())));
        }

    }

    private void computeNormalizedLengths() {

    }

    public UserRatingsTable getRatingsTable() {
        return ratingsTable;
    }

    public Map<Integer, BigDecimal> getMeanRatings() {
        return meanRatings;
    }
}

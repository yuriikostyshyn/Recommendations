package com.flux.recommendations.logic;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flux.recommendations.model.MeanRating;
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
    }

    public BigDecimal computeSimilarity(Integer firstUserId, Integer secondUserId) {
        BigDecimal result = new BigDecimal(0.0);

        Integer firstUserIndex = ratingsTable.getUserIds().indexOf(firstUserId);
        Integer secondUserIndex = ratingsTable.getUserIds().indexOf(secondUserId);

        UserRatingsList firstUserRatings = ratingsTable.getRatingsByUserId(firstUserId);
        UserRatingsList secondUserRatings = ratingsTable.getRatingsByUserId(secondUserId);

        Map<Integer, BigDecimal> firstUserRatingsMap = firstUserRatings.getRatingsMap();
        Map<Integer, BigDecimal> secondUserRatingsMap = secondUserRatings.getRatingsMap();

        int count = 0;
        BigDecimal firstMeanRating = new BigDecimal(0.0);
        BigDecimal secondMeanRating = new BigDecimal(0.0);
        for (Integer movieIndex : firstUserRatingsMap.keySet()) {
            if (secondUserRatingsMap.containsKey(movieIndex)) {
                count++;
                firstMeanRating = firstMeanRating.add(firstUserRatingsMap.get(movieIndex), new MathContext(11));
                secondMeanRating = secondMeanRating.add(secondUserRatingsMap.get(movieIndex), new MathContext(11));
            }
        }

        firstMeanRating = firstMeanRating.divide(new BigDecimal(count), new MathContext(11));
        secondMeanRating = secondMeanRating.divide(new BigDecimal(count), new MathContext(11));

        meanRatings.put(firstUserIndex, firstMeanRating);
        meanRatings.put(secondUserIndex, secondMeanRating);

        BigDecimal firstLength = new BigDecimal(0.0);
        BigDecimal secondLength = new BigDecimal(0.0);

        meanRatings.put(firstUserIndex, firstMeanRating);
        meanRatings.put(secondUserIndex, secondMeanRating);

        for (Integer movieIndex : firstUserRatingsMap.keySet()) {
            if (secondUserRatingsMap.containsKey(movieIndex)) {
                BigDecimal intermediateResult = firstUserRatingsMap.get(movieIndex).subtract(meanRatings.get(firstUserIndex),
                                new MathContext(7));
                intermediateResult = intermediateResult.multiply(
                                secondUserRatingsMap.get(movieIndex).subtract(meanRatings.get(secondUserIndex), new MathContext(11)),
                                new MathContext(7));

                BigDecimal intermediateLength = firstUserRatingsMap.get(movieIndex).subtract(meanRatings.get(firstUserIndex),
                                new MathContext(7));
                firstLength = firstLength.add(intermediateLength.pow(2));
                intermediateLength = secondUserRatingsMap.get(movieIndex).subtract(meanRatings.get(secondUserIndex), new MathContext(11));
                secondLength = secondLength.add(intermediateLength.pow(2));
                result = result.add(intermediateResult, new MathContext(11));
            }
        }

        userRatingLengths.put(firstUserIndex, new BigDecimal(Math.sqrt(firstLength.doubleValue()), new MathContext(11)));
        userRatingLengths.put(secondUserIndex, new BigDecimal(Math.sqrt(secondLength.doubleValue()), new MathContext(11)));

        result = result.divide(userRatingLengths.get(firstUserIndex), new MathContext(11));
        result = result.divide(userRatingLengths.get(secondUserIndex), new MathContext(7));

        return result;
    }

    public List<MeanRating> getTopNeighbors(int userId, int neighborCount) {
        List<Integer> userIds = ratingsTable.getUserIds();

        List<MeanRating> similarityList = new ArrayList<MeanRating>();

        for (Integer currentUserId : userIds) {
            if (userId != currentUserId.intValue()) {
                similarityList.add(new MeanRating(currentUserId.toString(), computeSimilarity(userId, currentUserId)));
            }
        }

        Collections.sort(similarityList);

        return similarityList.subList(0, neighborCount);
    }

    public List<MeanRating> getTopMoviesNotNormalized(int userId, int neighborCount) {
        List<MeanRating> neighbors = getTopNeighbors(userId, neighborCount);

        List<String> movieTitles = ratingsTable.getMovieTitles();

        List<MeanRating> movieRatings = new ArrayList<MeanRating>();
        for (int i = 0; i < movieTitles.size(); i++) {
            movieRatings.add(new MeanRating(movieTitles.get(i), computeRatingForMovie(userId, i, neighbors, false)));
        }

        Collections.sort(movieRatings);

        return movieRatings.subList(0, neighborCount);
    }

    public List<MeanRating> getTopMoviesNormalized(int userId, int neighborCount) {
        List<MeanRating> neighbors = getTopNeighbors(userId, neighborCount);
        computeMeanRatings(userId);
        
        List<String> movieTitles = ratingsTable.getMovieTitles();

        List<MeanRating> movieRatings = new ArrayList<MeanRating>();
        for (int i = 0; i < movieTitles.size(); i++) {
            movieRatings.add(new MeanRating(movieTitles.get(i), computeRatingForMovie(userId, i, neighbors, true)));
        }

        Collections.sort(movieRatings);

        return movieRatings.subList(0, neighborCount);
    }

    // correlations have to be unsorted
    private BigDecimal computeRatingForMovie(int userId, int movieIndex, List<MeanRating> correlations, Boolean normalize) {
        BigDecimal result = new BigDecimal(0.0);
        BigDecimal correlationsSum = new BigDecimal(0.0);
        int targetUserIndex = ratingsTable.getUserIds().indexOf(userId);

        for (int i = 0; i < correlations.size(); i++) {
            Map<Integer, BigDecimal> currentRating = ratingsTable.getRatingsByUserId(Integer.parseInt(correlations.get(i).getItemName()))
                            .getRatingsMap();
            if (currentRating.containsKey(movieIndex)) {
                correlationsSum = correlationsSum.add(correlations.get(i).getMeanData());
                if (normalize) {
                    int index = ratingsTable.getUserIds().indexOf(Integer.parseInt(correlations.get(i).getItemName()));
                    BigDecimal intermediateResult = currentRating.get(movieIndex).subtract(meanRatings.get(index), new MathContext(6));
                    result = result.add(correlations.get(i).getMeanData().multiply(intermediateResult, new MathContext(6)),
                                    new MathContext(11));
                } else {
                    result = result.add(correlations.get(i).getMeanData().multiply(currentRating.get(movieIndex), new MathContext(6)));
                }
            }
        }
        if (!correlationsSum.equals(BigDecimal.ZERO)) {
            result = result.divide(correlationsSum, new MathContext(11));
            if(normalize)
            result = result.add(meanRatings.get(targetUserIndex));
        } else {
            result = BigDecimal.ZERO;
        }
        return result;
    }

    private void computeMeanRatings(int userId) {
        List<UserRatingsList> userRatings = ratingsTable.getRatingsTable();
        Map<Integer, BigDecimal> targetUserRatingsTable = ratingsTable.getRatingsByUserId(userId).getRatingsMap();
        for (int i = 0; i < userRatings.size(); i++) {
            Map<Integer, BigDecimal> userRatingsTable = userRatings.get(i).getRatingsMap();
            BigDecimal meanRating = new BigDecimal(0.0);
            int count = 0;
            List<Integer> intersectingIds = new ArrayList<Integer>();
            for (Integer movieIndex : userRatingsTable.keySet()) {
                    count++;
                    intersectingIds.add(movieIndex);
                    meanRating = meanRating.add(userRatingsTable.get(movieIndex), new MathContext(6));
            }
            meanRating = meanRating.divide(new BigDecimal(count), new MathContext(6));
            meanRatings.put(i, meanRating);
        }

    }

    public UserRatingsTable getRatingsTable() {
        return ratingsTable;
    }

    public Map<Integer, BigDecimal> getMeanRatings() {
        return meanRatings;
    }
}

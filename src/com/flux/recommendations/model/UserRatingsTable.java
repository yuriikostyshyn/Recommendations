package com.flux.recommendations.model;

import java.util.List;
import java.util.Map;

import com.flux.recommendations.model.UserRatingsList;

public class UserRatingsTable {
    private final List<UserRatingsList> ratingsTable;
    private final List<Integer> userIds;
    private final List<String> movieTitles;

    public UserRatingsTable(List<UserRatingsList> ratingsTable, List<Integer> userIds, List<String> movieTitles) {
        this.ratingsTable = ratingsTable;
        this.userIds = userIds;
        this.movieTitles = movieTitles;
    }

    public UserRatingsList getRatingsByUserId(int userId){
        int userIndex = userIds.indexOf(userId);
        return ratingsTable.get(userIndex);
    }
    
    public List<UserRatingsList> getRatingsTable() {
        return ratingsTable;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public List<String> getMovieTitles() {
        return movieTitles;
    }

}

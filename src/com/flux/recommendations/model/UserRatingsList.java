package com.flux.recommendations.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class UserRatingsList {
    private Map<Integer, BigDecimal> ratingsMap;

    public UserRatingsList() {
        this.ratingsMap = new HashMap<Integer, BigDecimal>();
    }

    public Map<Integer, BigDecimal> getRatingsMap() {
        return ratingsMap;
    }

    public void setRatingsMap(Map<Integer, BigDecimal> ratingsMap) {
        this.ratingsMap = ratingsMap;
    }
}

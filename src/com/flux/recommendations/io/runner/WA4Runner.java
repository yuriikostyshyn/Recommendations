package com.flux.recommendations.io.runner;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;

import com.flux.recommendations.io.ListFileReader;
import com.flux.recommendations.logic.SimilarityComputator;
import com.flux.recommendations.model.MeanRating;
import com.flux.recommendations.model.UserRatingsTable;

public class WA4Runner {

    public static void main(String[] args) throws FileNotFoundException {
        ListFileReader fileReader = new ListFileReader("recsys_data_sample-rating-matrix.csv");
        UserRatingsTable ratingsTable = fileReader.getRatingsForUsers();
        SimilarityComputator collaborativeSimilarityComputator = new SimilarityComputator(ratingsTable);
        BigDecimal testSimilarity = collaborativeSimilarityComputator.computeSimilarity(1648, 5136);
        BigDecimal testSimilarity2 = collaborativeSimilarityComputator.computeSimilarity(918, 2824);
        List<MeanRating> testNeighbors= collaborativeSimilarityComputator.getTopNeighbors(3712, 5);
        List<MeanRating> testMovies= collaborativeSimilarityComputator.getTopMoviesNotNormalized(3712, 5);
        for(MeanRating i:testNeighbors){
            System.out.println(i);
        }
        System.out.println(testSimilarity);
        System.out.println(testSimilarity2);
        for(MeanRating i:testMovies){
            System.out.println(i);
        }
        System.out.println("Test normalized");
        testMovies= collaborativeSimilarityComputator.getTopMoviesNormalized(3712, 5);
        for(MeanRating i:testMovies){
            System.out.println(i);
        }
        System.out.println("3867 movies list");
        testMovies= collaborativeSimilarityComputator.getTopMoviesNotNormalized(3867, 5);
        for(MeanRating i:testMovies){
            System.out.println(i);
        }
        System.out.println("860 movies list");
        testMovies= collaborativeSimilarityComputator.getTopMoviesNotNormalized(860, 5);
        for(MeanRating i:testMovies){
            System.out.println(i);
        }
        System.out.println("3867 movies list");
        testMovies= collaborativeSimilarityComputator.getTopMoviesNormalized(3867, 5);
        for(MeanRating i:testMovies){
            System.out.println(i);
        }
        System.out.println("860 movies list");
        testMovies= collaborativeSimilarityComputator.getTopMoviesNormalized(860, 5);
        for(MeanRating i:testMovies){
            System.out.println(i);
        }
        System.out.println("Finished");
    }
}

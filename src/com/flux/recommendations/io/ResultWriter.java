package com.flux.recommendations.io;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import com.flux.recommendations.model.MeanRating;

public class ResultWriter {

	public static void writeInLine(Map<Integer, List<MeanRating>> ratings, String fileName) {
		try {
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");

			for (int movieId : ratings.keySet()) {
				writer.print(movieId);
				for (MeanRating rating : ratings.get(movieId)) {
					writer.print(",");
					writer.print(rating.getItemName());
					writer.print(",");
					writer.print(rating.getMeanData());
				}
				writer.println();
			}

			writer.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

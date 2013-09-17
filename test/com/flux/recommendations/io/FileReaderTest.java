package com.flux.recommendations.io;


import org.junit.Test;

import com.flux.recommendations.model.RatingInfo;

public class FileReaderTest {

	@Test
	public void shouldGetRatingsFromTestFile() throws Exception {
		String fileName = "test.csv";
		FileReader fileReader = new FileReader(fileName);

		RatingInfo result = fileReader.getRatings();
		System.out.println(result);
	}
}

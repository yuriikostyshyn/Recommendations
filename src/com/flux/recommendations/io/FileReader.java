package com.flux.recommendations.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.flux.recommendations.model.RatingInfo;

public class FileReader {
	private String fileName;

	public FileReader(String fileName) {
		this.fileName = fileName;
	}

	public RatingInfo getRatings() throws FileNotFoundException {
		RatingInfo result = new RatingInfo();
		File fileToRead = new File(fileName);
		Scanner scanner = new Scanner(fileToRead);
		if (scanner.hasNextLine()) {
			result.setItemNames(scanner.nextLine().split(","));
		}
		while (scanner.hasNextLine()) {
			result.addNewRow(scanner.nextLine());
		}
		return result;
	}
}

package PicMatchTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import PicMatch.CompareImage;
import PicMatch.PicImage;
import PicMatch.Settings;

class CompareImageTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	final void testCleanResultsFolder_True() {
		String imagePath = "ImageDatabase\\F1-Cars\\F1-Cars010.jpg";
		Settings appSettings = new Settings("ImageDatabase\\F1-Cars", "ImageDatabase\\Results", true, false, false);
		CompareImage compareImage = new CompareImage(imagePath, 80, appSettings);

		// Ensure the results folder is created
		File resultsDir = new File("ImageDatabase\\Results");
		if (!resultsDir.exists()) {
			resultsDir.mkdirs();
		}

		// Make sure the file exist before testing
		File expectedFile = new File("ImageDatabase\\Results\\F1-Cars010.jpg");
		if (!expectedFile.exists()) {
			File imageFile = new File(imagePath);
			try {
				ImageIO.write(ImageIO.read(imageFile), "jpg", expectedFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		compareImage.cleanResultsFolder();
		File[] fileArray = new File("ImageDatabase\\Results\\").listFiles();
		assertEquals(0, fileArray.length);
	}

	@Test
	final void testCleanResultsFolder_False() {
		String imagePath = "ImageDatabase\\F1-Cars\\F1-Cars010.jpg";
		Settings appSettings = new Settings("ImageDatabase\\F1-Cars", "ImageDatabase\\Results", false, false, false);
		new CompareImage(imagePath, 80, appSettings);

		// Ensure the results folder is created
		File resultsDir = new File("ImageDatabase\\Results");
		if (!resultsDir.exists()) {
			resultsDir.mkdirs();
		}

		// Make sure the file exist before testing
		File expectedFile = new File("ImageDatabase\\Results\\F1-Cars010.jpg");
		if (!expectedFile.exists()) {
			File imageFile = new File(imagePath);
			try {
				ImageIO.write(ImageIO.read(imageFile), "jpg", expectedFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		File[] fileArray = new File("ImageDatabase\\Results\\").listFiles();
		assertEquals(1, fileArray.length);
	}

	@Test
	final void testWriteImage() {
		String imagePath = "ImageDatabase\\F1-Cars\\F1-Cars010.jpg";
		Settings appSettings = new Settings("", "ImageDatabase\\Results", true, false, false);
		CompareImage compareImage = new CompareImage(imagePath, 80, appSettings);
		PicImage testPic = new PicImage(imagePath);

		// Make sure the file doesnt exist before testing
		File expectedFile = new File("ImageDatabase\\Results\\F1-Cars010.jpg");
		if (expectedFile.exists()) {
			expectedFile.delete();
		}

		compareImage.writeImage(testPic);

		assertTrue(expectedFile.exists());
	}

	@Test
	final void testWriteImage_FolderDoesnotExist() {
		String imagePath = "ImageDatabase\\F1-Cars\\F1-Cars010.jpg";
		Settings appSettings = new Settings("", "ImageDatabase\\Results", true, false, false);
		CompareImage compareImage = new CompareImage(imagePath, 80, appSettings);
		PicImage testPic = new PicImage(imagePath);

		File expectedFile = new File("ImageDatabase\\Results\\F1-Cars010.jpg");

		// Delete results folder
		Path directory = Paths.get("ImageDatabase\\Results");
		try {
			Files.delete(directory);
		} catch (IOException e) {
			e.printStackTrace();
		}

		compareImage.writeImage(testPic);

		assertTrue(expectedFile.exists());
	}

	@Test
	final void testWriteImage_AlreadyExists() {
		String imagePath = "ImageDatabase\\F1-Cars\\F1-Cars010.jpg";
		Settings appSettings = new Settings("", "ImageDatabase\\Results", true, false, false);
		CompareImage compareImage = new CompareImage(imagePath, 80, appSettings);
		PicImage testPic = new PicImage(imagePath);

		compareImage.writeImage(testPic);

		// Make sure the file exists before testing
		File expectedFile = new File("ImageDatabase\\Results\\F1-Cars010.jpg");
		if (!expectedFile.exists()) {
			assertFalse(true);
		}

		compareImage.writeImage(testPic);

		assertTrue(expectedFile.exists());
	}

	@Test
	final void testCompareImage_PixelsEqual() {
		String imagePath = null;
		Settings appSettings = new Settings("", "ImageDatabase\\Results", true, false, false);
		CompareImage compareImage = new CompareImage(imagePath, 80, appSettings);

		// create arrays
		int[][] arrayr1 = new int[6][6];
		int[][] arrayr2 = new int[6][6];
		int[][] arrayg1 = new int[6][6];
		int[][] arrayg2 = new int[6][6];
		int[][] arrayb1 = new int[6][6];
		int[][] arrayb2 = new int[6][6];

		int expectedResult = -16; // As the mask is 2x2 and I have 6 values. The system will 2^4, which is 16.

		double result = compareImage.comparePixels(arrayr1, arrayr2, arrayg1, arrayg2, arrayb1, arrayb2);

		assertEquals(expectedResult, (int) result);
	}

	@Test
	final void testCompareImage_PixelsWithinRange() {
		String imagePath = null;
		Settings appSettings = new Settings("", "ImageDatabase\\Results", true, false, false);
		CompareImage compareImage = new CompareImage(imagePath, 80, appSettings);

		// create arrays
		int[][] arrayr1 = new int[6][6];
		arrayr1 = addToArray(arrayr1, 1);
		int[][] arrayr2 = new int[6][6];
		int[][] arrayg1 = new int[6][6];
		arrayg1 = addToArray(arrayg1, 1);
		int[][] arrayg2 = new int[6][6];
		int[][] arrayb1 = new int[6][6];
		arrayb1 = addToArray(arrayb1, 1);
		int[][] arrayb2 = new int[6][6];

		int expectedResult = -16;

		double result = compareImage.comparePixels(arrayr1, arrayr2, arrayg1, arrayg2, arrayb1, arrayb2);

		assertEquals(expectedResult, (int) result);
	}

	@Test
	final void testCompareImage_PixelsOutOfRange() {
		String imagePath = null;
		Settings appSettings = new Settings("", "ImageDatabase\\Results", true, false, false);
		CompareImage compareImage = new CompareImage(imagePath, 80, appSettings);

		// create arrays
		int[][] arrayr1 = new int[6][6];
		arrayr1 = addToArray(arrayr1, 31);
		int[][] arrayr2 = new int[6][6];
		int[][] arrayg1 = new int[6][6];
		arrayg1 = addToArray(arrayg1, 31);
		int[][] arrayg2 = new int[6][6];
		int[][] arrayb1 = new int[6][6];
		arrayb1 = addToArray(arrayb1, 31);
		int[][] arrayb2 = new int[6][6];

		int expectedResult = 0;

		double result = compareImage.comparePixels(arrayr1, arrayr2, arrayg1, arrayg2, arrayb1, arrayb2);

		assertEquals(expectedResult, (int) result);
	}

	@Test
	final void testCompareImage_EmptyDatabase() {
		String imagePath = "ImageDatabase\\F1-Cars\\F1-Cars010.jpg";
		Settings appSettings = new Settings("ImageDatabase\\Results2", "ImageDatabase\\Results", true, false, false);
		new CompareImage(imagePath, 80, appSettings);

		File[] fileArray = new File("ImageDatabase\\Results").listFiles();

		assertTrue(fileArray.length == 0);
	}

	@Test
	final void testCompareImage_Null() {
		String imagePath = "";
		Settings appSettings = new Settings("ImageDatabase\\Results2", "ImageDatabase\\Results", true, false, false);
		new CompareImage(imagePath, 80, appSettings);

		File[] fileArray = new File("ImageDatabase\\Results").listFiles();

		assertTrue(fileArray.length == 0);
	}

	@Test
	final void testGetDistance() {
		String imagePath = null;
		Settings appSettings = new Settings("", "ImageDatabase\\Results", true, false, false);
		CompareImage compareImage = new CompareImage(imagePath, 80, appSettings);

		long expectedResult = 17;

		long result = compareImage.getEuclideanDistance(0, 10, 0, 10, 0, 10);

		assertEquals(expectedResult, result);
	}

	final int[][] addToArray(int[][] array, int val) {
		for (int x = 0; x < array.length; x++) {
			for (int y = 0; y < array.length; y++) {
				array[x][y] = val;
			}
		}
		return array;
	}
}

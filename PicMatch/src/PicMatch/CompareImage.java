package PicMatch;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

public class CompareImage {

	private PicImage uploadedPicImage;
	private String databasePath, resultsPath;
	private Boolean cleanResultsFolder, useEdgeExtraction, useHistogram;
	private HashMap<Double, PicImage> matches_dict = new HashMap<Double, PicImage>();
	private HashMap<String, int[][]> imageRGB = new HashMap<String, int[][]>();

	public CompareImage(String uploadedImage, double accuracy, Settings appSettings) {
		if (uploadedImage != null && uploadedImage != "") {
			this.databasePath = appSettings.getDatabasePath();
			this.resultsPath = appSettings.getResultsPath();
			this.cleanResultsFolder = appSettings.getCleanResultsFolder();
			this.useEdgeExtraction = appSettings.getEdgeExtraction();
			this.useHistogram = appSettings.getHistComparison();

			if (cleanResultsFolder == true) {
				cleanResultsFolder();
			}
			uploadedPicImage = new PicImage(uploadedImage);
			if (uploadedPicImage.getImageHeight() > 512) {
				uploadedPicImage.resize(512, 512);
			}

			compareImages(uploadedPicImage, databasePath);
		}
	}

	private void compareImages(PicImage image, String dir) {

		File[] fileArray = new File(dir).listFiles();
		if (fileArray != null) {
			getImageRGB(image);

			// Loop through every image in the directory and compare with the original image
			for (File f : fileArray) {
				double resultThreshold = -(image.getTotalPixels() * 0.85);

				if (f.getName().endsWith(".png") || f.getName().endsWith(".jpg")) {
					PicImage compareImg = new PicImage(f.getPath());

					double result = compareImage(image, compareImg);
					if (useEdgeExtraction == true) {
						result += compareEdges(image, compareImg) * 1.2;
						resultThreshold = resultThreshold * 1.4;
					}

					// Matched
					if (useHistogram == false && result <= resultThreshold) {
						matches_dict.put(result, compareImg);
					} else if (useHistogram == true && result <= 5000000) {
						matches_dict.put(result, compareImg);
					}
				}
			}
		}
	}

	private double compareImage(PicImage image, PicImage compareImage) {

		int width1 = image.getImageWidth();
		int width2 = compareImage.getImageWidth();
		int height1 = image.getImageHeight();
		int height2 = compareImage.getImageHeight();

		if (width1 != width2 || height1 != height2) {
			compareImage.resize(width1, height1);
			width2 = compareImage.getImageWidth();
			height2 = compareImage.getImageHeight();
		}

		// Create arrays for the RGB values
		int[][] r2Array = new int[height1][width1];
		int[][] g2Array = new int[height1][width1];
		int[][] b2Array = new int[height1][width1];

		for (int j = 0; j < height1; j++) {
			for (int i = 0; i < width1; i++) {

				int pixel2 = compareImage.getRGB(i, j);
				Color color2 = new Color(pixel2, true);
				r2Array[j][i] = color2.getRed();
				g2Array[j][i] = color2.getGreen();
				b2Array[j][i] = color2.getBlue();
			}
		}
		double data;
		if (useHistogram == true) {
			data = compareHistograms(imageRGB.get("r1"), r2Array, imageRGB.get("g1"), g2Array, imageRGB.get("b1"),
					b2Array);
		} else {
			data = comparePixels(imageRGB.get("r1"), r2Array, imageRGB.get("g1"), g2Array, imageRGB.get("b1"), b2Array);
		}

		return data;
	}

	private void getImageRGB(PicImage image) {

		int width1 = image.getImageWidth();
		int height1 = image.getImageHeight();

		int[][] r1Array = new int[height1][width1];
		int[][] g1Array = new int[height1][width1];
		int[][] b1Array = new int[height1][width1];

		for (int j = 0; j < height1; j++) {
			for (int i = 0; i < width1; i++) {

				int pixel1 = image.getRGB(i, j);
				Color color1 = new Color(pixel1, true);
				r1Array[j][i] = color1.getRed();
				g1Array[j][i] = color1.getGreen();
				b1Array[j][i] = color1.getBlue();
			}
		}

		imageRGB.put("r1", r1Array);
		imageRGB.put("g1", g1Array);
		imageRGB.put("b1", b1Array);
	}

	public double comparePixels(int[][] arrayr1, int[][] arrayr2, int[][] arrayg1, int[][] arrayg2, int[][] arrayb1,
			int[][] arrayb2) {
		// Using mask, the system loops through the pixels and stores the RGB values
		double count = 0;
		int mask = 2;
		for (int i = mask; i < arrayr2.length - mask; i++) {
			for (int j = mask; j < arrayr2[i].length - mask; j++) {

				for (int x = 1; x <= mask; x++) {
					for (int y = 1; y <= mask; y++) {

						double dataR0 = Math.abs(arrayr1[i][j] - arrayr2[i][j + y]);
						double dataR1 = Math.abs(arrayr1[i][j] - arrayr2[i + x][j]);
						double dataR2 = Math.abs(arrayr1[i][j] - arrayr2[i][j + y]);
						double dataR3 = Math.abs(arrayr1[i][j] - arrayr2[i][j]);
						double dataR4 = Math.abs(arrayr1[i][j] - arrayr2[i - x][j - y]);
						double dataR5 = Math.abs(arrayr1[i][j] - arrayr2[i][j - y]);
						double dataR6 = Math.abs(arrayr1[i][j] - arrayr2[i - x][j]);
						double red = (dataR0 + dataR1 + dataR2 + dataR3 + dataR4 + dataR5 + dataR6) / 7;

						double dataG0 = Math.abs(arrayg1[i][j] - arrayg2[i + x][j + y]);
						double dataG1 = Math.abs(arrayg1[i][j] - arrayg2[i + x][j]);
						double dataG2 = Math.abs(arrayg1[i][j] - arrayg2[i][j + y]);
						double dataG3 = Math.abs(arrayg1[i][j] - arrayg2[i][j]);
						double dataG4 = Math.abs(arrayg1[i][j] - arrayg2[i - x][j - y]);
						double dataG5 = Math.abs(arrayg1[i][j] - arrayg2[i][j - y]);
						double dataG6 = Math.abs(arrayg1[i][j] - arrayg2[i - x][j]);
						double green = (dataG0 + dataG1 + dataG2 + dataG3 + dataG4 + dataG5 + dataG6) / 7;

						double dataB0 = Math.abs(arrayb1[i][j] - arrayb2[i + x][j + y]);
						double dataB1 = Math.abs(arrayb1[i][j] - arrayb2[i + x][j]);
						double dataB2 = Math.abs(arrayb1[i][j] - arrayb2[i][j + y]);
						double dataB3 = Math.abs(arrayb1[i][j] - arrayb2[i][j]);
						double dataB4 = Math.abs(arrayb1[i][j] - arrayb2[i - x][j - y]);
						double dataB5 = Math.abs(arrayb1[i][j] - arrayb2[i][j - y]);
						double dataB6 = Math.abs(arrayb1[i][j] - arrayb2[i - x][j]);
						double blue = (dataB0 + dataB1 + dataB2 + dataB3 + dataB4 + dataB5 + dataB6) / 7;

						// If the values are close then the counter is decremented
						if (red < 30 && green < 30 && blue < 30) {
							count--;
						}
					}
				}
			}
		}
		return count;
	}

	private double compareEdges(PicImage img1, PicImage img2) {
		BufferedImage inputImg = EdgeExtraction.CannyEdges(img1.getBufferedImage());
		PicImage pi1 = new PicImage(inputImg);
		BufferedImage compareImg = EdgeExtraction.CannyEdges(img2.getBufferedImage());
		PicImage pi2 = new PicImage(compareImg);

		return compareImage(pi1, pi2);
	}

	public long compareHistograms(int[][] arrayr1, int[][] arrayr2, int[][] arrayg1, int[][] arrayg2, int[][] arrayb1,
			int[][] arrayb2) {
		long red1 = getHistogramCount(arrayr1);
		long red2 = getHistogramCount(arrayr2);
		long green1 = getHistogramCount(arrayg1);
		long green2 = getHistogramCount(arrayg2);
		long blue1 = getHistogramCount(arrayb1);
		long blue2 = getHistogramCount(arrayb2);

		long dis = getEuclideanDistance(red1, red2, green1, green2, blue1, blue2);
		return dis;
	}

	public int getHistogramCount(int[][] histogramArray) {
		int counter = 0;
		for (int x = 0; x < histogramArray.length; x++) {
			for (int y = 0; y < histogramArray.length; y++) {
				counter += histogramArray[x][y];
			}
		}

		return counter;
	}

	public long getEuclideanDistance(long red1, long red2, long green1, long green2, long blue1, long blue2) {
		long dis;
		dis = (long) Math.sqrt((red2 - red1) * (red2 - red1) + (green2 - green1) * (green2 - green1)
				+ (blue2 - blue1) * (blue2 - blue1));

		return dis;
	}

	public void writeImage(PicImage image) {
		String fileName = image.getImageName();
		BufferedImage buffImage = image.getBufferedImage();

		// Ensure the results folder is created
		File resultsDir = new File(resultsPath);
		if (!resultsDir.exists()) {
			resultsDir.mkdirs();
		}
		try {
			File outputFile = new File(resultsPath + "\\" + fileName);
			// if the image name already exists - create a random name and save it
			if (outputFile.exists()) {
				int randomNum = ThreadLocalRandom.current().nextInt(1, 1000 + 1);
				outputFile = new File(resultsPath + "\\_image" + randomNum + ".jpg");
			}
			ImageIO.write(buffImage, "jpg", outputFile);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void cleanResultsFolder() {
		File[] fileArray = new File(resultsPath).listFiles();
		if (fileArray != null) {
			for (File f : fileArray) {
				f.delete();
			}
		}
	}

	protected File[] getMatches() {
		File[] imageResults = new File[10];
		TreeMap<Double, PicImage> treeMap = new TreeMap<>(matches_dict);
		Set<Entry<Double, PicImage>> sorted = treeMap.entrySet();

		int i = 0;
		for (Entry<Double, PicImage> sortedItem : sorted) {
			if (i < 10) {
				imageResults[i] = sortedItem.getValue().getImageFile();
				writeImage(sortedItem.getValue());
			}
			i++;
		}
		return imageResults;
	}

}

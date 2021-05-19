package PicMatch;

import java.awt.image.BufferedImage;

public class EdgeExtraction {

	private static int stDev; // Standard deviation in magnitude of image's pixels
	private static int mean; // Mean of magnitude in image's pixels
	private static int numDev = 1; // Number of standard deviations above mean for high threshold
	private static double tHigh; // Hysteresis high threshold; Definitely edge pixels, do not examine
	private static double tLow; // Hysteresis low threshold; possible edge pixel, examine further.
	private static double tFract = 0.2; // Low threshold is this fraction of high threshold
	private static int[][] maskX; // Mask resulting from horizontal 3x3 Sobel mask
	private static int[][] maskY; // Mask resulting from vertical 3x3 Sobel mask
	private static double[][] mag; 

	public static BufferedImage CannyEdges(BufferedImage img) {
		int[][] rawImage = null;
		BufferedImage edges = null;

		if (img != null) {
			rawImage = PicImage.getGreyScaleArray(img);
			maskX = Sobel.Horizontal(rawImage);
			maskY = Sobel.Vertical(rawImage);

			Magnitude();

			edges = PicImage.getGreyScaleImage(Hysteresis());
		}

		return edges;
	}

	private static void Magnitude() {
		double sum = 0;
		double var = 0;
		int height = maskX.length;
		int width = maskX[0].length;
		double pixelTotal = height * width;
		mag = new double[height][width];

		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				mag[r][c] = Math.sqrt(maskX[r][c] * maskX[r][c] + maskY[r][c] * maskY[r][c]);

				sum += mag[r][c];
			}
		}

		mean = (int) Math.round(sum / pixelTotal);
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				double diff = mag[r][c] - mean;

				var += (diff * diff);
			}
		}

		stDev = (int) Math.sqrt(var / pixelTotal);
	}

	private static int[][] Hysteresis() {
		int height = mag.length - 1;
		int width = mag[0].length - 1;
		int[][] bin = new int[height - 1][width - 1];

		tHigh = mean + (numDev * stDev); // Magnitude greater than or equal to the high threshold is an edge pixel
		tLow = tHigh * tFract; // Magnitude less than the low threshold not an edge, equal or greater possible edge

		for (int r = 1; r < height; r++) {
			for (int c = 1; c < width; c++) {
				double magnitude = mag[r][c];

				if (magnitude >= tHigh) {
					bin[r - 1][c - 1] = 255;
				} else if (magnitude < tLow) {
					bin[r - 1][c - 1] = 0;
				} else {
					boolean connected = false;

					for (int nr = -1; nr < 2; nr++) {
						for (int nc = -1; nc < 2; nc++) {
							if (mag[r + nr][c + nc] >= tHigh) {
								connected = true;
							}
						}
					}

					bin[r - 1][c - 1] = (connected) ? 255 : 0;
				}
			}
		}

		return bin;
	}
}

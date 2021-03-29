package PicMatch;

import java.io.File;
import java.text.DecimalFormat;

public class PerformanceData {

	private String databasePath;
	private String resultsPath;
	private String uploadedImage;

	public PerformanceData(Settings appSettings, String uploadedImage) {

		this.databasePath = appSettings.getDatabasePath();
		this.resultsPath = appSettings.getResultsPath();
		this.uploadedImage = uploadedImage;
	}

	protected int getDatabaseSize() {

		File[] fileArray = new File(databasePath).listFiles();
		int databaseSize = fileArray.length;

		return databaseSize;
	}

	protected int getResultsSize() {

		File[] fileArray = new File(resultsPath).listFiles();
		int resultsSize = fileArray.length;

		return resultsSize;
	}

	protected int verifyResults() {
		int count = 0;
		File[] fileArray = new File(resultsPath).listFiles();
		if (fileArray != null) {
			for (File f : fileArray) {
				String filename = f.getName();
				if (filename.charAt(0) == uploadedImage.charAt(0)) {
					count++;
				}
			}
		}
		return count;
	}

	protected String accuracyRate() {
		double results = getResultsSize();
		double TP = verifyResults();

		double accuracy = (TP / results) * 100;
		DecimalFormat dp2 = new DecimalFormat("#.##");
		return dp2.format(accuracy);
	}

}

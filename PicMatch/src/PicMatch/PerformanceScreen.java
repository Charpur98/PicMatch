package PicMatch;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class PerformanceScreen {

	private Shell shell;
	private Display display;
	private PerformanceData performaceData;
	private long timeElapsed;
	private String uploadedImage;

	public PerformanceScreen(Display display, Settings appSettings, String uploadedImage, long timeElapsed) {

		performaceData = new PerformanceData(appSettings, uploadedImage);
		this.display = display;
		this.timeElapsed = timeElapsed;
		this.uploadedImage = uploadedImage;

		// Create and open the application window
		shell = new Shell(display);
		shell.setText("System Performance");
		shell.setSize(500, 500);
		shell.setLocation(500, 150);
		shell.open();

		showData();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void showData() {

		Label lblUploadedImageName = new Label(shell, SWT.NONE);
		lblUploadedImageName.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblUploadedImageName.setBounds(10, 10, 186, 16);
		lblUploadedImageName.setText(uploadedImage);

		Label lblDatabaseSize = new Label(shell, SWT.NONE);
		lblDatabaseSize.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblDatabaseSize.setBounds(10, 30, 186, 16);
		lblDatabaseSize.setText(performaceData.getDatabaseSize() + " images searched");

		Label lblResultsSize = new Label(shell, SWT.NONE);
		lblResultsSize.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblResultsSize.setBounds(10, 50, 186, 16);
		lblResultsSize.setText(performaceData.getResultsSize() + " matches found");

		Label lblVerifiedResults = new Label(shell, SWT.NONE);
		lblVerifiedResults.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblVerifiedResults.setBounds(10, 70, 186, 16);
		lblVerifiedResults.setText(performaceData.verifyResults() + " matches are correct");

		Label lblAccuracy = new Label(shell, SWT.NONE);
		lblAccuracy.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblAccuracy.setBounds(10, 90, 186, 16);
		lblAccuracy.setText(performaceData.accuracyRate() + "% accurate");

		Label lblTimeElapsed = new Label(shell, SWT.NONE);
		lblTimeElapsed.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblTimeElapsed.setBounds(10, 110, 186, 16);
		lblTimeElapsed.setText(timeElapsed / 1000 + "s taken to complete search");

	}

}

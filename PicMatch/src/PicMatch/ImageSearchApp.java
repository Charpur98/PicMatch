package PicMatch;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Label;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

public class ImageSearchApp {

	private Display display = new Display();
	private Shell shell = new Shell(display, SWT.CLOSE | SWT.TITLE | SWT.MIN);
	private Settings appSettings = new Settings("ImageDatabase\\All", "ImageDatabase\\Results", true, false, false); // Default Settings
	private Boolean isSettingsOpen = false;

	private double screenWidth = 1600;
	private double screenHeight = 1000;
	private double appWidth = 1200;
	private double appHeight = 810;
	private double accuracyLevel = 70;

	private Label lblImageSearch, imageDisplay, imageDisplay_1, imageDisplay_2, imageDisplay_3, imageDisplay_4,
			imageDisplay_5, imageDisplay_6, imageDisplay_7, imageDisplay_8, imageDisplay_9;
	private Button btnBack, btnNext;

	private String uploadedImage;
	private File[] imageResults;
	private int imageCount = 0;

	private Label UploadedImageLbl;

	public ImageSearchApp() {

		// Create and open the application window
		shell.pack();
		shell.setBackgroundImage(new Image(display, "background.png"));
		shell.setText("PicMatch Image Search");
		Image icon = new Image(display, "Icon.png");
		shell.setImage(icon);
		shell.open();

		// Get and scale the window to the size of the users screen
		// Create and display objects
		getScreen();
		shell.setSize((int) appWidth, (int) appHeight);
		shell.setLocation(200, 100);
		SetUp();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	public static void main(String[] args) throws IOException {
		new ImageSearchApp();
	}

	private void getScreen() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = screenSize.getWidth();
		screenHeight = screenSize.getHeight();

		appWidth = screenWidth * 0.62;
		appHeight = screenHeight * 0.75;
	}

	// This method create all of the objects for the application (Displays, buttons
	// etc.)
	private void SetUp() {

		lblImageSearch = new Label(shell, SWT.NONE);
		lblImageSearch.setBounds((int) (appWidth * 0.4), 1, (int) (appWidth * 0.19), (int) (appWidth * 0.133));
		lblImageSearch.setBackground(null);
		lblImageSearch.setImage(new Image(display, "PicMatch.png"));

		Button btnSettings = new Button(shell, SWT.NONE);
		btnSettings.setBounds((int) (appWidth * 0.935), 1, (int) (appWidth * 0.05), (int) (appHeight * 0.075));

		btnSettings.setImage(new Image(display, "SettingsButton.png"));
		btnSettings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (isSettingsOpen == false) {
					isSettingsOpen = true;
					new SettingsScreen(display, appSettings);
					isSettingsOpen = false;
				}
			}
		});

		createDisplays();

		btnBack = new Button(shell, SWT.NONE);
		btnBack.setBounds(15, 390, 70, 20);
		btnBack.setImage(new Image(display, "BackButton.png"));
		btnBack.setVisible(false);
		btnBack.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (imageCount > 0 && imageCount < imageResults.length) {
					imageCount--;
					String imagePath = imageResults[imageCount].getAbsolutePath();
					Image prevImage = new Image(display, imagePath);
					ImageData imgData = prevImage.getImageData();
					imgData = imgData.scaledTo(450, 400);
					prevImage = new Image(display, imgData);
					imageDisplay_1.setImage(prevImage);
				} else {
					imageCount = 0;
				}
			}
		});

		btnNext = new Button(shell, SWT.NONE);
		btnNext.setBounds(1095, 390, 70, 20);
		btnNext.setImage(new Image(display, "NextButton.png"));
		btnNext.setVisible(false);
		btnNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (imageCount >= 0 && imageCount < imageResults.length - 1) {
					imageCount++;
					String imagePath = imageResults[imageCount].getAbsolutePath();
					Image nextImage = new Image(display, imagePath);
					ImageData imgData = nextImage.getImageData();
					imgData = imgData.scaledTo(450, 400);
					nextImage = new Image(display, imgData);
					imageDisplay_1.setImage(nextImage);
				} else {
					imageCount = imageResults.length - 1;
				}
			}
		});

		Button btnUploadImage = new Button(shell, SWT.NONE);
		btnUploadImage.setBounds((int) (appWidth * 0.37), (int) (appHeight * 0.85), (int) (appWidth * 0.24),
				(int) (appHeight * 0.06));
		btnUploadImage.setImage(new Image(display, "UploadImageButton.png"));
		btnUploadImage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				uploadImage();
			}
		});

	}

	private void uploadImage() {
		try {
			FileDialog dialog = new FileDialog((Frame) null, "Select File to Open");
			dialog.setTitle("Import Image");
			dialog.setFile("*.jpg;*.jpeg; *.png; *.tiff; *.gif");

			dialog.setMode(FileDialog.LOAD);
			dialog.setVisible(true);
			String file = dialog.getFile();
			System.out.println(file + " chosen.");

			if (file != null) {
				uploadedImage = dialog.getDirectory() + dialog.getFile();
				final long startTime = System.currentTimeMillis();
				compareImages();
				final long endTime = System.currentTimeMillis();
				shell.setCursor(new Cursor(display, 0));

				// Output performance data in a new window
				if (appSettings.getPerfData() == true) {
					long timeElapsed = endTime - startTime;
					new PerformanceScreen(display, appSettings, dialog.getFile(), timeElapsed);
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	// Run the compareImages class that takes uploadImage and compares it to the
	// database
	private void compareImages() {
		CompareImage compareImg = new CompareImage(uploadedImage, accuracyLevel, appSettings);

		imageResults = compareImg.getMatches();
		if (imageResults.length > 0) {
			displayImages();
		}
		ImageData imgData = (new Image(display, uploadedImage)).getImageData().scaledTo(200, 200);
		UploadedImageLbl.setImage(new Image(display, imgData));
		UploadedImageLbl.setVisible(true);
	}

	// Create the objects that will eventually display the resulting images
	public void createDisplays() {
		int imageWidth = (int) (appWidth * 0.166);
		int imageHeight = (int) (appHeight * 0.248);
		int xPos = (int) (appWidth * 0.083);

		imageDisplay = new Label(shell, SWT.CENTER);
		imageDisplay.setBounds(xPos, (int) (imageHeight * 1.1), imageWidth, imageHeight);
		imageDisplay.setVisible(false);

		imageDisplay_1 = new Label(shell, SWT.CENTER);
		imageDisplay_1.setBounds(xPos + imageWidth, (int) (imageHeight * 1.1), imageWidth, imageHeight);
		imageDisplay_1.setVisible(false);

		imageDisplay_2 = new Label(shell, SWT.CENTER);
		imageDisplay_2.setBounds(xPos + (imageWidth * 2), (int) (imageHeight * 1.1), imageWidth, imageHeight);
		imageDisplay_2.setVisible(false);

		imageDisplay_3 = new Label(shell, SWT.CENTER);
		imageDisplay_3.setBounds(xPos + (imageWidth * 3), (int) (imageHeight * 1.1), imageWidth, imageHeight);
		imageDisplay_3.setVisible(false);

		imageDisplay_4 = new Label(shell, SWT.CENTER);
		imageDisplay_4.setBounds(xPos + (imageWidth * 4), (int) (imageHeight * 1.1), imageWidth, imageHeight);
		imageDisplay_4.setVisible(false);

		imageDisplay_5 = new Label(shell, SWT.CENTER);
		imageDisplay_5.setBounds(xPos, (int) (imageHeight * 2.1), imageWidth, imageHeight);
		imageDisplay_5.setVisible(false);

		imageDisplay_6 = new Label(shell, SWT.CENTER);
		imageDisplay_6.setBounds(xPos + (imageWidth), (int) (imageHeight * 2.1), imageWidth, imageHeight);
		imageDisplay_6.setVisible(false);

		imageDisplay_7 = new Label(shell, SWT.CENTER);
		imageDisplay_7.setBounds(xPos + (imageWidth * 2), (int) (imageHeight * 2.1), imageWidth, imageHeight);
		imageDisplay_7.setVisible(false);

		imageDisplay_8 = new Label(shell, SWT.CENTER);
		imageDisplay_8.setBounds(xPos + (imageWidth * 3), (int) (imageHeight * 2.1), imageWidth, imageHeight);
		imageDisplay_8.setVisible(false);

		imageDisplay_9 = new Label(shell, SWT.CENTER);
		imageDisplay_9.setBounds(xPos + (imageWidth * 4), (int) (imageHeight * 2.1), imageWidth, imageHeight);
		imageDisplay_9.setVisible(false);

		UploadedImageLbl = new Label(shell, SWT.NONE);
		UploadedImageLbl.setVisible(false);
		UploadedImageLbl.setBounds(xPos, (int) (appHeight * 0.0125), imageWidth, imageHeight);

	}

	public void displayImages() {

		Image[] images = new Image[10];
		for (int i = 0; i < imageResults.length && i < 10; i++) {
			try {
				images[i] = new Image(display, imageResults[i].getAbsolutePath());
				ImageData imgData = images[i].getImageData().scaledTo(200, 200);
				images[i] = new Image(display, imgData);
			} catch (Exception e) {
			}
		}

		displayEachImage(images, imageDisplay, 0);
		displayEachImage(images, imageDisplay_1, 1);
		displayEachImage(images, imageDisplay_2, 2);
		displayEachImage(images, imageDisplay_3, 3);
		displayEachImage(images, imageDisplay_4, 4);
		displayEachImage(images, imageDisplay_5, 5);
		displayEachImage(images, imageDisplay_6, 6);
		displayEachImage(images, imageDisplay_7, 7);
		displayEachImage(images, imageDisplay_8, 8);
		displayEachImage(images, imageDisplay_9, 9);

	}

	private void displayEachImage(Image[] images, Label label, int location) {
		if (images[location] != null) {
			label.setVisible(true);
			label.setText(imageResults[location].getName());
			label.setBackgroundImage(images[location]);
		} else {
			label.setVisible(false);
		}
	}

}

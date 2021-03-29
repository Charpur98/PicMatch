package PicMatchTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import PicMatch.PicImage;

class PicImageTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	final void testPicImageString() {
		PicImage testPic = new PicImage("ImageDatabase\\F1-Cars\\F1-Cars010.jpg");
		String expectedString = "F1-Cars010.jpg";

		assertEquals(expectedString, testPic.getImageName());
	}

	@Test
	final void testGetBufferedImage() {
		PicImage testPic = new PicImage("ImageDatabase\\F1-Cars\\F1-Cars010.jpg");
		BufferedImage testImage = testPic.getBufferedImage();

		assertNotNull(testImage);
	}

	@Test
	final void testGetImageFile() {
		PicImage testPic = new PicImage("ImageDatabase\\F1-Cars\\F1-Cars010.jpg");
		File imageFile = testPic.getImageFile();
		String expectedString = "F1-Cars010.jpg";

		assertNotNull(imageFile);
		assertEquals(expectedString, imageFile.getName());
	}

	@Test
	final void testGetImageWidth() {
		PicImage testPic = new PicImage("ImageDatabase\\F1-Cars\\F1-Cars010.jpg");
		int expectedWidth = 512;

		assertEquals(expectedWidth, testPic.getImageWidth());
	}

	@Test
	final void testSetImageWidth() {
		PicImage testPic = new PicImage("ImageDatabase\\F1-Cars\\F1-Cars010.jpg");
		int widthBefore = testPic.getImageWidth();
		int expectedWidth = 200;

		testPic.setImageWidth(expectedWidth);

		assertNotEquals(widthBefore, testPic.getImageWidth());
		assertEquals(expectedWidth, testPic.getImageWidth());
	}

	@Test
	final void testGetImageHeight() {
		PicImage testPic = new PicImage("ImageDatabase\\F1-Cars\\F1-Cars010.jpg");
		int expectedHeight = 512;

		assertEquals(expectedHeight, testPic.getImageHeight());
	}

	@Test
	final void testSetImageHeight() {
		PicImage testPic = new PicImage("ImageDatabase\\F1-Cars\\F1-Cars010.jpg");
		int heightBefore = testPic.getImageHeight();
		int expectedHeight = 200;

		testPic.setImageHeight(expectedHeight);

		assertNotEquals(heightBefore, testPic.getImageHeight());
		assertEquals(expectedHeight, testPic.getImageHeight());
	}

	@Test
	final void testResize() {
		PicImage testPic = new PicImage("ImageDatabase\\F1-Cars\\F1-Cars010.jpg");
		int heightBefore = testPic.getImageHeight();
		int expectedHeight = 200;
		int widthBefore = testPic.getImageWidth();
		int expectedWidth = 200;

		testPic.resize(expectedHeight, expectedHeight);

		assertNotEquals(heightBefore, testPic.getImageHeight());
		assertEquals(expectedHeight, testPic.getImageHeight());
		assertNotEquals(widthBefore, testPic.getImageWidth());
		assertEquals(expectedWidth, testPic.getImageWidth());
	}

	@Test
	final void testGetTotalPixels() {
		PicImage testPic = new PicImage("ImageDatabase\\F1-Cars\\F1-Cars010.jpg");
		int expectedPixels = 512 * 512;

		assertEquals(expectedPixels, testPic.getTotalPixels());
	}

}

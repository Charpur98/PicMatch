package PicMatch;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PicImage {

	private int ImageWidth;
	private int ImageHeight;
	private int ImagePixels;
	private BufferedImage bufferedImage;
	private File imageFile;

	public PicImage(String filepath) {
		bufferedImage = null;
		// read image
		try {
			imageFile = new File(filepath);
			bufferedImage = ImageIO.read(imageFile);
		} catch (IOException e) {
			System.out.println(e);
		}

		ImageWidth = bufferedImage.getWidth();
		ImageHeight = bufferedImage.getHeight();
		ImagePixels = ImageWidth * ImageHeight;
	}

	public PicImage(BufferedImage image) {
		bufferedImage = image;
		ImageWidth = bufferedImage.getWidth();
		ImageHeight = bufferedImage.getHeight();
		ImagePixels = ImageWidth * ImageHeight;
	}

	protected void displayImage() throws IOException {
		ImageIcon icon = new ImageIcon(bufferedImage);
		JFrame frame = new JFrame();
		frame.setLayout(new FlowLayout());
		frame.setSize(1200, 900);

		JLabel label = new JLabel();
		label.setIcon(icon);
		frame.add(label);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public File getImageFile() {
		return imageFile;
	}

	public String getImageName() {
		return imageFile.getName();
	}

	public int getImageWidth() {
		return ImageWidth;
	}

	public void setImageWidth(int imageWidth) {
		ImageWidth = imageWidth;
	}

	public int getImageHeight() {
		return ImageHeight;
	}

	public void setImageHeight(int imageHeight) {
		ImageHeight = imageHeight;
	}

	protected int getRGB(int x, int y) {
		int pixelRGB = bufferedImage.getRGB(x, y);
		return pixelRGB;
	}

	protected void setRGB(int x, int y, int rgb) {
		bufferedImage.setRGB(x, y, rgb);
	}

	public int getTotalPixels() {
		return ImagePixels;
	}

	public void resize(int newWidth, int newHeight) {

		int oldWidth = ImageWidth;
		int oldHeight = ImageHeight;

		if (newWidth == -1 || newHeight == -1) {
			if (newWidth == -1) {
				if (newHeight == -1) {
					return;
				}
				newWidth = newHeight * oldWidth / oldHeight;
			} else {
				newHeight = newWidth * oldHeight / oldWidth;
			}
		}

		BufferedImage result = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_BGR);

		double widthSkip = (oldWidth - newWidth) / (newWidth);
		double heightSkip = (oldHeight - newHeight) / (newHeight);
		double widthCounter = 0;
		double heightCounter = 0;

		int newY = 0;

		boolean isNewImageWidthSmaller = widthSkip > 0;
		boolean isNewImageHeightSmaller = heightSkip > 0;

		for (int y = 0; y < oldHeight && newY < newHeight; y++) {

			if (isNewImageHeightSmaller && heightCounter > 1) { // new image suppose to be smaller - skip row
				heightCounter -= 1;
			} else if (heightCounter < -1) { // new image suppose to be bigger - duplicate row
				heightCounter += 1;

				if (y > 1)
					y = y - 2;
				else
					y = y - 1;
			} else {

				heightCounter += heightSkip;

				int newX = 0;

				for (int x = 0; x < oldWidth && newX < newWidth; x++) {

					if (isNewImageWidthSmaller && widthCounter > 1) { // new image suppose to be smaller - skip column
						widthCounter -= 1;
					} else if (widthCounter < -1) { // new image suppose to be bigger - duplicate pixel
						widthCounter += 1;

						if (x > 1)
							x = x - 2;
						else
							x = x - 1;
					} else {

						int rgb = bufferedImage.getRGB(x, y);
						result.setRGB(newX, newY, rgb);

						newX++;
						widthCounter += widthSkip;
					}
				}

				newY++;
			}
		}

		ImageHeight = newHeight;
		ImageWidth = newWidth;
		ImagePixels = ImageHeight * ImageWidth;
		bufferedImage = result;
	}

	public void convertToGrey() {

		BufferedImage greyImage = bufferedImage;

		for (int y = 0; y < ImageHeight; y++) {
			for (int x = 0; x < ImageWidth; x++) {
				int p = greyImage.getRGB(x, y);

				int a = (p >> 24) & 0xff;
				int r = (p >> 16) & 0xff;
				int g = (p >> 8) & 0xff;
				int b = p & 0xff;

				// calculate average
				int avg = (r + g + b) / 3;
				p = (a << 24) | (avg << 16) | (avg << 8) | avg;
				greyImage.setRGB(x, y, p);
			}
		}
		bufferedImage = greyImage;
	}

	protected static int[][] greyScaleArray(BufferedImage img) {
		int[][] gs = null;
		int height = img.getHeight();
		int width = img.getWidth();

		if (height > 0 && width > 0) {
			gs = new int[height][width];

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					int bits = img.getRGB(j, i);
					long avg = Math.round((((bits >> 16) & 0xff) + ((bits >> 8) & 0xff) + (bits & 0xff)) / 3.0);
					gs[i][j] = (int) avg;
				}
			}
		}

		return gs;
	}

	protected static BufferedImage greyScaleImage(int[][] raw) {
		BufferedImage img = null;
		int height = raw.length;
		int width = raw[0].length;

		if (height > 0 && width > 0) {
			img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					img.setRGB(j, i, (raw[i][j] << 16) | (raw[i][j] << 8) | (raw[i][j]));
				}
			}
		}

		return img;
	}

}

package uk.ac.aber.dcs.neuralnetwork.resources;

/**
 * This class stores the MNIST image data after it is loaded from the files
 * @author James
 *
 */
public class MNISTImageData {
	private int label; //Variable to hold the label
	private double[] image; //Variable to hold the image data
	
	/**
	 * Constructor to set up the classes variable
	 * @param label
	 * @param image
	 */
	public MNISTImageData(int label, double[] image) {
		this.label = label;
		this.image = image;
		convertToBlackAndWhite();
	}
	
	/**
	 * Convert the image to black and white, 0's or 1's
	 */
	public void convertToBlackAndWhite() {
		for (int i=0; i<image.length; i++) {
			if (image[i] >= 0 && image[i] <= 128) {
				image[i] = 0;
			} else  { 
				image[i] = 1;
			}
		}
	}
	
	/**
	 * Return the image data
	 * @return
	 */
	public double[] getImage() {
		return image;
	}
	
	/**
	 * Return the label
	 * @return
	 */
	public int getLabel() {
		return label;
	}
	
}

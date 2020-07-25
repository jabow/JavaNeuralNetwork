package uk.ac.aber.dcs.neuralnetwork.resources;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Load the MNIST data from the files as suggested at http://yann.lecun.com/exdb/mnist/
 * @author James
 *
 */
public class LoadMNISTImageData {
	private File labelFile;
	private File imageFile;
	List<MNISTImageData> imageLabels = new ArrayList<MNISTImageData>();
	
	public LoadMNISTImageData(File imageFile, File labelFile) {
		this.imageFile = imageFile;
		this.labelFile = labelFile;
	}
	
	/**
	 * Read the MNIST data files and return this data
	 * @return
	 * @throws IOException
	 */
	public List<MNISTImageData> readDataFiles() throws IOException {
		DataInputStream imageData = new DataInputStream(new FileInputStream(imageFile));
		DataInputStream labelData = new DataInputStream(new FileInputStream(labelFile));
	    try {
	    	//Read the first image, then read the first label and create a new imageData object
	    	int magicNumber = imageData.readInt();
	    	if(magicNumber != 2051) {
	    		throw new IOException("Incorrect magic number read from image file expected 2051 recieved: " + magicNumber);
	    	}
	    	magicNumber = labelData.readInt();
	    	if(magicNumber != 2049) {
	    		throw new IOException("Incorrect magic number read from label file expected 2049 recieved: " + magicNumber);
	    	}
	    	int numImages = imageData.readInt();
	    	int numLabels = labelData.readInt();
	    	if(numImages != numLabels) {
	    		throw new IOException("The number of images and labels do not match, the image file contains: " + numImages + " The labels file contains: " + numLabels);
	    	}
	    	int numRows = imageData.readInt();
	    	int numCols = imageData.readInt();
	    	if(numRows != 28 || numCols != 28) {
	            throw new IOException("The number of rows and columns incorrect, recieved: " + numRows + "x" + numCols + " Expected: " + 28 + "x" + 28);
	        }

	    	int numImagesRead = 0;
	    	int numLabelsRead = 0;
	    	while (numImagesRead != numImages) {//-59990) {   numImages
	    		byte label = labelData.readByte();
	    		numLabelsRead++;
	    		//byte[] image = new byte[numCols * numRows];
	    		double[] image = new double[numCols * numRows];

	    		//for (int col = 0; col < numCols; col++) {
	    			//for (int row = 0; row < numRows; row++) {
	    		for (int c=0; c<numCols * numRows; c++) {
	    			//image[c] = imageData.readByte();
	    			image[c] = imageData.read();
	    		}
	    			//}
	    		//}
	    		numImagesRead++;
	    		
	    		MNISTImageData mnistImageData = new MNISTImageData(label, image);
	    		imageLabels.add(mnistImageData);
	    		System.out.println("Number of images read: " + numImagesRead + " Number of labels read: " + numLabelsRead);
	    	}
	    } finally {
	    	imageData.close();
	    	labelData.close();
	    }
	    return imageLabels;
	}
	
}

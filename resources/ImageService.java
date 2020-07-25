package uk.ac.aber.dcs.neuralnetwork.resources;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import uk.ac.aber.dcs.neuralnetwork.trainingData.DigitTestingDataGenerator;
import uk.ac.aber.dcs.neuralnetwork.trainingData.TrainingData;
import uk.ac.aber.dcs.neuralnetwork.trainingData.TrainingDataGenerator;

/**
 * This class contains some methods which allow us to perform process's on the image data
 * @author James
 *
 */
public class ImageService {
	private static int width = 28;
	private static int height = 28;
	List<MNISTImageData> imageLabels = new ArrayList<MNISTImageData>();

	public ImageService(double[] imageData) throws IOException {
		drawImage(imageData, "image.png");
	}

	public static void main(String[] args) throws IOException {
		File testImageFile = new File("src/uk/ac/aber/dcs/neuralnetwork/resources/testset/t10k-images.idx3-ubyte");
		File testLabelFile = new File("src/uk/ac/aber/dcs/neuralnetwork/resources/testset/t10k-labels.idx1-ubyte");
		//File trainImageFile = new File("src/uk/ac/aber/dcs/neuralnetwork/resources/trainset/train-images.idx3-ubyte");
		//File trainLabelFile = new File("src/uk/ac/aber/dcs/neuralnetwork/resources/trainset/train-labels.idx1-ubyte");
		//System.out.println(new File("src/uk/ac/aber/dcs/neuralnetwork/resources/trainset/t10k-labels.idx1-ubyte").getCanonicalPath());
		//LoadImageData testImageData = new LoadImageData(testImageFile, testLabelFile);
		LoadMNISTImageData testImageData = new LoadMNISTImageData(testImageFile, testLabelFile);
		TrainingDataGenerator digitTestDataGenerator = new DigitTestingDataGenerator(testImageData.readDataFiles());
		TrainingData testData = digitTestDataGenerator.getTrainingData();
		double[][] allImages = testData.getInputs();
		double[][] allLabels = testData.getOutputs();
		saveAllMnistImages(allImages, allLabels);
	}

	public static void saveAllMnistImages(double[][] allImages, double[][] allLabels) throws IOException {
		String filename;
		int count = 0;
		for (double[] image : allImages) {
			filename = ("resources/MNISTImages/MNIST_" + count + "_Digit_" + convertFromArray(allLabels[count]) + ".png");
			System.out.println(new File("MNISTImages/MNIST_" + count + "_Digit_" + convertFromArray(allLabels[count]) + ".png").getCanonicalPath());
			drawImage(image, filename);
			count++;
		}
	}

	public static int convertFromArray(double[] label) {
		int num = 0;
		for(int i=0; i<label.length; i++) {
			if(label[i] == 1) {
				num = i;
			}
		}
		return num;
	}

	public static void drawImage(double[] array, String filename) throws IOException {
		int[] data = convertToIntArray(array);

		// create the binary mapping
		//byte BLACK = (byte)255, WHITE = (byte)0;
		byte BLACK = (byte)255, WHITE = (byte)0;
		byte[] map = {BLACK, WHITE};
		IndexColorModel icm = new IndexColorModel(1, map.length, map, map, map);

		WritableRaster raster = icm.createCompatibleWritableRaster(width, height);
		raster.setPixels(0, 0, width, height, data);
		BufferedImage image = new BufferedImage(icm, raster, false, null);


		// Write it out:
		File outputfile = new File(filename);
		ImageIO.write(image, "jpg", outputfile);
	}

	public static int[] convertToIntArray(double[] doubleArray) {
		int[] intArray = new int[doubleArray.length];
		for (int i=0; i<intArray.length; ++i) {
			intArray[i] = (int) doubleArray[i];
		}
		return intArray;
	}

	public static byte[] convertToByteArray(double[] doubleArray) {
		int times = Double.SIZE / Byte.SIZE;
		byte[] bytes = new byte[doubleArray.length * times];
		for(int i=0;i<doubleArray.length;i++){
			ByteBuffer.wrap(bytes, i*times, times).putDouble(doubleArray[i]);
		}
		return bytes;
	}
}

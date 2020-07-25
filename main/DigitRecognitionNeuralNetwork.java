package uk.ac.aber.dcs.neuralnetwork.main;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

import uk.ac.aber.dcs.neuralnetwork.Layer;
import uk.ac.aber.dcs.neuralnetwork.NeuralNetwork;
import uk.ac.aber.dcs.neuralnetwork.Neuron;
import uk.ac.aber.dcs.neuralnetwork.activationFunctions.LinearActivation;
import uk.ac.aber.dcs.neuralnetwork.activationFunctions.SigmoidActivation;
import uk.ac.aber.dcs.neuralnetwork.backpropagators.Backpropagate;
import uk.ac.aber.dcs.neuralnetwork.resources.LoadMNISTImageData;
import uk.ac.aber.dcs.neuralnetwork.trainingData.DigitTestingDataGenerator;
import uk.ac.aber.dcs.neuralnetwork.trainingData.DigitTrainingDataGenerator;
import uk.ac.aber.dcs.neuralnetwork.trainingData.TrainingData;
import uk.ac.aber.dcs.neuralnetwork.trainingData.TrainingDataGenerator;
/**
 * In this class a digit recognition neural network can be created 
 * Test methods are also availiable
 * @author James
 *
 */
public class DigitRecognitionNeuralNetwork {
	private static double learningRate;	//The learning rate
	private static double momentum;	//The momentum
	private static int hiddenNeurons;	//The hidden neurons
	private static double errorThreshold; //The error threshold
	
	/**
	 * The constructor sets up the networks parameters
	 * @param learningRate
	 * @param momentum
	 * @param hiddenNeurons
	 * @param errorThreshold
	 */
	public DigitRecognitionNeuralNetwork(double learningRate, double momentum, int hiddenNeurons, double errorThreshold) {
		DigitRecognitionNeuralNetwork.learningRate = learningRate;
		DigitRecognitionNeuralNetwork.momentum = momentum;
		DigitRecognitionNeuralNetwork.hiddenNeurons = hiddenNeurons;
		DigitRecognitionNeuralNetwork.errorThreshold = errorThreshold;
	}
	
	/**
	 * It can be run directly from a main method which trains the network, saves it to a file and then tests with
	 * the entire MNIST database
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		learningRate = 0.2;
		momentum = 0.7;
		hiddenNeurons = 100;
		errorThreshold = 0.001;
		TrainingDataGenerator digitTrainingDataGenerator = loadMNISTTrainingData(); //Load the training data
		NeuralNetwork nn = createNetwork(digitTrainingDataGenerator); //Create the network
		saveToFile(nn, new File("Digit_Neural_Network.ser"));	//Save the network to a file
		//DigitTestingDataGenerator digitTestDataGenerator = loadMNISTTestingData(); 	//Load the test data
		testMNISTDataset(nn);	//Test the network
	}
	
	/**
	 * A network is created given training data
	 * The backpropagation is used and the trained neural network is returned
	 * @param digitTrainingDataGenerator
	 * @return
	 * @throws IOException
	 */
	public static NeuralNetwork createNetwork(TrainingDataGenerator digitTrainingDataGenerator) throws IOException {
		NeuralNetwork digitNeuralNetwork = new NeuralNetwork();
		Neuron inputBias = new Neuron(new LinearActivation());
		inputBias.setOutput(1);
		Layer inputLayer = new Layer(null, inputBias);
		for(int i=0; i<784; i++) {
			Neuron n = new Neuron(new SigmoidActivation());
			n.setOutput(0);
			inputLayer.addNeuron(n);
		}
		
		Neuron bias = new Neuron(new LinearActivation());
		bias.setOutput(1);
		Layer hiddenLayer = new Layer(inputLayer, bias);
		for(int i=0; i<hiddenNeurons; i++) {
			Neuron h = new Neuron(new SigmoidActivation());
			h.setOutput(0);
			hiddenLayer.addNeuron(h);
		}
		
		//*********************
		/*Neuron bias2 = new Neuron(new LinearActivation());
		bias2.setOutput(1);
		Layer hiddenLayer2 = new Layer(hiddenLayer, bias2);
		
		for(int i=0; i<30; i++) {
			Neuron h = new Neuron(new SigmoidActivation());
			h.setOutput(0);
			hiddenLayer2.addNeuron(h);
		}*/
		
		//*******************
		
		Layer outputLayer = new Layer(hiddenLayer);
		for(int i=0; i<10; i++) {
			Neuron output = new Neuron(new SigmoidActivation());
			outputLayer.addNeuron(output);
		}
		//Add all the layers to the network
		digitNeuralNetwork.addLayer(inputLayer);
		digitNeuralNetwork.addLayer(hiddenLayer);
		//digitNeuralNetwork.addLayer(hiddenLayer2);
		digitNeuralNetwork.addLayer(outputLayer);
		
		Backpropagate backpropagator = new Backpropagate(digitNeuralNetwork, learningRate, momentum);
		backpropagator.train(digitTrainingDataGenerator, errorThreshold, true, 850);
		
		return digitNeuralNetwork;
	}
	
	/**
	 * Save a neural network into the specified file
	 * @param neuralNetwork
	 * @param file
	 * @throws IOException
	 */
	public static void saveToFile(NeuralNetwork neuralNetwork, File file) throws IOException {
		FileOutputStream fout = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fout);   
		oos.writeObject(neuralNetwork);
		oos.close();
	}
	
	/**
	 * Load the MNIST data from the files
	 * @return
	 * @throws IOException
	 */
	public static TrainingDataGenerator loadMNISTTrainingData() throws IOException {
		File trainImageFile = new File("src/uk/ac/aber/dcs/neuralnetwork/resources/trainset/train-images.idx3-ubyte");
		File trainLabelFile = new File("src/uk/ac/aber/dcs/neuralnetwork/resources/trainset/train-labels.idx1-ubyte");
		LoadMNISTImageData trainingData = new LoadMNISTImageData(trainImageFile, trainLabelFile);
		TrainingDataGenerator digitTrainingDataGenerator = new DigitTrainingDataGenerator(trainingData.readDataFiles());
		
		return digitTrainingDataGenerator;
	}
	
	/**
	 * Load the MNIST test data from its files
	 * @return
	 * @throws IOException
	 */
	public static DigitTestingDataGenerator loadMNISTTestingData() throws IOException {
		File testImageFile = new File("src/uk/ac/aber/dcs/neuralnetwork/resources/testset/t10k-images.idx3-ubyte");
		File testLabelFile = new File("src/uk/ac/aber/dcs/neuralnetwork/resources/testset/t10k-labels.idx1-ubyte");
		LoadMNISTImageData testImageData = new LoadMNISTImageData(testImageFile, testLabelFile);
		TrainingDataGenerator digitTestDataGenerator = new DigitTestingDataGenerator(testImageData.readDataFiles());
		
		return (DigitTestingDataGenerator) digitTestDataGenerator;
	}
	
	/**
	 * Test the network given some image data
	 * @param imageData
	 * @param neuralNetwork
	 * @throws IOException
	 */
	public static void testImageData(double[] imageData, NeuralNetwork neuralNetwork) throws IOException {
		convertToBlackAndWhite(imageData);
		neuralNetwork.setInput(imageData);
		double[] actualOutput = neuralNetwork.getOutput();
		double max = 0, second = 0, third = 0;
		int neuronIndex = 0, secondIndex = 0, thirdIndex = 0;
		for(int j=0; j<actualOutput.length; j++) {
			if (max < actualOutput[j]) {
				max = actualOutput[j];
				neuronIndex = j;
			}
			else if (second < actualOutput[j] && second < max) {
				second = actualOutput[j];
				secondIndex = j;
			}
			else if (third < actualOutput[j] && third < second) {
				third = actualOutput[j];
				thirdIndex = j;
			}
		}
		double output = actualOutput[neuronIndex];
		System.out.println("Calculating your output");
		System.out.println("Output = " + neuronIndex + " With a certainty of - " + output);
		System.out.println("Second Choice = " + secondIndex + " With a certainty of - " + second);
		System.out.println("Third Choice = " + thirdIndex + " With a certainty of - " + third);
	}

	/**
	 * Test the network given an image file containing a handwritten character
	 * @param file
	 * @param neuralNetwork
	 * @throws IOException
	 */
	public static void testSpecificImage(File file, NeuralNetwork neuralNetwork) throws IOException {
		//Load the image
		BufferedImage img = ImageIO.read(file);
		Raster raster= ((BufferedImage) img).getData();
		int w=raster.getWidth(),h=raster.getHeight();
		double[] pixels = new double[784];
		int count = 0;
		for (int x=0;x<w;x++) {
			for(int y=0;y<h;y++) {
				pixels[count]=raster.getSample(y,x,0);
				count++;
			}
		}
		pixels = convertToBlackAndWhite(pixels);
		//Test the image with the network
		neuralNetwork.setInput(pixels);
		double[] actualOutput = neuralNetwork.getOutput();
		double max = 0, second = 0, third = 0;
		int neuronIndex = 0, secondIndex = 0, thirdIndex = 0;
		for(int j=0; j<actualOutput.length; j++) {
			if (max < actualOutput[j]) {
				max = actualOutput[j];
				neuronIndex = j;
			}
			else if (second < actualOutput[j] && second < max) {
				second = actualOutput[j];
				secondIndex = j;
			}
			else if (third < actualOutput[j] && third < second) {
				third = actualOutput[j];
				thirdIndex = j;
			}
		}
		double output = actualOutput[neuronIndex];
		System.out.println("Calculating your output");
		System.out.println("Output = " + neuronIndex + " With a certainty of - " + output);
		System.out.println("Second Choice = " + secondIndex + " With a certainty of - " + second);
		System.out.println("Third Choice = " + thirdIndex + " With a certainty of - " + third);
	}

	/**
	 * Test the network with the entire MNIST database
	 * @param neuralNetwork
	 * @throws IOException
	 */
	public static float testMNISTDataset(NeuralNetwork neuralNetwork) throws IOException {		
		TrainingDataGenerator digitTestDataGenerator = loadMNISTTestingData();
		TrainingData testData = digitTestDataGenerator.getTrainingData();
		int correctlyPredictedOutputs = 0;
		for(int i=0; i<testData.getInputs().length; i++) {
			double[] input = testData.getInputs()[i];
			double[] output = testData.getOutputs()[i];
			neuralNetwork.setInput(input);
			double[] actualOutput = neuralNetwork.getOutput();
			double max = 0;
			int neuronIndex = 0;
			for(int j=0; j<actualOutput.length; j++) {
				if (max < actualOutput[j]) {
					max = actualOutput[j];
					neuronIndex = j;
				}
			}
			int expectedOutput = 0;
			for(int digit=0; digit<10; digit++) {
				if (output[digit] == 1) {
					expectedOutput = digit;
				}
			}

			if(expectedOutput == neuronIndex) {
				correctlyPredictedOutputs ++;
			}
		}
		System.out.println("Correctly classified - " + correctlyPredictedOutputs + " MNIST Test Digits");
		float percentage = (float) ((correctlyPredictedOutputs*100.0)/10000.0);
		System.out.println("Accuracy - " + percentage + "%");
		return percentage;
	}
	
	/**
	 * Convert the images data from grayscale to black and white
	 * @param imageData
	 * @return
	 */
	public static double[] convertToBlackAndWhite(double[] imageData) {
		for (int i=0; i<imageData.length; i++) {
			if (imageData[i] >= 0 && imageData[i] <= 128) {
				imageData[i] = 1;
			} else  { 
				imageData[i] = 0;
			}
		}
		return imageData;
	}
	
}

package uk.ac.aber.dcs.neuralnetwork.testing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import uk.ac.aber.dcs.neuralnetwork.NeuralNetwork;
import uk.ac.aber.dcs.neuralnetwork.main.UniversityGradeNeuralNetwork;

/**
 * This class performed the tests which are explained in the report
 * It also produced the graphs
 * @author James
 *
 */
public class UniGradeTester {
	private static double learningRate;
	private static double momentum;
	private static int hiddenNeurons;
	private static int hiddenLayers;
	private static double errorThreshold;
	static boolean timedBackprop;
	static int timeThreshold;

	
	/**
	 * Main method to run each of the tests
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		hiddenNeuronsTest();
	}

	
	@SuppressWarnings("static-access")
	public static void momentumTest() throws IOException {
		//Firstly decide on values for learningRate, momentum, hiddenNeurons, errorThreshold
		learningRate = 0.1;
		momentum = 0.9;
		errorThreshold = 0.005;
		hiddenLayers = 2;
		hiddenNeurons = 4;
		timedBackprop = true;
		timeThreshold = 1;


		int numIterations = 20;
		double[][] timeData = new double[2][numIterations];
		double[][] accuracyData = new double[2][numIterations];

		//Change the number of hidden neurons while training the network
		NeuralNetwork trainedNetwork = new NeuralNetwork();
		NeuralNetwork initialNetwork = new NeuralNetwork(); //We need to use the same random weights for each test iteration

		for (int i=0; i<numIterations; i++) {		
			//Create the neural network and train it, keep track of the time
			UniversityGradeNeuralNetwork gradeClassifier = new UniversityGradeNeuralNetwork(learningRate, momentum, hiddenNeurons, hiddenLayers, errorThreshold, timedBackprop, timeThreshold);

			if (i==0) {
				initialNetwork = gradeClassifier.createUntrainedUniversityGradeClassifier(); //only create a new network once with random weights
			}
			NeuralNetwork untrainedNetwork = (NeuralNetwork)deepCopy(initialNetwork);
			
			trainedNetwork = gradeClassifier.trainNetwork(untrainedNetwork);

			//Test the network with the 10000 MNIST test images, keep track of the accuracy
			int correctlyPredictedOutputs = gradeClassifier.testNetwork(trainedNetwork);
			//Calculate the accuracy
			float percentage = (float) ((correctlyPredictedOutputs*100.0)/100.0);
			accuracyData[1][i] = percentage;
			accuracyData[0][i] = momentum;
			timeData[1][i] = trainedNetwork.getElapsedTime();
			timeData[0][i] = momentum;
			momentum-=0.05;
		}

		//Create two graphs from the results showing the number of hidden neurons compared with accuracy and time
		//Accuracy Graph
		createGraph(accuracyData, "How momentum affects accuracy", "momentum", "Accuracy");
		printToFile(accuracyData, "momentumAccuracy.txt");
		createGraph(timeData, "How momentum affects time taken", "momentum", "Time");
		printToFile(timeData, "momentumTime.txt");
	}

	@SuppressWarnings("static-access")
	public static void learningRateTest() throws IOException {
		//Firstly decide on values for learningRate, momentum, hiddenNeurons, errorThreshold
		learningRate = 0.05;
		momentum = 0.9;
		errorThreshold = 0.005;
		hiddenLayers = 2;
		hiddenNeurons = 4;
		timedBackprop = true;
		timeThreshold = 1;


		int numIterations = 20;
		double[][] timeData = new double[2][numIterations];
		double[][] accuracyData = new double[2][numIterations];

		//Change the number of hidden neurons while training the network
		NeuralNetwork trainedNetwork = new NeuralNetwork();
		NeuralNetwork initialNetwork = new NeuralNetwork(); //We need to use the same random weights for each test iteration

		for (int i=0; i<numIterations; i++) {		
			//Create the neural network and train it, keep track of the time
			UniversityGradeNeuralNetwork gradeClassifier = new UniversityGradeNeuralNetwork(learningRate, momentum, hiddenNeurons, hiddenLayers, errorThreshold, timedBackprop, timeThreshold);

			if (i==0) {
				initialNetwork = gradeClassifier.createUntrainedUniversityGradeClassifier(); //only create a new network once with random weights
			}
			NeuralNetwork untrainedNetwork = (NeuralNetwork)deepCopy(initialNetwork);
			
			trainedNetwork = gradeClassifier.trainNetwork(untrainedNetwork);

			//Test the network with the 10000 MNIST test images, keep track of the accuracy
			int correctlyPredictedOutputs = gradeClassifier.testNetwork(trainedNetwork);
			//Calculate the accuracy
			float percentage = (float) ((correctlyPredictedOutputs*100.0)/100.0);
			accuracyData[1][i] = percentage;
			accuracyData[0][i] = learningRate;
			timeData[1][i] = trainedNetwork.getElapsedTime();
			timeData[0][i] = learningRate;
			learningRate+=0.05;
		}

		//Create two graphs from the results showing the number of hidden neurons compared with accuracy and time
		//Accuracy Graph
		createGraph(accuracyData, "How the Learning Rate affects accuracy", "Learning Rate", "Accuracy");
		printToFile(accuracyData, "LearnRateAccuracy.txt");
		createGraph(timeData, "How the Learning Rate affects time taken", "Learning Rate", "Time");
		printToFile(timeData, "LearnRateTime.txt");
	}

	@SuppressWarnings("static-access")
	public static void hiddenNeuronsTest() throws IOException {
		//Firstly decide on values for learningRate, momentum, hiddenNeurons, errorThreshold
		learningRate = 0.1;
		momentum = 0.9;
		errorThreshold = 0.005;
		hiddenLayers = 2;
		hiddenNeurons = 4;
		timedBackprop = true;
		timeThreshold = 1;


		int numIterations = 30;
		double[][] timeData = new double[2][numIterations];
		double[][] accuracyData = new double[2][numIterations];

		//Change the number of hidden neurons while training the network
		NeuralNetwork trainedNetwork = new NeuralNetwork();
		NeuralNetwork initialNetwork = new NeuralNetwork(); //We need to use the same random weights for each test iteration
		

		for (int i=0; i<numIterations; i++) {		
			//Create the neural network and train it, keep track of the time
			UniversityGradeNeuralNetwork gradeClassifier = new UniversityGradeNeuralNetwork(learningRate, momentum, hiddenNeurons, hiddenLayers, errorThreshold, timedBackprop, timeThreshold);
			if (i==0) {
				initialNetwork = gradeClassifier.createUntrainedUniversityGradeClassifier(); //only create a new network once with random weights
			}
			//Make a deep copy of the initial network
			NeuralNetwork untrainedNetwork = (NeuralNetwork)deepCopy(initialNetwork);
		
			trainedNetwork = gradeClassifier.trainNetwork(untrainedNetwork);
			//Test the network with the 10000 MNIST test images, keep track of the accuracy
			int correctlyPredictedOutputs = gradeClassifier.testNetwork(trainedNetwork);
			//Calculate the accuracy
			float percentage = (float) ((correctlyPredictedOutputs*100.0)/100.0);
			accuracyData[1][i] = percentage;
			accuracyData[0][i] = hiddenNeurons;
			timeData[1][i] = trainedNetwork.getElapsedTime();
			timeData[0][i] = hiddenNeurons;
			hiddenNeurons++;
		}

		//Create two graphs from the results showing the number of hidden neurons compared with accuracy and time
		//Accuracy Graph
		createGraph(accuracyData, "How the number of hidden neurons affects accuracy", "Hidden Neurons", "Accuracy");
		printToFile(accuracyData, "HiddenNeuronsAccuracy.txt");
		createGraph(timeData, "How the number of hidden neurons affects time taken", "Hidden Neurons", "Time");
		printToFile(timeData, "HiddenNeuronsTime.txt");
	}
	
	public static Object deepCopy(Object object) throws IOException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void createGraph(double[][] data, String name, String x, String y) throws IOException {
		XYDataset ds = createDataset(data);
		JFreeChart chart = ChartFactory.createXYLineChart(name,
				x, y, ds, PlotOrientation.VERTICAL, true, true, false);
		chart.setAntiAlias(true);
		File lineChart = new File( "resources/graphs/" + x + y + ".png" ); 
		ChartUtilities.saveChartAsPNG(lineChart ,chart, 640 , 480);
	}

	public static void printToFile(double data[][], String file)  {
		try {
			PrintWriter pr = new PrintWriter("resources/graphs/" + file);   
			boolean flag = true;
			for (int i=0; i<data.length; i++) {
				for (int j=0; j<data[i].length; j++) {
					if (i == 1 && flag) {
						pr.println("");
						flag = false;
					}
					pr.print(data[i][j]);
					pr.print(" ");
				}
			}
			pr.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			System.out.println("No such file exists.");
		}
	}

	public static XYDataset createDataset(double[][] data) {
		DefaultXYDataset ds = new DefaultXYDataset();
		ds.addSeries("series1", data);
		return ds;
	}
}

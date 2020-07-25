package uk.ac.aber.dcs.neuralnetwork.testing;

import java.io.File;
import java.io.IOException;

import uk.ac.aber.dcs.neuralnetwork.NeuralNetwork;
import uk.ac.aber.dcs.neuralnetwork.main.DigitRecognitionNeuralNetwork;
import uk.ac.aber.dcs.neuralnetwork.trainingData.TrainingDataGenerator;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

/**
 * This class is used to perform some tests on the digit recognition neural network it is not yet complete!
 * @author James
 *
 */
public class DigitRecognitionTester {

	private static double learningRate;
	private static double momentum;
	private static int hiddenNeurons;
	private static double errorThreshold;
	//private static double[][] data = new double[2][10];

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws IOException {
		//Firstly decide on values for learningRate, momentum, hiddenNeurons, errorThreshold
		learningRate = 0.3;
		momentum = 0.5;
		errorThreshold = 0.1;

		/*double[] accuracy = new double[10];
		double[] time = new double[10];
		double[] xAxis = new double[10];*/
		int numIterations = 30;
		double[][] timeData = new double[2][numIterations];
		double[][] accuracyData = new double[2][numIterations];


		//Load the MNIST digits
		TrainingDataGenerator digitTrainingDataGenerator = DigitRecognitionNeuralNetwork.loadMNISTTrainingData();
		//DigitTestingDataGenerator digitTestDataGenerator = DigitRecognitionNeuralNetwork.loadMNISTTestingData();

		//Change the number of hidden neurons while training the network
		NeuralNetwork nn = new NeuralNetwork();
		hiddenNeurons = 30;
		for (int i=0; i<numIterations; i++) {		
			//Create the neural network and train it, keep track of the time
			DigitRecognitionNeuralNetwork digitRecogniton = new DigitRecognitionNeuralNetwork(learningRate, momentum, hiddenNeurons, errorThreshold);

			nn = digitRecogniton.createNetwork(digitTrainingDataGenerator);

			//Test the network with the 10000 MNIST test images, keep track of the accuracy
			float percentage = DigitRecognitionNeuralNetwork.testMNISTDataset(nn);
			//int correctlyPredictedOutputs = DigitRecognitionNeuralNetwork.testMNISTDataset(nn);
			//Calculate the accuracy
			//float percentage = (float) ((correctlyPredictedOutputs*100.0)/10000.0);
			accuracyData[1][i] = percentage;
			accuracyData[0][i] = hiddenNeurons;
			timeData[1][i] = nn.getElapsedTime();
			timeData[0][i] = hiddenNeurons;
			hiddenNeurons+=20;
		}

		//Create two graphs from the results showing the number of hidden neurons compared with accuracy and time
		//Accuracy Graph
		XYDataset ds = createDataset(accuracyData);
		JFreeChart chart = 
				ChartFactory.createXYLineChart("How the number of hidden neurons affects accuracy taken",
						"Hidden Neurons", "Accuracy", ds, PlotOrientation.VERTICAL, true, true, false);
		chart.setAntiAlias(true);
		File lineChart = new File( "Accuracy.png" ); 
		ChartUtilities.saveChartAsPNG(lineChart ,chart, 640 , 480);

		//Time graph
		XYDataset ds2 = createDataset(timeData);
		JFreeChart chart2 = ChartFactory.createXYLineChart("How the number of hidden neurons affects time taken",
				"Hidden Neurons", "Time", ds2, PlotOrientation.VERTICAL, true, true, false);
		chart2.setAntiAlias(true);
		File lineChart2 = new File( "Time.png" ); 
		ChartUtilities.saveChartAsPNG(lineChart2 ,chart2, 640 , 480);
	}

	private static XYDataset createDataset(double[][] data) {
		DefaultXYDataset ds = new DefaultXYDataset();
		ds.addSeries("series1", data);
		return ds;
	}
}



package uk.ac.aber.dcs.neuralnetwork.main;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import uk.ac.aber.dcs.neuralnetwork.Layer;
import uk.ac.aber.dcs.neuralnetwork.NeuralNetwork;
import uk.ac.aber.dcs.neuralnetwork.Neuron;
import uk.ac.aber.dcs.neuralnetwork.activationFunctions.SigmoidActivation;
import uk.ac.aber.dcs.neuralnetwork.activationFunctions.ThresholdActivation;
import uk.ac.aber.dcs.neuralnetwork.backpropagators.Backpropagate;
import uk.ac.aber.dcs.neuralnetwork.trainingData.TrainingDataGenerator;
import uk.ac.aber.dcs.neuralnetwork.trainingData.XorTrainingDataGenerator;

/**
 * This class will give an example of simple neural networks such as AND, OR and XOR
 * @author James
 *
 */
public class SimpleNeuralNetworks {
	
	public static void main(String[] args) {
		//Ask the user which neural network to create
		Scanner user_input = new Scanner(System.in);
		boolean exit = false;
		while(exit != true) { 
			System.out.println("Enter the network you would like to test - and, or, xor: ");
			String usrIn = user_input.next();
			switch (usrIn) {
				case "and":	createAndNN();
							break;
				case "or": 	createOrNN();
							break;
				case "xor": createXorNN();
							System.out.println("Would you like to create an untrained Xor neural network and train it? y/n: ");
							if(user_input.next().equals("y")) {
								NeuralNetwork nn = createUntrainedXorNN(0.1, 0.9, 2, 0.0001);
								testUntrainedXORNN(nn);
							}
							break;
				default:	System.out.println("Exiting");
							exit = true;
							user_input.close();
							break;
			}
		}
	}
	
	/**
	 * Create an AND neural network with set weights
	 */
	public static void createAndNN() {
		NeuralNetwork andNN = new NeuralNetwork();
		Layer inputLayer = new Layer(null);
		Neuron a = new Neuron(new ThresholdActivation(1));	//Use the threshold activation function
		Neuron b = new Neuron(new ThresholdActivation(1));
		inputLayer.addNeuron(a);
		inputLayer.addNeuron(b);
		//No hidden layer is necessary
		Layer outputLayer = new Layer(inputLayer);
        Neuron andNeuron = new Neuron(new ThresholdActivation(1.5));
        outputLayer.addNeuron(andNeuron, new double[]{1, 1});
        //Add the two layers to the network
        andNN.addLayer(inputLayer);
        andNN.addLayer(outputLayer);
        
        System.out.println("Testing AND neural network");
        //Test the network with all possible input variations
        andNN.setInput(new double[]{0, 0});
        System.out.println("Input - 0 0: " + andNN.getOutput()[0]);
        andNN.setInput(new double[]{0, 1});
        System.out.println("Input - 0 1: " + andNN.getOutput()[0]);
        andNN.setInput(new double[]{1, 0});
        System.out.println("Input - 1 0: " + andNN.getOutput()[0]);
        andNN.setInput(new double[]{1, 1});
        System.out.println("Input - 1 1: " + andNN.getOutput()[0]);
	}
	
	/**
	 * Create an OR neural network with set weights
	 */
	private static void createOrNN() {
        NeuralNetwork orNN = new NeuralNetwork();
        Layer inputLayer = new Layer(null);
        Neuron a = new Neuron(new ThresholdActivation(1));
        Neuron b = new Neuron(new ThresholdActivation(1));
        inputLayer.addNeuron(a);
        inputLayer.addNeuron(b);
        Layer outputLayer = new Layer(inputLayer);
        Neuron orNeuron = new Neuron(new ThresholdActivation(0.9));
        outputLayer.addNeuron(orNeuron, new double[]{1, 1});
        orNN.addLayer(inputLayer);
        orNN.addLayer(outputLayer);
        
        System.out.println("Testing OR neural network");
        orNN.setInput(new double[]{0, 0});
        System.out.println("Input - 0 0: " + orNN.getOutput()[0]);
        orNN.setInput(new double[]{0, 1});
        System.out.println("Input - 0 1: " + orNN.getOutput()[0]);
        orNN.setInput(new double[]{1, 0});
        System.out.println("Input - 1 0: " + orNN.getOutput()[0]);
        orNN.setInput(new double[]{1, 1});
        System.out.println("Input - 1 1: " + orNN.getOutput()[0]);
    }
	
	/**
	 * Create an XOR neural network with set weights
	 */
	private static void createXorNN() {
		NeuralNetwork xorNN = new NeuralNetwork();
        Layer inputLayer = new Layer(null);
        Neuron a = new Neuron(new ThresholdActivation(1));
        Neuron b = new Neuron(new ThresholdActivation(1));
        inputLayer.addNeuron(a);
        inputLayer.addNeuron(b);
        //A hidden layer is required for the XOR network
        Layer hiddenLayer = new Layer(inputLayer);
        Neuron hiddenA = new Neuron(new ThresholdActivation(1.5));
        Neuron hiddenB = new Neuron(new ThresholdActivation(0.5));
        hiddenLayer.addNeuron(hiddenA, new double[]{1, 1});
        hiddenLayer.addNeuron(hiddenB, new double[]{1, 1});
        Layer outputLayer = new Layer(hiddenLayer);
        Neuron xorNeuron = new Neuron(new ThresholdActivation(0.5));
        outputLayer.addNeuron(xorNeuron, new double[]{-1, 1});
        xorNN.addLayer(inputLayer);
        xorNN.addLayer(hiddenLayer);
        xorNN.addLayer(outputLayer);
        
        System.out.println("Testing XOR neural network");
        xorNN.setInput(new double[]{0, 0});
        System.out.println("Input - 0 0: " + xorNN.getOutput()[0]);
        xorNN.setInput(new double[]{0, 1});
        System.out.println("Input - 0 1: " + xorNN.getOutput()[0]);
        xorNN.setInput(new double[]{1, 0});
        System.out.println("Input - 1 0: " + xorNN.getOutput()[0]);
        xorNN.setInput(new double[]{1, 1});
        System.out.println("Input - 1 1: " + xorNN.getOutput()[0]);
	}
  
	/**
	 * Create an untrained XOR neural network and train it with
	 * the backpropagation algorithm
	 */
	public static NeuralNetwork createUntrainedXorNN(double learningRate, double momentum, int hiddenNeurons, double errorThreshold) {
		NeuralNetwork untrainedXorNN = new NeuralNetwork();
		Neuron inputBias = new Neuron(new SigmoidActivation()); //Use a bias neuron in the input layer
		inputBias.setOutput(1);
		Layer inputLayer = new Layer(null, inputBias);
		Neuron a = new Neuron(new SigmoidActivation());
		Neuron b = new Neuron(new SigmoidActivation());
		inputLayer.addNeuron(a);
		inputLayer.addNeuron(b);
		untrainedXorNN.addLayer(inputLayer);
		Neuron bias = new Neuron(new SigmoidActivation());
		bias.setOutput(1);
		Layer hiddenLayer = new Layer(inputLayer, bias);
		for(int i=0; i<hiddenNeurons; i++) {
			Neuron h = new Neuron(new SigmoidActivation());
			hiddenLayer.addNeuron(h);
		}
		untrainedXorNN.addLayer(hiddenLayer);
		Layer outputLayer = new Layer(hiddenLayer);
		Neuron xorNeuron = new Neuron(new SigmoidActivation());
		outputLayer.addNeuron(xorNeuron);
		untrainedXorNN.addLayer(outputLayer);
		//We need to load training data as the network is currently untrained
		TrainingDataGenerator xorTrainingData = new XorTrainingDataGenerator();
		//Use the backpropagation algorithm to train the network
		Backpropagate backpropagator = new Backpropagate(untrainedXorNN, learningRate, momentum);
		backpropagator.train(xorTrainingData, errorThreshold, false, 0);
		return untrainedXorNN;
	}  
	
	/**
	 * Test the XOR network
	 * @param untrainedXorNN
	 */
	public static void testUntrainedXORNN(NeuralNetwork untrainedXorNN) {
		System.out.println("Testing trained XOR neural network");
		untrainedXorNN.setInput(new double[]{0, 0});
		System.out.println("Input - 0 0: " + (untrainedXorNN.getOutput()[0]));
		untrainedXorNN.setInput(new double[]{0, 1});
		System.out.println("Input - 0 1: " + (untrainedXorNN.getOutput()[0]));
		untrainedXorNN.setInput(new double[]{1, 0});
		System.out.println("Input - 1 0: " + (untrainedXorNN.getOutput()[0]));
		untrainedXorNN.setInput(new double[]{1, 1});
		System.out.println("Input - 1 1: " + (untrainedXorNN.getOutput()[0]));
	}
	
	/**
	 * Save the network into a file
	 */
	public static void saveToFile(NeuralNetwork neuralNetwork, File file) throws IOException {
		FileOutputStream fout = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fout);   
		oos.writeObject(neuralNetwork);
		oos.close();
	}
}

package uk.ac.aber.dcs.neuralnetwork.main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import uk.ac.aber.dcs.neuralnetwork.Layer;
import uk.ac.aber.dcs.neuralnetwork.NeuralNetwork;
import uk.ac.aber.dcs.neuralnetwork.Neuron;
import uk.ac.aber.dcs.neuralnetwork.activationFunctions.SigmoidActivation;
import uk.ac.aber.dcs.neuralnetwork.backpropagators.Backpropagate;
import uk.ac.aber.dcs.neuralnetwork.trainingData.TrainingDataGenerator;
import uk.ac.aber.dcs.neuralnetwork.trainingData.UniversityGradeTrainingDataGenerator;


public class UniversityGradeNeuralNetwork {
	static double learningRate;
	static double momentum;
	static int hiddenNeurons;
	static int hiddenLayers;
	static double errorThreshold;
	static boolean timedBackprop;
	static int timeThreshold;
	
	static File file = new File("src/uk/ac/aber/dcs/neuralnetwork/resources/training_data.txt");
	
	@SuppressWarnings("static-access")
	public UniversityGradeNeuralNetwork(double learningRate, double momentum, int hiddenNeurons, int hiddenLayers, double errorThreshold, boolean timedBackprop, int timeThreshold) {
		this.learningRate = learningRate;
		this.momentum = momentum;
		this.hiddenNeurons = hiddenNeurons;
		this.hiddenLayers = hiddenLayers;
		this.errorThreshold = errorThreshold;
		this.timedBackprop = timedBackprop;
		this.timeThreshold = timeThreshold;
	}

	/**
	 * The main method allows us to create a university grade neural network
	 * @param args
	 */
	public static void main(String[] args) {
		learningRate = 0.1;
		momentum = 0.9;
		errorThreshold = 0.005;
		timedBackprop = false;
		timeThreshold = 0;
		NeuralNetwork gradeClassifier = createUntrainedUniversityGradeClassifier();
		trainNetwork(gradeClassifier);
		testNetwork(gradeClassifier);
	}

	/**
	 * Create and train a neural network for university grade classification
	 * @return
	 */
	public static NeuralNetwork createUntrainedUniversityGradeClassifier() {
		NeuralNetwork gradeClassifier = new NeuralNetwork();

		Neuron inputBias = new Neuron(new SigmoidActivation());
		inputBias.setOutput(1);
		Layer inputLayer = new Layer(null, inputBias);

		Neuron a = new Neuron(new SigmoidActivation());
		Neuron b = new Neuron(new SigmoidActivation());
		Neuron c = new Neuron(new SigmoidActivation());
		Neuron d = new Neuron(new SigmoidActivation());
		Neuron e = new Neuron(new SigmoidActivation());
		Neuron f = new Neuron(new SigmoidActivation());
		Neuron g = new Neuron(new SigmoidActivation());

		inputLayer.addNeuron(a);
		inputLayer.addNeuron(b);
		inputLayer.addNeuron(c);
		inputLayer.addNeuron(d);
		inputLayer.addNeuron(e);
		inputLayer.addNeuron(f);
		inputLayer.addNeuron(g);
		gradeClassifier.addLayer(inputLayer);

		Neuron bias = new Neuron(new SigmoidActivation());
		bias.setOutput(1);
		Layer hiddenLayer = new Layer(inputLayer, bias);

		for(int i=0; i<hiddenNeurons; i++) {
			Neuron h = new Neuron(new SigmoidActivation());
			//h.setOutput(0);
			hiddenLayer.addNeuron(h);
		}
		
		gradeClassifier.addLayer(hiddenLayer);

		//  **********Hidden Layer 2 ****************
		Layer outputLayer;
		if (hiddenLayers > 1) {
			Neuron bias2 = new Neuron(new SigmoidActivation());
			bias2.setOutput(1);
			Layer hiddenLayer2 = new Layer(hiddenLayer, bias2);

			for(int i=0; i<hiddenNeurons; i++) {
				Neuron h = new Neuron(new SigmoidActivation());
				//h.setOutput(0);
				hiddenLayer2.addNeuron(h);
			}

			gradeClassifier.addLayer(hiddenLayer2);
			outputLayer = new Layer(hiddenLayer2);
		}
		else {
			outputLayer = new Layer(hiddenLayer);
		}

		//  **********Hidden Layer 2 END ****************
		
		Neuron outputA = new Neuron(new SigmoidActivation());
		Neuron outputB = new Neuron(new SigmoidActivation());
		Neuron outputC = new Neuron(new SigmoidActivation());
		Neuron outputD = new Neuron(new SigmoidActivation());
		Neuron outputE = new Neuron(new SigmoidActivation());


		outputLayer.addNeuron(outputA);
		outputLayer.addNeuron(outputB);
		outputLayer.addNeuron(outputC);
		outputLayer.addNeuron(outputD);
		outputLayer.addNeuron(outputE);
		
		gradeClassifier.addLayer(outputLayer);
		
		//Return the network before the random weights have been changed
		return gradeClassifier;		
	}
	
	/**
	 * Save the network into a file
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
	 * Train the network
	 * @param gradeClassifier
	 * @return
	 */
	public static NeuralNetwork trainNetwork(NeuralNetwork gradeClassifier) {
		TrainingDataGenerator universityGradeTrainingDataGenerator = new UniversityGradeTrainingDataGenerator();

		Backpropagate backpropagator = new Backpropagate(gradeClassifier, learningRate, momentum);
		backpropagator.train(universityGradeTrainingDataGenerator, errorThreshold, timedBackprop, timeThreshold);
		
		return gradeClassifier;
	}

	/**
	 * Test the network
	 * @param gradeClassifier
	 * @return
	 */
	public static int testNetwork(NeuralNetwork gradeClassifier) {
		System.out.println("Testing trained grade classifier neural network");

		int correctlyPredictedOutputs = 0;
		for (int i=0; i<100; i++) {
			double[][] inputs = null;
			double highest = 0;
			int neuronIndex = 0;
			try {
				inputs = readInputFile(file);
			} catch (NumberFormatException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			gradeClassifier.setInput(inputs[i]);
			for(int j=0; j<gradeClassifier.getOutput().length; j++) {
				//System.out.println(i + " = " + (gradeClassifier.getOutput()[j]) + "\n");
				if (highest < gradeClassifier.getOutput()[j]) {
					highest = gradeClassifier.getOutput()[j];
					neuronIndex = j;
				}
			}
			int expectedNeuronIndex = 0;
			if (i <= 40) {
				expectedNeuronIndex = 0;
			} else if (i < 50) {
				expectedNeuronIndex = 1;
			} else if (i < 60) {
				expectedNeuronIndex = 2;
			} else if (i < 70) {
				expectedNeuronIndex = 3;
			} else if (i <= 100) {
				expectedNeuronIndex = 4;
			}

			if (neuronIndex == expectedNeuronIndex) {
				correctlyPredictedOutputs++;
			} /*else {
				System.out.println("Incorrect prediction = " + i + ". Index should be " + expectedNeuronIndex + " but was " + neuronIndex);
			}*/
		}

		System.out.println("Number of correctly predicted outputs = " + correctlyPredictedOutputs);
		return correctlyPredictedOutputs;
		//check each output, add up the correct/incorrect outputs and then print out the numbers
	}
	
	/**
	 * Test a network with user input
	 * @param nn
	 * @param userInput
	 */
	public static void testUserInput(NeuralNetwork nn, int userInput) {
		double[][] inputs = null;
		try {
			inputs = readInputFile(file);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		nn.setInput(inputs[userInput]);
		
		double[] output = nn.getOutput();
		double highest = 0;
		int neuronIndex = 0;
		for (int i=0; i<output.length; i++) {
			System.out.println(i + " Output Value = " + output[i]);
			if (highest < output[i]) {
				highest = output[i];
				neuronIndex = i;
			}
		}
		String degreeClassification = null;
		if (neuronIndex == 0) {
			degreeClassification = "Fail";
		} else if(neuronIndex == 1) {
			degreeClassification = "3rd degree";
		} else if(neuronIndex == 2) {
			degreeClassification = "2:2 degree";
		} else if(neuronIndex == 3) {
			degreeClassification = "2:1 degree";
		} else if(neuronIndex == 4) {
			degreeClassification = "1st degree";
		}
		
		System.out.println("Highest Output Value is = " + highest + "Grade: " + degreeClassification);
	}

	/**
	 * Get the users input to test the network
	 * @param nn
	 */
	public static void getUserInput(NeuralNetwork nn) {
		double[][] inputs = null;
		try {
			inputs = readInputFile(file);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scanner user_input = new Scanner(System.in);
		System.out.println("Would you like to test the network? y/n");
		String in = user_input.next();
		while(!in.equals("n")) {
			System.out.println("Please enter a number between 0-100");
			int grade = user_input.nextInt();
			if (grade >=0 && grade <=100) {
				nn.setInput(inputs[grade]);
				double[] output = nn.getOutput();
				double highest = 0;
				int neuronIndex = 0;
				for (int i=0; i<output.length; i++) {
					System.out.println(i + " Output Value = " + output[i]);
					if (highest < output[i]) {
						highest = output[i];
						neuronIndex = i;
					}
				}
				String degreeClassification = null;
				if (neuronIndex == 0) {
					degreeClassification = "Fail";
				} else if(neuronIndex == 1) {
					degreeClassification = "3rd degree";
				} else if(neuronIndex == 2) {
					degreeClassification = "2:2 degree";
				} else if(neuronIndex == 3) {
					degreeClassification = "2:1 degree";
				} else if(neuronIndex == 4) {
					degreeClassification = "1st degree";
				}
				System.out.println("Highest Output Value is = " + highest + "Grade: " + degreeClassification);
				//System.out.println("The neural network has classified this degree percentage as: " + degreeClassification);
			} else {
				System.out.println("Incorrect input please enter a number between 0-100");
			}
			System.out.println("Would you like to test the network again? y/n");
			in = user_input.next();
		}
		System.out.println("Testing Completed");
		user_input.close();
	}

	/**
	 * Read the training data from a file
	 * @param file
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public static double[][] readInputFile(File file) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		double[][] inputs = new double[101][7];
		double[][] outputs = new double[101][5];
		try {
			int count = 0;
			String line; 
			String line2;
			//Read the lines two at a time one is input the other is output
			while((line = br.readLine()) != null && (line2 = br.readLine()) != null) {
				for (int i=0; i<7; i++) {
					String[] num = line.split(", ", 7);	        		
					inputs[count][i] = Double.parseDouble(num[i]); //Store all the input values in a 2D array
				}
				for (int i=0; i<5; i++) {
					String[] num = line2.split(", ", 5);
					outputs[count][i] = Double.parseDouble(num[i]); //Store all the output values in a 2D array
				}
				count++;
			}
		} finally {
			br.close();
		}
		return inputs;
	}
}
package uk.ac.aber.dcs.neuralnetwork.junit;



import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.aber.dcs.neuralnetwork.Layer;
import uk.ac.aber.dcs.neuralnetwork.NeuralNetwork;
import uk.ac.aber.dcs.neuralnetwork.Neuron;
import uk.ac.aber.dcs.neuralnetwork.activationFunctions.SigmoidActivation;
import uk.ac.aber.dcs.neuralnetwork.backpropagators.Backpropagate;
import uk.ac.aber.dcs.neuralnetwork.trainingData.TrainingDataGenerator;
import uk.ac.aber.dcs.neuralnetwork.trainingData.XorTrainingDataGenerator;


public class TestBackpropagator {

	@Before
	public void setup() { 
		
	}

	@Test
	public void testAndNN() {
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
		for(int i=0; i<2; i++) {
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
		Backpropagate backpropagator = new Backpropagate(untrainedXorNN, 0.1, 0.9);
		backpropagator.train(xorTrainingData, 0.0001, false, 0);
		
		//As the network will not be able to produce a perfect value of 0 or 1 I will round the output of the network
		untrainedXorNN.setInput(new double[]{0, 0});
		assertEquals("Incorrect output", 0, Math.round(untrainedXorNN.getOutput()[0]), 0);
		untrainedXorNN.setInput(new double[]{0, 1});
		assertEquals("Incorrect output", 1, Math.round(untrainedXorNN.getOutput()[0]), 0);
		untrainedXorNN.setInput(new double[]{1, 0});
		assertEquals("Incorrect output", 1, Math.round(untrainedXorNN.getOutput()[0]), 0);
		untrainedXorNN.setInput(new double[]{1, 1});
		assertEquals("Incorrect output", 0, Math.round(untrainedXorNN.getOutput()[0]), 0);
	}

}

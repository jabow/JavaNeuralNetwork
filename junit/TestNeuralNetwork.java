package uk.ac.aber.dcs.neuralnetwork.junit;



import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.aber.dcs.neuralnetwork.Layer;
import uk.ac.aber.dcs.neuralnetwork.NeuralNetwork;
import uk.ac.aber.dcs.neuralnetwork.Neuron;
import uk.ac.aber.dcs.neuralnetwork.activationFunctions.ThresholdActivation;


public class TestNeuralNetwork {

	@Before
	public void setup() { 
		
	}

	@Test
	public void testAndNN() {
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
		andNN.setInput(new double[]{0, 0});
		assertEquals("Incorrect output", 0, andNN.getOutput()[0], 0);
		andNN.setInput(new double[]{0, 1});
		assertEquals("Incorrect outputt", 0, andNN.getOutput()[0], 0);
		andNN.setInput(new double[]{1, 0});
		assertEquals("Incorrect output", 0, andNN.getOutput()[0], 0);
		andNN.setInput(new double[]{1, 1});
		assertEquals("Incorrect output", 1, andNN.getOutput()[0], 0);
	}
	
	@Test
	public void testORNN() {
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
        
        orNN.setInput(new double[]{0, 0});
		assertEquals("Incorrect output", 0, orNN.getOutput()[0], 0);
		orNN.setInput(new double[]{0, 1});
		assertEquals("Incorrect output", 1, orNN.getOutput()[0], 0);
		orNN.setInput(new double[]{1, 0});
		assertEquals("Incorrect output", 1, orNN.getOutput()[0], 0);
		orNN.setInput(new double[]{1, 1});
		assertEquals("Incorrect output", 1, orNN.getOutput()[0], 0);
	}
	
	@Test
	public void testXORNN() {
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
        
        xorNN.setInput(new double[]{0, 0});
		assertEquals("Incorrect output", 0, xorNN.getOutput()[0], 0);
		xorNN.setInput(new double[]{0, 1});
		assertEquals("Incorrect output", 1, xorNN.getOutput()[0], 0);
		xorNN.setInput(new double[]{1, 0});
		assertEquals("Incorrect output", 1, xorNN.getOutput()[0], 0);
		xorNN.setInput(new double[]{1, 1});
		assertEquals("Incorrect output", 0, xorNN.getOutput()[0], 0);
	}

}

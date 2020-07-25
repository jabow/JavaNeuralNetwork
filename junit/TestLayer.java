package uk.ac.aber.dcs.neuralnetwork.junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.aber.dcs.neuralnetwork.Layer;
import uk.ac.aber.dcs.neuralnetwork.Neuron;
import uk.ac.aber.dcs.neuralnetwork.activationFunctions.LinearActivation;

public class TestLayer {
	
	private Layer testLayer; 
	private Layer prevLayer; 
	private Neuron bias;
	//private ActivationFunction activation;
	
	@Before
	public void setup() { 
		bias = new Neuron(new LinearActivation());
		prevLayer = new Layer(null, bias);
		testLayer = new Layer(prevLayer, bias);
	}

	@Test
	public void testCreateLayer() {
		Neuron diffBias = new Neuron(new LinearActivation());
		//Test with just a previous layer
		assertEquals("Incorrect previous layer", prevLayer, testLayer.getPrevLayer());
		//Test with a bias neuron
		assertEquals("Incorrect previous layer", bias, testLayer.getNeurons().get(0));
		assertNotEquals("Incorrect previous layer", diffBias, testLayer.getNeurons().get(0));
		assertEquals("No bias neuron", 1, testLayer.hasBias());
	}
	
	@Test
	public void testAddNeuron() {
		Layer inputLayer = new Layer(null); 
		Layer nextLayer = new Layer(inputLayer); 
		Neuron testNeuron = new Neuron(new LinearActivation());
		assertEquals("Incorrect number of neurons in array list", 0, nextLayer.getNeurons().size());
		nextLayer.addNeuron(testNeuron);
		assertEquals("Not the same neuron", testNeuron, nextLayer.getNeurons().get(0));
		assertEquals("Incorrect number of neurons in array list", 1, nextLayer.getNeurons().size());
		
		//Add a neuron with a set of weights
		double[] weights = {0.5, 1, 0.25};
		for(int i=0; i<weights.length; i++) {
			Neuron neuron = new Neuron(new LinearActivation());
			inputLayer.addNeuron(neuron);
		}
		nextLayer.addNeuron(testNeuron, weights);
		
		for (int i=0; i<weights.length; i++) {
			assertEquals("Weight not the same", weights[i], testNeuron.getConnections().get(i).getWeight(), 0);
		}
	}
	
	

}

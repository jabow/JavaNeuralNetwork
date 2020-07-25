package uk.ac.aber.dcs.neuralnetwork.junit;



import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.aber.dcs.neuralnetwork.Connection;
import uk.ac.aber.dcs.neuralnetwork.Neuron;
import uk.ac.aber.dcs.neuralnetwork.activationFunctions.ActivationFunction;
import uk.ac.aber.dcs.neuralnetwork.activationFunctions.LinearActivation;


public class TestConnection {
	private Connection testConnection;
	private Neuron sourceNeuron;
	
	
	@Before
	public void setup() { 
		ActivationFunction activationFunction = new LinearActivation();
		sourceNeuron = new Neuron(activationFunction);
		testConnection = new Connection(sourceNeuron);
	}
	
	@Test
	public void testSetWeight() {
		testConnection.setWeight(1);
		assertEquals("Incorrect weight", 1, testConnection.getWeight(), 0);
	}
	
	@Test
	public void testGetSourceNeuron() {
		//Test the source neuron is the same as the source neuron we used in the constructor
		assertSame("Incorrect source neuron", sourceNeuron, testConnection.getSourceNeuron());
	}

}

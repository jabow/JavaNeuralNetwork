package uk.ac.aber.dcs.neuralnetwork.junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import uk.ac.aber.dcs.neuralnetwork.Connection;
import uk.ac.aber.dcs.neuralnetwork.Neuron;
import uk.ac.aber.dcs.neuralnetwork.activationFunctions.ActivationFunction;
import uk.ac.aber.dcs.neuralnetwork.activationFunctions.LinearActivation;

public class TestNeuron {
	private Neuron testNeuron;
	private Connection testConnection;
	private ActivationFunction activationFunction;
	private Neuron sourceNeuron;

	@Before
	public void setup() { 
		activationFunction = new LinearActivation();
		testNeuron = new Neuron(activationFunction);
		testConnection = new Connection(new Neuron(activationFunction));
		sourceNeuron  = new Neuron(activationFunction);
	}

	@Test
	public void testAddConnectionWithPrevNeuron() {
		//The array list of connections should be empty
		assertEquals("Incorrect number of connections", 0, testNeuron.getConnections().size());
		testNeuron.addConnection(sourceNeuron);
		//Test that the number of connections in the arrayList has changed
		assertEquals("Incorrect number of connections", 1, testNeuron.getConnections().size());
		//Test that the connection in the array list is the same as what we added
		assertSame("Different source neuron stored in the connection array list", sourceNeuron, testNeuron.getConnections().get(0).getSourceNeuron());
	}

	@Test
	public void testAddConnection() {
		//The array list of connections should be empty
		assertEquals("Incorrect number of connections", 0, testNeuron.getConnections().size());
		testNeuron.addConnection(testConnection);
		//Test that the number of connections in the arrayList has changed
		assertEquals("Incorrect number of connections", 1, testNeuron.getConnections().size());
		//Test that the connection in the array list is the same as what we added
		assertSame("Different connection in the array list", testConnection, testNeuron.getConnections().get(0));
	}

	@Test
	public void testSetError() {
		testNeuron.setError(1);
		assertEquals("Incorrect error", 1, testNeuron.getError(), 0);
	}

	@Test
	public void testSetOutput() {
		testNeuron.setOutput(1);
		assertEquals("Incorrect output", 1, testNeuron.getOutput(), 0);
		testNeuron.setOutput(5);
		assertEquals("Incorrect output", 5, testNeuron.getOutput(), 0);
	}

	@Test
	public void testCalculateInputSum() {
		//Add two connection from two seperate source neurons
		Neuron source1 = new Neuron(activationFunction);
		source1.setOutput(1);
		Neuron source2 = new Neuron(activationFunction);
		source2.setOutput(1);
		Connection conn = new Connection(source1);
		Connection conn2 = new Connection(source2);
		testNeuron.addConnection(conn);
		testNeuron.addConnection(conn2);
		//Set the connections weights
		conn.setWeight(1);
		conn2.setWeight(1);

		//The input sum in this case should be ((1*1) + (0.5*1)) = 1.5
		testNeuron.calculateInputSum();

		assertEquals("Incorrect input sum", 2, testNeuron.getInputSum() , 0);

		//Change the outputs, weights and recalculate the input sum
		source1.setOutput(0.2);
		source2.setOutput(0.6);
		conn.setWeight(0.25);
		conn2.setWeight(-0.5);
		//The input sum in this case should be ((0.2*0.25) + (0.6*-0.5)) = -0.25
		testNeuron.calculateInputSum();
		assertEquals("Incorrect input sum", -0.25, testNeuron.getInputSum() , 0);

		//Set the weights to random values
		source1.setOutput(1);
		source2.setOutput(0.5);
		conn.setWeight();
		conn2.setWeight();
		double expected = ((1 * conn.getWeight()) + (0.5 * conn2.getWeight()));
		testNeuron.calculateInputSum();
		assertEquals("Incorrect input sum", expected, testNeuron.getInputSum(), 0);
	}
	
	@Test
	public void testSetActivationFunction() {
		assertSame("Incorrect activation function", activationFunction, testNeuron.getActivationFunction());
	}

}

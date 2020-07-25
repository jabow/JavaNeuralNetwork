package uk.ac.aber.dcs.neuralnetwork;

import java.io.Serializable;
import java.util.ArrayList;

import uk.ac.aber.dcs.neuralnetwork.activationFunctions.ActivationFunction;

/**
 * This class represents a neuron
 * 
 * @author James Bowen (jab76@aber.ac.uk)
 * @version 1.0
 */

public class Neuron  implements Serializable {
	private static final long serialVersionUID = 8078810597993876399L;
	private double output; //The output value of the neuron
	private double error; //The neurons error
	private double inputSum; //The sum of all the inputs to a neuron multiplied by their weights
	private double derivative; //The derivative of the neuron
	private ActivationFunction activationFunction; //The neurons activation function

	/**
	 * Initialise the error variable to 0
	 * and set the activation function
	 * @param activationFunction
	 */
	public Neuron(ActivationFunction activationFunction){
		error = 0; 
		this.activationFunction = activationFunction;
	}

	/**
	 * Create a new connection for the neuron and store it in the arraylist
	 * Tell the connection which neuron the connection comes from
	 * @param prev
	 */
	public void addConnection(Neuron prev)	{
		Connection connection = new Connection(prev);
		getConnections().add(connection);
	}

	/**
	 * We may need to add a connection that already exists
	 * @param connection
	 */
	public void addConnection(Connection connection) {
		getConnections().add(connection);
	}

	/**
	 * Return the neurons error
	 * @return
	 */
	public double getError() {
		return this.error;
	}

	/**
	 * Set the neurons error
	 * @param error
	 */
	public void setError(double error) {
		this.error = error;
	}

	/**
	 * Return the output of this neuron
	 * @return
	 */
	public double getOutput() {
		return this.output;
	}

	/**
	 * Set the output of this neuron
	 * @param output
	 */
	public void setOutput(double output) {
		this.output = output;
	}

	/**
	 * Calculate the sum of all the inputs to the neuron 
	 * We first need to multiply by the connection weight
	 */
	public void calculateInputSum() {
		inputSum = 0;
		for(int i=0; i<getConnections().size(); i++) { //Loop through all the connections the neuron has
			//Add up the inputs by multiplying the weight by the previous output
			inputSum += getConnections().get(i).getWeight() * getConnections().get(i).getSourceNeuron().getOutput();
		}
	}

	/**
	 * The activate method calls the calculate input sum method
	 * then sets the output of the neuron to the value returned from the activation function 
	 * the derivative is calculated from the output of the activation function
	 */
	public void activate() {
		calculateInputSum();
		output = activationFunction.activate(inputSum);
		derivative = activationFunction.getDerivative(output);
	}

	/**
	 * Returns the derivative
	 * @return
	 */
	public double getDerivative() {
		return this.derivative;
	}

	/**
	 * returns the activation function
	 * @return
	 */
	public ActivationFunction getActivationFunction() {
		return activationFunction;
	}

	/**
	 * Returns an array list of connections
	 * @return
	 */
	public ArrayList<Connection> getConnections() {
		return connections;
	}

	/**
	 * Recieves an array list of connections for this neuron
	 * @param connections
	 */
	/*public void setConnections(ArrayList<Connection> connections) {
		this.connections = connections;
	}*/
	
	/**
	 * Return the input sum
	 * @return
	 */
	public double getInputSum() {
		return inputSum;
	}

	private ArrayList<Connection> connections = new ArrayList<>(); //An array list to store all the connections a neuron has
}

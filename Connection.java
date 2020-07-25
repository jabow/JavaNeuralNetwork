package uk.ac.aber.dcs.neuralnetwork;

import java.io.Serializable;

/**
 * This class represents a connection between two neurons
 * 
 * @author James Bowen (jab76@aber.ac.uk)
 * @version 1.0
 */

public class Connection  implements Serializable {
	private static final long serialVersionUID = -5281575020270984100L;
	private double weight;  //The weight of the connection
	private Neuron sourceNeuron; //The neuron from which the connection is made

	/**
	 * Set the source neuron and initialise the weight
	 * @param sourceNeuron
	 */
	public Connection(Neuron sourceNeuron)	{ 
		this.sourceNeuron = sourceNeuron;
		setWeight();
	}

	/**
	 * Set the weight to a random number between -1 and 1
	 */
	public void setWeight () {
		weight = ((Math.random() * 1) - 0.5); //Choose a random number
	}  

	/**
	 * Set the weight to the new weight value
	 * @param newWeight
	 */
	public void setWeight(double newWeight) {
		weight = newWeight;
	}

	/**
	 * Return the weight
	 * @return
	 */
	public double getWeight() {
		return weight; 
	}

	/**
	 * Return the source neuron
	 * @return
	 */
	public Neuron getSourceNeuron() {
		return sourceNeuron;
	}
}

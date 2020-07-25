package uk.ac.aber.dcs.neuralnetwork;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a layer in the network and contains methods which allow
 * for the network to determine its outputs
 * 
 * @author James Bowen (jab76@aber.ac.uk)
 * @version 1.0
 */

public class Layer implements Serializable {
	private static final long serialVersionUID = -1837852872877603028L;
	private Layer prevLayer; //The previous layer
	private ArrayList<Neuron> neurons = new ArrayList<>(); //An arraylist or neurons in the layer
	private Neuron bias; //A bias neuron

	/**
	 * Constructer with only a previous layer
	 * @param prevLayer
	 */
	public Layer(Layer prevLayer) {
		this.prevLayer = prevLayer; //Set the previous layer
	}

	/**
	 * Constructor with a bias neuron and a previous layer
	 * @param previousLayer
	 * @param bias
	 */
	public Layer(Layer previousLayer, Neuron bias) {
		this(previousLayer);
		this.bias = bias;
		getNeurons().add(bias); //Add the bias neuron
	}

	/**
	 * Return a list of all the neurons in this layer
	 * @return
	 */
	public List<Neuron> getNeurons() { 
		return this.neurons;
	}

	/** 
	 * Go through the layer and activate each neuron
	 *  Skip the bias neuron if there is one
	 */
	public void calculate() {
		int bias = hasBias();

		for(int i = bias; i < getNeurons().size(); i++) {
			getNeurons().get(i).activate(); 
		}
	}

	/**
	 * Check if the neuron has a bias
	 * @return
	 */
	public int hasBias() {
		if (bias != null) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Add a neuron to the layer
	 * Add connections for the neuron
	 * @param neuron
	 */
	public void addNeuron(Neuron neuron) {
		getNeurons().add(neuron);
		if(prevLayer != null) {
			for(Neuron prevLayerNeurons : prevLayer.getNeurons()) {
				neuron.addConnection(new Connection(prevLayerNeurons));
			}
		}
	}

	/**
	 * Add a neuron to the layer given its weights
	 * @param neuron
	 * @param weights
	 */
	public void addNeuron(Neuron neuron, double[] weights) {
		getNeurons().add(neuron);
		if(prevLayer != null) {
			if(weights.length == prevLayer.getNeurons().size() ) { 	//Check that we have the correct number of weights
				List<Neuron> prevLayerNeurons = prevLayer.getNeurons();		
				for(int i = hasBias(); i < prevLayerNeurons.size(); i++) {		//Loop through each neuron in the previous layer 
					Connection con = new Connection(prevLayerNeurons.get(i));
					neuron.addConnection(con);	//Add a connection
					con.setWeight(weights[i]);  //Set the connection weight                       
				}
			}
			else {
				throw new IllegalArgumentException("Unequal number of weights compared to neurons in the previous layer");
			}
		}
	}

	/**
	 * Return the previous layer
	 * @return
	 */
	public Layer getPrevLayer() {
		return this.prevLayer;
	}

	/**
	 * Set the neurons in this layer
	 * @param neurons
	 */
	/*public void setNeurons(ArrayList<Neuron> neurons) {
		this.neurons = neurons;
	}*/


}

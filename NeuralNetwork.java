package uk.ac.aber.dcs.neuralnetwork;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the overall structure of the neural network.
 * 
 * @author James Bowen (jab76@aber.ac.uk)
 * @version 1.0
 */

public class NeuralNetwork implements Serializable {
	private static final long serialVersionUID = -5788251497614339233L;
	private ArrayList<Layer> layers = new ArrayList<>();	//An array list of layers
	//private double[][] input;	//The neural networks inputs
	//private double idealOutput[][];	//The ideal output
	private Layer inputLayer;	//The input layer
	private Layer outputLayer;	//The output layer
	private double elapsedTime;	//The time taken for the network to train

	/**
	 * Set the outputs of the input layer neurons directly to the inputs
	 * @param inputs
	 */
	public void setInput(double[] inputs) {
		if(inputLayer != null) {
			int bias = inputLayer.hasBias(); //Check if the layer has a bias neuron
			if(inputLayer.getNeurons().size() - bias != inputs.length) { //Check the number of input neurons is equal to the number of inputs
				throw new IllegalArgumentException("The number of inputs does not match the number of neurons in the input layer, expected: " + inputs.length + " Recieved: " + (inputLayer.getNeurons().size() - bias));
			}
			else {
				List<Neuron> neurons = inputLayer.getNeurons();	//Get the neurons in the input layer
				for(int i = bias; i < neurons.size(); i++) {
					neurons.get(i).setOutput(inputs[i - bias]);	//Set the outputs directly as this is the input layer
				}
			}
		}
	}
	
	/**
	 * Start at the hidden layer go through each layer and calculate its output
	 * For each neuron in the output layer store its output value in an array
	 * Return the array of outputs
	 * @return
	 */
	public double[] getOutput() {
		double[] outputs = new double[outputLayer.getNeurons().size()];
		for(int i = 1; i < getLayers().size(); i++) {
			/*Layer layer = getLayers().get(i);
			layer.calculate();*/
			getLayers().get(i).calculate();
		}
		int i = 0;
		for(Neuron neuron : outputLayer.getNeurons()) {
			outputs[i] = neuron.getOutput();
			i++;
		}
		return outputs;
	}

	/**
	 * This method calculates the error of the neural network
	 * @param actual
	 * @param expected
	 * @return
	 */
	/*public static double error(double[] actual, double[] expected) {
		double sum = 0;
		for (int i=0; i<expected.length; i++) {		//For each output
			sum += Math.pow(expected[i] - actual[i], 2);  //Add up the sum of errors
			
		}
		return sum/2;
	}*/

	/**
	 * Add a new layer to the array list 
	 * Set the input and output layers
	 * @param layer
	 */
	public void addLayer(Layer layer) {
		getLayers().add(layer);		//Add a layer to the array list
		if(getLayers().size() == 1) {
			inputLayer = layer;	//Set the input layer
		}
		outputLayer = getLayers().get(getLayers().size() - 1);	//Set the ouput layer
	}

	/**
	 * return the layers in this network
	 * @return
	 */
	public ArrayList<Layer> getLayers() {
		return this.layers;
	}

	/*public void setLayers(ArrayList<Layer> layers) {
		this.layers = layers;
	}*/

	/**
	 * Set the elapsed time
	 * @param elapsedTime
	 */
	public void setElapsedTime(double elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	/**
	 * Return the elasped time
	 * @return
	 */
	public double getElapsedTime() {
		return this.elapsedTime;		
	}
}

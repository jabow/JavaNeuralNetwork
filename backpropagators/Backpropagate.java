package uk.ac.aber.dcs.neuralnetwork.backpropagators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.aber.dcs.neuralnetwork.Connection;
import uk.ac.aber.dcs.neuralnetwork.Layer;
import uk.ac.aber.dcs.neuralnetwork.NeuralNetwork;
import uk.ac.aber.dcs.neuralnetwork.Neuron;
import uk.ac.aber.dcs.neuralnetwork.trainingData.TrainingData;
import uk.ac.aber.dcs.neuralnetwork.trainingData.TrainingDataGenerator;

/**
 * The backpropagate class contains a training method along with the backpropagate method
 * @author James
 *
 */
public class Backpropagate {
	
    private NeuralNetwork nn;	//A neural network
    private double learningRate;	//The learning rate
    private double momentum;	//The momentum
    //private double currentEpoch;
    
    /**
     * The constructor sets the three variables
     * @param neuralNetwork
     * @param learningRate
     * @param momentum
     */
    public Backpropagate(NeuralNetwork neuralNetwork, double learningRate, double momentum) {
        this.nn = neuralNetwork;
        this.learningRate = learningRate;
        this.momentum = momentum;
    }
    
    /**
     * The train method calls the backpropagate class a until the error is below the error threshold or a time threshold
     * A timer is set once training commences and is checked after each iteration
     * An average error is taken from the last 25 samples and it is this error we compare to the error threshold
     * New training data is generated each iteration
     * @param generator
     * @param errorThreshold
     * @param timedBackprop
     * @param timeThreshold
     */
    public void train(TrainingDataGenerator generator, double errorThreshold, boolean timedBackprop, int timeThreshold) {
        double error;
        int epoch = 1;
        double averageError = 1;
        double sum = 0;
        double elapsedMinutes = 0;
        int count = 0;
        long startTime = System.currentTimeMillis(); //Start Time        
        do {
            TrainingData trainingData = generator.getTrainingData();
            error = backpropagate(trainingData.getInputs(), trainingData.getOutputs());
            
            if(count < 25) {
            	sum += error;
            	count++;
            }
            else {
            	count = 0;     	
            	averageError = sum / 25;
            	sum = error;
            }
            System.out.println("Error " + epoch + " : " + error + " Average Error = " + averageError);
            epoch++;
            //Sometimes it might be useful to stop the backpropagation after a certain amount of time, 
            //incase the error threshold cannot be met in a reasonable time
            if (timedBackprop) {
            	long currentTime = System.currentTimeMillis(); //End time
        		long deltaTime = currentTime - startTime;
        		elapsedMinutes = (deltaTime/1000.0)/60.0; //Elapsed seconds
            }
        } while(averageError > errorThreshold || elapsedMinutes < timeThreshold);
        long endTime = System.currentTimeMillis(); //End time
		long deltaTime = endTime - startTime;
		double elapsedSeconds = deltaTime / 1000.0; //Elapsed seconds
		elapsedMinutes = elapsedSeconds/60.0;
		nn.setElapsedTime(elapsedMinutes);
    }
    
    /**
	 * The backpropagate method receives the actual output of the network and the ideal output
	 * Firstly the error is backpropagated through the network from the output layer through the hidden layers
	 * After the error has been backpropagated the weights are changed
	 * @param actualOutput
	 * @param idealOutput
     * @return 
	 */
    public double backpropagate(double[][] inputs, double[][] idealOutputs)	{
    	double error = 0;
    	Map<Connection, Double> deltaMap = new HashMap<Connection, Double>();

    	for (int i=0; i<inputs.length; i++) {
    		//System.out.println("Input: " + i);
    		 double[] input = inputs[i];
             double[] idealOutput = idealOutputs[i];
             
    		//Start with the last layer and move backwards through the network calculating each neurons error
    		ArrayList<Layer> layers = nn.getLayers();
    		nn.setInput(input);
            double[] output = nn.getOutput();
    		int lastLayer = layers.size()-1;
    		for (int l=lastLayer; l>0; l--) {
    			ArrayList<Neuron> neurons = (ArrayList<Neuron>) layers.get(l).getNeurons();
    			for (int n=0; n<neurons.size(); n++) {
    				double neuronError = 0;
    				if(l == lastLayer) { //If it is the last layer the error can be calculated by
    					neuronError = neurons.get(n).getDerivative() * (output[n] - idealOutput[n]);
    				} else { //For the other layers calculate the error this way
    					neuronError = neurons.get(n).getDerivative();
    					double sum = 0;
    					List<Neuron> nextLayerNeurons = layers.get(l+1).getNeurons();
    					for (Neuron nextLayerNeuron : nextLayerNeurons) { //Loop through each neuron down stream of the current neuron
    						boolean f = false;
    						int j = 0;
    						while (j < nextLayerNeuron.getConnections().size() && !f) {
    							Connection connection = nextLayerNeuron.getConnections().get(j);
    							if(connection.getSourceNeuron() == neurons.get(n)) {
    								sum += (connection.getWeight() * nextLayerNeuron.getError());
    								f = true;
    							}
    							j++;
    						}
    					}				
    					neuronError *= sum;					
    				}
    				neurons.get(n).setError(neuronError); //Set the neurons error value
    			}	
    		}
    		//Start at the last layer again and go backwards through the network changing the weights
    		for (int l=lastLayer; l>0; l--) {
    			ArrayList<Neuron> neurons = (ArrayList<Neuron>) layers.get(l).getNeurons();
    			for (int n=0; n<neurons.size(); n++) {
    				ArrayList<Connection> conn = neurons.get(n).getConnections();
    				for (int c=0; c<conn.size(); c++) {
    					//double newLearningRate = characteristicTime > 0 ? learningRate / (1 + (currentEpoch / characteristicTime)) : learningRate;
    					double delta = learningRate  * neurons.get(n).getError() * conn.get(c).getSourceNeuron().getOutput();
    					if(deltaMap.get(conn.get(c)) != null) {
    						double previousDelta = deltaMap.get(conn.get(c));
    						delta += momentum * previousDelta;
    					}
    					deltaMap.put(conn.get(c), delta);
    					conn.get(c).setWeight(conn.get(c).getWeight()- delta);
    				}
    			}
    		}
    		output = nn.getOutput();
    		error += error(output, idealOutput);
    	}
    	return error;
    }
    
    /**
     * The overall error of the neural network is calculated in this method
     * @param actual
     * @param ideal
     * @return
     */
    public double error(double[] actual, double[] ideal) {
    	//Check the arrays are the same length
        if (actual.length != ideal.length) {
            throw new IllegalArgumentException("The lengths of the actual and ideal arrays are not the same");
        }
        double errorSum = 0;
        //for each output add up the sum of the errors
        for (int i = 0; i < ideal.length; i++) {
            errorSum += Math.pow(ideal[i] - actual[i], 2); //We use ^2 so that we do not get a negative error
        }
        return errorSum / 2;
    }
}

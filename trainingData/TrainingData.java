package uk.ac.aber.dcs.neuralnetwork.trainingData;

/**
 * A class to store the training data
 * @author James
 *
 */
public class TrainingData {
    private double[][] inputs;
    private double[][] outputs;

    /**
     * Constructor to set up the inputs and outputs
     * @param inputs
     * @param outputs
     */
    public TrainingData(double[][] inputs, double[][] outputs) {
        this.inputs = inputs;
        this.outputs = outputs;
    }

    /**
     * Return the inputs
     * @return
     */
    public double[][] getInputs() {
        return inputs;
    }

    /**
     * Return the outputs
     * @return
     */
    public double[][] getOutputs() {
    	
        return outputs;
    }
}
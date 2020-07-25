package uk.ac.aber.dcs.neuralnetwork.activationFunctions;

import java.io.Serializable;

/**
 * This interface lays out the methods for each activation function
 * @author James
 *
 */
public interface ActivationFunction  extends Serializable {
	
	/**
	 * Activate the neuron given the input sum
	 * return the output
	 * @param inputSum
	 * @return
	 */
    double activate(double inputSum);
    
    /**
     * Get the derivative given the input sum
     * return the derivative
     * @param inputSum
     * @return
     */
    double getDerivative(double inputSum);
    
    /**
     * Return the activation function being used
     * @return
     */
    ActivationFunction getFunction();
    
}

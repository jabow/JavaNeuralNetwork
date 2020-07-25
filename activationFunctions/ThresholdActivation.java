package uk.ac.aber.dcs.neuralnetwork.activationFunctions;

/**
 * The threshold activation function
 * @author James
 *
 */
public class ThresholdActivation implements ActivationFunction {
	private static final long serialVersionUID = 6249896617585406279L;
	private double threshold; //A variable to store the threshold

	/**
	 * The constructor sets the threshold variable
	 * @param threshold
	 */
	public ThresholdActivation(double threshold) {
		this.threshold = threshold;
	}

	/**
	 * Activate the neruon and return 1 or 0 based on whether the input sum is higher or lower than the threshold value 
	 */
	public double activate(double inputSum) {
		return inputSum > threshold ? 1 : 0;
	}

	/**
	 * Return 0 as the derivative
	 */
	public double getDerivative(double inputSum) {
		return 0;
	}

	/**
	 * Return the activation function
	 */
	public ActivationFunction getFunction() {
		return new ThresholdActivation(threshold);
	}

}

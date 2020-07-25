package uk.ac.aber.dcs.neuralnetwork.activationFunctions;

/**
 * The linear activation function
 * @author James
 *
 */
public class LinearActivation implements ActivationFunction {
	private static final long serialVersionUID = 909346510820212754L;

	/**
	 * Return just the input sum
	 */
	public double activate(double inputSum) {
		return inputSum;
	}

	/**
	 * Return 1 as the derivative
	 */
	public double getDerivative(double inputSum) {
		return 1;
	}

	/**
	 * return this activation function
	 */
	public ActivationFunction getFunction() {
		return new LinearActivation();
	}


}

package uk.ac.aber.dcs.neuralnetwork.activationFunctions;

/**
 * The sigmoid activation function
 * @author James
 *
 */
public class SigmoidActivation implements ActivationFunction {
	private static final long serialVersionUID = -3432519425808425568L;

	/**
	 * Activate using the sigmoid function
	 */
	public double activate(double inputSum) {
		return 1.0/(1+Math.exp(-1.0*inputSum));
	}

	/**
	 * Return the derivative
	 */
	public double getDerivative(double inputSum) {
		return inputSum * (1.0 - inputSum);
	}

	/**
	 * Return the activation function
	 */
	public ActivationFunction getFunction() {
		return new SigmoidActivation();
	}

}

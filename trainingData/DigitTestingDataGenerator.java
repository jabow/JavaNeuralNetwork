package uk.ac.aber.dcs.neuralnetwork.trainingData;

import java.util.ArrayList;
import java.util.List;
import uk.ac.aber.dcs.neuralnetwork.resources.MNISTImageData;
/**
 * Produce testing data for the digit recognition neural network
 * @author James
 *
 */
public class DigitTestingDataGenerator implements TrainingDataGenerator {
	static double[][] inputs;
	static double[][] outputs;
	int[] inputIndices;
	List<MNISTImageData> imageLabels = new ArrayList<MNISTImageData>();
	
	public DigitTestingDataGenerator(List<MNISTImageData> imageLabels) {
		this.imageLabels = imageLabels;
		inputIndices = new int[10];
		inputs = new double[imageLabels.size()][784];	
		outputs = new double[imageLabels.size()][10];
	}
	
	/**
	 * Return the training data
	 */
	@Override
	public TrainingData getTrainingData() {
		
		int j =0;
		for(MNISTImageData imageLabel : imageLabels)	{
			//inputIndices[j] = j;
			inputs[j] = imageLabel.getImage();
			int label = imageLabel.getLabel();
			outputs[j] = convertLabelToOutput(label);
			j++;
		}        
		
		return new TrainingData(inputs, outputs);
	}
	
	/**
	 * Convert the label into an array
	 * @param label
	 * @return
	 */
	public double[] convertLabelToOutput(int label) {
		double[] labelArray = new double[10];
		//Firstly convert the label number into an array to display the correct output as a neural network would
		for (int i=0; i<10; i++) {
			if (label == i) {
				labelArray[i] = 1;
			} else {
				labelArray[i] = 0;
			}
		}
		return labelArray;
	}
}

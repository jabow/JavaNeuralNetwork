package uk.ac.aber.dcs.neuralnetwork.trainingData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import uk.ac.aber.dcs.neuralnetwork.resources.MNISTImageData;

/**
 * This class produces the training data for the digit recognition neural network
 * @author James
 *
 */
public class DigitTrainingDataGenerator implements TrainingDataGenerator {
	static double[][] inputs;
	static double[] outputs;
	int[] inputIndices;
	List<MNISTImageData> imageLabels = new ArrayList<MNISTImageData>();
	
	public DigitTrainingDataGenerator(List<MNISTImageData> imageLabels) {
		this.imageLabels = imageLabels;
		inputIndices = new int[10];
		inputs = new double[imageLabels.size()][784];	
		outputs = new double[imageLabels.size()];
	}
	
	/**
	 * This method returns 10 random images with each digit 0-9
	 */
	@Override
	public TrainingData getTrainingData() {
		
		int j =0;
		for(MNISTImageData imageLabel : imageLabels)	{
			//inputIndices[j] = j;
			inputs[j] = imageLabel.getImage();
			outputs[j] = imageLabel.getLabel();
			j++;
		}
        
        double[][] limitedInputs = new double[10][784];
        double[][] limitedOutputs = new double[10][10];
        
        for(int i=0; i<10; i++) {
        	inputIndices[i] = i;
        	limitedInputs[i] = getRandomLabelData(i);
        	limitedOutputs[i] = convertLabelToOutput(i);
        }
        
        inputIndices = shuffle(inputIndices);
        double[][] randomizedInputs = new double[10][784];
		double[][] randomizedOutputs = new double[10][10];
        for(int i = 0; i < inputIndices.length; i++) {
            randomizedInputs[i] = limitedInputs[inputIndices[i]];
            randomizedOutputs[i] = limitedOutputs[inputIndices[i]];
        }
           	
		return new TrainingData(randomizedInputs, randomizedOutputs);
	}
	
	/**
	 * Convert the array into an integer label
	 * @param label
	 * @return
	 */
	public double[] getRandomLabelData(int label) {
		Random random = new Random();
		int min = 0;
    	int max = inputs.length-1;
    	int rnd = random.nextInt(max - min + 1) + min;
    	while(outputs[rnd] != label) {
    		rnd = random.nextInt(max - min + 1) + min;
    	}
		return inputs[rnd];
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
	
	/**
	 * Shuffle the digits
	 * @param array
	 * @return
	 */
	private int[] shuffle(int[] array) {
		//Random number
		Random random = new Random();
		for(int i = array.length - 1; i > 0; i--) {

			int index = random.nextInt(i + 1);

			int temp = array[i];
			array[i] = array[index];
			array[index] = temp;
		}

		return array;
	}
}

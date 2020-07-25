package uk.ac.aber.dcs.neuralnetwork.trainingData;

import java.util.Random;

public class XorTrainingDataGenerator implements TrainingDataGenerator {
	
	double[][] inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
    double[][] outputs = {{0}, {1}, {1}, {0}};
    
    int[] inputIndices = {0, 1, 2, 3};

    public TrainingData getTrainingData() {
        double[][] randomizedInputs = new double[4][2];
        double[][] randomizedOutputs = new double[4][1];

        inputIndices = shuffle(inputIndices);

        for(int i = 0; i < inputIndices.length; i++) {
            randomizedInputs[i] = inputs[inputIndices[i]];
            randomizedOutputs[i] = outputs[inputIndices[i]];
        }

//        return new TrainingData(inputs, outputs);
        return new TrainingData(randomizedInputs, randomizedOutputs);
    }

    private int[] shuffle(int[] array) {

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

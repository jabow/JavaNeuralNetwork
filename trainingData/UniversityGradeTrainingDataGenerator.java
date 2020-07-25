package uk.ac.aber.dcs.neuralnetwork.trainingData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class UniversityGradeTrainingDataGenerator implements TrainingDataGenerator {

	static double[][] inputs = new double[101][7];	
	static double[][] outputs = new double[101][5];
	int[] inputIndices = new int[101];
	@Override
	public TrainingData getTrainingData() {
		// TODO Auto-generated method stub
		try {
			readInputFile("src/uk/ac/aber/dcs/neuralnetwork/resources/training_data.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i=0; i<100; i++) {
			inputIndices[i] = i;
		}

		double[][] randomizedInputs = new double[10][7];
		double[][] randomizedOutputs = new double[10][5];

		inputIndices = shuffle(inputIndices);
		

        for(int i = 0; i < randomizedInputs.length; i++) {
        	Random random = new Random();
    		int min = 0;
        	int max = inputs.length-1;
        	int rnd = random.nextInt(max - min + 1) + min;
            randomizedInputs[i] = inputs[rnd];
            randomizedOutputs[i] = outputs[rnd];
        }
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
	
	public static void readInputFile(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
	    try {
	    	int count = 0;
	    	String line; 
	    	String line2;
	    	//Read the lines two at a time one is input the other is output
	    	while((line = br.readLine()) != null && (line2 = br.readLine()) != null) {
	    		for (int i=0; i<7; i++) {
	        		String[] num = line.split(", ", 7);	        		
	        		inputs[count][i] = Double.parseDouble(num[i]); //Store all the input values in a 2D array
	        	}
	        	for (int i=0; i<5; i++) {
	        		String[] num = line2.split(", ", 5);
	        		outputs[count][i] = Double.parseDouble(num[i]); //Store all the output values in a 2D array
	        	}
	        	count++;
	        }
	    } finally {
	        br.close();
	    }
	}

}

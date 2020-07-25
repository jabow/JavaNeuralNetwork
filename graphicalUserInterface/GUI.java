package uk.ac.aber.dcs.neuralnetwork.graphicalUserInterface;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import uk.ac.aber.dcs.neuralnetwork.NeuralNetwork;
import uk.ac.aber.dcs.neuralnetwork.main.DigitRecognitionNeuralNetwork;
import uk.ac.aber.dcs.neuralnetwork.main.SimpleNeuralNetworks;
import uk.ac.aber.dcs.neuralnetwork.main.UniversityGradeNeuralNetwork;
import uk.ac.aber.dcs.neuralnetwork.trainingData.TrainingDataGenerator;

public class GUI{

	//private static final long serialVersionUID = 7204971402685651376L;
	private static JFrame frame;
	private static JPanel mainPanel;
	private static Container content;
	private static File networkFile = null;
	private static JTextArea textArea;

	public static void main(String[] args) {
		frame = new JFrame("Neural Networks");
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createMenu();		
				createPanel();
				createFrame();
			}
		});
	}

	public static void createFrame() {
		frame.setSize(700, 300); //set the size of the frame
		frame.setResizable(false);
		frame.setLocationRelativeTo(null) ; //Center the frame in the middle of the screen
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setVisible(true);  //make it visible
	}

	public static void createPanel() {
		content = frame.getContentPane();
		content.setLayout(new BorderLayout());
		mainPanel = new JPanel();
		String text = "<html>This is an Artificial Neural Network program.<br>" 
				+ "Please use the menu bar at the top to navigate.<br>" 
				+ "Create new networks and test previous ones.<br></html>";

		JLabel label = new JLabel(text, JLabel.CENTER);
		Font font = new Font("Serif", Font.BOLD, 20);
		label.setFont(font);

		mainPanel.add(label);
		content.add(mainPanel, BorderLayout.CENTER);
	}

	public static void createMenu() {
		JMenuBar menuBar = new JMenuBar();;
		JMenu menu;
		//Build the first menu.
		menu = new JMenu("File");
		menuBar.add(menu);

		//Train a new network, set network parameters but allow only, XOR, Uni grade and Digit recognition
		JMenuItem trainMenuItem = new JMenuItem("Train New Network");
		menu.add(trainMenuItem);
		trainMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				content.removeAll();
				mainPanel.removeAll();
				content.setLayout(new GridLayout(1, 1));

				//Create the text area
				String text = "Training Progress will be displayed here: \n";
				textArea = new JTextArea(text);
				PrintStream out = new PrintStream( new TextAreaOutputStream(textArea));
				System.setOut(out);		
				System.setErr(out);
				textArea.setEditable(false);
				JScrollPane scroll = new JScrollPane(textArea,
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				scroll.setPreferredSize(new Dimension(600, 130));

				JLabel label = new JLabel("Hidden Neurons");
				JTextField hidden = new JTextField(3);
				hidden.setText("5");
				JLabel label2 = new JLabel("Learning Rate");
				JTextField learningRate = new JTextField(3);
				learningRate.setText("0.1");
				JLabel label3 = new JLabel("Momentum");
				JTextField momentum = new JTextField(3);
				momentum.setText("0.9");
				JLabel label4 = new JLabel("Error Threshold");
				JTextField error = new JTextField(3);
				error.setText("0.05");

				JButton digitButton = new JButton("Train Digit Recognition ANN");
				digitButton.addActionListener(new ActionListener(){
					@SuppressWarnings("static-access")
					public void actionPerformed(ActionEvent e){

						textArea.setText(text);
						int hn = Integer.parseInt(hidden.getText());
						double lr = Double.parseDouble(learningRate.getText());
						double m = Double.parseDouble(momentum.getText());
						double err = Double.parseDouble(error.getText());
						DigitRecognitionNeuralNetwork digitRecognition = new DigitRecognitionNeuralNetwork(lr, m, hn, err);
						TrainingDataGenerator digitTrainingDataGenerator = null;
						NeuralNetwork nn;
						try {				
							System.out.println("Loading the MNIST data");
							digitTrainingDataGenerator = digitRecognition.loadMNISTTrainingData();

							textArea.append("60000 Training images loaded");
							nn = digitRecognition.createNetwork(digitTrainingDataGenerator);

							JFileChooser fileChooser = new JFileChooser();
							File file = null;
							int returnValue = fileChooser.showOpenDialog(null);
							if (returnValue == JFileChooser.APPROVE_OPTION) {
								file = fileChooser.getSelectedFile();
							}
							if(file == null) {
								throw new IOException("No File selected to save the network");
							} else {
								digitRecognition.saveToFile(nn, file);
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}


					}

				});

				JButton uniButton = new JButton("Train Uni Grade Classifier ANN");
				uniButton.addActionListener(new ActionListener(){
					@SuppressWarnings("static-access")
					public void actionPerformed(ActionEvent e){
						textArea.setText(text);
						int hn = Integer.parseInt(hidden.getText());
						double lr = Double.parseDouble(learningRate.getText());
						double m = Double.parseDouble(momentum.getText());
						double err = Double.parseDouble(error.getText());

						UniversityGradeNeuralNetwork uniGrade = new UniversityGradeNeuralNetwork(lr, m, hn, 2, err, false, 0);
						NeuralNetwork gradeClassifier = uniGrade.createUntrainedUniversityGradeClassifier();
						uniGrade.trainNetwork(gradeClassifier);
						JFileChooser fileChooser = new JFileChooser();
						File file = null;
						int returnValue = fileChooser.showOpenDialog(null);
						if (returnValue == JFileChooser.APPROVE_OPTION) {
							file = fileChooser.getSelectedFile();
						}
						try {
							uniGrade.saveToFile(gradeClassifier, file);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});

				JButton xorButton = new JButton("Train XOR ANN");
				xorButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						textArea.setText(text);
						double lr = Double.parseDouble(learningRate.getText());
						double m = Double.parseDouble(momentum.getText());
						double err = Double.parseDouble(error.getText());

						NeuralNetwork nn = SimpleNeuralNetworks.createUntrainedXorNN(lr, m, 2, err);

						JFileChooser fileChooser = new JFileChooser();
						File file = null;
						int returnValue = fileChooser.showOpenDialog(null);
						if (returnValue == JFileChooser.APPROVE_OPTION) {
							file = fileChooser.getSelectedFile();
						}
						if(file == null) {
							try {
								throw new IOException("No File selected to save the network");
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						} else {
							try {
								SimpleNeuralNetworks.saveToFile(nn, file);
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}
				});

				mainPanel.add(label);
				mainPanel.add(hidden);
				mainPanel.add(label2);
				mainPanel.add(learningRate);
				mainPanel.add(label3);
				mainPanel.add(momentum);
				mainPanel.add(label4);
				mainPanel.add(error);
				mainPanel.add(digitButton);
				mainPanel.add(uniButton);
				mainPanel.add(xorButton);
				mainPanel.add(scroll);
				content.add(mainPanel);
				createFrame();
			}
		});




		//Display the screen so we can test a neural network created previously
		JMenuItem testMenuItem = new JMenuItem("Test Digit Neural Network");
		menu.add(testMenuItem);
		testMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				networkFile = null; //Reset the variable to null
				JFrame.setDefaultLookAndFeelDecorated(true);
				JDialog.setDefaultLookAndFeelDecorated(true);
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					networkFile = fileChooser.getSelectedFile();
				}
				frame.setTitle("Testing: " + networkFile);
				content.removeAll();
				content.setLayout(new BorderLayout());
				JPanel paintPanel = new JPanel();
				paintPanel.setLayout(new GridLayout(2, 0));

				JPanel testPanel = new JPanel();
				testPanel.setLayout(new GridLayout(0, 2));

				JPanel displayPanel = new JPanel();

				//Create the text area
				String text = "The Results of the test will be displayed here: \n";
				textArea = new JTextArea(text);
				PrintStream out = new PrintStream( new TextAreaOutputStream(textArea));
				System.setOut(out);				
				textArea.setEditable(false);
				JScrollPane scroll = new JScrollPane(textArea,
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				scroll.setPreferredSize(new Dimension(600, 130));

				final PadDraw drawPad = new PadDraw();
				//drawPad.setSize(100, 100);

				JButton clearButton = new JButton("Clear");
				//creates the clear button and sets the text as "Clear"
				clearButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						drawPad.clear();
					}
				});

				JButton loadButton = new JButton("Test with MNIST Dataset");
				loadButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						//String filename = textField.getText();
						if (networkFile == null) {
							JFrame.setDefaultLookAndFeelDecorated(true);
							JDialog.setDefaultLookAndFeelDecorated(true);
							JFileChooser fileChooser = new JFileChooser();
							int returnValue = fileChooser.showOpenDialog(null);
							if (returnValue == JFileChooser.APPROVE_OPTION) {
								networkFile = fileChooser.getSelectedFile();
							}
							frame.setTitle("Testing: " + networkFile);
						}
						try {

							//LoadNeuralNetwork load = new LoadNeuralNetwork(networkFile);
							textArea.setText(text);
							NeuralNetwork nn = loadNeuralNetwork();
							DigitRecognitionNeuralNetwork.testMNISTDataset(nn);
						} catch (ClassNotFoundException | IOException ex) {
							ex.printStackTrace();
						}
					}
				});

				JButton testButton = new JButton("Test Draw Pad Image");
				testButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						if (networkFile == null) {
							JFrame.setDefaultLookAndFeelDecorated(true);
							JDialog.setDefaultLookAndFeelDecorated(true);
							JFileChooser fileChooser = new JFileChooser();
							int returnValue = fileChooser.showOpenDialog(null);
							if (returnValue == JFileChooser.APPROVE_OPTION) {
								networkFile = fileChooser.getSelectedFile();
							}
							frame.setTitle("Testing: " + networkFile);
						}
						//String filename = textField.getText();
						try {
							double[] imageData = drawPad.getImageData();
							//LoadNeuralNetwork test = new LoadNeuralNetwork(networkFile, imageData);
							textArea.setText(text);
							NeuralNetwork nn = loadNeuralNetwork();
							DigitRecognitionNeuralNetwork.testImageData(imageData, nn);
						} catch (IOException e1) {
							e1.printStackTrace();
						} catch (ClassNotFoundException e1) {
							e1.printStackTrace();
						}
					}
				});

				JButton loadImage = new JButton("Test image from file");
				loadImage.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						//String filename = textField.getText();
						if (networkFile == null) {
							JFrame.setDefaultLookAndFeelDecorated(true);
							JDialog.setDefaultLookAndFeelDecorated(true);
							JFileChooser fileChooser = new JFileChooser();
							int returnValue = fileChooser.showOpenDialog(null);
							if (returnValue == JFileChooser.APPROVE_OPTION) {
								networkFile = fileChooser.getSelectedFile();
							}
							frame.setTitle("Testing: " + networkFile);
						}
						File imageFile = null;
						JFrame.setDefaultLookAndFeelDecorated(true);
						JDialog.setDefaultLookAndFeelDecorated(true);
						try {
							//double[] imageData = drawPad.getImageData();
							JFileChooser fileChooser = new JFileChooser();
							int returnValue = fileChooser.showOpenDialog(null);
							if (returnValue == JFileChooser.APPROVE_OPTION) {
								imageFile = fileChooser.getSelectedFile();
							}
							if (imageFile != null) {
								//displayImage(imageFile); //Display the selected image on screen
								int h = drawPad.getHeight();
								int w = drawPad.getWidth();
								drawPad.drawImageOntoDrawPad(imageFile, h, w);
								drawPad.repaint();
								//LoadNeuralNetwork test = new LoadNeuralNetwork(networkFile, imageFile);
								textArea.setText(text);
								NeuralNetwork nn = loadNeuralNetwork();
								DigitRecognitionNeuralNetwork.testSpecificImage(imageFile, nn);

							} else {
								throw new IOException("No File selected");
							}
						} catch (IOException | ClassNotFoundException e1) {
							e1.printStackTrace();
						}
					}
				});

				paintPanel.add(clearButton);
				paintPanel.add(testButton);
				testPanel.add(loadButton);
				testPanel.add(loadImage);
				displayPanel.add(scroll);
				content.add(drawPad, BorderLayout.LINE_START);
				content.add(paintPanel, BorderLayout.CENTER);
				content.add(testPanel, BorderLayout.LINE_END);
				content.add(displayPanel, BorderLayout.PAGE_END);
				createFrame();
			}
		});



		JMenuItem testUniMenuItem = new JMenuItem("Test Grade Classifier Neural Network");
		menu.add(testUniMenuItem);
		testUniMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				networkFile = null; //Reset the variable to null
				JFrame.setDefaultLookAndFeelDecorated(true);
				JDialog.setDefaultLookAndFeelDecorated(true);
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					networkFile = fileChooser.getSelectedFile();
				}
				frame.setTitle("Testing: " + networkFile);
				content.removeAll();
				mainPanel.removeAll();
				content.setLayout(new BorderLayout());

				String text = "The Results of the test will be displayed here: \n";
				textArea = new JTextArea(text);
				PrintStream out = new PrintStream( new TextAreaOutputStream(textArea));
				System.setOut(out);				
				textArea.setEditable(false);
				JScrollPane scroll = new JScrollPane(textArea,
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				scroll.setPreferredSize(new Dimension(600, 130));

				JLabel label = new JLabel("Percentage");
				JTextField userInput = new JTextField(3);

				JButton testButton = new JButton("Test Grade Classifier with all options");
				testButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						if (networkFile == null) {
							JFrame.setDefaultLookAndFeelDecorated(true);
							JDialog.setDefaultLookAndFeelDecorated(true);
							JFileChooser fileChooser = new JFileChooser();
							int returnValue = fileChooser.showOpenDialog(null);
							if (returnValue == JFileChooser.APPROVE_OPTION) {
								networkFile = fileChooser.getSelectedFile();
							}
							frame.setTitle("Testing: " + networkFile);
						}
						textArea.setText(text);
						NeuralNetwork nn = null;
						try {
							nn = loadNeuralNetwork();
						} catch (ClassNotFoundException | IOException e1) {
							e1.printStackTrace();
						}
						UniversityGradeNeuralNetwork.testNetwork(nn);
					}
				});

				JButton testButton2 = new JButton("Test Grade Classifier with specific input");
				testButton2.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						if (networkFile == null) {
							JFrame.setDefaultLookAndFeelDecorated(true);
							JDialog.setDefaultLookAndFeelDecorated(true);
							JFileChooser fileChooser = new JFileChooser();
							int returnValue = fileChooser.showOpenDialog(null);
							if (returnValue == JFileChooser.APPROVE_OPTION) {
								networkFile = fileChooser.getSelectedFile();
							}
							frame.setTitle("Testing: " + networkFile);
						}
						textArea.setText(text);
						NeuralNetwork nn = null;
						try {
							nn = loadNeuralNetwork();
						} catch (ClassNotFoundException | IOException e1) {
							e1.printStackTrace();
						}
						int input = Integer.parseInt(userInput.getText());
						if (!(input >=0 && input <=100)) {
							throw new IllegalArgumentException("Input must be between 0-100");
						}
						UniversityGradeNeuralNetwork.testUserInput(nn, input);
					}
				});

				mainPanel.add(testButton, BorderLayout.PAGE_START);
				mainPanel.add(label, BorderLayout.PAGE_START);
				mainPanel.add(userInput, BorderLayout.PAGE_START);
				mainPanel.add(testButton2, BorderLayout.PAGE_START);
				mainPanel.add(scroll, BorderLayout.PAGE_END);
				content.add(mainPanel);
				createFrame();
			}
		});



		JMenuItem testXORMenuItem = new JMenuItem("Test XOR Neural Network");
		menu.add(testXORMenuItem);
		testXORMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				networkFile = null; //Reset the variable to null
				JFrame.setDefaultLookAndFeelDecorated(true);
				JDialog.setDefaultLookAndFeelDecorated(true);
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					networkFile = fileChooser.getSelectedFile();
				}
				frame.setTitle("Testing: " + networkFile);
				content.removeAll();
				mainPanel.removeAll();
				content.setLayout(new BorderLayout());

				//Create the text area
				String text = "The Results of the test will be displayed here: \n";
				textArea = new JTextArea(text);
				PrintStream out = new PrintStream( new TextAreaOutputStream(textArea));
				System.setOut(out);				
				textArea.setEditable(false);
				JScrollPane scroll = new JScrollPane(textArea,
						JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
						JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				scroll.setPreferredSize(new Dimension(600, 130));

				JButton testButton = new JButton("Test XOR network Image");
				testButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						if (networkFile == null) {
							JFrame.setDefaultLookAndFeelDecorated(true);
							JDialog.setDefaultLookAndFeelDecorated(true);
							JFileChooser fileChooser = new JFileChooser();
							int returnValue = fileChooser.showOpenDialog(null);
							if (returnValue == JFileChooser.APPROVE_OPTION) {
								networkFile = fileChooser.getSelectedFile();
							}
							frame.setTitle("Testing: " + networkFile);
						}
						textArea.setText(text);
						NeuralNetwork untrainedXorNN = null;
						try {
							untrainedXorNN = loadNeuralNetwork();
						} catch (ClassNotFoundException | IOException e1) {
							e1.printStackTrace();
						}
						SimpleNeuralNetworks.testUntrainedXORNN(untrainedXorNN);
					}
				});
				mainPanel.add(testButton, BorderLayout.PAGE_START);
				mainPanel.add(scroll, BorderLayout.PAGE_END);
				content.add(mainPanel);
				createFrame();
			}
		});

		JMenuItem exitMenuItem = new JMenuItem("Exit");
		menu.add(exitMenuItem);
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		//Add the menu items
		frame.setJMenuBar(menuBar);
	}

	public static NeuralNetwork loadNeuralNetwork() throws ClassNotFoundException, IOException {
		FileInputStream fis = new FileInputStream(networkFile);
		ObjectInputStream ois = new ObjectInputStream(fis);
		NeuralNetwork neuralNetwork = (NeuralNetwork) ois.readObject();
		ois.close();
		return neuralNetwork;
	}





}

class PadDraw extends JComponent{
	private static final long serialVersionUID = 1L;
	Image image;
	Graphics2D graphics2D;
	int currentX, currentY, oldX, oldY;

	public PadDraw(){
		setDoubleBuffered(false);
		addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				oldX = e.getX();
				oldY = e.getY();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter(){
			public void mouseDragged(MouseEvent e){
				currentX = e.getX();
				currentY = e.getY();
				if(graphics2D != null) {
					graphics2D.drawLine(oldX, oldY, currentX, currentY);
				}
				repaint();
				oldX = currentX;
				oldY = currentY;
			}

		});
	}

	@Override
	public Dimension getPreferredSize(){
		return new Dimension(100, 100);
	}

	public void paintComponent(Graphics g){
		if(image == null){
			image = createImage(getSize().width, getSize().height);
			graphics2D = (Graphics2D)image.getGraphics();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			clear();

		}
		g.drawImage(image, 0, 0, null);
	}

	public void clear(){
		graphics2D.setPaint(Color.white);
		graphics2D.fillRect(0, 0, getSize().width, getSize().height);
		graphics2D.setPaint(Color.black);
		graphics2D.setStroke(new BasicStroke(6)); //set line thickness
		repaint();
	}

	public double[] getImageData() throws IOException {
		/*File outputfile = new File("userimage.png");
	    ImageIO.write((RenderedImage) image, "png", outputfile);*/
		Image resizedImage = resize();
		File outputfile = new File("new.png");
		ImageIO.write((RenderedImage) resizedImage, "png", outputfile);

		Raster raster= ((BufferedImage) resizedImage).getData();
		int w=raster.getWidth(),h=raster.getHeight();
		double[] pixels = new double[784];
		int count = 0;
		for (int x=0;x<w;x++) {
			for(int y=0;y<h;y++) {
				pixels[count]=raster.getSample(y,x,0);
				count++;
			}
		}
		return pixels;
	}

	public Image resize() throws IOException {
		int type = ((BufferedImage) image).getType() == 0? BufferedImage.TYPE_INT_ARGB : ((BufferedImage) image).getType();
		BufferedImage resizedImage = new BufferedImage(28, 28, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(image, 0, 0, 28, 28, null);
		g.dispose();

		return resizedImage;
	}

	public void drawImageOntoDrawPad(File file, int height, int width) throws IOException {
		//Get the image from file
		BufferedImage img = ImageIO.read(file);
		Raster raster= ((BufferedImage) img).getData();
		int w=raster.getWidth(),h=raster.getHeight();
		double[] pixels = new double[784];
		int count = 0;
		for (int x=0;x<w;x++) {
			for(int y=0;y<h;y++) {
				pixels[count]=raster.getSample(y,x,0);
				count++;
			}
		}
		//graphics2D.drawImage(img, 0, 0, 100, 100, null);
		graphics2D.drawImage(img, 0, 0, width, height, null);
	}
}


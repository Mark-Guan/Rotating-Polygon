
/*
 * Display Class: This class is the basis for the Graphical User Interface of the project.
 * It contains code for a Jframe, and contains buttons, sliders and textfields accompanied by
 * listeners to carry out actions which ultimately are able to rotate polygons around an
 * axis. It also contains some helper methods such as an isValid and timerRotate to help
 * with some of the listeners.
 * 
 * February 5, 2014
 * By: Mark Guan and Ryan Page
 * 
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Display {

	
	private static JPanel menuPanel;
	private static JPanel bottomPanel;
	private static PolygonDrawerPanel polygonDrawerPanel;
	private static int delay = 10;
	private static ActionListener task; // task for clockWiseTimer
	private static ActionListener task2; // task for counterClockWiseTimer
	private static Timer counterClockWiseTimer = new Timer(delay, task2);
	private static Timer clockWiseTimer = new Timer(delay, task);
	private static Polygon polygon;
	private static ArrayList<Polygon> polygons = new ArrayList<Polygon>();

	
	// returns the List of polygons
	public static ArrayList<Polygon> getPolygons() {
		return polygons;
	}

	
	// Method that begins the rotation for a desired polygon. Sets up the timer
	// so all one has to do in order to rotate the polygon is simply say
	// timer.start()
	// version:clockwise
	public static void timerRotate(Polygon polygon) {
		task = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				polygon.setAxis(new Point((int) (polygonDrawerPanel.getWidth() * .5),
						(int) (polygonDrawerPanel.getHeight() * .5)));
				polygon.rotateArray(true);
				polygonDrawerPanel.repaint();
			}
		};
		clockWiseTimer = new Timer(delay, task);
	}

	
	// Method that begins the rotation for a desired polygon. Sets up the timer
	// so all one has to do in order to rotate the polygon is simply say
	// timer.start()
	// version:counter-clockwise
	public static void timerRotatecc(Polygon polygon) {
		task2 = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				polygon.setAxis(new Point((int) (polygonDrawerPanel.getWidth() * .5),
						(int) (polygonDrawerPanel.getHeight() * .5)));
				polygon.rotateArray(false);
				polygonDrawerPanel.repaint();
			}
		};
		counterClockWiseTimer = new Timer(delay, task2);
	}

	
	// if 1 <= width <= 19 then return the width as an int,
	// otherwise return 1 as the default width
	public static int checkWidth(String val) {
		int value = Integer.parseInt(val);
		if (value < 20 && value > 1)
			return value;
		else
			return 1;
	}

	
	//creating the main menu for the program
	public static void initializeMenu(JFrame frame, Border border, Color backGroundColor){
		menuPanel = new JPanel();
		menuPanel.setPreferredSize(new Dimension(175, 200));
		menuPanel.setBorder(border);
		frame.getContentPane().add(menuPanel, BorderLayout.WEST);
		
		// Creating title and information labels for creating new shapes that
		// are going to be in the menu panel
		JLabel numSidesLabel = new JLabel("Please enter # of sides(2-25)");
		numSidesLabel.setFont(new Font("", Font.PLAIN, 12));
		JLabel newShapeNote = new JLabel("(Note: a new shape is created");
		JLabel newShapeNote2 = new JLabel("every time you press add)");
		newShapeNote.setFont(new Font("", Font.PLAIN, 9));
		newShapeNote2.setFont(new Font("", Font.PLAIN, 9));
		JLabel widthLabel = new JLabel("Please enter a Width (1-19)");
		JLabel menuLabel = new JLabel("Menu");
		menuLabel.setFont(new Font("", Font.PLAIN, 20));
		JButton addShapeButton = new JButton("Add Shape");
		addShapeButton.setPreferredSize(new Dimension(80, 20));
		JButton setWidthButton = new JButton("Set Width");
		setWidthButton.setPreferredSize(new Dimension(80, 20));
		JLabel axisLabel = new JLabel();
		axisLabel.setFont(new Font("", Font.PLAIN, 10));
		
		// creating textfields to allow user input in the menu panel
		JTextField numSidesTextField = new JTextField();
		numSidesTextField.setPreferredSize(new Dimension(50, 20));
		JTextField widthTextField = new JTextField();
		widthTextField.setPreferredSize(new Dimension(50, 20));
		
		// add parts of menu panel to the menu panel
		menuPanel.add(menuLabel);
		menuPanel.add(axisLabel);
		menuPanel.add(numSidesLabel);
		menuPanel.add(newShapeNote);
		menuPanel.add(newShapeNote2);
		menuPanel.add(numSidesTextField);
		menuPanel.add(addShapeButton);
		menuPanel.add(widthLabel);
		menuPanel.add(widthTextField);
		menuPanel.add(setWidthButton);
		
		// Action Listener that sets the current polygon's width
		ActionListener setWidthListener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int value = checkWidth(widthTextField.getText());
				getPolygons().get(getPolygons().size() - 1).setLineThickness(value);
			}
		};
		// Action listener that adds the desired Polygon onto the screen
				ActionListener addPolygonListener = new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						int numSides = Integer.parseInt(numSidesTextField.getText());
						if (numSides < 2 || numSides > 25)
							numSides = 2; // default
						polygon = new Polygon(Color.WHITE, numSides);
						getPolygons().add(polygon);
						polygonDrawerPanel.repaint();
						timerRotate(polygon);
						timerRotatecc(polygon);
						clockWiseTimer.start();
					}
		};
		
		// adding all the action listeners to the desired jComponents
		setWidthButton.addActionListener(setWidthListener);
		addShapeButton.addActionListener(addPolygonListener);
	}

	
	// panel at the bottom with options on how the polygon should be rotated
	public static void initializeBottomPanel(JFrame frame, Border border, Color backGroundColor){
		bottomPanel = new JPanel();
		bottomPanel.setPreferredSize(new Dimension(25, 75));
		bottomPanel.setBorder(border);
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		
		// creating the labels and buttons that are going on the bottom panel
		// JSlider allows user to adjust rotation speed
		JSlider framesPerSecond = new JSlider(JSlider.HORIZONTAL, 0, 50, 10);
		framesPerSecond.setMajorTickSpacing(10);
		framesPerSecond.setMinorTickSpacing(1);
		framesPerSecond.setPaintTicks(true);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer(0), new JLabel("Slow"));
		labelTable.put(new Integer(50), new JLabel("Fast"));
		framesPerSecond.setLabelTable(labelTable);
		framesPerSecond.setPaintLabels(true);
		JButton stopRotationButton = new JButton("Stop Rotation");
		JButton rotateClockWiseButton = new JButton("Start ClockWise Rotation");
		JButton rotateCounterClockWiseButton = new JButton("Start Counter-Clockwise");
		
		// adding the parts of the screen panel to the screen panel
		bottomPanel.add(stopRotationButton);
		bottomPanel.add(rotateClockWiseButton);
		bottomPanel.add(rotateCounterClockWiseButton);
		bottomPanel.add(framesPerSecond);
		
		// Action Listener that stops the current polygons rotation
		ActionListener stopRotation = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				clockWiseTimer.stop();
				counterClockWiseTimer.stop();
			}
		};
		// Action Listener that rotates the current polygon counter-clockwise
		ActionListener rotateCounterClockWiseListener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				counterClockWiseTimer.start();
				clockWiseTimer.stop();
			}
		};
		// Action Listener that rotates the current polygons clockwise
		ActionListener rotateClockWiseListener = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				clockWiseTimer.start();
				counterClockWiseTimer.stop();
			}
		};
		// ChangeListener for the slider, the slider either slows or
		// fastens the pace of rotation
		ChangeListener sliderChangeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent evt) {
				JSlider source = (JSlider) evt.getSource();
				if (!source.getValueIsAdjusting()) {
					int fps = (int) source.getValue();
					if (fps == 0) {
						delay = 1000;
					} else {
						delay = 100 / fps;
						clockWiseTimer.setDelay(delay);
						clockWiseTimer.setInitialDelay(delay);
						counterClockWiseTimer.setDelay(delay);
						counterClockWiseTimer.setDelay(delay);
					}
				}
			}
		};
		
		// adding all the action listeners to the desired jComponents
		stopRotationButton.addActionListener(stopRotation);
		rotateClockWiseButton.addActionListener(rotateClockWiseListener);
		framesPerSecond.addChangeListener(sliderChangeListener);
		rotateCounterClockWiseButton.addActionListener(rotateCounterClockWiseListener);
	}
	
	
	// panel that will be painted onto
	public static void initializePolygonDrawerPanel(JFrame frame, Border border, Color backGroundColor){
		polygonDrawerPanel = new PolygonDrawerPanel();
		polygonDrawerPanel.setBorder(border);
		polygonDrawerPanel.setBackground(backGroundColor);
		frame.getContentPane().add(polygonDrawerPanel, BorderLayout.CENTER);
	}
	
	
	// Main method that creates the actual JFrame, adds many Jcomponents to the
	// Jframe. Aciton listeners to the jcomponenets and in essence, puts the
	// entire project together into the graphical user interface.
	public static void main(String[] args) {
		final JFrame frame = new JFrame("Rotating Polygon Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(900, 600));
		frame.setMinimumSize(new Dimension(800, 500));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		Color backGroundColor = Color.decode("#3264C8");
		Border border = BorderFactory.createLineBorder(backGroundColor, 1);
		
		initializeMenu(frame, border, backGroundColor);
		initializeBottomPanel(frame, border, backGroundColor);
		initializePolygonDrawerPanel(frame, border, backGroundColor);
		
		frame.pack();
	}

}
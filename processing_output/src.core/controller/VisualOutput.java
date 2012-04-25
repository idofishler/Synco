package controller;


import controller.IController;
import model.MainModel;
import processing.core.PApplet;
import processing.serial.Serial;
import utils.Recorder;
import view.MainView;


public class VisualOutput extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static boolean ARDUINO_INPUT_ON = false;

	MainController mainController;
	Serial port;
	
	Recorder recorder;
	private boolean record = false;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] {"controller.VisualOutput"});
	}

	public void setup() {

		initSirialPort();

		MainModel mainModel = new MainModel();

		size(MainModel.STAGE_WIDTH, MainModel.STAGE_HEIGHT);

		MainView mainView = new MainView(this, mainModel);

		mainController = new MainController(mainModel, mainView);
		
		initRecording();
	}

	private void initRecording() {
		recorder = new Recorder();
		recorder.init(this);
	}

	// TODO change this to use proper port handling. See SerialTest example.
	private void initSirialPort() {
		try {
			System.out.println("Sirial Port to use: " + Serial.list()[0]);
			port = new Serial(this, Serial.list()[0], 115200);
			ARDUINO_INPUT_ON = true;
		}
		catch (Exception e) {
			if (MainController.DEBUG) {
				System.err.println(e.getMessage());
			}
			System.out.println("Arduino input will not work");
		}

	}

	public void draw() {
		if (ARDUINO_INPUT_ON) {
			CheckSerialEvent();			
		}
		mainController.doUI();
		
		if (record) {
			recorder.saveVideo();
		}
	}

	private void CheckSerialEvent() { 
		if (port.available() > 0) {
			byte inChar = (byte) port.read();
			port.clear();

			if (inChar == '0') {
				mainController.event(0);
			}
			if (inChar == '1') {
				mainController.event(1);
			}
		}
	}

	@Override
	public void keyPressed() {
		if (key == '0') {
			mainController.event(0);
		}
		if (key == '1') {
			mainController.event(1);
		}
		if (key == ' ') {
			if (record) {
				recorder.stop();
				record = false;
			} else {
				record = true;
			}
		}
		if (key == 'p') {
			recorder.snap();
		}
		if (key == ESC) {  
			recorder.stop();
			mainController.getModel().getSoundModel().stop();
			exit();
		}
	}

	/* (non-Javadoc)
	 * @see processing.core.PApplet#mouseClicked()
	 */
	@Override
	public void mouseClicked() {
		if (mouseButton == RIGHT) {
			mainController.event(0);
		}
		if (mouseButton == LEFT) {
			mainController.event(1);
		}
	}


}


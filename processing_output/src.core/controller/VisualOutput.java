package controller;


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
	private static final float DEFAULT_FRAME_RATE = 20;

	private static boolean ARDUINO_LEFT_INPUT_ON = false;
	private static boolean ARDUINO_RIGHT_INPUT_ON = false;
	private static boolean ARDUINO_OUTPUT_ON = false;
	private static final boolean RECORDING_ENABLE = true;

	private static final String IN_LEFT_PORT_NAMES[] = { 
		"/dev/tty.usbserial-A700ekac", // Mac OS X
		"/dev/ttyUSB0", // Linux
		"COM7", // Windows
	};

	private static final String IN_RIGHT_PORT_NAMES[] = { 
		"/dev/tty.usbserial-A600AH19", // Mac OS X
		"/dev/ttyUSB2", // Linux
		"COM3", // Windows
	};

	private static final String OUT_PORT_NAMES[] = { 
		"/dev/tty.usbserial-A600AH19", // Mac OS X
		"/dev/ttyUSB1", // Linux
		"COM5", // Windows
	};

	private MainController mainController;
	private Serial inLeftPort;
	private Serial inRightPort;
	private Serial outPort;
	private Recorder recorder;


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] {"controller.VisualOutput"});
	}

	public void setup() {

		frameRate(DEFAULT_FRAME_RATE);

		initSirialPorts();

		MainModel mainModel = new MainModel();

		size(MainModel.STAGE_WIDTH, MainModel.STAGE_HEIGHT);

		MainView mainView = new MainView(this, mainModel);

		mainController = new MainController(mainModel, mainView);

		recorder = new Recorder();

		reset();
	}

	private void initRecording() {
		if (RECORDING_ENABLE) {			
			recorder.init(this);
		}
	}

	private void initSirialPorts() {
		try {
			String inLeftPortName = null;
			String inRightPortName = null;
			String outPortName = null;
			int serialsNo = Serial.list().length;
			for (int n = 0; n < serialsNo; n++) {
				for (int i = 0; i < OUT_PORT_NAMES.length; i++) {
					if (IN_LEFT_PORT_NAMES[i].equalsIgnoreCase(Serial.list()[n])) {
						inLeftPortName = Serial.list()[n];
					}
					if (IN_RIGHT_PORT_NAMES[i].equalsIgnoreCase(Serial.list()[n])) {
						inRightPortName = Serial.list()[n];
					}
					if (OUT_PORT_NAMES[i].equalsIgnoreCase(Serial.list()[n])) {
						outPortName = Serial.list()[n];
					}
				}				
			}
			if (inLeftPortName != null) {
				System.out.println("IN LEFT Sirial port to use: " + inLeftPortName);
				inLeftPort = new Serial(this, inLeftPortName, 115200);
				ARDUINO_LEFT_INPUT_ON = true;
			}
			else {
				System.out.println("Arduino LEFT input will not work");
			}
			if (inRightPortName  != null) {
				System.out.println("IN RIGHT Sirial port to use: " + inRightPortName);
				inRightPort = new Serial(this, inRightPortName, 115200);
				ARDUINO_RIGHT_INPUT_ON = true;
			}
			else {
				System.out.println("Arduino RIGHT input will not work");
			}
			if (outPortName != null) {
				System.out.println("OUT Serial port to use: " + outPortName);
				outPort = new Serial(this, outPortName, 9600);
				ARDUINO_OUTPUT_ON = true;
			}
			else {
				System.out.println("Arduino output will not work");
			}
		}
		catch (Exception e) {
			if (MainController.DEBUG) {
				System.err.println(e.getMessage());
			}
			System.out.println("Arduino input will not work");
		}
	}

	public void draw() {
		if (ARDUINO_OUTPUT_ON) {
			if (mainController.getModel().areSynced()) {
				//System.out.println("SYNCED");
				outPort.write('1');				
			}
			else {
				//System.out.println("NOT_SYNCED");
				outPort.write('0');	
			}
		}
		mainController.doUI();
	}
	
	public void serialEvent(Serial port) {
		if (inLeftPort.available() > 0) {
			String inRightData = port.readStringUntil('\n');  
			inRightData = trim(inRightData);             // trim the \n off the end
			if (inRightData.charAt(0) == 'B'){
				mainController.event(0);
			}
		}
		if (inRightPort.available() > 0) {
			String inLeftData = port.readStringUntil('\n');  
			inLeftData = trim(inLeftData);             // trim the \n off the end
			if (inLeftData.charAt(0) == 'B'){
				mainController.event(1);
			}
		}
		return;     
	}

//	private void readSerialInput() {
//		if (ARDUINO_LEFT_INPUT_ON) {
//			if (inLeftPort.available() > 0) {
//				byte inChar = (byte) inLeftPort.read();
//				//System.out.println(inChar);
//				inLeftPort.clear();
//
//				if (inChar == '0') {
//					mainController.event(0);
//				}
//				if (inChar == '1') {
//					mainController.event(1);
//				}
//			}
//		}
//	}

	@Override
	public void keyPressed() {
		if (key == '0') {
			mainController.event(0);
		}
		if (key == '1') {
			mainController.event(1);
		}
		if (RECORDING_ENABLE) {
			if (key == 'p') {
				doSnap();
			}
			if (key == 's') {
				doShare();
			}
		}
		if (ARDUINO_OUTPUT_ON) {
			if (key == '+') {
				outPort.write('+');
			}
			if (key == '-') {
				outPort.write('-');
			}
			if (key == 't') {
				outPort.write('t');
			}
		}
		if (key == 'r') {
			reset();
		}
		if (key == ESC) {
			mainController.getModel().getSoundModel().stop();
			exit();
		}
		if (key == ESC) {
			mainController.getModel().getSoundModel().stop();
			exit();
		}
	}

	private void doShare() {
		recorder.showQR();
		mainController.getModel().getSoundModel().stop();
		noLoop();	
	}

	private void reset() {
		//		if (ARDUINO_INPUT_ON) {
		//			inPort.write('R');
		//		}
		//		if (ARDUINO_OUTPUT_ON) {
		//			outPort.write('R');
		//		}
		mainController.init();
		initRecording();

		if (!looping) {
			loop();
		}

		// this ugly hack triggers another event that will cause the un-sync
		try {
			Thread.sleep(500); 
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mainController.event(0);
		mainController.event(1);
		// end of ugly hack
	}

	private void doSnap() {
		String snapName = recorder.snap();
		if (MainController.DEBUG) {
			System.out.println("snapshot saved to: " + snapName);
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


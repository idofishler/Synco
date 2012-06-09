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
	private static final float DEFAULT_FRAME_RATE = 30;

	private static boolean ARDUINO_INPUT_ON = false;
	private static boolean ARDUINO_OUTPUT_ON = false;
	private static final boolean RECORDING_ENABLE = true;

	private static final String IN_PORT_NAMES[] = { 
		"/dev/tty.usbserial-A700ekac", // Mac OS X
		"/dev/ttyUSB0", // Linux
		"COM7", // Windows
	};

	private static final String OUT_PORT_NAMES[] = { 
		"/dev/tty.usbserial-A600AH19", // Mac OS X
		"/dev/ttyUSB1", // Linux
		"COM5", // Windows
	};


	MainController mainController;
	Serial inPort;
	Serial outPort;
	Recorder recorder;


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

	@Deprecated
	private void initSirialPort() {
		try {
			String portName = null;
			for (int i = 0; i < IN_PORT_NAMES.length; i++) {
				if (IN_PORT_NAMES[i].equalsIgnoreCase(Serial.list()[0])) {
					portName = Serial.list()[0];
				}
			}
			if (portName != null) {
				System.out.println("Sirial Port to use: " + portName);
				inPort = new Serial(this, portName, 115200);
				ARDUINO_INPUT_ON = true;
			}
			else {
				System.out.println("Arduino input will not work");
			}
		}
		catch (Exception e) {
			if (MainController.DEBUG) {
				System.err.println(e.getMessage());
			}
			System.out.println("Arduino input will not work");
		}
	}

	private void initSirialPorts() {
		try {
			String inPortName = null;
			String outPortName = null;
			int serialsNo = Serial.list().length;
			for (int n = 0; n < serialsNo; n++) {
				for (int i = 0; i < IN_PORT_NAMES.length; i++) {
					if (IN_PORT_NAMES[i].equalsIgnoreCase(Serial.list()[n])) {
						inPortName = Serial.list()[n];
					}
					if (OUT_PORT_NAMES[i].equalsIgnoreCase(Serial.list()[n])) {
						outPortName = Serial.list()[n];
					}
				}				
			}
			if (inPortName != null) {
				System.out.println("IN Sirial port to use: " + inPortName);
				inPort = new Serial(this, inPortName, 115200);
				ARDUINO_INPUT_ON = true;
			}
			else {
				System.out.println("Arduino input will not work");
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
	
	public void serialEvent(Serial p) {
		readSerialInput();
	}
	
	private void readSerialInput() {
		if (ARDUINO_INPUT_ON) {
			if (inPort.available() > 0) {
				byte inChar = (byte) inPort.read();
				inPort.clear();

				if (inChar == '0') {
					mainController.event(0);
				}
				if (inChar == '1') {
					mainController.event(1);
				}
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
	}

	private void doShare() {
		recorder.showQR();
		mainController.getModel().getSoundModel().stop();
		noLoop();	
	}

	private void reset() {
		if (ARDUINO_INPUT_ON) {
			inPort.write('R');
		}
		if (ARDUINO_OUTPUT_ON) {
			outPort.write('R');
		}
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


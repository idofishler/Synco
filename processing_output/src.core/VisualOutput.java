
import model.MainModel;
import processing.core.PApplet;
import processing.serial.Serial;
import view.MainView;
import controller.IController;
import controller.MainController;


public class VisualOutput extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int STAGE_WIDTH = 300;
	private static final int STAGE_HEIGHT = 300;
	
	private static final boolean ARDUINO_INPUT_ON = false;

	IController mainController;
	Serial port;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] {  "--present", "VisualOutput" });
	}

	public void setup() {

		if (ARDUINO_INPUT_ON) {
			initSirialPort();
		}

		size(STAGE_WIDTH, STAGE_HEIGHT, P3D);
		loadPixels();
		frameRate(3000);

		MainModel mainModel = new MainModel();
		MainView mainView = new MainView(this, mainModel);

		mainController = new MainController(mainModel, mainView);

	}

	private void initSirialPort() {

		port = new Serial(this, Serial.list()[0], 9600);

	}

	public void draw() {
		if (ARDUINO_INPUT_ON) {
			CheckSerialEvent();			
		}
		mainController.doUI();
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



import model.MainModel;
import processing.core.PApplet;
import view.MainView;
import controller.IController;
import controller.MainController;
import processing.serial.*;


public class VisualOutput extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int STAGE_WIDTH = 700;
	private static final int STAGE_HEIGHT = 500;

	IController mainController;
	Serial port;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] { "--present", "VisualOutput" });
	}

	public void setup() {

		initSirialPort();

		size(STAGE_WIDTH, STAGE_HEIGHT);
		noFill();
		strokeWeight(5);
		stroke(0, 0, 0, 255);
		ellipseMode(CENTER);

		MainModel mainModel = new MainModel();
		MainView mainView = new MainView(this, mainModel);

		mainController = new MainController(mainModel, mainView);

	}

	private void initSirialPort() {

		port = new Serial(this, Serial.list()[0], 9600);

	}

	public void draw() {
		CheckSerialEvent();
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
}


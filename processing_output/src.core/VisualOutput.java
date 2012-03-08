
import model.MainModel;
import processing.core.PApplet;
import view.MainView;
import controller.IController;
import controller.MainController;


public class VisualOutput extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	IController mainController;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PApplet.main(new String[] { "--present", "VisualOutput" });
	}

	public void setup() {

		MainModel mainModel = new MainModel();
		MainView mainView = new MainView(this, mainModel);
		
		mainController = new MainController(mainModel, mainView);
		
		size(500, 350);
		noFill();
		strokeWeight(5);
		stroke(0, 0, 0, 128);
		ellipseMode(CENTER);
		
	}

	public void draw() {
		// move to view
		background(255);
		
		mainController.doUI();
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

